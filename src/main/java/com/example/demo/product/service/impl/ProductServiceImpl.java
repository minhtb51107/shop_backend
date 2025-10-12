package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.ProductResponse;
import com.example.demo.product.entity.*;
import com.example.demo.product.mapper.ProductMapper;
import com.example.demo.product.repository.*;
import com.example.demo.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSpecsLaptopRepository laptopSpecsRepository;
    private final ProductSpecsPhoneRepository phoneSpecsRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository,
                              CategoryRepository categoryRepository, ProductSpecsLaptopRepository laptopSpecsRepository,
                              ProductSpecsPhoneRepository phoneSpecsRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.laptopSpecsRepository = laptopSpecsRepository;
        this.phoneSpecsRepository = phoneSpecsRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductById(Integer id) {
        return productRepository.findById(id).map(this::mapProductToResponseWithSpecs);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + request.getBrandId()));
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        saveSpecsForProduct(savedProduct, request.getSpecs());

        return mapProductToResponseWithSpecs(savedProduct);
    }

    @Override
    @Transactional
    public Optional<ProductResponse> updateProduct(Integer id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    productMapper.updateEntityFromRequest(request, existingProduct);

                    Brand brand = brandRepository.findById(request.getBrandId())
                            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + request.getBrandId()));
                    ProductCategory category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));

                    existingProduct.setBrand(brand);
                    existingProduct.setCategory(category);

                    Product updatedProduct = productRepository.save(existingProduct);
                    // Xử lý logic xóa specs cũ, tạo specs mới nếu category thay đổi (có thể thêm sau)
                    saveSpecsForProduct(updatedProduct, request.getSpecs());

                    return mapProductToResponseWithSpecs(updatedProduct);
                });
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            // ON DELETE CASCADE sẽ tự động xóa specs liên quan
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Helper Methods ---

    private void saveSpecsForProduct(Product product, Map<String, String> specs) {
        if (specs == null || specs.isEmpty()) return;

        String categoryName = product.getCategory().getName().toLowerCase();
        Integer productId = product.getId();

        if (categoryName.contains("laptop")) {
            ProductSpecsLaptop laptopSpecs = new ProductSpecsLaptop(
                    productId,
                    specs.get("cpu"),
                    specs.get("ram"),
                    specs.get("storage"),
                    specs.get("gpu"),
                    specs.get("screenSize")
            );
            laptopSpecsRepository.save(laptopSpecs);
        } else if (categoryName.contains("phone")) {
            ProductSpecsPhone phoneSpecs = new ProductSpecsPhone(
                    productId,
                    specs.get("screenSize"),
                    specs.get("ram"),
                    specs.get("storage"),
                    specs.get("battery"),
                    specs.get("os")
            );
            phoneSpecsRepository.save(phoneSpecs);
        }
    }

    private ProductResponse mapProductToResponseWithSpecs(Product product) {
        ProductResponse response = productMapper.toResponse(product);
        String categoryName = product.getCategory().getName().toLowerCase();

        if (categoryName.contains("laptop")) {
            laptopSpecsRepository.findById(product.getId()).ifPresent(specs -> {
                response.setSpecs(Map.of(
                        "cpu", specs.getCpu(),
                        "ram", specs.getRam(),
                        "storage", specs.getStorage(),
                        "gpu", specs.getGpu(),
                        "screenSize", specs.getScreenSize()
                ));
            });
        } else if (categoryName.contains("phone")) {
            phoneSpecsRepository.findById(product.getId()).ifPresent(specs -> {
                response.setSpecs(Map.of(
                        "screenSize", specs.getScreenSize(),
                        "ram", specs.getRam(),
                        "storage", specs.getStorage(),
                        "battery", specs.getBattery(),
                        "os", specs.getOs()
                ));
            });
        }
        return response;
    }
}
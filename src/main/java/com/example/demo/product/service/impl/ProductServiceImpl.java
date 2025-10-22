package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.ProductResponse;
import com.example.demo.product.entity.*;
import com.example.demo.product.mapper.ProductMapper;
import com.example.demo.product.repository.*;
import com.example.demo.product.service.ProductService;
import com.example.demo.shared.exception.ResourceNotFoundException; // <-- Đổi Exception cho phù hợp
import jakarta.persistence.EntityNotFoundException; // <-- Giữ lại nếu vẫn dùng ở đâu đó
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.example.demo.product.repository.ProductSpecification;
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

    // ----- Bỏ các repo không cần thiết -----
    // private final ProductImageRepository imageRepository;
    // private final VariantRepository variantRepository;

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository,
                              CategoryRepository categoryRepository, ProductSpecsLaptopRepository laptopSpecsRepository,
                              ProductSpecsPhoneRepository phoneSpecsRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.laptopSpecsRepository = laptopSpecsRepository;
        this.phoneSpecsRepository = phoneSpecsRepository;
        this.productMapper = productMapper;
        // ----- Bỏ gán giá trị -----
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable, String search, Integer categoryId, Integer brandId) {
        Specification<Product> spec = ProductSpecification.filterBy(search, categoryId, brandId);
        // Gọi findAll đã được override với @EntityGraph
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        // Map kết quả sang DTO
        return productPage.map(this::mapProductToResponseWithSpecs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getRelatedProducts(Integer productId, int limit) {
        Product currentProduct = productRepository.findById(productId)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (currentProduct.getCategory() == null) {
            return List.of();
        }
        Integer categoryId = currentProduct.getCategory().getId();
        Pageable pageable = PageRequest.of(0, limit);
        // Gọi hàm repository MỚI để fetch đủ dữ liệu
        List<Product> related = productRepository.findRelatedProductsEagerly(categoryId, productId, pageable);
        return related.stream()
                .map(this::mapProductToResponseWithSpecs)
                .collect(Collectors.toList());
    }


    // ========== QUAN TRỌNG: SỬA HÀM NÀY ==========
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductById(Integer id) {
        // ----- SỬ DỤNG HÀM MỚI findByIdWithDetails TỪ REPOSITORY -----
        Optional<Product> productOpt = productRepository.findByIdWithDetails(id); // <--- THAY ĐỔI Ở ĐÂY!!!

        // Map sang DTO nếu tìm thấy (productOpt đã chứa đủ variants, images)
        return productOpt.map(this::mapProductToResponseWithSpecs);
    }
    // ========== KẾT THÚC SỬA HÀM getProductById ==========

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Logic tạo product giữ nguyên
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        saveSpecsForProduct(savedProduct, request.getSpecs());

        // Gọi lại getProductById (đã sửa) để lấy bản đầy đủ
        return getProductById(savedProduct.getId())
                 .orElseThrow(() -> new EntityNotFoundException("Could not retrieve created product with id: " + savedProduct.getId()));
    }

    @Override
    @Transactional
    public Optional<ProductResponse> updateProduct(Integer id, ProductRequest request) {
         // Logic cập nhật giữ nguyên
        return productRepository.findById(id)
                .map(existingProduct -> {
                    productMapper.updateEntityFromRequest(request, existingProduct);

                    Brand brand = brandRepository.findById(request.getBrandId())
                            .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));
                    ProductCategory category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

                    existingProduct.setBrand(brand);
                    existingProduct.setCategory(category);

                    Product updatedProduct = productRepository.save(existingProduct);
                    saveSpecsForProduct(updatedProduct, request.getSpecs());

                    // Gọi lại getProductById (đã sửa) để lấy bản đầy đủ
                    return getProductById(updatedProduct.getId()).orElse(null);
                });
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        // Logic xóa giữ nguyên
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Helper Methods ---

    // saveSpecsForProduct giữ nguyên
    private void saveSpecsForProduct(Product product, Map<String, String> specs) {
        if (specs == null || specs.isEmpty()) {
             // Optional: Xóa specs cũ nếu map specs mới là null/empty
             laptopSpecsRepository.deleteById(product.getId());
             phoneSpecsRepository.deleteById(product.getId());
            return;
        }
        String categoryName = product.getCategory().getName().toLowerCase();
        Integer productId = product.getId();
        // Xóa specs cũ trước khi lưu specs mới
        laptopSpecsRepository.deleteById(productId);
        phoneSpecsRepository.deleteById(productId);
        if (categoryName.contains("laptop")) {
            ProductSpecsLaptop laptopSpecs = new ProductSpecsLaptop(productId, specs.get("cpu"), specs.get("ram"), specs.get("storage"), specs.get("gpu"), specs.get("screenSize"));
            laptopSpecsRepository.saveAndFlush(laptopSpecs);
        } else if (categoryName.contains("phone")) {
            ProductSpecsPhone phoneSpecs = new ProductSpecsPhone(productId, specs.get("screenSize"), specs.get("ram"), specs.get("storage"), specs.get("battery"), specs.get("os"));
            phoneSpecsRepository.saveAndFlush(phoneSpecs);
        }
    }

    // ----- HELPER mapProductToResponseWithSpecs ĐÃ ĐƯỢC ĐƠN GIẢN HÓA -----
    /**
     * Helper map từ Product Entity sang ProductResponse DTO.
     * Product entity đầu vào đã được fetch đủ thông tin.
     */
    private ProductResponse mapProductToResponseWithSpecs(Product product) {
        // 1. GỌI MAPPER CHÍNH (Mapper giờ nhận đủ variants/images từ product entity)
        ProductResponse response = productMapper.toResponse(product, product.getProductVariants(), product.getProductImages());

        // 2. Mapping specs (giữ nguyên logic cũ)
        String categoryName = product.getCategory() != null && product.getCategory().getName() != null
                                ? product.getCategory().getName().toLowerCase() : "";
        Integer productId = product.getId();

        Optional<ProductSpecsLaptop> laptopSpecsOpt = laptopSpecsRepository.findById(productId);
        Optional<ProductSpecsPhone> phoneSpecsOpt = phoneSpecsRepository.findById(productId);

        if (categoryName.contains("laptop") && laptopSpecsOpt.isPresent()) {
            ProductSpecsLaptop specs = laptopSpecsOpt.get();
            response.setSpecs(Map.of(
                    "cpu", Optional.ofNullable(specs.getCpu()).orElse("N/A"),
                    "ram", Optional.ofNullable(specs.getRam()).orElse("N/A"),
                    "storage", Optional.ofNullable(specs.getStorage()).orElse("N/A"),
                    "gpu", Optional.ofNullable(specs.getGpu()).orElse("N/A"),
                    "screenSize", Optional.ofNullable(specs.getScreenSize()).orElse("N/A")
            ));
        } else if (categoryName.contains("phone") && phoneSpecsOpt.isPresent()) {
             ProductSpecsPhone specs = phoneSpecsOpt.get();
             response.setSpecs(Map.of(
                     "screenSize", Optional.ofNullable(specs.getScreenSize()).orElse("N/A"),
                     "ram", Optional.ofNullable(specs.getRam()).orElse("N/A"),
                     "storage", Optional.ofNullable(specs.getStorage()).orElse("N/A"),
                     "battery", Optional.ofNullable(specs.getBattery()).orElse("N/A"),
                     "os", Optional.ofNullable(specs.getOs()).orElse("N/A")
             ));
        } else {
             response.setSpecs(Map.of());
        }

        return response;
    }
}
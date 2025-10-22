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
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.jpa.domain.Specification; // Import Specification
import com.example.demo.product.repository.ProductSpecification; // Import lớp Specification mới
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository; // Vẫn cần để tạo/cập nhật
    private final CategoryRepository categoryRepository; // Vẫn cần để tạo/cập nhật
    private final ProductSpecsLaptopRepository laptopSpecsRepository;
    private final ProductSpecsPhoneRepository phoneSpecsRepository;
    private final ProductMapper productMapper;

    private final ProductImageRepository imageRepository; // <-- THÊM REPO
    private final VariantRepository variantRepository;   // <-- THÊM REPO

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository,
                              CategoryRepository categoryRepository, ProductSpecsLaptopRepository laptopSpecsRepository,
                              ProductSpecsPhoneRepository phoneSpecsRepository, ProductMapper productMapper,
                              // --- THÊM VÀO CONSTRUCTOR ---
                              ProductImageRepository imageRepository,
                              VariantRepository variantRepository
                              ) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.laptopSpecsRepository = laptopSpecsRepository;
        this.phoneSpecsRepository = phoneSpecsRepository;
        this.productMapper = productMapper;
        // --- GÁN GIÁ TRỊ ---
        this.imageRepository = imageRepository;
        this.variantRepository = variantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable, String search, Integer categoryId, Integer brandId) {
        // Tạo Specification dựa trên các tham số lọc
        Specification<Product> spec = ProductSpecification.filterBy(search, categoryId, brandId);

        // Gọi repository với Specification và Pageable
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // Map kết quả sang DTO (cần fetch specs riêng)
         return productPage.map(this::mapProductToResponseWithSpecs); // Tái sử dụng helper cũ
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getRelatedProducts(Integer productId, int limit) {
        // 1. Lấy sản phẩm hiện tại để biết categoryId
        Product currentProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        if (currentProduct.getCategory() == null) {
            return List.of(); // Trả về danh sách rỗng nếu không có category
        }
        Integer categoryId = currentProduct.getCategory().getId();

        // 2. Tạo Pageable để giới hạn số lượng kết quả
        Pageable pageable = PageRequest.of(0, limit); // Lấy trang đầu tiên, tối đa 'limit' sản phẩm

        // 3. Gọi repository để tìm sản phẩm liên quan
        List<Product> related = productRepository.findRelatedProducts(categoryId, productId, pageable);

        // 4. Map sang DTO (bao gồm cả specs)
        return related.stream()
                .map(this::mapProductToResponseWithSpecs) // Tái sử dụng helper
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductById(Integer id) {
        // Không cần JOIN FETCH ở đây nữa vì có thể gây lỗi khi kết hợp Specification
         return productRepository.findById(id).map(this::mapProductToResponseWithSpecs);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Logic tạo product giữ nguyên vì cần lấy brand/category từ repo riêng
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + request.getBrandId()));
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        saveSpecsForProduct(savedProduct, request.getSpecs());

        // Gọi lại repo để lấy bản đầy đủ (bao gồm cả specs vừa lưu)
        // Hoặc có thể map trực tiếp nếu không cần specs ngay lập tức
        return getProductById(savedProduct.getId())
                 .orElseThrow(() -> new EntityNotFoundException("Could not retrieve created product with id: " + savedProduct.getId())); // Should not happen
    }

    @Override
    @Transactional
    public Optional<ProductResponse> updateProduct(Integer id, ProductRequest request) {
         // Logic cập nhật giữ nguyên
        return productRepository.findById(id) // Vẫn dùng findById thường để lấy entity gốc
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

                    // Sau khi lưu, fetch lại bản đầy đủ để trả về
                     return getProductById(updatedProduct.getId()).orElse(null); // Trả về null nếu có lỗi khi fetch lại
                });
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        // Logic xóa giữ nguyên
        if (productRepository.existsById(id)) {
            // ON DELETE CASCADE sẽ tự động xóa specs liên quan
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Helper Methods ---

    // saveSpecsForProduct giữ nguyên
    private void saveSpecsForProduct(Product product, Map<String, String> specs) {
        if (specs == null || specs.isEmpty()) {
            // Optional: Xóa specs cũ nếu map specs mới là null/empty?
             // laptopSpecsRepository.deleteById(product.getId());
             // phoneSpecsRepository.deleteById(product.getId());
            return;
        };

        String categoryName = product.getCategory().getName().toLowerCase();
        Integer productId = product.getId();

        // Xóa specs cũ trước khi lưu specs mới (đề phòng trường hợp đổi category)
        laptopSpecsRepository.deleteById(productId);
        phoneSpecsRepository.deleteById(productId);


        if (categoryName.contains("laptop")) {
            ProductSpecsLaptop laptopSpecs = new ProductSpecsLaptop(
                    productId,
                    specs.get("cpu"),
                    specs.get("ram"),
                    specs.get("storage"),
                    specs.get("gpu"),
                    specs.get("screenSize")
            );
             // Sử dụng saveAndFlush để đảm bảo dữ liệu được ghi ngay lập tức nếu cần đọc lại ngay
            laptopSpecsRepository.saveAndFlush(laptopSpecs);
        } else if (categoryName.contains("phone")) {
            ProductSpecsPhone phoneSpecs = new ProductSpecsPhone(
                    productId,
                    specs.get("screenSize"),
                    specs.get("ram"),
                    specs.get("storage"),
                    specs.get("battery"),
                    specs.get("os")
            );
            phoneSpecsRepository.saveAndFlush(phoneSpecs);
        }
    }

    // mapProductToResponseWithSpecs giữ nguyên logic lấy specs
    private ProductResponse mapProductToResponseWithSpecs(Product product) {
        // 1. Lấy danh sách variants và images cho sản phẩm này
        // Lưu ý: Việc này có thể gây ra N+1 query nếu gọi trong vòng lặp (như trong getAllProducts)
        // Cần tối ưu bằng cách fetch theo batch hoặc JOIN FETCH nếu cần hiệu năng cao hơn cho getAllProducts.
        // Đối với getProductById thì gọi riêng lẻ như này là chấp nhận được.
        List<ProductVariant> variants = variantRepository.findByProductId(product.getId());
        List<ProductImage> images = imageRepository.findByProductIdOrderByIsMainDesc(product.getId()); // Repo này đã có sẵn

        // 2. Gọi mapper chính để map thông tin cơ bản, giá và ảnh đại diện
        ProductResponse response = productMapper.toResponse(product, variants, images);

        // 3. Mapping specs (giữ nguyên logic cũ)
        String categoryName = product.getCategory() != null && product.getCategory().getName() != null
                                ? product.getCategory().getName().toLowerCase() : "";
        Integer productId = product.getId();

        Optional<ProductSpecsLaptop> laptopSpecsOpt = laptopSpecsRepository.findById(productId);
        Optional<ProductSpecsPhone> phoneSpecsOpt = phoneSpecsRepository.findById(productId);

        if (categoryName.contains("laptop") && laptopSpecsOpt.isPresent()) {
            // ... map specs laptop ...
             ProductSpecsLaptop specs = laptopSpecsOpt.get();
                response.setSpecs(Map.of(
                        "cpu", Optional.ofNullable(specs.getCpu()).orElse("N/A"),
                        "ram", Optional.ofNullable(specs.getRam()).orElse("N/A"),
                        "storage", Optional.ofNullable(specs.getStorage()).orElse("N/A"),
                        "gpu", Optional.ofNullable(specs.getGpu()).orElse("N/A"),
                        "screenSize", Optional.ofNullable(specs.getScreenSize()).orElse("N/A")
                ));
        } else if (categoryName.contains("phone") && phoneSpecsOpt.isPresent()) {
             // ... map specs phone ...
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
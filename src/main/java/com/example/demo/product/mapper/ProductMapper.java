package com.example.demo.product.mapper;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.BrandResponse;
import com.example.demo.product.dto.response.ProductCategoryResponse;
import com.example.demo.product.dto.response.ProductResponse;
import com.example.demo.product.entity.Product;
import org.springframework.stereotype.Component;
import com.example.demo.product.entity.ProductImage;
import com.example.demo.product.entity.ProductVariant;
import java.math.BigDecimal; // Import BigDecimal
import java.util.Comparator; // Import Comparator
import java.util.List;      // Import List
import java.util.Optional;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) return null;
        Product product = new Product();
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
        return product;
    }

    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) return;
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
    }

    public ProductResponse toResponse(Product product, List<ProductVariant> variants, List<ProductImage> images) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSkuPrefix(product.getSkuPrefix());
        response.setDescription(product.getDescription());
        response.setIsActive(product.getIsActive());

        // Mapping Brand và Category (giữ nguyên)
        if (product.getBrand() != null) {
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(product.getBrand().getId());
            brandResponse.setName(product.getBrand().getName());
            response.setBrand(brandResponse);
        }
        if (product.getCategory() != null) {
            ProductCategoryResponse categoryResponse = new ProductCategoryResponse();
            categoryResponse.setId(product.getCategory().getId());
            categoryResponse.setName(product.getCategory().getName());
            response.setCategory(categoryResponse);
        }

        // --- LOGIC LẤY GIÁ VÀ ẢNH ĐẠI DIỆN ---
        // Lấy giá từ variant đầu tiên (hoặc variant có ID nhỏ nhất nếu danh sách không có thứ tự)
        if (variants != null && !variants.isEmpty()) {
            // Sắp xếp theo ID để đảm bảo lấy variant đầu tiên một cách nhất quán (tùy chọn)
            variants.sort(Comparator.comparing(ProductVariant::getId));
            response.setPrice(variants.get(0).getPrice());
        } else {
             response.setPrice(BigDecimal.ZERO); // Hoặc null nếu muốn
        }

        // Lấy ảnh đại diện (ưu tiên isMain=true, sau đó là ảnh đầu tiên)
        if (images != null && !images.isEmpty()) {
            Optional<ProductImage> mainImage = images.stream()
                                                    .filter(img -> img.getIsMain() != null && img.getIsMain())
                                                    .findFirst();
            if (mainImage.isPresent()) {
                response.setImageUrl(mainImage.get().getImageUrl());
            } else {
                 // Nếu không có ảnh main, lấy ảnh đầu tiên theo displayOrder hoặc ID
                 images.sort(Comparator.comparing(ProductImage::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                                      .thenComparing(ProductImage::getId));
                response.setImageUrl(images.get(0).getImageUrl());
            }
        } else {
            response.setImageUrl(null); // Hoặc ảnh placeholder mặc định
        }
        // --- KẾT THÚC LOGIC ---

        // Mapping specs sẽ được thực hiện trong Service sau khi gọi mapper này

        return response;
    }

    // --- (Tùy chọn) Thêm overload nếu không muốn truyền variants/images ---
     public ProductResponse toResponse(Product product) {
         // Gọi hàm chính với list rỗng/null, giá và ảnh sẽ là mặc định
         return toResponse(product, null, null);
     }
}
package com.example.demo.product.controller;

import com.example.demo.product.entity.Brand;
import com.example.demo.product.repository.BrandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /**
     * Lấy tất cả các thương hiệu.
     * @return Danh sách tất cả các thương hiệu.
     */
    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    /**
     * Lấy thông tin một thương hiệu theo ID.
     * @param id ID của thương hiệu.
     * @return ResponseEntity chứa thương hiệu nếu tìm thấy, ngược lại trả về 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        return brandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Tạo một thương hiệu mới.
     * @param brand Đối tượng Brand chứa thông tin cần tạo.
     * @return Thương hiệu đã được tạo.
     */
    @PostMapping
    public Brand createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }

    /**
     * Cập nhật thông tin một thương hiệu.
     * @param id ID của thương hiệu cần cập nhật.
     * @param brandDetails Đối tượng Brand chứa thông tin mới.
     * @return ResponseEntity chứa thương hiệu đã được cập nhật, ngược lại trả về 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brandDetails) {
        return brandRepository.findById(id)
                .map(brand -> {
                    brand.setName(brandDetails.getName());
                    // Cập nhật các trường khác nếu có
                    Brand updatedBrand = brandRepository.save(brand);
                    return ResponseEntity.ok(updatedBrand);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa một thương hiệu theo ID.
     * @param id ID của thương hiệu cần xóa.
     * @return ResponseEntity với status 204 No Content nếu xóa thành công, ngược lại trả về 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        // Cách viết này rõ ràng và dễ hiểu hơn, tránh các lỗi suy luận kiểu phức tạp.
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()) {
            brandRepository.delete(optionalBrand.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

        /*
         * Cách viết gốc của bạn cũng đã chính xác và hoạt động tốt.
         * Trình biên dịch có thể suy luận được kiểu chung là ResponseEntity<Void>.
         * Giữ lại cách viết dưới đây nếu bạn thích phong cách lập trình hàm (functional style).
         */
        // return brandRepository.findById(id)
        //         .<ResponseEntity<Void>>map(brand -> {
        //             brandRepository.delete(brand);
        //             return ResponseEntity.noContent().build();
        //         })
        //         .orElse(ResponseEntity.notFound().build());
    }
}

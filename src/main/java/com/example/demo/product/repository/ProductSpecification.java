package com.example.demo.product.repository; // Đảm bảo package chính xác

import com.example.demo.product.entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filterBy(
            String search,
            Integer categoryId,
            Integer brandId
    ) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join để lọc (nếu dùng LAZY loading)
            Join<Object, Object> brandJoin = root.join("brand", JoinType.LEFT);
            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);

            // Lọc theo search
            if (StringUtils.hasText(search)) {
                String searchLower = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), searchLower);
                Predicate brandNamePredicate = cb.like(cb.lower(brandJoin.get("name")), searchLower);
                Predicate categoryNamePredicate = cb.like(cb.lower(categoryJoin.get("name")), searchLower);
                predicates.add(cb.or(namePredicate, brandNamePredicate, categoryNamePredicate));
            }

            // Lọc theo categoryId
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            // Lọc theo brandId
            if (brandId != null) {
                predicates.add(cb.equal(root.get("brand").get("id"), brandId));
            }

             // Luôn lọc sản phẩm active
             predicates.add(cb.isTrue(root.get("isActive")));
             predicates.add(cb.isFalse(root.get("isDeleted")));

            // Tối ưu: tránh fetch join khi query count
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                 root.fetch("brand", JoinType.LEFT);
                 root.fetch("category", JoinType.LEFT);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
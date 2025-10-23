package com.example.demo.sale.mapper;

// import com.example.demo.sale.dto.request.CreateOrderRequest; // Không cần import này ở đây nữa
import com.example.demo.sale.dto.response.OrderItemResponse;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import com.example.demo.user.entity.Customer; // Import Customer nếu cần lấy tên
import lombok.RequiredArgsConstructor; // Dùng @RequiredArgsConstructor thay @Autowired
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import java.math.BigDecimal; // Không cần nếu không tính toán ở đây
import java.util.Collections; // Import Collections
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor // Sử dụng constructor injection
public class OrderMapper {

    // Inject OrderItemMapper qua constructor (nhờ @RequiredArgsConstructor)
    private final OrderItemMapper orderItemMapper;

    // --- Phương thức toEntity không cần thiết nữa vì OrderServiceImpl tự tạo entity ---
    /*
    public Order toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }
        Order order = new Order();
        // Chỉ map các trường cơ bản nếu cần, logic chính ở service
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());
        // ... không nên map hết ở đây
        return order;
    }
    */

    // --- Sửa phương thức toDto (hoặc đổi tên thành toOrderResponse) ---
    public OrderResponse toDto(Order order) { // Giữ tên toDto khớp với các lời gọi hiện tại
        if (order == null) {
            return null;
        }

        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());

        // Map Customer ID (và có thể cả tên nếu cần)
        Customer customer = order.getCustomer();
        if (customer != null) {
            // Chuyển đổi Integer ID của Customer sang Long cho DTO
            dto.setCustomerId(customer.getId() != null ? customer.getId().longValue() : null);
            // dto.setCustomerName(customer.getFullname()); // Lấy tên nếu Customer entity có trường fullname
        }

        // Map các trường mới từ Order entity
        dto.setShippingAddress(order.getShippingAddress());
        dto.setReceiverFullname(order.getReceiverFullname());
        dto.setReceiverPhoneNumber(order.getReceiverPhoneNumber());
        dto.setNotes(order.getNotes());
        dto.setShippingMethod(order.getShippingMethod());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setShippingFee(order.getShippingFee());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTotalAmount(order.getTotalAmount()); // Map totalAmount
        dto.setStatus(order.getStatus()); // Map status (Enum)
        dto.setOrderDate(order.getOrderDate()); // Map orderDate (LocalDateTime)
        dto.setCreatedAt(order.getCreatedAt()); // Map createdAt (LocalDateTime)
        dto.setUpdatedAt(order.getUpdatedAt()); // Map updatedAt (LocalDateTime)

        // Bỏ map các trường cũ không còn tồn tại trong Order entity mới
        // if (order.getWarehouse() != null) { ... }
        // dto.setGrandTotal(order.getGrandTotal()); // Lỗi vì không có grandTotal
        // if (order.getHandledBy() != null) { ... }

        // Map danh sách OrderItem (giữ nguyên logic gọi OrderItemMapper)
        if (order.getItems() != null) {
            List<OrderItemResponse> itemResponses = order.getItems().stream()
                    .map(orderItemMapper::toDto) // Gọi toDto của OrderItemMapper
                    .collect(Collectors.toList());
            dto.setItems(itemResponses);
        } else {
            dto.setItems(Collections.emptyList()); // Trả về list rỗng nếu items là null
        }

        return dto;
    }

    // --- Sửa phương thức toDtoList ---
    public List<OrderResponse> toDtoList(List<Order> orders) {
        if (orders == null) {
            return Collections.emptyList(); // Trả về list rỗng thay vì null
        }
        return orders.stream()
                .map(this::toDto) // Gọi toDto đã sửa
                .filter(Objects::nonNull) // Lọc bỏ kết quả null nếu có lỗi map
                .collect(Collectors.toList());
    }
}
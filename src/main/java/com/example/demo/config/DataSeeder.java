//package com.example.demo.config;
//
//import com.example.demo.user.entity.Employee;
//import com.example.demo.user.entity.Role;
//import com.example.demo.user.entity.User;
//import com.example.demo.user.entity.UserStatus;
//import com.example.demo.user.repository.EmployeeRepository;
//import com.example.demo.user.repository.RoleRepository;
//import com.example.demo.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.Optional;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final EmployeeRepository employeeRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Kiểm tra xem user admin đã tồn tại chưa
//        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
//            System.out.println("--- Creating ADMIN user ---");
//
//            // Tìm hoặc tạo vai trò ADMIN
//            Role adminRole = roleRepository.findByName("ADMIN")
//                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").description("Quản trị viên").build()));
//
//            // Tạo User mới
//            User adminUser = User.builder()
//                    .email("admin@example.com")
//                    // Mã hóa mật khẩu bằng chính PasswordEncoder của ứng dụng
//                    .password(passwordEncoder.encode("adminpassword"))
//                    .status(UserStatus.ACTIVE)
//                    .build();
//
//            // Tạo Employee mới
//            Employee adminEmployee = Employee.builder()
//                    .user(adminUser) // Gán user vào employee
//                    .fullname("Admin User")
//                    .employeeCode("ADMIN001")
//                    .hiredDate(LocalDate.now())
//                    .active(true)
//                    .roles(Set.of(adminRole)) // Gán vai trò ADMIN
//                    .build();
//            
//            // Do có cascade, chỉ cần lưu employee là user cũng sẽ được lưu
//            employeeRepository.save(adminEmployee);
//
//            System.out.println("--- ADMIN user created successfully ---");
//        }
//    }
//}
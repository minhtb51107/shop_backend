package com.example.demo.config;

import com.example.demo.user.entity.Employee;
import com.example.demo.user.entity.Permission;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import com.example.demo.user.repository.EmployeeRepository;
import com.example.demo.user.repository.PermissionRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            System.out.println("--- Seeding Roles and Permissions ---");

            // Tạo các quyền cơ bản
            Permission userRead = permissionRepository.save(new Permission(null, "USER_READ", "Read user data"));
            Permission userWrite = permissionRepository.save(new Permission(null, "USER_WRITE", "Write user data"));
            Permission roleRead = permissionRepository.save(new Permission(null, "ROLE_READ", "Read role data"));
            Permission roleWrite = permissionRepository.save(new Permission(null, "ROLE_WRITE", "Write role data"));
            Permission productRead = permissionRepository.save(new Permission(null, "PRODUCT_READ", "Read product data"));
            Permission productWrite = permissionRepository.save(new Permission(null, "PRODUCT_WRITE", "Write product data"));
            // Thêm quyền cho module tài chính
            Permission viewFinance = permissionRepository.save(new Permission(null, "VIEW_FINANCE", "Xem dữ liệu tài chính"));
            Permission manageFinance = permissionRepository.save(new Permission(null, "MANAGE_FINANCE", "Quản lý dữ liệu tài chính"));


            // Tạo vai trò ADMIN với tất cả các quyền
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Quản trị viên hệ thống");
            adminRole.setPermissions(new HashSet<>(Arrays.asList(userRead, userWrite, roleRead, roleWrite, productRead, productWrite, viewFinance, manageFinance)));
            roleRepository.save(adminRole);


            // Tạo vai trò SALE_MANAGER
            Role saleManagerRole = new Role();
            saleManagerRole.setName("SALE_MANAGER");
            saleManagerRole.setDescription("Quản lý bán hàng");
            saleManagerRole.setPermissions(new HashSet<>(Arrays.asList(userRead, productRead, productWrite)));
            roleRepository.save(saleManagerRole);

            // Tạo vai trò CUSTOMER (không có quyền đặc biệt, chỉ là vai trò định danh)
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            customerRole.setDescription("Khách hàng");
            roleRepository.save(customerRole);


            System.out.println("--- Roles and Permissions seeded successfully ---");
        }

        if (userRepository.count() == 0) {
            System.out.println("--- Seeding Admin User ---");
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            
            User adminUser = User.builder()
                    .email("admin@shop.com")
                    .password(passwordEncoder.encode("admin123"))
                    .status(UserStatus.ACTIVE)
                    .build();

            Employee adminEmployee = Employee.builder()
                    .user(adminUser)
                    .fullname("Admininistrator")
                    .employeeCode("ADMIN001")
                    .hiredDate(LocalDate.now())
                    .active(true)
                    .roles(Set.of(adminRole))
                    .build();
            
            employeeRepository.save(adminEmployee);
            System.out.println("--- Admin User seeded successfully ---");
        }
    }
}
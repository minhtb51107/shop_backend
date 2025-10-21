package com.example.demo.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class ShopE2ETest extends BaseTest {

    // !!! THAY URL FRONTEND CỦA BẠN VÀO ĐÂY !!!
    private final String BASE_URL = "http://localhost:5173";

    /**
     * Test chức năng Đăng ký
     */
    @Test(priority = 1)
    public void testRegister() {
        driver.get(BASE_URL + "/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // SỬA LỖI: Sử dụng By.name(...) để tìm element, đáng tin cậy hơn
        WebElement fullNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fullname")));
        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement phoneInput = driver.findElement(By.name("phoneNumber"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement confirmPasswordInput = driver.findElement(By.name("confirmPassword"));
        WebElement agreeCheckbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
        WebElement registerButton = driver.findElement(By.xpath("//button[contains(., 'Đăng Ký')]"));

        // Điền thông tin
        String randomEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        fullNameInput.sendKeys("Test User E2E");
        emailInput.sendKeys(randomEmail);
        phoneInput.sendKeys("0987123456"); // Dùng SĐT ngẫu nhiên để tránh trùng
        passwordInput.sendKeys("password123");
        confirmPasswordInput.sendKeys("password123");
        agreeCheckbox.click();

        // Click nút đăng ký
        registerButton.click();

        // Kiểm tra kết quả: Chờ thông báo thành công (v-alert) xuất hiện
        WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'v-alert') and contains(., 'Đăng ký thành công')]")
        ));
        Assert.assertTrue(successAlert.isDisplayed(), "Thông báo đăng ký thành công không hiển thị.");
    }

    /**
     * Test chức năng Đăng nhập
     */
    @Test(priority = 2)
    public void testLogin() {
        driver.get(BASE_URL + "/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // SỬA LỖI: Sử dụng By.name(...)
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(., 'Đăng Nhập')]"));

        // Điền thông tin (dùng tài khoản đã có và đã được kích hoạt)
        // !!! ĐẢM BẢO TÀI KHOẢN NÀY TỒN TẠI TRONG BACKEND CỦA BẠN !!!
        emailInput.sendKeys("admin@shop.com");
        passwordInput.sendKeys("admin123");

        // Click đăng nhập
        loginButton.click();

        // Kiểm tra kết quả: Chờ avatar của user xuất hiện trong thanh điều hướng
        WebElement userAvatar = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//header//div[contains(@class, 'v-avatar')]")
        ));
        Assert.assertTrue(userAvatar.isDisplayed(), "Avatar người dùng không hiển thị sau khi đăng nhập.");
    }
    
    /**
     * Test CRUD: Cập nhật Profile
     */
    @Test(priority = 3, dependsOnMethods = "testLogin")
    public void testCRUD_UpdateProfile() {
        // Test này sẽ tự động chạy sau khi testLogin PASS.
        // Trình duyệt và trạng thái đăng nhập sẽ được giữ lại.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // (R)ead: Mở trang profile
        driver.get(BASE_URL + "/profile");

        // Chờ trang profile load xong
        WebElement fullNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.name("fullname")
        ));

        // (U)pdate: Cập nhật thông tin
        String newName = "Updated Name " + System.currentTimeMillis();
        fullNameInput.clear();
        fullNameInput.sendKeys(newName);

        WebElement saveButton = driver.findElement(By.xpath("//button[contains(., 'Lưu thay đổi')]"));
        saveButton.click();

        // Kiểm tra kết quả
        WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'v-alert') and contains(., 'Cập nhật hồ sơ thành công')]")
        ));
        Assert.assertTrue(successAlert.isDisplayed(), "Thông báo cập nhật thành công không hiển thị.");

        // Kiểm tra lại giá trị đã lưu bằng cách tải lại trang
        driver.navigate().refresh();
        WebElement fullNameAfterRefresh = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fullname")));
        Assert.assertEquals(fullNameAfterRefresh.getAttribute("value"), newName, "Tên trong input chưa được cập nhật sau khi tải lại trang.");
    }
}


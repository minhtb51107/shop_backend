package com.example.demo.e2e; // (Bạn có thể đổi package)

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod // TestNG: Chạy trước mỗi @Test
    public void setUp() {
        // Tự động tải và cài đặt ChromeDriver
        WebDriverManager.chromedriver().setup();

        // Cấu hình Chrome (tùy chọn, ví dụ: chạy ẩn danh)
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Chạy không mở giao diện
        options.addArguments("--start-maximized"); // Phóng to cửa sổ

        // Khởi tạo trình duyệt
        driver = new ChromeDriver(options);
        
        // Cài đặt thời gian chờ ngầm (chờ tối đa 10s nếu không tìm thấy element)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod // TestNG: Chạy sau mỗi @Test
    public void tearDown() {
        // Đóng trình duyệt sau khi test xong
        if (driver != null) {
            driver.quit();
        }
    }
}
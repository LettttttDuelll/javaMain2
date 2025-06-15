package com.example.laptopshop.ui;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.edge.EdgeDriver;
import java.io.FileInputStream;
import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DangKyTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    void setup() {
        System.setProperty("webdriver.edge.driver", "C:/msedgedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    void tearDown() {
        if (driver != null) driver.quit();
    }

    static Stream<Arguments> readTestDataFromExcel() throws Exception {
        try (FileInputStream fis = new FileInputStream(new File("src/main/resources/DangKy_TestInputs.xlsx"));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Arguments> testData = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                testData.add(Arguments.of(
                        i,
                        getCell(row, 1),
                        getCell(row, 2),
                        getCell(row, 3),
                        getCell(row, 4),
                        getCell(row, 5)
                ));
            }
            return testData.stream();
        }
    }

    private static String getCell(Row row, int col) {
        Cell cell = row.getCell(col);
        return (cell == null) ? "" : cell.toString().trim();
    }

    @ParameterizedTest(name = "TC{0}: Đăng ký với username={1}, email={4}")
    @MethodSource("readTestDataFromExcel")
    void testDangKy(int tcIndex, String username, String password, String rePassword, String email,String expectedResult ) {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("rePassword")).sendKeys(rePassword);

        driver.findElement(By.id("email")).sendKeys(email);


        driver.findElement(By.id("registerForm")).submit();

        try {
            // Đợi alert popup nếu có
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText().toLowerCase();
            alert.accept();

            if (expectedResult.equalsIgnoreCase("PASS")) {
                Assertions.assertTrue(alertText.contains("thành công"),
                        "Mong muốn thành công nhưng thất bại: " + alertText);
            } else {
                // Với trường hợp mong đợi fail, có thể check alert chứa lỗi
                Assertions.assertTrue(alertText.contains("thất bại") ||
                                alertText.contains("không hợp lệ") ||
                                alertText.contains("lỗi"),
                        "Mong muốn thất bại nhưng lại thành công: " + alertText);
            }
        } catch (TimeoutException e) {
            // Nếu không có alert, có thể check lỗi inline
            boolean isErrorPresent = false;

            // Ví dụ kiểm tra thông báo lỗi từng field
            if (!expectedResult.equalsIgnoreCase("PASS")) {
                if (!driver.findElements(By.id("Message_Username")).isEmpty()) {
                    String msgUser = driver.findElement(By.id("Message_Username")).getText();
                    isErrorPresent |= !msgUser.isEmpty();
                }
                if (!driver.findElements(By.id("Message_Password")).isEmpty()) {
                    String msgPass = driver.findElement(By.id("Message_Password")).getText();
                    isErrorPresent |= !msgPass.isEmpty();
                }
                if (!driver.findElements(By.id("Message_RePassword")).isEmpty()) {
                    String msgRePass = driver.findElement(By.id("Message_RePassword")).getText();
                    isErrorPresent |= !msgRePass.isEmpty();
                }
                if (!driver.findElements(By.id("Message_Email")).isEmpty()) {
                    String msgEmail = driver.findElement(By.id("Message_Email")).getText();
                    isErrorPresent |= !msgEmail.isEmpty();
                }

                Assertions.assertTrue(isErrorPresent, "Mong muốn thất bại nhưng không thấy lỗi hiển thị trên form");
            } else {
                // Nếu mong đợi PASS nhưng không có alert, bạn có thể kiểm tra URL hoặc trạng thái khác
                Assertions.fail("Mong muốn thành công nhưng không có alert hoặc thông báo");
            }
        }
    }
}

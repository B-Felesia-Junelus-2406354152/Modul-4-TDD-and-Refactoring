package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setUpTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrder_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");

        // Verify we are on the create order page
        String createPageTitle = driver.getTitle();
        assertEquals("Create New Order", createPageTitle);

        // Fill in author name
        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.clear();
        authorInput.sendKeys("Felesia");

        // Submit the form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect, assuming it redirects to either order history or somewhere. Let's just assume it redirects to a page where "Create New Order" is not the title anymore
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("Create New Order")));
    }

    @Test
    void orderHistory_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/history");

        // Verify we are on the history input page
        String historyPageTitle = driver.getTitle();
        assertEquals("Order History", historyPageTitle);

        // Fill in author name
        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.clear();
        authorInput.sendKeys("Felesia");

        // Submit the form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect to order list page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleIs("Order List"));

        // Verify it shows an order list page
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Order List"));
    }
}

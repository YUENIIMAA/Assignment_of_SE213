import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class demo {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","/Users/Shared/chromedriver");
        WebDriver webDriver = new ChromeDriver();
        String url = "https://www.baidu.com/";
        webDriver.get(url);
        webDriver.quit();
    }
}

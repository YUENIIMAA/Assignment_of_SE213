import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class testAdvancedSearch {
    public static void run(WebDriver webDriver) {
        System.out.print("[info] test advanced search\n");

        webDriver.navigate().refresh();

        String url = "https://dev.bbs.sjtu.edu.cn/search?expanded=true";
        webDriver.get(url);

        utils.wait(500);

        WebDriverWait wait = new WebDriverWait(webDriver, 10);

        WebElement keywordInput = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.id("ember17"));
            }
        });

        WebElement titleCheckbox = webDriver.findElement(By.id("ember35"));
        WebElement likesCheckbox = webDriver.findElement(By.id("ember36"));
        WebElement personalCheckbox = webDriver.findElement(By.id("ember37"));
        WebElement seenCheckbox = webDriver.findElement(By.id("ember38"));

        /* test select them one by one */
        int point = 0;
        titleCheckbox.click();
        if (keywordInput.getAttribute("value").equals("in:title")) point++;
        titleCheckbox.click();
        likesCheckbox.click();
        if (keywordInput.getAttribute("value").equals("in:likes")) point++;
        likesCheckbox.click();
        personalCheckbox.click();
        if (keywordInput.getAttribute("value").equals("in:personal")) point++;
        personalCheckbox.click();
        seenCheckbox.click();
        if (keywordInput.getAttribute("value").equals("in:seen")) point++;
        seenCheckbox.click();

        if (point < 4) {
            System.out.print("[info] single check failed\n");
        } else {
            System.out.print("[error] single check passed\n");
        }

        /* test select them all at once */
        titleCheckbox.click();
        likesCheckbox.click();
        personalCheckbox.click();
        seenCheckbox.click();
        if (keywordInput.getAttribute("value").equals("in:title in:likes in:personal in:seen"))
            System.out.print("[info] multiple check passed\n");
        else
            System.out.print("[info] multiple check failed\n");

        System.out.print("[info] test advanced search done\n");
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class testCreateTopic {
    public static void run(WebDriver webDriver) {
        System.out.print("[info] test create topic\n");

        String url = "https://dev.bbs.sjtu.edu.cn/";
        webDriver.get(url);

        WebDriverWait wait = new WebDriverWait(webDriver, 10);

        WebElement createTopicButton= wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.id("create-topic"));
            }
        });
        createTopicButton.click();

        WebElement titleInput = webDriver.findElement(By.id("reply-title"));
        titleInput.clear();
        titleInput.sendKeys("SE213-软件测试-WEB-GUI-测试用例");

        webDriver.findElement(By.className("category-input")).click();
        WebElement typeButton = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/section/div/div[6]/div[3]/div[1]/div[2]/div[2]/div[1]/div[2]/ul[2]/li[1]"));
            }
        });
        wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body/section/div/div[6]/div[3]/div[1]/div[2]/div[2]/div[1]/div[1]"), "aria-expanded", "true"));
        typeButton.click();

        WebElement textInput = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/section/div/div[6]/div[3]/div[2]/div/div[1]/div[1]/textarea"));
            }
        });
        //try {
        //    Thread.sleep(500);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        textInput.sendKeys("这是一条完全没有意义的主题，仅用于完成论坛的WEB-GUI测试，打扰了。");

        WebElement sendButton = webDriver.findElement(By.xpath("/html/body/section/div/div[6]/div[3]/div[3]/div/button"));

        /* in order to prevent polluting the bbs */
        /* we stop actually sending the topic after several successful attempts */
        /* sendButton.click(); */

        /* instead, we click cancel */
        webDriver.findElement(By.className("cancel")).click();

        /* and delete the draft */
        WebElement cancelButton = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/div[4]/div[2]/a[1]"));
            }
        });
        cancelButton.click();

        System.out.print("[info] test create topic done\n");
    }
}

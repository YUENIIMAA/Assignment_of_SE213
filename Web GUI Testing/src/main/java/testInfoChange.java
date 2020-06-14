import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class testInfoChange {
    public static void run(WebDriver webDriver) {
        System.out.print("[info] test account info change\n");

        String url = "https://dev.bbs.sjtu.edu.cn/u/" + jaccount.getUsername() + "/preferences/account";
        webDriver.get(url);

        WebDriverWait wait = new WebDriverWait(webDriver, 10);

        /* wait until the change avatar button is available */
        WebElement changeAvatarButton = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.id("ember50"));
            }
        });
        /* click change avatar button */
        changeAvatarButton.click();

        /* click the close button to test model dialogue box */
        WebElement closeDialogueButton = webDriver.findElement(By.id("ember18"));
        closeDialogueButton.click();
        wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "user-preferences-page"));

        /* open it again */
        changeAvatarButton.click();

        /* choose custom to test radio box */
        WebElement defaultAvatarRadio = wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/section/div/div[4]/div/div/div/div[3]/div[1]/input"));
            }
        });
        WebElement gravatarRadio = webDriver.findElement(By.xpath("/html/body/section/div/div[4]/div/div/div/div[3]/div[2]/input"));
        WebElement customAvatarRadio = webDriver.findElement(By.xpath("/html/body/section/div/div[4]/div/div/div/div[3]/div[3]/input"));
        defaultAvatarRadio.click();
        gravatarRadio.click();
        customAvatarRadio.click();

        /* choose a local file to test upload */
        WebElement chooseAvatarButton = webDriver.findElement(By.xpath("/html/body/section/div/div[4]/div/div/div/div[3]/div[3]/span/label/input"));
        chooseAvatarButton.sendKeys("/Users/Shared/avatar.png");

        /* wait until upload is finished */
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/section/div/div[4]/div/div/div/div[4]/button")));

        WebElement uploadAvatarButton = webDriver.findElement(By.xpath("/html/body/section/div/div[4]/div/div/div/div[4]/button"));
        uploadAvatarButton.click();

        /* wait until modification is saved */
        wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "user-preferences-page"));

        System.out.print("[info] test account info change done\n");
    }
}

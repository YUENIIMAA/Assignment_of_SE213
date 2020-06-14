import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class jaccount {
    private static String username;

    private static String password;

    private static boolean loginStatus;

    public static void login(WebDriver webDriver) {
        System.out.print("[info] login with jaccount\n");

        int attemptTimes = 0;

        /* prepare tesseract */
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/Users/Shared/tessdata");
        /* note: captcha images generated have a fixed dpi of 96 */
        tesseract.setTessVariable("user_defined_dpi", "96");

        while (jaccount.loginStatus != true && attemptTimes < 10) {
            /* print attempt time */
            System.out.print("[info] attempt " + attemptTimes + " start\n");

            /* get captcha-related elements */
            WebElement captchaElement = webDriver.findElement(By.id("captcha-img"));

            /* extract captcha image src from element */
            String captchaUrl = captchaElement.getAttribute("src");
            System.out.print("[info] catpcha image src: " + captchaUrl + "\n");

            URL captchaSrc;
            try {
                captchaSrc = new URL(captchaUrl);
            } catch (MalformedURLException e) {
                System.out.print("[error] form captcha url failed\n");
                return;
            }

            InputStream captchaFile = null;
            BufferedImage captchaImage = null;
            String captchaCode;
            try {
                captchaFile = captchaSrc.openStream();
                captchaImage = ImageIO.read(captchaFile);
                captchaCode = tesseract.doOCR(captchaImage);
                captchaFile.close();
                captchaCode = captchaCode.replaceAll(" ", "");
                captchaCode = captchaCode.replaceAll("I", "l");
                captchaCode = captchaCode.replaceAll("0", "o");
                captchaCode = captchaCode.replaceAll("1", "l");
                captchaCode = captchaCode.toLowerCase();
            } catch (FileNotFoundException e) {
                System.out.print("[error] download captcha failed\n");
                return;
            } catch (IOException e) {
                System.out.print("[error] read captcha failed\n");
                return;
            } catch (TesseractException e) {
                System.out.print("[error] scan captcha failed\n");
                return;
            }
            System.out.print("[info] captcha scan result: " + captchaCode + "\n");

            /* insert username */
            WebElement usernameInput = webDriver.findElement(By.id("user"));
            usernameInput.clear();
            usernameInput.sendKeys(jaccount.username);

            /* insert password */
            WebElement passwordInput = webDriver.findElement(By.id("pass"));
            passwordInput.clear();
            passwordInput.sendKeys(jaccount.password);

            /* insert captcha */
            WebElement captchaInput = webDriver.findElement(By.id("captcha"));
            captchaInput.clear();
            captchaInput.sendKeys(captchaCode);

            if (webDriver.getCurrentUrl().startsWith("https://jaccount.sjtu.edu.cn/")) {
                System.out.print("[info] attempt " + attemptTimes + " failed\n");
                attemptTimes++;
            } else {
                System.out.print("[info] attempt " + attemptTimes + " succeeded\n");
                jaccount.loginStatus = true;
            }
        }

        if (loginStatus == false) {
            System.out.print("[info] final attempt, please input captcha here manually\n");
            Scanner userInput = new Scanner(System.in);
            String captchaCode = userInput.next();
            captchaCode += "\n";

            /* insert username */
            WebElement usernameInput = webDriver.findElement(By.id("user"));
            usernameInput.clear();
            usernameInput.sendKeys(jaccount.username);

            /* insert password */
            WebElement passwordInput = webDriver.findElement(By.id("pass"));
            passwordInput.clear();
            passwordInput.sendKeys(jaccount.password);

            /* insert captcha */
            WebElement captchaInput = webDriver.findElement(By.id("captcha"));
            captchaInput.clear();
            captchaInput.sendKeys(captchaCode);

            if (webDriver.getCurrentUrl().startsWith("https://jaccount.sjtu.edu.cn/")) {
                System.out.print("[info] login failed\n");
            } else {
                System.out.print("[info] final attempt succeeded\n");
                jaccount.loginStatus = true;
            }
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        System.out.print("[info] jaccount username set to: " + username + "\n");
        jaccount.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        System.out.print("[info] jaccount password set to: " + password + "\n");
        jaccount.password = password;
    }

    public static boolean isLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(boolean loginStatus) {
        jaccount.loginStatus = loginStatus;
    }
}

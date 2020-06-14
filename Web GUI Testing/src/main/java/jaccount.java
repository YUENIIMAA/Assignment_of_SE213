import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.*;

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

        /* try smooth login */
        while (attemptTimes < 10) {
            /* print attempt time */
            System.out.print("[info] attempt " + attemptTimes + " start\n");

            /* get captcha-related elements */
            WebElement captchaElement = webDriver.findElement(By.id("captcha-img"));

            /* get screenshot as bytes */
            byte[] screenShot = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES);

            /* get location and size information of the captcha */
            Rectangle rect = captchaElement.getRect();

            /* make the screenshot in bytes an input stream */
            InputStream inputStream = new ByteArrayInputStream(screenShot);
            BufferedImage screenShotImage = null;
            BufferedImage captchaImage = null;
            String captchaCode;
            try {
                /* read input stream as an image */
                /* thus nothing needs to be written to the disk */
                screenShotImage = ImageIO.read(inputStream);
                inputStream.close();

                /* extract the captcha from the screenshot and perform OCR */
                /* note that this time the image the same as the one in the browser */
                captchaImage = screenShotImage.getSubimage((int)2.20 * rect.getX(),(int)2.22 * rect.getY(), (int)2.22 * rect.getWidth(), (int)2.22 * rect.getHeight());
                captchaCode = tesseract.doOCR(captchaImage);

                /* perform extra correction */
                captchaCode = captchaCode.replaceAll(" ", "");
                captchaCode = captchaCode.replaceAll("I", "l");
                captchaCode = captchaCode.replaceAll("0", "o");
                captchaCode = captchaCode.replaceAll("1", "l");
                captchaCode = captchaCode.toLowerCase();
            } catch (IOException e) {
                System.out.print("[error] IO-related operation failed\n");
                attemptTimes++;
                continue;
            } catch (TesseractException e) {
                System.out.print("[error] scan captcha failed\n");
                attemptTimes++;
                continue;
            }
            System.out.print("[info] captcha scan result: " + captchaCode.replace("\n", "") + "\n");

            /* insert username */
            WebElement usernameInput = webDriver.findElement(By.id("user"));
            usernameInput.clear();
            usernameInput.sendKeys(jaccount.username);

            /* insert password */
            WebElement passwordInput = webDriver.findElement(By.id("pass"));
            passwordInput.clear();
            passwordInput.sendKeys(jaccount.password);

            /* insert captcha */
            /* note that the result returned by tesseract is followed by '\n', thus no need to click button */
            WebElement captchaInput = webDriver.findElement(By.id("captcha"));
            captchaInput.clear();
            captchaInput.sendKeys(captchaCode);

            if (webDriver.getCurrentUrl().startsWith("https://jaccount.sjtu.edu.cn/")) {
                System.out.print("[info] attempt " + attemptTimes + " failed\n");
                attemptTimes++;
            } else {
                System.out.print("[info] attempt " + attemptTimes + " succeeded\n");
                jaccount.loginStatus = true;
                return;
            }
        }
        System.out.print("[info] all smooth attempts failed, switch to another mode\n");

        /* try force login */
        while (attemptTimes < 20) {
            /* print attempt time */
            System.out.print("[info] attempt " + attemptTimes + " start\n");

            /* get captcha-related elements */
            WebElement captchaElement = webDriver.findElement(By.id("captcha-img"));

            /* extract captcha image src from element */
            String captchaUrl = captchaElement.getAttribute("src");
            System.out.print("[info] catpcha image src: " + captchaUrl + "\n");

            /* make image src an URL */
            URL captchaSrc;
            try {
                captchaSrc = new URL(captchaUrl);
            } catch (MalformedURLException e) {
                System.out.print("[error] form captcha url failed\n");
                return;
            }

            /* fetch captcha image and perform OCR */
            /* note that this image is very likely to be different from the one in the browser */
            InputStream captchaFile = null;
            BufferedImage captchaImage = null;
            String captchaCode;
            try {
                captchaFile = captchaSrc.openStream();
                captchaImage = ImageIO.read(captchaFile);
                captchaCode = tesseract.doOCR(captchaImage);
                captchaFile.close();

                /* perform extra correction */
                captchaCode = captchaCode.replaceAll(" ", "");
                captchaCode = captchaCode.replaceAll("I", "l");
                captchaCode = captchaCode.replaceAll("0", "o");
                captchaCode = captchaCode.replaceAll("1", "l");
                captchaCode = captchaCode.toLowerCase();
            } catch (FileNotFoundException e) {
                System.out.print("[error] download captcha failed\n");
                attemptTimes++;
                continue;
            } catch (IOException e) {
                System.out.print("[error] read captcha failed\n");
                attemptTimes++;
                continue;
            } catch (TesseractException e) {
                System.out.print("[error] scan captcha failed\n");
                attemptTimes++;
                continue;
            }
            System.out.print("[info] captcha scan result: " + captchaCode.replace("\n", "") + "\n");

            /* insert username */
            WebElement usernameInput = webDriver.findElement(By.id("user"));
            usernameInput.clear();
            usernameInput.sendKeys(jaccount.username);

            /* insert password */
            WebElement passwordInput = webDriver.findElement(By.id("pass"));
            passwordInput.clear();
            passwordInput.sendKeys(jaccount.password);

            /* insert captcha */
            /* note that the result returned by tesseract is followed by '\n', thus no need to click button */
            WebElement captchaInput = webDriver.findElement(By.id("captcha"));
            captchaInput.clear();
            captchaInput.sendKeys(captchaCode);

            if (webDriver.getCurrentUrl().startsWith("https://jaccount.sjtu.edu.cn/")) {
                System.out.print("[info] attempt " + attemptTimes + " failed\n");
                attemptTimes++;
            } else {
                System.out.print("[info] attempt " + attemptTimes + " succeeded\n");
                jaccount.loginStatus = true;
                return;
            }
        }
        System.out.print("[info] all automatic attempts failed, switch to manual mode\n");

        while (loginStatus == false) {
            /* print attempt time */
            System.out.print("[info] attempt " + attemptTimes + " start\n");

            System.out.print("[info] please input captcha here manually\n");
            Scanner userInput = new Scanner(System.in);
            String captchaCode = userInput.next();
            /* add '\n' so that there's no need to click the button */
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
                System.out.print("[info] attempt " + attemptTimes + " failed\n");
            } else {
                System.out.print("[info] attempt " + attemptTimes + " succeeded\n");
                jaccount.loginStatus = true;
                return;
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

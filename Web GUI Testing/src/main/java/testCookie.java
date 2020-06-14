import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.util.Set;

public class testCookie {
    public static void run(WebDriver webDriver) {
        System.out.print("[info] test cookie\n");

        utils.wait(500);
        String url = "https://dev.bbs.sjtu.edu.cn/";
        webDriver.get(url);
        utils.wait(500);

        System.out.println("cookies:\n");
        printCookies(webDriver);

        webDriver.navigate().refresh();
        utils.wait(500);
        System.out.println("after refresh:\n");
        printCookies(webDriver);

        webDriver.manage().deleteAllCookies();
        utils.wait(500);
        System.out.println("after delete all cookies:\n");
        printCookies(webDriver);

        webDriver.navigate().refresh();
        System.out.println("current url:\n");
        System.out.println(webDriver.getCurrentUrl());

        System.out.print("[info] test cookie done\n");
    }

    public static void printCookies(WebDriver dr) {
        Cookie cookie = new Cookie("name", "value");
        dr.manage().addCookie(cookie);

        Set<Cookie> cookies = dr.manage().getCookies();

        System.out.println(String
                .format("Domain -> name -> value -> expiry -> path"));

        for (Cookie c : cookies)
            System.out.println(String.format("%s -> %s -> %s -> %s -> %s",
                    c.getDomain(), c.getName(), c.getValue(), c.getExpiry(),c.getPath()));

        System.out.println("<------------------------SEPARATOR------------------------>");
    }
}

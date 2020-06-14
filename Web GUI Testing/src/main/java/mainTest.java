import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class mainTest {

    /* note: please set your program arguments to jaccount's username and password */
    public static void main(String[] args) {

        /* check if the number of arguments is correct */
        if (args.length != 2) {
            System.out.print("[warning] invalid args\n");
            return;
        }

        /* setup jaccount information */
        jaccount.setUsername(args[0]);
        jaccount.setPassword(args[1]);
        jaccount.setLoginStatus(false);

        System.out.print("[info] start web gui testing\n");
        System.setProperty("webdriver.chrome.driver","/Users/Shared/chromedriver");
        WebDriver chrome = new ChromeDriver();

        String url = "https://dev.bbs.sjtu.edu.cn";
        chrome.get(url);

        jaccount.login(chrome);

        infoChangeTest.run();

        /* keep it running for now */
        /*chrome.quit();*/
    }


}

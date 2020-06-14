public class utils {
    public static void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            System.out.print("[error] unexpected error\n");
        }
    }
}

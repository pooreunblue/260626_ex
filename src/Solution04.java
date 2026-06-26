import oop.App;
import oop.AppProvider;
import oop.AppService;

public class Solution04 {
    public static void main(String[] args) throws Exception { // 실행하는 주체.
        AppProvider appProvider = new AppProvider();
        AppService appService = new AppService(appProvider);
        App app = new App(appService);
        try {
            app.run();
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            // e.getMessage() = java.lang.Exception: AppProvider.run.exception
            // 화면이라고 가정하면 -> 적절한 메시지나 타입. 유저의 잘못인지? 서버의 잘못인지? 천재지변인지? Exception으로 분류.
        }

    }
}
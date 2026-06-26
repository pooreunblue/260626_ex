package oop;

public class AppService {
    private AppProvider appProvider;

    public AppService(AppProvider appProvider) {
        this.appProvider = appProvider;
    }

    //    public void run() throws Exception {
    public void run() {
        System.out.println("AppService.run");
        try {
            appProvider.run();
        } catch (Exception e) {
            System.out.println("e.getClass() = " + e.getClass());
            System.out.println("AppService.run.exception.catch");
            System.out.println("rethrow");
//            throw e; // checked라면 throws 포함하시키면서 rethrow를 하거나 unchecked로 변형
            throw new RuntimeException(e); //     Throwable cause
//            e.printStackTrace();
            // 가장 최신의 감싸진 exception부터 가장 깊은 원인이 된 것까지를 출력
        }
        System.out.println("AppService.run.complete");
    }
}
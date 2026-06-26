package oop;

public class App {
    private AppService appService;

    public App(AppService appService) {
        this.appService = appService;
    }

    public void run() throws Exception {
        System.out.println("App.run");
        appService.run();
        System.out.println("App.run.complete");
    }
}
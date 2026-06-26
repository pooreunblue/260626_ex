package oop;

public class AppProvider {
    //    public void run() { // Runtime이라면
    public void run() throws Exception {
        System.out.println("AppProvider.run");
        throw new Exception("AppProvider.run.exception"); // checked
//        throw new RuntimeException("AppProvider.run.runtimeException"); // unchecked
        // 이걸로 되어 있지 않음
    }
}
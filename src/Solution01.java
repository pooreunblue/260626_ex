import java.net.http.HttpClient;

// Exception
public class Solution01 {
    public static void main(String[] args) {
//        Runtime Exception
//        int a = 1 / 0;
        int a = 10;
        int b = (int) (Math.random() * 2 - 1); // 입력이나 프로그래밍 과정에서 달라져서 발생할 수 있는 예외
        System.out.println(a / b);
        // Exception in thread "main" java.lang.ArithmeticException: / by zero
        // public class ArithmeticException extends RuntimeException
//        int a = 100; // 재선언 에러

        // throws java.io.IOException, InterruptedException <- 대표적인 체크드
        HttpClient client = HttpClient.newHttpClient();
//        client.send(); // 메서드 시그니처에 throws가 붙어있고, 그것이 checked 라면 (runtime이 아니라면) try-catch으로 명시적 대응 필요
//        method1(); // 1) try-catch 2) throws -> main 혹은 우리가 직접 소스코드에서 만나는 최상단.
        try {
            method2(); // 내가 만약에 이 단계에서 혹시 발생할지 모르는 Exception을 처리해도 된다
            // 의무적이지 않음 (unchecked)
        } catch (IllegalArgumentException e) {

        }
    }

    // checked exception
//    static void method1() {
    static void method1() throws Exception {
        // 1
        try {
            throw new Exception(); // checked는 무조건 대응이 되어야함
        } catch (Exception e) {
        }
        // 2
        throw new Exception(); // checked는 무조건 대응이 되어야함
        // 이걸 호출해서 쓰는 메서드에 '위임'시킬 수 있음.
    }

    // unchecked exception
//    static void method2() throws IllegalArgumentException { // 강제 아님. 딱히 상단에서 처리하지 않아도 된다
    static void method2() {
        throw new IllegalArgumentException();
    }
}
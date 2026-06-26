import java.util.Scanner;

public class Solution03 {
    public static void main(String[] args) {
        run1();
        run2();
    }

    static void run1() {
        try {
            Scanner sc = new Scanner(System.in);
            int a = sc.nextInt();
            int b = sc.nextInt();
            System.out.println(a / b);
            sc.close(); // 에러 시 여기에 도달하지 못함
            System.out.println("자원 반납완료");
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    static void run2() {
//        Scanner sc; // 선언만 한 상태는 객체가 부여된 것이 아니므로 close 반환 X
        Scanner sc = null; // 강제로 null이라도 넣어서 활성화되었다고 가정
        // 스코프 문제 때문에 일반적으로 NullPointerException이 나옴.
        try {
            sc = new Scanner(System.in);
            int a = sc.nextInt();
            int b = sc.nextInt();
            System.out.println(a / b);
        } catch (Exception e) {
            System.out.println("Exception");
        } finally {
            // 스코프 문제가 생김
            sc.close();
            System.out.println("자원 반납완료");
        }
    }

    static void run3() {
        // try () <- try-with-resources
        try (Scanner sc = new Scanner(System.in)) {
            // ...
        } // 알아서 finally에서 close가 호출된다는 것을 보장
    }
}

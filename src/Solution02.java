import java.util.Random;

public class Solution02 {
    public static void main(String[] args) {
//        throw new CustomException(); // Exception이 checked이므로
        // throws or exception block
        try {
            Random random = new Random();
            int r = random.nextInt(4); // 끝점 제외 (0,9)
//            int r = random.nextInt(1, 10); // 끝점 제외 (1,9)
//            boolean f = random.nextBoolean(); // t, f 반반
//            if (f) {
            if (r == 0) {
                throw new CustomException(); // 발생하면 뒤로 안 감
            }
            if (r == 1) {
                throw new CustomException("뭔가 잘못됐어 단단히");
            }
            if (r == 2) {
                // unchecked 이기에 catch에 안잡힘
                // + checked라고 하더라도 상위 개념인 Exception이 있으면 알아서 처리해버림
                throw new UncheckedCustomException();
            }
            throw new Exception("그냥 익셉션");
            // } catch (Exception e) {
            // Exception 'CustomException' has already been caught
        } catch (CustomException | UncheckedCustomException e) {
//        } catch (CustomException e) { // Exception을 상속했기 때문에
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e); // 체크만 푸는 느낌으로 보면 된다
//        } catch (UncheckedCustomException e) {
//            System.out.println("e = " + e);
            System.out.println("e.getMessage() = " + e.getMessage());
            System.out.println("e.getClass() = " + e.getClass());
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            System.out.println("e.getClass() = " + e.getClass());
            System.out.println("더 넓은 걸 커버하는 게 뒤로 오거나 서로 상속 등으로 업캐스팅 관계가 아닐 것");
        }
    }
}

// Exception
// RuntimeException
// -> Custom Exception

class CustomException extends Exception {
    public final int code;
    private static final String defaultMessage = "너 무슨 짓을 저지른 거야?";

    CustomException() {
        super(defaultMessage);
        code = 100;
    }

    CustomException(String message) {
        super(message);
        code = 101;
    }

    CustomException(String message, int code) {
        super(message);
        this.code = code;
    }
}

class UncheckedCustomException extends RuntimeException {
}
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// 바이트 스트림, 문자 스트림.
public class Solution05 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String text = sc.nextLine();
            System.out.println("text = " + text);
            saveByByteStream(text);
        }
    }

    static void saveByByteStream(String text) {
        String fileName = "output-byte.txt";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(text.getBytes(StandardCharsets.UTF_8));
            System.out.println("바이트 스트림으로 파일에 저장 완료");
        } catch (IOException e) {
            // throws로 넣는 선택
            // runtime으로 던질 거냐
            // 여기서 마무리 지을 거냐
            throw new RuntimeException(e);
        }
    }
}
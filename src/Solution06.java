import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Solution06 {
    public static void main(String[] args) {
        String charFileName = "output-char.txt";
        String text = "안녕하세요 Java입니다 잘 부탁드립니다";
        saveByCharStream(text, charFileName);
        loadByCharStream(charFileName);
    }

    private static void saveByCharStream(String text, String charFileName) {
        try (FileWriter writer = new FileWriter(charFileName)) {
            writer.write(text);
            System.out.println("문자 스트림 작성 완료");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadByCharStream(String charFileName) {
        try (FileReader reader = new FileReader(charFileName)) {
            int c;
            while ((c = reader.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
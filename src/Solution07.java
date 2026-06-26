import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution07 {
    public static void main(String[] args) {
        String file1 = "file1.txt";
        writeTextWithBuffer(file1);
        readTextWithBuffer(file1);
        String file2 = "file2.txt";
        useScannerWithBuffer(file2);
    }

    private static void useScannerWithBuffer(String file) {
        List<String> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(System.in);) {
            System.out.println("[저장할 텍스트를 입력하세요] 저장은 w");
            while (true) {
                String input = sc.nextLine();
                if (input.equals("w")) {
                    break;
                }
                System.out.println(input);
                lines.add(input);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Path path = Paths.get(file);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void readTextWithBuffer(String file) {
        Path path = Paths.get(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeTextWithBuffer(String file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
            writer.write("반갑습니다\n"); // \n : 개행문자(줄바꿈)
            writer.write("반갑습니다");
            writer.newLine(); // \n
            writer.write("JDK17+로 작업중");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
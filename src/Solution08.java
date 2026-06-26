import java.io.BufferedWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Solution08 {
    public static void main(String[] args) {
        // https://developers.naver.com/docs/serviceapi/search/image/image.md#%EC%9D%B4%EB%AF%B8%EC%A7%80
        // 다운로드를 위해서 URL 리스트를 받음
        String keyword = "창억떡";
        List<String> urlList = getImageUrlList(keyword);
        System.out.println("urlList = " + urlList);
        // URL 리스트를 자체를 파일로 저장
        saveUrlList(urlList, keyword);
        // 이미지를 파일 형태로 각각 다운로드
        downloadImages(urlList, keyword);
    }

    private static void downloadImages(List<String> urlList, String keyword) {
        HttpClient httpClient = HttpClient.newHttpClient();
        for (String url : urlList) {
            System.out.println("url = " + url);
            String[] tmp = url.split("\\.");
            String ext = tmp[tmp.length - 1]; // 확장자 추출
            if (ext.length() > 4) { // 일반적인 이미지 확장자는 4자리까지인데, 5자리 이상 나온다면 확장자 X
//                throw new RuntimeException("확장자 오류");
                System.out.println("확장자 오류");
                continue;
            }
            Path path = Paths.get("%s-%d.%s".formatted(keyword, System.currentTimeMillis(), ext));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            try {
                HttpResponse<Path> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofFile(path));
                System.out.println("response = " + response.uri());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void saveUrlList(List<String> urlList, String keyword) {
        String fileName = "%s-%s.txt".formatted(
//                URLEncoder.encode(keyword, StandardCharsets.UTF_8), // 가장 파일명상 안전함
                keyword.replace(" ", "_"), // 보기 좋음
                ZonedDateTime.now(
                                ZoneId.of("Asia/Seoul"))
                        .format(java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd_HHmmss"))
        );
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//            writer.write(String.join("\n", urlList));
            for (String url : urlList) {
                // \ -> 생략해버림. \\ <- 문자로 인식함
//                writer.write(url.replace("\\/", "/"));
                writer.write(url);
                writer.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getImageUrlList(String keyword) {
        String clientId = System.getenv("NAVER_CLIENT_ID");
        String clientSecret = System.getenv("NAVER_CLIENT_SECRET");
        if (clientId == null || clientSecret == null) {
            throw new RuntimeException("인증정보가 없습니다");
        }
        System.out.println("clientId = " + clientId.substring(0, 4) + "*".repeat(8));
        System.out.println("clientSecret = " + clientSecret.substring(0, 4) + "*".repeat(8));
//        https://developers.naver.com/apps/#/list
        List<String> urlList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://openapi.naver.com/v1/search/image?query=%s&display=%d&start=%d&sort=sim"
//                .formatted(keyword, 5, 1);
                .formatted(URLEncoder.encode(keyword, StandardCharsets.UTF_8), 5, 1);
        HttpRequest request = HttpRequest.newBuilder()
//                .GET() // 기본
                .uri(URI.create(url))
                .headers("X-Naver-Client-Id", clientId, "X-Naver-Client-Secret", clientSecret)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("response = " + response.body());
            String body = response.body();
            // 정규표현식
            // https://regexr.com/
            // https://regex101.com/
            // 정규표현식, String -> 강제적으로 JSON을 해석 -> Jackson/ObjectMapper
            Pattern pattern = Pattern.compile("\"link\":\"(.*?)\"");
            for (MatchResult matchResult : pattern.matcher(body).results().toList()) {
                // ( ) <- Group(1)
                System.out.println("matchResult = " + matchResult.group(1));
//                urlList.add(matchResult.group(1));
                urlList.add(matchResult.group(1).replace("\\/", "/"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        return List.of();
        return urlList;
    }
}
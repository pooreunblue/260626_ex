import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Solution08 {
    public static void main(String[] args) {
        // https://developers.naver.com/docs/serviceapi/search/image/image.md#%EC%9D%B4%EB%AF%B8%EC%A7%80
        // 다운로드를 위해서 URL 리스트를 받음
        String keyword = "창억떡";
        List<String> urlList = getImageUrlList(keyword);
        System.out.println("urlList = " + urlList);
        // URL 리스트를 자체를 파일로 저장
        // 이미지를 파일 형태로 각각 다운로드
        // github actions를 사용해서 외부에서 다운로드 받을 수 있게
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
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://openapi.naver.com/v1/search/image?query=%s&display=%d&start=%d&sort=sim"
                .formatted(URLEncoder.encode(keyword, StandardCharsets.UTF_8), 5, 1);
        HttpRequest request = HttpRequest.newBuilder()
//                .GET() // 기본
                .uri(URI.create(url))
                .headers("X-Naver-Client-Id", clientId, "X-Naver-Client-Secret", clientSecret)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response = " + response.body());
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

        return List.of();
    }
}
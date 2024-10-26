package main.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class GoogleBooksAPI {
    // Thay YOUR_GOOGLE_API_KEY bằng key API thật của bạn.
    private static final String API_KEY = "AIzaSyAsosNdLv_cAgIN6EaS27kx8a7SlmetS_I";

    public static void searchBookByISBN(String isbn) {
        int retryCount = 0;
        int maxRetries = 5;
        int backoffTime = 1000; // Start with 1 second backoff

        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Error: ISBN is empty or null");
            return;
        }

        while (retryCount < maxRetries) {
            try {
                // Xây dựng URL với API key và ISBN
                URI uri = new URI("https", "www.googleapis.com", "/books/v1/volumes",
                        "q=isbn:" + isbn + "&key=" + API_KEY, null);

                URL url = uri.toURL(); // Chuyển đổi từ URI sang URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // Nếu trả về 200 OK
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // In ra kết quả API trả về
                    System.out.println("Response from Google Books API: " + response.toString());
                    break; // Thành công, thoát vòng lặp

                } else if (responseCode == 429) {
                    // Nếu bị giới hạn số lượng request, chờ và thử lại
                    System.out.println("Rate limited. Retrying after " + backoffTime / 1000 + " seconds...");
                    Thread.sleep(backoffTime);
                    retryCount++;
                    backoffTime *= 2; // Exponential backoff tăng thời gian chờ
                } else {
                    // In ra lỗi chi tiết khi gặp phải lỗi khác
                    System.out.println("Failed request with Error code: " + responseCode);
                    BufferedReader errorStream = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorStream.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorStream.close();
                    System.out.println("Error response from API: " + errorResponse.toString());
                    throw new RuntimeException("HTTP GET Request Failed with Error code : " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (retryCount == maxRetries) {
            System.out.println("Failed to retrieve book data after maximum retries.");
        }
    }
}

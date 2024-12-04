package main.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * The {@code GoogleBooksAPI} class provides functionality to search for books
 * using the Google Books API based on an ISBN number. It handles API requests,
 * response processing, and implements retry logic with exponential backoff
 * in case of rate limiting.
 */
public class GoogleBooksAPI {
    /**
     * Loads environment variables from a .env file.
     */
    static Dotenv dotenv = Dotenv.load();

    /**
     * The API key used to authenticate requests to the Google Books API.
     */
    private static final String API_KEY = dotenv.get("API_KEY");

    /**
     * Searches for a book by its ISBN using the Google Books API.
     * <p>
     * This method constructs the appropriate API request URL, handles the HTTP
     * connection, processes the response, and implements retry logic with
     * exponential backoff in case of rate limiting (HTTP 429).
     * </p>
     *
     * @param isbn The ISBN number of the book to search for. Must not be null or empty.
     * @throws RuntimeException if the HTTP GET request fails with an error code other than 429.
     */
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

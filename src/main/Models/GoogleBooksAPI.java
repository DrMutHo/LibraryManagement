package main.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // For using streams (this will be removed for JsonArray)
import io.github.cdimascio.dotenv.Dotenv;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleBooksAPI {
    // Thay YOUR_GOOGLE_API_KEY bằng key API thật của bạn.
    static Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("API_KEY");

    public static StringBuilder searchBook(String token) {
        int retryCount = 0;
        int maxRetries = 5;
        int backoffTime = 1000; // Start with 1 second backoff
        String encoded = token.replace(" ", "+");
        encoded.toLowerCase();

        if (encoded == null || encoded.trim().isEmpty()) {
            System.out.println("Error: Search field is empty or null");
            return null;
        }

        while (retryCount < maxRetries) {
            try {
                // Build the URL with the API key and ISBN
                URI uri = new URI("https", "www.googleapis.com", "/books/v1/volumes",
                        "q=" + encoded + "&key=" + API_KEY, null);

                URL url = uri.toURL(); // Convert URI to URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // If response is OK (200)
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response;  // Return the response after reading the content
                } else if (responseCode == 429) {
                    // If rate limited, wait and try again
                    System.out.println("Rate limited. Retrying after " + backoffTime / 1000 + " seconds...");
                    Thread.sleep(backoffTime);
                    retryCount++;
                    backoffTime *= 2; // Exponential backoff
                } else {
                    // Handle other response errors
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

        return null; // Return null if all retries failed
    }

    public static List<Book> ParseData(String query) {
        StringBuilder response = searchBook(query);
        
        if (response == null || response.length() == 0) {
            System.out.println("No response data received.");
            return new ArrayList<>();
        }

        try {
            // Parse the response string into a JsonObject
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray itemsArray = jsonResponse.getAsJsonArray("items");

            // Create a list to store the Book objects
            List<Book> bookInfoList = new ArrayList<>();

            // Loop through each item in the itemsArray
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                // Extract book details
                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No Title";
                
                // Convert JsonArray of authors to a List of Strings manually
                String authors = "No Authors";  // Default value
                if (volumeInfo.has("authors")) {
                    JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
                    List<String> authorList = new ArrayList<>();
                    for (int j = 0; j < authorsArray.size(); j++) {
                        authorList.add(authorsArray.get(j).getAsString()); // Add each author as a String
                    }
                    authors = String.join(", ", authorList);  // Join the authors into a single string
                }
                
                String isbn = "No ISBN";  // Default value in case ISBN is not found
                if (volumeInfo.has("industryIdentifiers")) {
                    JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                    for (int j = 0; j < identifiers.size(); j++) {
                        JsonObject identifier = identifiers.get(j).getAsJsonObject();
                        if (identifier.get("type").getAsString().equalsIgnoreCase("ISBN_13") || 
                            identifier.get("type").getAsString().equalsIgnoreCase("ISBN_10")) {
                            isbn = identifier.get("identifier").getAsString();
                            break;  // Stop once we find an ISBN (either ISBN_10 or ISBN_13)
                        }
                    }
                }

                // Extracting genre from the volumeInfo (if available)
                String genre = "No Genre";  // Default value in case genre is not found
                if (volumeInfo.has("categories")) {
                    JsonArray categories = volumeInfo.getAsJsonArray("categories");
                    if (categories.size() > 0) {
                        genre = categories.get(0).getAsString();  // Get the first genre from the categories array
                    }
                }

                String language = volumeInfo.has("language") ? volumeInfo.get("language").getAsString() : "Unknown Language";
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No Description";
                int publicationYear = volumeInfo.has("publishedDate") ? getPublicationYear(volumeInfo.get("publishedDate").getAsString()) : 0;
                String imageUrl = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                        ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : "";

                // Create Book object with default values for book_id, average_rating, and review_count
                Book bookInfo = new Book(0, title, authors, isbn, genre, language, description, publicationYear, imageUrl, 0, 0);
                bookInfoList.add(bookInfo);
            }

            return bookInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error parsing the API response.");
        }

        return new ArrayList<>();
    }

    // Helper method to extract the publication year from a date string (e.g., "2020-01-01")
    private static int getPublicationYear(String date) {
        try {
            String[] parts = date.split("-");
            if (parts.length > 0) {
                return Integer.parseInt(parts[0]);  // Return the year part
            }
        } catch (Exception e) {
            System.out.println("Error parsing publication year: " + e.getMessage());
        }
        return 0; // Default year if parsing fails
    }

    private static void EditBookBrowsing(Book book){
        
    }
}

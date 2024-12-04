package main.Controllers.Admin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import main.Models.Notification;
import main.Models.NotificationRequest;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Models.BookCopy;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookDetailWithReviewController {

    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount, labelDescription;
    @FXML
    private TableView<BookReview> reviewTable;
    @FXML
    private TableColumn<BookReview, String> colRating;
    @FXML
    private TableColumn<BookReview, String> colComment;
    @FXML
    private TableColumn<BookReview, String> colReviewDate;
    @FXML
    private TableColumn<BookReview, String> colReviewer;

    private Book currentBook;

    public void setBook(Book book) {
        this.currentBook = book;
        displayBookDetails();
        loadReviews();
    }

    private void displayBookDetails() {
        labelTitle.textProperty().bind(Bindings.concat(currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat(currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ",
                currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        if (currentBook.getPublication_year() == -1) {
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: No Publication Year",
                            currentBook.publication_yearProperty().asString()));
        } else
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: ",
                            currentBook.publication_yearProperty().asString()));
        labelAverageRating.textProperty()
                .bind(Bindings.createStringBinding(() -> getStars(currentBook.average_ratingProperty().get()),
                        currentBook.average_ratingProperty()));
        labelAverageRating.setStyle("-fx-text-fill: gold;");
        labelReviewCount.textProperty()
                .bind(Bindings.concat("(", currentBook.review_countProperty().asString(), " ratings)"));

        labelDescription.textProperty().bind(Bindings.concat(currentBook.descriptionProperty()));

        if (currentBook.getImagePath() != null && !currentBook.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(currentBook.getImagePath()));
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + currentBook.getImagePath());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }

    private void loadReviews() {
        ObservableList<BookReview> reviews = FXCollections.observableArrayList(
                Model.getInstance().getDatabaseDriver().getAllReviewsForBook(currentBook.getBook_id()));
        reviewTable.setItems(reviews);

        colRating.setCellValueFactory(cellData -> {
            double rating = cellData.getValue().getRating();
            String stars = getStars(rating);
            return new javafx.beans.property.SimpleStringProperty(stars);
        });

        colReviewer.setCellValueFactory(cellData -> {
            String clientName = Model.getInstance().getDatabaseDriver()
                    .getClientNameById(cellData.getValue().getClientId());
            return new javafx.beans.property.SimpleStringProperty(clientName);
        });

        // Cột Comment: Sử dụng TextFlow để hiển thị comment dài
        colComment.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComment()));

        // Tùy chỉnh Cell của cột Comment để sử dụng TextFlow
        colComment.setCellFactory(column -> {
            return new TableCell<BookReview, String>() {
                private final TextFlow textFlow = new TextFlow(); // Tạo TextFlow cho comment

                {
                    textFlow.setMaxWidth(Double.MAX_VALUE); // Đảm bảo TextFlow chiếm hết chiều rộng của cell
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Text text = new Text(item); // Tạo Text từ comment
                        text.setWrappingWidth(300); // Đặt chiều rộng để tự động xuống dòng
                        textFlow.getChildren().clear(); // Làm sạch các phần tử cũ trong TextFlow
                        textFlow.getChildren().add(text); // Thêm Text vào TextFlow
                        setGraphic(textFlow); // Gắn TextFlow vào cell

                        // Tính toán chiều cao của TextFlow và điều chỉnh chiều cao của TableCell
                        double height = getTextHeight(item);
                        setPrefHeight(height); // Cập nhật chiều cao của cell
                    }
                }

                // Tính toán chiều cao cần thiết cho comment
                private double getTextHeight(String comment) {
                    Text text = new Text(comment);
                    text.setWrappingWidth(300); // Đặt chiều rộng để nội dung tự động xuống dòng
                    text.setFont(Font.getDefault()); // Sử dụng font mặc định
                    double height = text.getLayoutBounds().getHeight();
                    return Math.max(40, height); // Đảm bảo chiều cao tối thiểu cho cell
                }
            };
        });

        // Cột Review Date: Định dạng ngày tháng
        colReviewDate.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getReviewDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
    }

    private String getStars(double rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}

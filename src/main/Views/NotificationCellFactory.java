package main.Views;

import main.Controllers.Client.NotificationCellController;
import main.Models.Notification;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class NotificationCellFactory extends ListCell<Notification> {

    @Override
    protected void updateItem(Notification notification, boolean empty) {
        super.updateItem(notification, empty);

        if (empty || notification == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Client/NotificationCell.fxml"));
                NotificationCellController controller = new NotificationCellController(notification);
                loader.setController(controller);

                setText(null);
                setGraphic(loader.load());

                notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                    updateItem(notification, false);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

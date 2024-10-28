package main.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ViewFactory {
    private AccountType loginAccountType;

    public ViewFactory() {
        this.loginAccountType = AccountType.CLIENT;
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        try {
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/resources/Images/icon.png"))));
        } catch (Exception e) {
            System.out.print("Không tìm thấy ảnh" + e.getMessage());
            e.printStackTrace();

        }
        stage.setResizable(false);
        stage.setTitle("ELib");
        stage.show();
    }

    public void closeStage(Stage stage) {
            stage.close();
    }
    
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showSignupWindow(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Signup.fxml"));
        createStage(loader);
    }
}

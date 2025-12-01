

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent;   
import javafx.scene.Scene;

public class Controller {

    @FXML
    private Button hostSession;

    @FXML
    private Button joinSessionButton;


    @FXML
    void onHSBtnClick(ActionEvent event) {
        Server.start();
    }
    private Stage mainWindow;
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    void onJSBtnClick(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ipSelection.fxml"));
            Parent ipSelectionRoot = loader.load();

            // Create a new Scene with the loaded FXML
            System.out.println("Server started. Waiting for two clients...");
            Scene ipSelectionScene = new Scene(ipSelectionRoot, 700, 700);

            // Display the new Scene
            mainWindow.setScene(ipSelectionScene);
            mainWindow.setTitle("Join Session"); // Update the title

        } catch (IOException e) {
            System.err.println("Error loading ipSelection.fxml:");
            e.printStackTrace();
        }
    }

    void openChatScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lockndip_chatroom.fxml"));
            Parent chatRoot = loader.load();

            Scene chatScene = new Scene(chatRoot, 1159, 768);

            mainWindow.setScene(chatScene);
            mainWindow.setTitle("Chat");

        } catch (IOException e) {
            System.err.println("Error loading chat.fxml:");
            e.printStackTrace();
        }
    }

}

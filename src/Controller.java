import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

    private Stage mainWindow;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    void onHSBtnClick(ActionEvent event) {
        // Start the server first
        Task<Void> serverTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Starting server...");
                
                // Start server (it will accept 2 clients)
                ServerRunner.startServer();
                Thread.sleep(500);
                return null;
            }
        };

        serverTask.setOnSucceeded(e -> {
            System.out.println("Server started successfully");
            // After server starts, connect as first client
            Platform.runLater(() -> {
                loadChatPageAsHost();
            });
        });

        serverTask.setOnFailed(e -> {
            System.err.println("Server task failed");
            serverTask.getException().printStackTrace();
            Platform.runLater(() -> {
                backToStartScreen();
            });
        });

        Thread serverThread = new Thread(serverTask);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @FXML
    void onJSBtnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ipSelection.fxml"));
            Parent ipSelectionRoot = loader.load();

            // Pass the main window to the IP selection controller
            IpSelectionController ipController = loader.getController();
            ipController.setMainWindow(mainWindow);

            Scene ipSelectionScene = new Scene(ipSelectionRoot, 700, 700);
            mainWindow.setScene(ipSelectionScene);
            mainWindow.setTitle("Join Session");

        } catch (IOException e) {
            System.err.println("Error loading ipSelection.fxml:");
            e.printStackTrace();
        }
    }

    private void loadChatPageAsHost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
            Parent chatRoot = loader.load();
            
            ChatController chatController = loader.getController();
            chatController.setMainWindow(mainWindow);
            // Host connects to their own server on localhost
            chatController.setHostMode(true);
            chatController.connectToServer("localhost", 1234);
            
            Scene chatScene = new Scene(chatRoot, 700, 700);
            mainWindow.setScene(chatScene);
            mainWindow.setTitle("Chat Room - Host");
            
        } catch (IOException e) {
            System.err.println("Error loading chat.fxml:");
            e.printStackTrace();
        }
    }

    private void backToStartScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
            Parent root = loader.load();
            
            Controller controller = loader.getController();
            controller.setMainWindow(mainWindow);
            
            Scene scene = new Scene(root, 700, 700);
            mainWindow.setScene(scene);
            mainWindow.setTitle("Lock N Dip");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
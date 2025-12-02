import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class IpSelectionController {

    @FXML
    private Button backButton;

    @FXML
    private Button connectButton;

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Label statusLabel;

    private Stage mainWindow;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    private void initialize() {
        // Set default values
        if (portField != null) {
            portField.setText("1234");
        }
        if (ipAddressField != null) {
            ipAddressField.setText("localhost");
        }
    }

    @FXML
    private void onConnectBtnClick(ActionEvent event) {
        String host = ipAddressField.getText().trim();
        String portText = portField.getText().trim();

        if (host.isEmpty() || portText.isEmpty()) {
            if (statusLabel != null) {
                statusLabel.setText("Please enter both IP address and port");
            }
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            
            // Load chat page and connect
            loadChatPageAndConnect(host, port);
            
        } catch (NumberFormatException e) {
            if (statusLabel != null) {
                statusLabel.setText("Invalid port number");
            }
        }
    }

    @FXML
    private void onBackBtnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
            Parent root = loader.load();

            Controller controller = loader.getController();
            controller.setMainWindow(mainWindow);

            mainWindow.setScene(new Scene(root, 700, 700));
            mainWindow.setTitle("Lock N Dip");

        } catch (IOException e) {
            System.err.println("Error loading startScreen.fxml:");
            e.printStackTrace();
        }
    }

    private void loadChatPageAndConnect(String host, int port) {
    try {
        System.out.println("Attempting to load: chat.fxml");
        
        // Check if resource exists
        if (getClass().getResource("chat.fxml") == null) {
            System.err.println("ERROR: chat.fxml not found!");
            System.err.println("Looking in: " + getClass().getResource("").getPath());
            if (statusLabel != null) {
                statusLabel.setText("Error: chat.fxml not found");
            }
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        System.out.println("FXMLLoader created, attempting to load...");
        
        Parent chatRoot = loader.load();
        System.out.println("FXML loaded successfully");
        
        ChatController chatController = loader.getController();
        
        if (chatController == null) {
            System.err.println("ERROR: ChatController is null - check fx:controller in FXML");
            if (statusLabel != null) {
                statusLabel.setText("Error: Controller not loaded");
            }
            return;
        }
        
        System.out.println("ChatController loaded successfully");
        
        chatController.setMainWindow(mainWindow);
        chatController.setHostMode(false);
        chatController.connectToServer(host, port);
        
        Scene chatScene = new Scene(chatRoot, 700, 700);
        mainWindow.setScene(chatScene);
        mainWindow.setTitle("Chat Room");
        
    } catch (IOException e) {
        System.err.println("Error loading chat.fxml:");
        e.printStackTrace();
        if (statusLabel != null) {
            statusLabel.setText("Error loading chat interface: " + e.getMessage());
        }
    } catch (Exception e) {
        System.err.println("Unexpected error:");
        e.printStackTrace();
        if (statusLabel != null) {
            statusLabel.setText("Unexpected error: " + e.getMessage());
        }
    }
}
}
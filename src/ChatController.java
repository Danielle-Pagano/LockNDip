import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ChatController implements Client.ClientMessageListener {
    
    @FXML
    private TextArea chatArea;
    
    @FXML
    private TextField messageField;
    
    @FXML
    private Button sendButton;

    @FXML
    private Button exitButton;
    
    private Client client;
    private Stage mainWindow;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setHostMode(boolean isHost) {
        // Don't do anything here - connection is handled explicitly
        // by the controller calling connectToServer()
    }

    @FXML
    public void initialize() {
        // Initialize client with this controller as listener
        client = new Client(this);
        sendButton.setDisable(true); // Disable until connected
    }

    public void connectToServer(String host, int port) {
        chatArea.appendText("Connecting to " + host + ":" + port + "...\n");
        client.connect(host, port);
    }

    @FXML
    void onSendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && client.isConnected()) {
            // Display your own message
            chatArea.appendText("You: " + message + "\n");
            
            // Send to server
            client.sendMessage(message);
            
            // Clear input field
            messageField.clear();
        }
    }

    @FXML
    void onExitChat() {
        client.sendMessage("exit");
        client.disconnect();
        backToStartScreen();
    }

    // ClientMessageListener implementation
    @Override
    public void onMessageReceived(String message) {
        chatArea.appendText(message + "\n");
    }

    @Override
    public void onConnected() {
        chatArea.appendText("✅ Connected to chat room!\n");
        chatArea.appendText("Chat started! Type 'exit' to leave.\n\n");
        sendButton.setDisable(false);
    }

    @Override
    public void onDisconnected() {
        chatArea.appendText("\n❌ Disconnected from server.\n");
        sendButton.setDisable(true);
    }

    @Override
    public void onConnectionFailed(String error) {
        chatArea.appendText("❌ Connection failed: " + error + "\n");
        sendButton.setDisable(true);
    }

    private void backToStartScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
            Parent root = loader.load();
            
            Controller controller = loader.getController();
            controller.setMainWindow(mainWindow);
            
            mainWindow.setScene(new Scene(root, 700, 700));
            mainWindow.setTitle("Lock N Dip");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
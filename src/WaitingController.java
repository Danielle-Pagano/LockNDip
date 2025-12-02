import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WaitingController {
    
    @FXML
    private Label statusLabel;
    
    private Stage mainWindow;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    public void initialize() {
        if (statusLabel != null) {
            statusLabel.setText("Waiting for clients to connect...");
        }
    }
}
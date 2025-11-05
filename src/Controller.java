import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class Controller {

    @FXML
    private Button hostSession;

    @FXML
    private Button joinSessionButton;

    @FXML
    void onHSBtnClick(ActionEvent event) {

    }
private Stage mainWindow; // Define mainWindow as a variable of type Stage

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    void onJSBtnClick(ActionEvent event) {
        // Example code just to check it works
mainWindow.setTitle("Changed name example");
    }

}

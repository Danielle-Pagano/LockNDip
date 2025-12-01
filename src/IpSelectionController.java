import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IpSelectionController {

    @FXML
    private Button backButton;

    @FXML
    private void onBackBtnClick(ActionEvent event) throws IOException {
        // Load the start screen again
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
        Parent root = loader.load();

        // Get current window
        Stage stage = (Stage) backButton.getScene().getWindow();

        // Reattach the original controller so its buttons still work
        Controller controller = loader.getController();
        controller.setMainWindow(stage);

        // Switch back to start screen
        stage.setScene(new Scene(root, 700, 700));
        stage.setTitle("Lock N Dip");
        stage.show();
    }
}

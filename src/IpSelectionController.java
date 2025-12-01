import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class IpSelectionController {

    @FXML
    private Button backButton;

    @FXML
    private void onBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();

        Controller controller = loader.getController();
        controller.setMainWindow(stage);

        stage.setScene(new Scene(root, 700, 700));
        stage.setTitle("Lock N Dip");
        stage.show();
    }
}

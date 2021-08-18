package calendar.sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddCalendarController implements Initializable {

    private FXMLDocumentController mainController ;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }

    @FXML
    private Label topLabel;
    @FXML
    private Label exit;
    @FXML
    private JFXTextField calendarName;
    @FXML
    private JFXComboBox<String> startYear;
    @FXML
    private JFXComboBox<String> endYear;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton generate;
    @FXML
    private JFXButton cancel;

    private double xOffset;
    private double yOffset;
    //TODO
    FileWriter fileWriter;
    {
        try {
            fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/calendar/sample/calendar.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private AnchorPane rootPane;
    @FXML
    void generateNewCalendar(MouseEvent event) {
        if ( (date.getValue() != null) && (!calendarName.getText().isEmpty())) {

            Model.getInstance().calendar_start_date = "" + date.getValue();
            Model.getInstance().calendar_start = date.getValue().getYear();
            Model.getInstance().calendar_end = date.getValue().getYear() + 1;
            Model.getInstance().calendar_name = calendarName.getText();
            Alert alertMessage = new Alert(AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Календар успішно створено");
            alertMessage.showAndWait();
            mainController.calendarGenerate();
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING, "Будь ласка, заповність усі поля.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        topLabel.setOnMousePressed(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        topLabel.setOnMouseDragged(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        topLabel.setOnMouseEntered(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setCursor(Cursor.HAND);
        });

        topLabel.setOnMouseExited(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setCursor(Cursor.DEFAULT);
        });

    }

    @FXML
    private void exit(MouseEvent event) {

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(MouseEvent event) {

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    public void Save(FileWriter fileWriter, String e ) throws IOException
    {
        fileWriter.write(e);
    }

}

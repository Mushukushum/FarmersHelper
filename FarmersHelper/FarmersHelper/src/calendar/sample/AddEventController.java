package calendar.sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddEventController implements Initializable {
    public static HashTable<Integer, Event> hashTable = new HashTable<>();
    private FXMLDocumentController mainController ;
    FileWriter fileWriter;
    boolean same;
    {
        try {
            fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/calendar/sample/event.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    EditRuleController editRuleController = new EditRuleController();

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }

    @FXML
    private Label topLabel;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXDatePicker date;

    private double xOffset;
    private double yOffset;

    @FXML
    void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(MouseEvent event) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(subject.getText().isEmpty()||termSelect.getSelectionModel().isEmpty()
                ||date.getValue() == null){
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Будь ласка, заповність усі поля");
            alertMessage.showAndWait();
            return;
        }

        String calendarDate = date.getValue().format(myFormat);
        String eventSubject = subject.getText();
        String term = termSelect.getValue();

            hashTable.put(hashTable.size(), new Event(Integer.parseInt(term), eventSubject, calendarDate));
            mainController.repaintView();
            try {
                saveToFile(term, eventSubject, calendarDate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    void autofillDatePicker() {

        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;

        date.setValue(LocalDate.of(year, month, day));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        autofillDatePicker();

        ObservableList<String> terms = editRuleController.getListOfTerms();
        //TODO
        termSelect.setItems(terms);

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
            scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
        });
    }

public void saveToFile(String term, String description, String date)
        throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("event.txt", true));
    writer.append("\n");
    writer.append(term);
    writer.append("\n");
    writer.append(description);
    writer.append("\n");
    writer.append(date);
    writer.append("\n");

    writer.close();
}

}


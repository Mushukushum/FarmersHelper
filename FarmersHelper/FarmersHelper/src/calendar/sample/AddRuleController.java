package calendar.sample;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

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

public class AddRuleController implements Initializable {

    //--------------------------------------------------------------------
    EditRuleController editRuleController = new EditRuleController();
    private FXMLDocumentController mainController ;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    // -------------------------------------------------------------------

    @FXML
    private Label topLabel;
    @FXML
    private JFXTextField eventDescript;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXTextField daysFromStart;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane rootPane;

    private double xOffset;
    private double yOffset;
    FileWriter fileWriter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> terms = editRuleController.getListOfTerms();
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
            scene.setCursor(Cursor.DEFAULT);
        });
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(MouseEvent event) {

        if(eventDescript.getText().isEmpty()||termSelect.getSelectionModel().isEmpty()
                ||daysFromStart.getText().isEmpty()){
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Будь ласка, заповність усі поля.");
            alertMessage.showAndWait();
            return;
        }
        Scanner auxScanner = new Scanner(daysFromStart.getText());
        if (!auxScanner.hasNextInt())
        {
            Alert alertMessage = new Alert(Alert.AlertType.WARNING);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Кількість днів для виконання має бути цілим числом");
            alertMessage.showAndWait();
            return;
        }
        String eventDescription = eventDescript.getText();
        String days = daysFromStart.getText();
        String term = termSelect.getValue();

        saveRule(eventDescription, term, Integer.parseInt(days));
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void saveRule(String eventDescription, String termName, int daysFromStart)
    {
        try {
            Save(eventDescription, termName, daysFromStart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Save(String eventDescription, String termName, int daysFromStart) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter("rules.txt", true));
        writer.append("\n");
        writer.append(eventDescription);
        writer.append("\n");
        writer.append(termName);
        writer.append("\n");
        writer.append(String.valueOf(daysFromStart));
        writer.append("\n");

        writer.close();
    }

}


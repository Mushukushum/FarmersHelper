package calendar.sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditEventController implements Initializable {
    private FXMLDocumentController mainController;
    // -------------------------------------------------------------------
    EditRuleController editRuleController = new EditRuleController();
    int editEventDefinition;
    //--------------------------------------------------------------------
    @FXML
    private Label topLabel;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXDatePicker date;
    @FXML
    private AnchorPane rootPane;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    AddEventController add = new AddEventController();
    private double xOffset;
    private double yOffset;

    void autofillDatePicker() {
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String descript = Model.getInstance().event_subject;
        String stringDate = String.valueOf(year + '-' + month + '-' + day);

        String selectedPriority = "";
        for(int i=0;i<AddEventController.hashTable.size();i++)
        {
            if(AddEventController.hashTable.get(i).getDate().equals(stringDate)&& AddEventController.hashTable.get(i).getSubject().equals(descript))
            {
                selectedPriority = AddEventController.hashTable.get(i).getTerm();
            }
        }

        date.setValue(LocalDate.of(year, month, day));

        subject.setText(descript);

        termSelect.getSelectionModel().select(selectedPriority);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        autofillDatePicker();
        ObservableList<String> termsList;
        termsList = editRuleController.getListOfTerms();
        termSelect.setItems(termsList);

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
    private void update(MouseEvent event) {
        updateEvent();
    }

    @FXML
    private void delete(MouseEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Діалог підтвердження дії");
        alert.setHeaderText("Вилучення запису");
        alert.setContentText("Ви точно хочете видалити даний запис?");

        ButtonType buttonTypeYes = new ButtonType("Так");
        ButtonType buttonTypeNo = new ButtonType("Ні");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes){
            try {
                deleteEvent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            alert.close();
        }
        else
        {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }

    }

    public void updateEvent(){

        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String descript = Model.getInstance().event_subject;
        String stringDate = year + "-" + String.format("%02d",month) + "-" + String.format("%02d",day);


        String newCalendarDate = date.getValue().format(myFormat);
        String newEventSubject = subject.getText();
        String term = termSelect.getValue();

        for(int i=0;i<AddEventController.hashTable.size();i++)
        {
            if(AddEventController.hashTable.get(i).getDate().equals(stringDate)&&AddEventController.hashTable.get(i).getSubject().equals(descript)) {
                AddEventController.hashTable.replace(i, new Event(Integer.parseInt(term), newEventSubject, newCalendarDate));
            }
        }


        try {
            BufferedWriter flushwriter = Files.newBufferedWriter(Paths.get("event.txt"));
            flushwriter.write("");
            flushwriter.flush();
            BufferedWriter writer = new BufferedWriter(new FileWriter("event.txt", true));
            for(int i=0;i<AddEventController.hashTable.size();i++) {
                System.out.println(AddEventController.hashTable.get(i));
                writer.append("\n");
                writer.append(AddEventController.hashTable.get(i).getTerm());
                writer.append("\n");
                writer.append(AddEventController.hashTable.get(i).getSubject());
                writer.append("\n");
                writer.append(AddEventController.hashTable.get(i).getDate());
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.repaintView();


        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }


    public void deleteEvent() throws IOException {

        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String eventDate = year + "-" + String.format("%02d",month) + "-" + String.format("%02d",day);
        String descript = Model.getInstance().event_subject;
        for(int i=0;i<AddEventController.hashTable.size();i++) {
            System.out.println(AddEventController.hashTable.get(i));
        }
        for(int i=0;i<AddEventController.hashTable.size();i++){
            if(AddEventController.hashTable.get(i).getDate().equals(eventDate)&&AddEventController.hashTable.get(i).getSubject().equals(descript)) {
                AddEventController.hashTable.remove(i);
                break;
            }
        }
        HashTable<Integer, Event> temp = new HashTable<>();
        for(int i=0;i<AddEventController.hashTable.size();i++)
        {
            if(AddEventController.hashTable.get(i)!=null)
                temp.put(temp.size(), AddEventController.hashTable.get(i));
        }
        AddEventController.hashTable.clear();
        for(int i=0;i<temp.size();i++)
        {
            AddEventController.hashTable.put(AddEventController.hashTable.size(),temp.get(i));
        }
        for(int i=0;i<AddEventController.hashTable.size();i++) {
            System.out.println(AddEventController.hashTable.get(i));
        }
        mainController.repaintView();
        Alert alertMessage = new Alert(AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Запис було успішно вилучено");
        alertMessage.showAndWait();

    }

}


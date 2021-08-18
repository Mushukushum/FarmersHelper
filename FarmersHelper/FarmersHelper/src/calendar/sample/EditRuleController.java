package calendar.sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditRuleController implements Initializable {
    private FXMLDocumentController mainController;
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    AddRule addRule = new AddRule();
    private ListRulesController listController;

    public void setListController(ListRulesController listController) {
        this.listController = listController ;
    }
    private static String[] priorities = {"5","4","3","2","1"};

    @FXML
    private AnchorPane rootPane;
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

    private double xOffset;
    private double yOffset;

    private void autofill(){

        String days = Integer.toString(Model.getInstance().rule_days);
        String descript = Model.getInstance().rule_descript;
        String term = Model.getInstance().rule_term;

        eventDescript.setText(descript);
        daysFromStart.setText(days);
        termSelect.getSelectionModel().select(term);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> termsList;
        termsList = getListOfTerms();
        termSelect.setItems(termsList);
        autofill();
        topLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        topLabel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
        topLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });
        topLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });
    }
    public ObservableList<String> getListOfTerms(){
        ObservableList<String> listOfTerms = FXCollections.observableArrayList();
        Collections.addAll(listOfTerms,priorities);
        return listOfTerms;
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(MouseEvent event) {
        updateRule();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void updateRule(){


        String term = Model.getInstance().rule_term;
        String descript = Model.getInstance().rule_descript;
        int days = Model.getInstance().rule_days;
        System.out.println(term + descript + days);
        Scanner auxScanner = new Scanner(daysFromStart.getText());
        if (!auxScanner.hasNextInt())
        {
            Alert alertMessage = new Alert(Alert.AlertType.WARNING);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Кількість днів повинна бути цілим числом");
            alertMessage.showAndWait();
            return;
        }

        String newTerm = termSelect.getValue();
        String newDescript = eventDescript.getText();
        int newDays = Integer.parseInt(daysFromStart.getText());
        for(int i=0;i<addRule.hashTable.size();i++) {
            System.out.println(i + " "+addRule.hashTable.get(i).getEventDescription()+addRule.hashTable.get(i).getTermID()+addRule.hashTable.get(i).getDaysFromStart());
            if(addRule.hashTable.get(i).getEventDescription().equals(descript)&&Integer.parseInt(addRule.hashTable.get(i).getDaysFromStart())==days&&addRule.hashTable.get(i).getTermID().equals(term)) {
                addRule.hashTable.replace(i, new Rule(newDescript, newTerm, String.valueOf(newDays)));
                System.out.println(addRule.hashTable.get(i).getTermID());
            }
        }

        listController.loadData();
        mainController.repaintView();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}


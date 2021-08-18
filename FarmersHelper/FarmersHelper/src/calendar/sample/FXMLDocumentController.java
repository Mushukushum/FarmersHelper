package calendar.sample;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {
    @FXML
    private Label monthLabel;
    @FXML
    private HBox weekdayHeader;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private JFXComboBox<String> selectedYear;
    @FXML
    private JFXListView<String> monthSelect;
    PriorityQueue priorityQueue = new PriorityQueue();
    Hashtable<Integer, Event> temp = new Hashtable<>();

    @FXML
    private JFXColorPicker veryHighCP;
    @FXML
    private JFXColorPicker highCP;
    @FXML
    private JFXColorPicker mediumCP;
    @FXML
    private JFXColorPicker lowCP;
    @FXML
    private JFXColorPicker veryLowCP;

    @FXML
    private JFXCheckBox fallSemCheckBox;
    @FXML
    private JFXCheckBox springSemCheckBox;
    @FXML
    private JFXCheckBox summerSemCheckBox;
    @FXML
    private JFXCheckBox allQtrCheckBox;
    @FXML
    private JFXCheckBox allMbaCheckBox;
    @FXML
    private JFXCheckBox allHalfCheckBox;
    @FXML
    private JFXCheckBox allCampusCheckBox;
    @FXML
    private JFXCheckBox allHolidayCheckBox;
    @FXML
    private JFXCheckBox allTraTrbCheckBox;
    @FXML
    private JFXCheckBox selectAllCheckBox;

    public static boolean workingOnCalendar = false;
    public static boolean checkBoxesHaveBeenClicked = false;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox colorRootPane;
    @FXML
    private VBox toolsRootPane;
    @FXML
    private VBox centerArea;
    @FXML
    private Label calendarNameLbl;

    protected static String[] terms = {"250-255-255", "255-0-255", "0-255-255", "255-255-0", "255-100-255"};

    private void addEvent(VBox day) {

        if (!day.getChildren().isEmpty()) {

            Label lbl = (Label) day.getChildren().get(0);

            Model.getInstance().event_day = Integer.parseInt(lbl.getText());
            Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());
            Model.getInstance().event_year = Integer.parseInt(selectedYear.getValue());

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("add_event.fxml"));
                AnchorPane rootLayout = loader.load();
                Stage stage = new Stage(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);

                AddEventController eventController = loader.getController();
                eventController.setMainController(this);

                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void editEvent(VBox day, String descript, String termID) {

        Label dayLbl = (Label) day.getChildren().get(0);
        Model.getInstance().event_day = Integer.parseInt(dayLbl.getText());
        Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());
        Model.getInstance().event_year = Integer.parseInt(selectedYear.getValue());
        Model.getInstance().event_subject = descript;
        Model.getInstance().event_term_id = Integer.parseInt(termID);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("edit_event.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            EditEventController eventController = loader.getController();
            eventController.setMainController(this);

            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newCalendarEvent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_calendar.fxml"));
            AnchorPane rootLayout = loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            AddCalendarController calendarController = loader.getController();
            calendarController.setMainController(this);

            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listRulesEvent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("list_rules.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            ListRulesController listController = loader.getController();
            listController.setMainController(this);
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newRuleEvent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_rule.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            AddRuleController ruleController = loader.getController();
            ruleController.setMainController(this);

            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeMonthSelector() {

        monthSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                monthLabel.setText(newValue);

                Model.getInstance().viewing_month = Model.getInstance().getMonthIndex(newValue);
                repaintView();
            }

        });
        selectedYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                Model.getInstance().viewing_year = Integer.parseInt(newValue);
                repaintView();
            }
        });
    }

    private void loadCalendarLabels() {

        int year = Model.getInstance().viewing_year;
        int month = Model.getInstance().viewing_month;

        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int firstDay = gc.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);

        int offset = firstDay;
        int gridCount = 1;
        int lblCount = 1;

        for (Node node : calendarGrid.getChildren()) {

            VBox day = (VBox) node;

            day.getChildren().clear();
            day.setStyle("-fx-backgroud-color: white");
            day.setStyle("-fx-font: 14px \"System\" ");

            if (gridCount < offset) {
                gridCount++;
                day.setStyle("-fx-background-color: #E9F2F5");
            } else {
                if (lblCount > daysInMonth) {
                    day.setStyle("-fx-background-color: #E9F2F5");
                } else {
                    Label lbl = new Label(Integer.toString(lblCount));
                    lbl.setPadding(new Insets(5));
                    lbl.setStyle("-fx-text-fill:darkslategray");

                    day.getChildren().add(lbl);
                }
                lblCount++;
            }
        }
    }

    public void calendarGenerate() {
        Model.getInstance().calendar_start_date = "2020-01-01";
        Model.getInstance().calendar_start = 2020;
        Model.getInstance().calendar_end = 2021;
        Model.getInstance().calendar_name = "Календар";
        selectedYear.getItems().clear();
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_start));
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_end));
        selectedYear.getSelectionModel().selectFirst();

        Model.getInstance().viewing_year = Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem());

        selectedYear.setVisible(true);

        calendarNameLbl.setText(Model.getInstance().calendar_name);

        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String[] months = dateFormat.getMonths();
        String[] spliceMonths = Arrays.copyOfRange(months, 0, 12);

        monthSelect.getItems().clear();
        monthSelect.getItems().addAll(spliceMonths);

        monthSelect.getSelectionModel().selectFirst();
        monthLabel.setText(monthSelect.getSelectionModel().getSelectedItem());

        Model.getInstance().viewing_month =
                Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());

        File file = new File("event.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Open(br);
        } catch (IOException e) {
            e.printStackTrace();
        }


        repaintView();
    }

    public void repaintView() {
        loadCalendarLabels();
        if (!checkBoxesHaveBeenClicked) {
            populateMonthWithEvents();
        } else {
            ActionEvent actionEvent = new ActionEvent();
            handleCheckBoxAction(actionEvent);
        }
    }

    private void populateMonthWithEvents() {
        for (int key = 0; key < AddEventController.hashTable.size(); key++) {
            priorityQueue.add(AddEventController.hashTable.get(key));
        }
        AddEventController.hashTable.clear();
        while (!priorityQueue.isEmpty()) {
            AddEventController.hashTable.put(AddEventController.hashTable.size(), priorityQueue.peek());
            priorityQueue.remove();
        }
        String currentMonth = monthLabel.getText();
        greedyAlgorithm();
        int currentMonthIndex = Model.getInstance().getMonthIndex(currentMonth) + 1;
        int currentYear = Integer.parseInt(selectedYear.getValue());

        for (int i = 0; i < AddEventController.hashTable.size(); i++) {
            if (AddEventController.hashTable.get(i) != null) {
                String eventDescript = AddEventController.hashTable.get(i).getSubject();
                int eventTermID = Integer.parseInt(AddEventController.hashTable.get(i).getTerm());
                if (currentYear == Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(0, 4))) {
                    if (currentMonthIndex == Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(5, 7))) {
                        int day = Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(8, 10));
                        showDate(day, eventDescript, eventTermID);

                    }
                }
            }
        }
    }

    public void showDate(int dayNumber, String descript, int termID) {

        Image img = new Image("file:icon2.png");
        ImageView imgView = new ImageView();
        imgView.setImage(img);

        for (Node node : calendarGrid.getChildren()) {
            VBox day = (VBox) node;

            if (!day.getChildren().isEmpty()) {
                Label lbl = (Label) day.getChildren().get(0);

                int currentNumber = Integer.parseInt(lbl.getText());

                if (currentNumber == dayNumber) {

                    Label eventLbl = new Label(descript);
                    eventLbl.setGraphic(imgView);
                    eventLbl.getStyleClass().add("event-label");

                    eventLbl.setAccessibleText(Integer.toString(termID));
                    eventLbl.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                        editEvent((VBox) eventLbl.getParent(), eventLbl.getText(), eventLbl.getAccessibleText());

                    });

                    String eventRGB = getTermColor(termID);

                    String[] colors = eventRGB.split("-");
                    String red = colors[0];
                    String green = colors[1];
                    String blue = colors[2];

                    eventLbl.setStyle("-fx-background-color: rgb(" + red +
                            ", " + green + ", " + blue + ", " + 1 + ");");

                    eventLbl.setMaxWidth(Double.MAX_VALUE);

                    eventLbl.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.HAND);
                    });
                    eventLbl.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.DEFAULT);
                    });
                    day.getChildren().add(eventLbl);
                }
            }
        }
    }


    private void initalizeColorPicker() {

        String veryHighRGB = terms[0];
        String highRGB = terms[1];
        String mediumRGB = terms[2];
        String lowRGB = terms[3];
        String veryLowRGB = terms[4];

        String[] colors = veryHighRGB.split("-");
        String red = colors[0];
        String green = colors[1];
        String blue = colors[2];
        Color c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        veryHighCP.setValue(c);
        colors = highRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        highCP.setValue(c);

        colors = mediumRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        mediumCP.setValue(c);

        colors = lowRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        lowCP.setValue(c);

        colors = veryLowRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        veryLowCP.setValue(c);
    }

    public void initializeCalendarGrid() {

        int rows = 6;
        int cols = 7;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

                vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    addEvent(vPane);
                });

                GridPane.setVgrow(vPane, Priority.ALWAYS);

                calendarGrid.add(vPane, j, i);
            }
        }
        for (int i = 0; i < 7; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(scrollPane.getHeight() / 7);
            calendarGrid.getRowConstraints().add(row);
        }
    }

    public void initializeCalendarWeekdayHeader() {
        int weekdays = 7;

        String[] weekAbbr = {"Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"};

        for (int i = 0; i < weekdays; i++) {
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);

            pane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

            weekdayHeader.getChildren().add(pane);

            pane.getChildren().add(new Label(weekAbbr[i]));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCalendarGrid();
        initializeCalendarWeekdayHeader();
        initializeMonthSelector();
        JFXDepthManager.setDepth(scrollPane, 1);

        initalizeColorPicker();
    }

    @FXML
    private void newCalendar(MouseEvent event) {
        calendarGenerate();
    }


    @FXML
    private void manageRules(MouseEvent event) {
        listRulesEvent();
    }

    @FXML
    private void newRule(MouseEvent event) {
        newRuleEvent();
    }


    @FXML
    private void handleCheckBoxAction(ActionEvent e) {
        if (!checkBoxesHaveBeenClicked) {
            checkBoxesHaveBeenClicked = true;
        }

        ArrayList<String> termsToFilter = new ArrayList();

        if (fallSemCheckBox.isSelected()) {
            termsToFilter.add("FA SEM");
        }

        if (!fallSemCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("FA SEM");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (springSemCheckBox.isSelected()) {
            termsToFilter.add("SP SEM");
        }
        if (!springSemCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("SP SEM");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (summerSemCheckBox.isSelected()) {
            termsToFilter.add("SU SEM");
        }
        if (!summerSemCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("SU SEM");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allQtrCheckBox.isSelected()) {
            termsToFilter.add("QTR");
        }
        if (!allQtrCheckBox.isSelected()) {

            int auxIndex = termsToFilter.indexOf("QTR");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allMbaCheckBox.isSelected()) {
            termsToFilter.add("MBA");
        }
        if (!allMbaCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("MBA");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allHalfCheckBox.isSelected()) {
            termsToFilter.add("Half");
        }
        if (!allHalfCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("Half");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allCampusCheckBox.isSelected()) {
            termsToFilter.add("Campus");
        }
        if (!allCampusCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("Campus");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allHolidayCheckBox.isSelected()) {
            termsToFilter.add("Holiday");
        }
        if (!allHolidayCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("Holiday");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
        }

        if (allTraTrbCheckBox.isSelected()) {
            termsToFilter.add("TRA");
            termsToFilter.add("TRB");
        }
        if (!allTraTrbCheckBox.isSelected()) {
            int auxIndex = termsToFilter.indexOf("TRA");
            int auxIndex2 = termsToFilter.indexOf("TRB");
            if (auxIndex != -1) {
                termsToFilter.remove(auxIndex);
            }
            if (auxIndex2 != -1) {
                termsToFilter.remove(auxIndex2);
            }
        }
        String calName = Model.getInstance().calendar_name;

        if (termsToFilter.isEmpty()) {
            selectAllCheckBox.setSelected(false);
            loadCalendarLabels();
        }

    }


    public void showFilteredEventsInMonth(ArrayList<String> filteredEventsList) {

        String currentMonth = monthLabel.getText();
        int currentMonthIndex = Model.getInstance().getMonthIndex(currentMonth) + 1;

        int currentYear = Integer.parseInt(selectedYear.getValue());

        for (int i = 0; i < filteredEventsList.size(); i++) {
            String[] eventInfo = filteredEventsList.get(i).split("~");
            String eventDescript = eventInfo[0];
            String eventDate = eventInfo[1];
            int eventTermID = Integer.parseInt(eventInfo[2]);
            String eventCalName = eventInfo[3];
            String[] dateParts = eventDate.split("-");
            int eventYear = Integer.parseInt(dateParts[0]);
            int eventMonth = Integer.parseInt(dateParts[1]);
            int eventDay = Integer.parseInt(dateParts[2]);

            if (currentYear == eventYear) {
                if (currentMonthIndex == eventMonth) {
                    showDate(eventDay, eventDescript, eventTermID);
                }
            }
        }
    }


    @FXML
    private void deleteAllEvents(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Попередження");
        alert.setHeaderText("Видалення всіх записів");
        alert.setContentText("Ви впевнені?");
        ButtonType buttonTypeYes = new ButtonType("Так");
        ButtonType buttonTypeNo = new ButtonType("Ні");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            deleteAllEventsInCalendar();
            BufferedWriter flushwriter = null;
            try {
                flushwriter = Files.newBufferedWriter(Paths.get("event.txt"));
                flushwriter.write("");
                flushwriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public void deleteAllEventsInCalendar() {

        String calName = Model.getInstance().calendar_name;

        try {
            AddEventController.hashTable.clear();
            repaintView();
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("All events were successfully deleted");
            alertMessage.showAndWait();
        } catch (Exception e) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Events Failed!");
            alertMessage.showAndWait();
        }
    }

    public String getTermColor(int auxTermID) {

        return terms[auxTermID - 1];
    }

    public void greedyAlgorithm() {
        String currentMonth = monthLabel.getText();
        int currentMonthIndex = Model.getInstance().getMonthIndex(currentMonth) + 1;
        int currentYear = Integer.parseInt(selectedYear.getValue());
        for (int i = 0; i < AddEventController.hashTable.size(); i++) {
            if (AddEventController.hashTable.get(i) != null) {
                if (currentYear == Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(0, 4))) {
                    if (currentMonthIndex == Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(5, 7))) {
                        int day = Integer.parseInt(AddEventController.hashTable.get(i).getDate().substring(8, 10));
                        int summOfPriorities = 0;
                        for (int key = 0; key < AddEventController.hashTable.size(); key++) {
                            if (AddEventController.hashTable.get(key).getDate().equals(currentYear + "-" + String.format("%02d",currentMonthIndex) + "-" + String.format("%02d",day))) {
                                summOfPriorities += Integer.parseInt(AddEventController.hashTable.get(key).getTerm());
                            }
                        }
                        int minimumPriority=5;
                        if (summOfPriorities > 15)
                        {
                            for (int key = 0; key < AddEventController.hashTable.size(); key++) {
                                if (AddEventController.hashTable.get(key).getDate().equals(currentYear + "-" + String.format("%02d",currentMonthIndex) + "-" + String.format("%02d",day))) {
                                    if (Integer.parseInt(AddEventController.hashTable.get(key).getTerm()) < minimumPriority)
                                        minimumPriority = Integer.parseInt(AddEventController.hashTable.get(key).getTerm());
                                }
                            }
                        }
                            //TODO
                            for (int key=0;key<AddEventController.hashTable.size();key++) {
                                if (AddEventController.hashTable.get(key).getDate().equals(currentYear + "-" + String.format("%02d", currentMonthIndex) + "-" + String.format("%02d", day)) && Integer.parseInt(AddEventController.hashTable.get(key).getTerm()) == minimumPriority&&summOfPriorities>15) {
                                    AddEventController.hashTable.replace(key, new Event(Integer.parseInt(AddEventController.hashTable.get(key).getTerm()), AddEventController.hashTable.get(key).getSubject(), currentYear + "-" + String.format("%02d", currentMonthIndex) + "-" + String.format("%02d", day + 1)));
                                    summOfPriorities-=5;
                                }
                            }
                        }
                    }
                }
            }
        }
    public void Open( BufferedReader bufferedReader) throws IOException
    {
        while(bufferedReader.readLine()!=null) {
            String prio = bufferedReader.readLine();
            String descript = bufferedReader.readLine();
            String date = bufferedReader.readLine();
            AddEventController.hashTable.put(AddEventController.hashTable.size(), new Event(Integer.parseInt(prio), descript, date));
        }
    }
    }
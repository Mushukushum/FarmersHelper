package weather;

import com.jfoenix.controls.JFXAutoCompletePopup;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import mainwindow.Main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class WeatherFirstGroupController {
    private static String cityName = "Vinnytsia";
    private static boolean animationRunning = false;

    @FXML
    Group group;

    @FXML
    ImageView bgImage;

    @FXML
    Label city, temperature, description, day;

    @FXML
    ImageView icon;

    @FXML
    TextField textField;

    @FXML
    private void initialize() {
        try {
            WeatherManager weatherManager = new WeatherManager(cityName);
            WeatherSecondGroupController.setWeatherManager(weatherManager);

            weatherManager.getWeather();

            city.setText(weatherManager.getCity());
            temperature.setText(weatherManager.getTemperature() + "°");
            description.setText(weatherManager.getDescription());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            day.setText(dtf.format(now));

            icon.setImage(new Image(ImageHandler.getImage(weatherManager.getIcon())));
        }
        catch (NullPointerException e) {
            cityName = "null";
            city.setText("null");
            System.out.println(e);
        }

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();

        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream("resources/cities.txt")));
        while (in.hasNext()) autoCompletePopup.getSuggestions().add(in.nextLine());
        in.close();


        autoCompletePopup.setSelectionHandler(event -> {
            textField.setText(event.getObject());
        });

        // filtering options
        textField.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(textField.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || textField.getText().isEmpty()) {
                autoCompletePopup.hide();
            } else {
                autoCompletePopup.show(textField);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                cityName = textField.getText();
                city.setVisible(true);
                initialize();
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                textField.setVisible(false);
                city.setVisible(true);
            }
        });

        textField.setVisible(false);
    }
    
    @FXML
    private void setOnItemClicked() {
        if(WeatherFirstGroupController.isAnimationRunning() ||
                WeatherSecondGroupController.isAnimationRunning()) {return;}
        else {
            animationRunning = true;
            RotateTransition rotator = createRotator(group, 0, 90);
            rotator.setOnFinished(event -> {
                animationRunning = false;
                Group newGroup = WeatherSecondGroupController.getGroup();
                ((Pane) (group.getParent())).getChildren().add(newGroup);
                ((Pane) (group.getParent())).getChildren().remove(group);
                RotateTransition rotator1 = createRotator(newGroup, 90, 0);
                rotator1.play();
            });
            rotator.play();
        }
    }

    @FXML
    private void onCityLabelClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                textField.setVisible(true);
                textField.requestFocus();
                city.setVisible(false);
            }
        }
    }

    public static Group getGroup() {
        Group root = (Group) loadFXML();
        assert root != null;
        return root;
    }

    public static String getCityName() { return cityName; }
    public static void setCityName(String cityName) { WeatherFirstGroupController.cityName = cityName; }

    public static boolean isAnimationRunning() { return animationRunning; }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(WeatherSecondGroupController.class.getResource("weatherFirstGroup.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    private RotateTransition createRotator(Node card, int fromAngle, int toAngle) {
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), card);
        rotator.setAxis(Rotate.X_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);

        return rotator;
    }
}

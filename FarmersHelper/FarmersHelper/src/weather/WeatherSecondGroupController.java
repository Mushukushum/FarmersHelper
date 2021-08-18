package weather;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;

public class WeatherSecondGroupController {
    private static WeatherManager weatherManager;
    private static boolean animationRunning = false;

    @FXML
    Group group;

    @FXML
    Label windSpeed, cloudiness, pressure, humidity;

    @FXML
    private void initialize() {
        weatherManager.getWeather();
        windSpeed.setText(weatherManager.getWindSpeed() + " m/s");
        cloudiness.setText(weatherManager.getCloudiness() + " %");
        pressure.setText(weatherManager.getPressure() + " hPa");
        humidity.setText(weatherManager.getHumidity() + " %");
    }

    @FXML
    private void setOnGroupClicked() {
        if(WeatherFirstGroupController.isAnimationRunning() ||
                WeatherSecondGroupController.isAnimationRunning()) {return;}
        else {
            animationRunning = true;
            RotateTransition rotator = createRotator(group, 0, 90);
            rotator.setOnFinished(event -> {
                animationRunning = false;
                Group newGroup = WeatherFirstGroupController.getGroup();
                ((Pane) (group.getParent())).getChildren().add(newGroup);
                ((Pane) (group.getParent())).getChildren().remove(group);
                RotateTransition rotator1 = createRotator(newGroup, 90, 0);
                rotator1.play();
            });
            rotator.play();
        }
    }

    public static void setWeatherManager(WeatherManager weatherManager) {
        WeatherSecondGroupController.weatherManager = weatherManager;
    }

    public static Group getGroup() {
        Group root = (Group) loadFXML();
        assert root != null;
        return root;
    }

    public static boolean isAnimationRunning() { return animationRunning; }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(WeatherSecondGroupController.class.getResource("weatherSecondGroup.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    private static RotateTransition createRotator(Node card, int fromAngle, int toAngle) {
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), card);
        rotator.setAxis(Rotate.X_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);

        return rotator;
    }
}

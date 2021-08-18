package mainwindow;

import encyclopedia.EncycWindowController;
import calendar.sample.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import shop.CategoriesWindowController;

import java.io.IOException;
import java.util.*;

public class MainWindowController {
    private static final List<String> advices = getAdvices("resources/advices.txt");

    @FXML
    Button button1, button2, shopButton;

    @FXML
    Label tip;

    @FXML
    private void initialize() {
        Random r = new Random();
        tip.setText(advices.get(r.nextInt(advices.size())));
        addRandomChangingAdvicesAnimation(tip, 3000, 5000);
    }

    @FXML
    private void onCalendarButtonPressed() { Main.mainStage.setScene(AcademicCalendar.getScene(Main.mainStage)); }

    @FXML
    private void onShopButtonPressed() { Main.mainStage.setScene(CategoriesWindowController.getScene(Main.mainStage)); }

    @FXML
    private void onEncyclopediaButtonPressed() { Main.mainStage.setScene(EncycWindowController.getScene(Main.mainStage)); }


    public static Scene getScene() {
        Parent root = loadFXML();
        assert root != null;
        return new Scene(root);
    }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(MainWindowController.class.getResource("mainWindow.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    private static void addRandomChangingAdvicesAnimation(Label label, int duration_millis, int delay_millis) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration_millis), label);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(delay_millis));
        ft.setOnFinished(new EventHandler<ActionEvent>() {
            int counter = 1;
            @Override
            public void handle(ActionEvent event) {
                if (counter % 2 == 1) {
                    Random r = new Random();
                    label.setText(advices.get(r.nextInt(advices.size())));
                    ft.setFromValue(0);
                    ft.setToValue(1.0);
                    ft.setDelay(Duration.ZERO);
                }
                else {
                    ft.setFromValue(1.0);
                    ft.setToValue(0);
                    ft.setDelay(Duration.millis(delay_millis));
                }
                ft.play();
                counter++;
            }
        });

        ft.play();
    }

    private static List<String> getAdvices(String file) {
        List<String> list = new ArrayList<>();
        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream(file)));
        while (in.hasNext()) list.add(in.nextLine());
        in.close();
        return list;
    }
}
package calendar.sample;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import mainwindow.MainWindowController;

import java.io.IOException;

public class AcademicCalendar {
    public static Scene getScene(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(AcademicCalendar.class.getResource("FXMLDocument.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stage.setScene(MainWindowController.getScene());
                    }
                });

        stage.getIcons().add(new Image("/calendar/sample/app_icon.png"));
        return scene;
    }
}


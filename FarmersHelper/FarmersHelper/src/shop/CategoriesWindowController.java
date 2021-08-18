package shop;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mainwindow.Main;
import mainwindow.MainWindowController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CategoriesWindowController {
    private Point2D point;
    protected static Map<String, String> categories = new HashMap<>();

    @FXML
    GridPane gridPane;

    @FXML
    private void initialize() {
        gridPane.setOnMouseMoved(event -> {
            point = new Point2D(event.getX(), event.getY());
            for (int i = 0; i < gridPane.getRowCount(); i++) {
                for (int j = 0; j < gridPane.getColumnCount(); j++) {
                    if (gridPane.getCellBounds(j, i).contains(point))
                        gridPane.getChildren().get(i * 3 + j).setEffect(new Glow(1));
                    else gridPane.getChildren().get(i * 3 + j).setEffect(null);
                }
            }
        });

        gridPane.setOnMouseClicked(event -> {
            point = new Point2D(event.getX(), event.getY());
            for (int i = 0; i < gridPane.getRowCount(); i++) {
                for (int j = 0; j < gridPane.getColumnCount(); j++) {
                    if (gridPane.getCellBounds(j, i).contains(point)) {
                        gridPane.getChildren().get(i * 3 + j).setEffect(new MotionBlur(0, 15));
                        String categoryName = ((Label)((Group)gridPane.getChildren().get(i*3+j)).getChildren().get(2)).getText();

                        Main.mainStage.setScene(ProductsWindowController.getScene(Main.mainStage, categoryName));
                    }
                }
            }
        });
    }


    public static Scene getScene(Stage stage) {
        Parent root = loadFXML();
        assert root != null;
        Scene scene = new Scene(root);

        categories.put("Трактори", "https://moto-lux.com.ua/uk/traktora/ " +
                "https://moto-lux.com.ua/uk/minitractors/");
        categories.put("Насіння", "https://moto-lux.com.ua/uk/semena-gazonnykh-trav/ " +
                "https://moto-lux.com.ua/uk/semena/ " +
                "https://moto-lux.com.ua/uk/semena-pryanykh-trav/ " +
                "https://moto-lux.com.ua/uk/semena-tsvetov/");
        categories.put("Причепи до тракторів", "https://moto-lux.com.ua/uk/pritsepy-k-traktoram/");
        categories.put("Добрива", "https://moto-lux.com.ua/uk/udobreniya-stimulyatory-i-regulyatory-rosta/");
        categories.put("Навісне обладнання", "https://moto-lux.com.ua/uk/diskoplugi-dlya-traktora/ " +
                "https://moto-lux.com.ua/uk/kartofelesazhalka/ " +
                "https://moto-lux.com.ua/uk/seyalki/ " +
                "https://moto-lux.com.ua/uk/plugi/ " +
                "https://moto-lux.com.ua/uk/kultivatory/ " +
                "https://moto-lux.com.ua/uk/kosilki/");
        categories.put("Мінісільгосптехніка", "https://moto-lux.com.ua/uk/motobloki/ " +
                "https://moto-lux.com.ua/uk/motobloki-s-vozdushnym-ohklazhdeniem/ " +
                "https://moto-lux.com.ua/uk/mototraktora/");
        categories.put("Запчастини", "https://moto-lux.com.ua/uk/dvigatel-i-toplivnaya-sistema/ " +
                "https://moto-lux.com.ua/uk/gidravlika/ " +
                "https://moto-lux.com.ua/uk/kabina-kuzov/ " +
                "https://moto-lux.com.ua/uk/rulevoe-upravlenie/ " +
                "https://moto-lux.com.ua/uk/sistema-okhlazhdeniya/ " +
                "https://moto-lux.com.ua/uk/toplivnaya-sistema/ " +
                "https://moto-lux.com.ua/uk/transmissiya-kkp/ " +
                "https://moto-lux.com.ua/uk/tormoznaya-sistema-i-khodovaya-chast/ " +
                "https://moto-lux.com.ua/uk/elektrooborudovanie-i-optika/ " +
                "https://moto-lux.com.ua/uk/obshchie-zapchasti/ " +
                "https://moto-lux.com.ua/uk/zapchasti-na-mototraktor/ " +
                "https://moto-lux.com.ua/uk/kolesa-i-diski/");
        categories.put("Садова техніка", "https://moto-lux.com.ua/uk/vozdukhoduvy/ " +
                "https://moto-lux.com.ua/uk/gazonokosilki/ " +
                "https://moto-lux.com.ua/uk/trimmery-motokosy/ " +
                "https://moto-lux.com.ua/uk/sadovye-traktory/ " +
                "https://moto-lux.com.ua/uk/rotornye-senokosilki/ " +
                "https://moto-lux.com.ua/uk/benzopily-elektropily/");
        categories.put("Спецтехніка", "https://moto-lux.com.ua/uk/spetstekhnika/");

        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.setScene(MainWindowController.getScene());

                for (Task<Void> task : LoadTask.tasks) task.cancel();
                ProductsWindowController.products.clear();

            }
        });

        return scene;
    }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(CategoriesWindowController.class.getResource("categoriesWindow.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }
}

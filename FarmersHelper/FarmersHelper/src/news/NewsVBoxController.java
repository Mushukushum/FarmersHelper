package news;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class NewsVBoxController {
    @FXML
    VBox vBox;

    @FXML
    private void initialize() {
        for (News news : News.loadNews()) {
            vBox.getChildren().add(news.getPane());
        }
    }
}

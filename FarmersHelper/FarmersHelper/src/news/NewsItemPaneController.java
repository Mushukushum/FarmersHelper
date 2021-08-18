package news;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import mainwindow.Main;

import java.io.IOException;

public class NewsItemPaneController {

    @FXML
    Hyperlink hyperlink;

    @FXML
    Label pubDate, description;


    @FXML
    private void hyperlinkClicked() {
        Scene previousScene = Main.mainStage.getScene();
        WebView root = new WebView();

        for (News news: News.getNews())
            if(description.getText().equals(news.getDescription()))
                root.getEngine().load(news.getLink());
        
        Scene scene = new Scene(root, previousScene.getWidth(), previousScene.getHeight());
        Main.mainStage.setScene(scene);

        scene.setOnKeyPressed(event -> { if(event.getCode().equals(KeyCode.ESCAPE)) Main.mainStage.setScene(previousScene); });
    }

    public static Parent loadFXML() {
        try {
            return FXMLLoader.load(NewsItemPaneController.class.getResource("newsItemPane.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }
}

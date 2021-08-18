package shop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import mainwindow.Main;

import java.io.IOException;

public class ItemGroupController {
    @FXML
    Label title;

    public static Parent loadFXML() {
        try {
            return FXMLLoader.load(ItemGroupController.class.getResource("itemGroup.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    @FXML
    private void buttonClicked() {
        Scene previousScene = Main.mainStage.getScene();

        WebView root = new WebView();
        for (Product product: ProductsWindowController.products) {
            if (product.getTitle().equals(title.getText())) {
                root.getEngine().load(product.getDetailsURL());
                break;
            }
        }
        Scene scene = new Scene(root, previousScene.getWidth(), previousScene.getHeight());
        Main.mainStage.setScene(scene);

        scene.setOnKeyPressed(event -> { if(event.getCode().equals(KeyCode.ESCAPE)) Main.mainStage.setScene(previousScene); });
    }
}

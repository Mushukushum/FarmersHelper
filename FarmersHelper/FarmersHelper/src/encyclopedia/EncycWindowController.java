package encyclopedia;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mainwindow.Main;
import mainwindow.MainWindowController;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class EncycWindowController {

    @FXML
    ListView<String> list;

    @FXML
    WebView info;

    @FXML
    public void initialize() {
        File folder = new File(System.getProperty("user.dir") + "/src/resources/encyclopedia");
        File[] files = folder.listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                list.getItems().add(name.substring(0, name.length() - 4));
            }
        }
        list.getItems().sort(String::compareTo);
    }

    @FXML
    private void onListClicked() {
        String selectedItem = list.getSelectionModel().getSelectedItem();
        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream("resources/encyclopedia/" + selectedItem + ".txt")));
        String htmlCode = "";
        while (in.hasNext()) htmlCode += in.nextLine();
        in.close();
        info.getEngine().loadContent(htmlCode);
    }

    public static Scene getScene(Stage stage) {
        Parent root = loadFXML();
        assert root != null;
        Scene scene = new Scene(root);

        for(Node node : scene.getRoot().getChildrenUnmodifiable())
            if(node instanceof ListView)
                node.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stage.setScene(MainWindowController.getScene());
                    }
                });

        return scene;
    }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(EncycWindowController.class.getResource("encycWindow.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }
}

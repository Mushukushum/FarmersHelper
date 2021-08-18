package shop;

import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainwindow.Main;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ProductsWindowController {
    protected static List<Product> products = new ArrayList<>();

    public static String categoryName = "";
    public static VBox windowVBox;

    @FXML
    ScrollPane scrollPane;

    @FXML
    VBox vBox;

    @FXML
    Label lotsCounter, titleText, descriptionText, loadingText, sortTime;

    @FXML
    TextField searchField;

    @FXML
    private void initialize() {
        // TODO: 13.12.2020 Add description to categories
        windowVBox = vBox;
        StringBuilder description = new StringBuilder();
        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream("resources/titles/" + categoryName + ".txt")));
        String title = in.nextLine();
        while (in.hasNext()) description.append(in.nextLine());
        in.close();

        titleText.setText(title);
        descriptionText.setText(description.toString());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            vBox.getChildren().clear();
            for (Product product : filterList(products, newValue))
                vBox.getChildren().add(product.getFxGroupItem());
        });

        vBox.getChildren().addListener((ListChangeListener<Node>) c -> {
            lotsCounter.setText("Кількість найменувань: " + vBox.getChildren().size());
            loadingText.setVisible(false);
        });
    }

    @FXML
    private void titleSortButtonPressed() {
        sortTime.setOpacity(1);

        Instant start = Instant.now();

        products = Product.selectionSort(products, "title");

        Instant end = Instant.now();
        Duration elapsedTime = Duration.between(start, end);
        sortTime.setText("Час сортування: " + elapsedTime.getNano() / 1E9);

        addLabelFadeTransition(sortTime, 1000, 3000);

        vBox.getChildren().clear();
        for (Product product : products) {
            ProductsWindowController.windowVBox.getChildren().add(product.getFxGroupItem());
        }
    }

    @FXML
    private void lowPriceSortButtonPressed() {
        sortTime.setOpacity(1);

        Instant start = Instant.now();

        products = Product.shellSort(products, "price");

        Instant end = Instant.now();
        Duration elapsedTime = Duration.between(start, end);
        sortTime.setText("Час сортування: " + elapsedTime.getNano() / 1E9);

        addLabelFadeTransition(sortTime, 1000, 3000);

        vBox.getChildren().clear();
        for (Product product : products) {
            ProductsWindowController.windowVBox.getChildren().add(product.getFxGroupItem());
        }
    }

    @FXML
    private void highPriceSortButtonPressed() {
        sortTime.setOpacity(1);

        Instant start = Instant.now();

        products = Product.shellSort(products, "price");

        Collections.reverse(products);
        Instant end = Instant.now();
        Duration elapsedTime = Duration.between(start, end);
        sortTime.setText("Час сортування: " + elapsedTime.getNano() / 1E9);

        addLabelFadeTransition(sortTime, 1000, 3000);

        vBox.getChildren().clear();
        for (Product product : products) {
            ProductsWindowController.windowVBox.getChildren().add(product.getFxGroupItem());
        }
    }

    public static Scene getScene(Stage stage, String category) {
        LoadTask.tasks.clear();
        products.clear();

        categoryName = category;
        Parent root = loadFXML();

        String urls = CategoriesWindowController.categories.get(category);
        assert root != null;
        Scene scene = new Scene(root);
        String[] urlsArray = urls.split(" ");
        for (String url : urlsArray) {
            for (int i = 1; i < 20; i++) {
                LoadTask loadTask = new LoadTask();
                loadTask.setURL(url);
                loadTask.setPage(i);

                loadTask.start();

                loadTask.setOnSucceeded(event -> {
                    for (Product product : loadTask.getNewLots())
                        ProductsWindowController.windowVBox.getChildren().add(product.getFxGroupItem());
                    products.addAll(loadTask.getNewLots());
                    LoadTask.tasks.remove(loadTask);
                });
                loadTask.setOnFailed(event -> LoadTask.tasks.remove(loadTask));
            }
        }

        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.setScene(CategoriesWindowController.getScene(Main.mainStage));

                for (Task<Void> task : LoadTask.tasks) task.cancel();
                products.clear();

            }
        });

        return scene;
    }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(CategoriesWindowController.class.getResource("productsWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    private boolean searchFindsOrder(Product product, String searchText) {
        return (product.getTitle().toLowerCase().contains(searchText.toLowerCase()));
    }

    private List<Product> filterList(List<Product> list, String searchText) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : list) {
            if (searchFindsOrder(product, searchText)) filteredList.add(product);
        }
        return filteredList;
    }

    private static void addLabelFadeTransition(Label label, int duration_millis, int delay_millis) {
        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(duration_millis), label);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(javafx.util.Duration.millis(delay_millis));
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(javafx.util.Duration.millis(delay_millis));
        ft.play();
    }
}

package mainwindow;

import javafx.application.Application;
import javafx.stage.Stage;
import weather.WeatherFirstGroupController;
import weather.WeatherManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        WeatherManager.loadCityFromFile();
        Stage stage = new Stage();
        mainStage = stage;
        stage.setTitle("Помічник фермера");
        stage.setResizable(false);
        stage.setScene(MainWindowController.getScene());
        stage.showAndWait();

        WeatherManager.saveCityToFile();
    }

    private static void saveCityToFile() {
        try {
            FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/src/resources/current_weather_city.txt");
            fw.write(WeatherFirstGroupController.getCityName());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCityFromFile() {
        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream("resources/current_weather_city.txt")));
        String cityFromFile = in.nextLine();
        WeatherFirstGroupController.setCityName(cityFromFile.equals("null") ? "Vinnytsia" : in.nextLine());
        in.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
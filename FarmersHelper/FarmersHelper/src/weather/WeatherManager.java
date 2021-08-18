package weather;

import lib.json.JSONException;
import lib.json.JSONObject;
import mainwindow.Main;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class WeatherManager {
    private String city;
    private String day;
    private Integer temperature;
    private String icon;
    private String description;
    private String windSpeed;
    private String cloudiness;
    private String pressure;
    private String humidity;

    public WeatherManager(String city) {
        this.city = city;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void getWeather(){
        int d = 0;

        JSONObject json;
        JSONObject json_specific;

        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=b5f9d2bca1219ebeb81690318b122c0e&lang=eng&units=metric");
        } catch (IOException e) {
            return;
        }

        json_specific = json.getJSONObject("main");
        this.temperature = json_specific.getInt("temp");
        this.pressure = json_specific.get("pressure").toString();
        this.humidity = json_specific.get("humidity").toString();
        json_specific = json.getJSONObject("wind");
        this.windSpeed = json_specific.get("speed").toString();
        json_specific = json.getJSONObject("clouds");
        this.cloudiness = json_specific.get("all").toString();

        c.add(Calendar.DATE, d);
        this.day = df2.format(c.getTime());

        json_specific = json.getJSONArray("weather").getJSONObject(0);
        this.description = capitalize(json_specific.get("description").toString());
        this.icon = json_specific.get("icon").toString();
    }


    public String getCity() {
        return city;
    }

    public String getDay() {
        return day;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    private static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void saveCityToFile() {
        try {
            FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/src/resources/current_weather_city.txt");
            fw.write(WeatherFirstGroupController.getCityName());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCityFromFile() {
        Scanner in = new Scanner(
                Objects.requireNonNull(Main.class.getClassLoader().
                        getResourceAsStream("resources/current_weather_city.txt")));
        String cityFromFile = in.nextLine();
        WeatherFirstGroupController.setCityName(cityFromFile.equals("null") ? "Vinnytsia" : cityFromFile);
        in.close();
    }
}

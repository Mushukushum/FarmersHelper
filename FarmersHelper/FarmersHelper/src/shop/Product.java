package shop;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Product {
    private int id;
    private String article;
    private String brand;
    private String title;
    private int price;
    private String detailsURL;
    private String imageURL;

    private Group fxGroupItem = (Group) ItemGroupController.loadFXML();

    public Product(int id, String article, String brand, String title, int price, String detailsURL, String imageURL) {
        this.id = id;
        this.article = article;
        this.brand = brand;
        this.title = title;
        this.price = price;
        this.detailsURL = detailsURL;
        this.imageURL = imageURL;

        ((ImageView) fxGroupItem.getChildren().get(1)).setImage(new Image(this.imageURL));
        ((Label) fxGroupItem.getChildren().get(2)).setText(this.title);
        ((Label) fxGroupItem.getChildren().get(3)).setText("Артикул: " + this.article);
        ((Label) fxGroupItem.getChildren().get(4)).setText(this.price + " грн.");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDetailsURL() { return detailsURL; }
    public void setDetailsURL(String detailsURL) { this.detailsURL = detailsURL; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public Group getFxGroupItem() { return fxGroupItem; }
    public void setFxGroupItem(Group fxGroupItem) { this.fxGroupItem = fxGroupItem; }

    public static List<Product> selectionSort(List<Product> unsortedList, String baseField) {

        int n = unsortedList.size();
        List<Product> list = new ArrayList<>(unsortedList);

        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (baseField.equals("title"))
                    if (list.get(j).getTitle().compareToIgnoreCase(list.get(min_idx).getTitle()) < 0)
                        min_idx = j;
                if (baseField.equals("price"))
                    if (list.get(j).getPrice() < list.get(min_idx).getPrice())
                        min_idx = j;
            }


            Product temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }

        return list;
    }

    public static List<Product> shellSort(List<Product> unsortedList, String baseField) {
        List<Product> list = new ArrayList<>(unsortedList);
        int n = list.size();
        int gap = n / 2;
        while (gap > 0) {
            for (int i = gap; i < n; i++) {
                Product temp = list.get(i);
                int j = i;
                if(baseField.equals("title"))
                    while (j >= gap && list.get(j - gap).getTitle().compareToIgnoreCase(temp.getTitle()) > 0) {
                        list.set(j, list.get(j - gap));
                        j -= gap;
                    }
                else if(baseField.equals("price"))
                    while (j >= gap && list.get(j - gap).getPrice() > temp.getPrice()) {
                        list.set(j, list.get(j - gap));
                        j -= gap;
                    }
                else return null;
                list.set(j, temp);
            }
            gap /= 2;
        }
        return list;
    }

    public static class Parser {

        public static List<Product> getProducts(String urlString) {

            String pageContent = "";
            String listToGetFrom = "";
            List<Product> resultProductList = new ArrayList<>();

            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            assert url != null;
            Scanner sc = null;
            try {
                sc = new Scanner(url.openStream());
            } catch (IOException e) {}
            assert sc != null;
            while(sc.hasNext()) {
                String next = sc.nextLine();
                pageContent += next;
                next = next.replace("\\/","/");
                if(next.contains("products =")) {
                    listToGetFrom = next;
                }
                if(next.contains("<div class=\"pager__container\">")) break;
            }

            String[] fields = extractTags(listToGetFrom, "\\[\\{", "}]").split("},\\{");

            for (String field : fields) {
                int id = Integer.parseInt(extractTags(field, "\"id\":", ","));
                String article = extractTags(field, "\"article\":\"", "\",");
                String brand = extractTags(field, "\"brand_title\":\"", "\",");
                String title = extractTags(field, "\"title\":\"", "\",");
                int price = Integer.parseInt(extractTags(field, "\"price\":", ","));
                String detailsURL = extractTags(field, "\"url\":\"", "\",");
                String imgInfo = extractTags(pageContent, "<a href='" + detailsURL.substring("https://moto-lux.com.ua".length()), "</a>");
                String imageURL = "https://moto-lux.com.ua" + extractTags(imgInfo, "src='", "'");
                resultProductList.add(new Product(id, article, brand, title, price, detailsURL, imageURL));
            }

            return resultProductList;
        }

        private static String extractTags(String str, String open_tag, String close_tag) {
            int foundStartIndex = performKMPSearch(str, open_tag);
            String substring = str.substring(foundStartIndex + open_tag.length());
            int foundEndIndex = performKMPSearch(substring, close_tag);
            return (substring.substring(0, foundEndIndex));
        }

        public static int performKMPSearch(String text, String pattern) {
            int[] compliedPatternArray = compilePatternArray(pattern);

            int textIndex = 0;
            int patternIndex = 0;

            List<Integer> foundIndexes = new ArrayList<>();

            while (textIndex < text.length()) {
                if (pattern.charAt(patternIndex) == text.charAt(textIndex)) {
                    patternIndex++;
                    textIndex++;
                }
                if (patternIndex == pattern.length()) {
                    return (textIndex - patternIndex);
                } else if (textIndex < text.length() && pattern.charAt(patternIndex) != text.charAt(textIndex)) {
                    if (patternIndex != 0)
                        patternIndex = compliedPatternArray[patternIndex - 1];
                    else
                        textIndex = textIndex + 1;
                }
            }
            return -1;
        }

        public static int[] compilePatternArray(String pattern) {
            int patternLength = pattern.length();
            int len = 0;
            int i = 1;
            int[] compliedPatternArray = new int[patternLength];
            compliedPatternArray[0] = 0;

            while (i < patternLength) {
                if (pattern.charAt(i) == pattern.charAt(len)) {
                    len++;
                    compliedPatternArray[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        len = compliedPatternArray[len - 1];
                    } else {
                        compliedPatternArray[i] = len;
                        i++;
                    }
                }
            }
            return compliedPatternArray;
        }
    }
}

package shop;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class LoadTask extends Service<Void> {
    public static List<Task<Void>> tasks = new ArrayList<>();

    private String pageurl;
    private int i = 1;
    private List<Product> newLots = new ArrayList<>();
    @Override
    protected Task<Void> createTask() throws NullPointerException {
        return new Task<>() {
            @Override
            protected Void call() {
                tasks.add(this);
                if (i == 1) newLots = Product.Parser.getProducts(pageurl);
                else newLots = Product.Parser.getProducts(pageurl + "filter/page=" + i + "/");
                return null;
            }
        };
    }
    public void setURL(String url) {
        pageurl = url;
    }

    public List<Product> getNewLots() {
        return newLots;
    }

    public void setPage(int page) { i = page; }
}

package news;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class News {
    private static List<News> news = new ArrayList<>();

    private String title;
    private String link;
    private String pubDate;
    private String description;

    private Pane pane = (Pane) NewsItemPaneController.loadFXML();

    public News(String title, String link, String pubDate, String description) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        for(Node node : pane.getChildren()) {
            switch (node.getId() != null ? node.getId() : "") {
                case "hyperlink":
                    ((Hyperlink) node).setText(this.title);
                    break;
                case "pubDate":
                    ((Label) node).setText(this.pubDate.substring(0, this.pubDate.length() - 5));
                    break;
                case "description":
                    ((Label) node).setText(this.description);
                    break;
            }
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Pane getPane() { return pane; }

    public static List<News> getNews() { return news; }

    public static List<News> loadNews() {
        news = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
            XMLHandler xmlHandler = new XMLHandler();
            parser.parse("https://www.pravda.com.ua/rss/", xmlHandler);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return news;
    }


    private static class XMLHandler extends DefaultHandler {
        private String title, link, pubDate, description, lastElementName;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            lastElementName = qName;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ( (title != null && !title.isEmpty()) &&
                    (link != null && !link.isEmpty()) &&
                    (pubDate != null && !pubDate.isEmpty()) &&
                    (description != null && !description.isEmpty())) {
                news.add(new News(title, link, pubDate, description));
                title = null;
                link = null;
                pubDate = null;
                description = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equals("title"))
                    title = information;
                if (lastElementName.equals("link"))
                    link = information;
                if (lastElementName.equals("pubDate"))
                    pubDate = information;
                if (lastElementName.equals("description"))
                    description = information;
            }
        }
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

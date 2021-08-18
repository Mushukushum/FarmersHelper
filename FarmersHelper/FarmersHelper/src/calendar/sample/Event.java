package calendar.sample;


import javafx.beans.property.SimpleStringProperty;

public class Event implements Comparable<Event> {
    Event e;
    private final SimpleStringProperty term;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty date;

    public Event(int term, String subject, String date) {
        this.term = new SimpleStringProperty(String.valueOf(term));
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleStringProperty(date);
    }

    public String getTerm() {
        return term.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getDate() {
        return date.get();
    }

    public void setTerm(String term) {
        this.term.set(term);
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    @Override
    public int compareTo(Event event) {
        return this.getTerm().compareTo(event.getTerm());
    }

    @Override
    public String toString() {
        return " [priority=" + term + ", subject=" + subject + ", date=" + date + "]";
    }


}

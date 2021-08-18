package calendar.sample;

import java.util.Hashtable;

public class AddRule {
    Hashtable<Integer, Rule> hashTable = new Hashtable<>();

    public void addRule(String eventSubject, String auxTermID, String days)
    {
        hashTable.put(hashTable.size(), new Rule(eventSubject,auxTermID,days));
    }
}

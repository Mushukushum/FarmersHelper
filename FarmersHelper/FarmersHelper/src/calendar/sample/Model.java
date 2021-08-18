package calendar.sample;



//Packages and Imports

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Model {
    private final static Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    // for adding/editing events
    public int event_day;
    public int event_month;
    public int event_year;
    public int event_term_id;
    public String event_subject;

    // for the year and month the user has open, is "viewing"
    public int viewing_month;
    public int viewing_year;

    // for the current calendar being worked on
    public int calendar_start;
    public int calendar_end;
    public String calendar_start_date;
    public String calendar_name;

    // for editing rules
    public int rule_days;
    public String rule_term;
    public String rule_descript;

    // for editing terms
    public String term_name;
    public String term_date;

    //Function that returns a month Index based on the given month name
    public int getMonthIndex(String month){
        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String[] months = dateFormat.getMonths();
        final String[] spliceMonths = Arrays.copyOfRange(months, 0, 12);

        for(int i = 0; i < spliceMonths.length; i++)
            if(month.equals(spliceMonths[i]))
                return i;
        return 0;
    }
}


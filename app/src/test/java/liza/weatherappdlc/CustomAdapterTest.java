package liza.weatherappdlc;

import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CustomAdapterTest {

    @Test
    public void getDateSting_isCorrect_forDate() {
        String actual = CustomAdapter.getDateString("2018-06-13 11:12:30","EEE, MMM dd");
        assertThat(actual,is("Wed, Jun 13"));
    }
    @Test
    public void getDateString_isNull() {
        String actual = CustomAdapter.getDateString("2018-06-13","EEE, MMM dd");
        assertNull(actual);
    }
    @Test
    public void getDateSting_isCorrect_forTime() {
        String actual = CustomAdapter.getDateString("2018-06-13 11:12:30","hh a");
        assertThat(actual,is("11 AM"));

        actual = CustomAdapter.getDateString("2018-06-13 12:12:30","hh a");
        assertThat(actual,is("12 PM"));

        actual = CustomAdapter.getDateString("2018-06-13 23:12:30","hh a");
        assertThat(actual,is("11 PM"));

        actual = CustomAdapter.getDateString("2018-06-13 3:12:30","hh a");
        assertThat(actual,is("03 AM"));
    }



}
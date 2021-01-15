package com.company;
import java.time.LocalDate;
import java.time.LocalTime;


// Timer class
// contains all functions on showing or extracting Date and Time
public class Timer {


    public Timer(){}

    //Get current time
    public String getTimeAndDateFull(){

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();


        //concatenate date and time
        String timeAndDate = localDate.toString() +" "+ localTime.toString();


        return timeAndDate;

    }


    // get local date
    public String getDate(){

        LocalDate localDate = LocalDate.now();
        String date = localDate.toString();

        return date;
    }

    // get local time, has nanoseconds
    public String getTimeFull(){
        LocalTime localTime = LocalTime.now();
        String time = localTime.toString();

        return time;
    }

    //get local time without nanoseconds
    public String getTime(){
        LocalTime localTime = LocalTime.now();
        String time = localTime.toString();

        //get rid of unnecessary seconds and nanoseconds
        return time.substring(0,5);
    }


    // to compare time using set time
    public boolean compareTime(String input){

        // get the time now
        String timeNow = getTime();
        //System.out.println(timeNow);


        //can be simplified, most easy to understand like this
        if (input.equals(timeNow)) {

            return true;
        }

        else {

            return false;
        }

    }
}

package com.company;
import java.time.LocalDate;
import java.time.LocalTime;



public class Timer {



    public Timer(){}

    /*

        Get current time

     */
    public void getTimeAndDateFull(){

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        String timeAndDate = localDate.toString() +" "+ localTime.toString();

        //System.out.println(formatter.format(date));

        System.out.println("Local time and date is: " + timeAndDate);
    }

    public String getDate(){

        LocalDate localDate = LocalDate.now();
        String date = localDate.toString();

        return date;
    }

    public String getTimeFull(){
        LocalTime localTime = LocalTime.now();
        String time = localTime.toString();

        return time;
    }

    public String getTime(){
        LocalTime localTime = LocalTime.now();
        String time = localTime.toString();

        //get rid of unnecessary seconds and nanoseconds
        return time.substring(0,5);
    }


    /*
        Compares time
     */
    public boolean compareTime(String input){

        String timeNow = getTime();
        System.out.println(timeNow);


        //can be simplified, most easy to understand like this

        if (input.equals(timeNow)) {

            return true;
        }

        else {

            return false;
        }

    }
}

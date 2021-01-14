package com.company;

public class Main{

    public static void main(String [] args){
        System.out.println("Starting Datacraft plugin");

        Timer timer = new Timer();
        Config configuration = new Config();

        // get the time to update and post out of the config file
        String timeToPost = configuration.getUpdateConfig();

        //timer.getTimeAndDateFull();


        //System.out.println(timeToPost);
        boolean check = timer.compareTime(timeToPost);

        if (check){

            System.out.println("updating statistics");
            Statistics statistics = new Statistics();
            statistics.updateStatistics();
        }
        System.out.println(check);

    }

}
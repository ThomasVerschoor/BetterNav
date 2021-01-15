package com.company;

public class Main{

    //TODO: empty data folder fix

    public static void main(String [] args){

        // start up routine plugin
        System.out.println("Starting Datacraft plugin");

        // start timing class
        Timer timer = new Timer();
        System.out.println("Current date and time is: "+ timer.getTimeAndDateFull());

        // read in configuration of plugin
        Config configuration = new Config();

        // check if plugin has been used before
        Statistics statistics = new Statistics();
        statistics.loadStatistics();

        // get the time to update and post out of the config file
        String timeToPost = configuration.getUpdateConfig();
            //timer.getTimeAndDateFull();
            //System.out.println(timeToPost);
        boolean check = timer.compareTime(timeToPost);




        if (check){

            System.out.println("updating statistics");

            statistics.updateStatistics();
        }

        // start or continue capturing new statistics
        //statistics.logStatistics();


    }

}
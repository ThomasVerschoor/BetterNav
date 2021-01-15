package com.company;

//contains all functions on statistics updating and loading
public class Statistics {

    public Statistics(){};


    // check and load already created data
    public void loadStatistics(){
        CapturedData captData = new CapturedData();
        captData.printData();

        // get all the files in data folder
        String[] loadData = captData.getData();

        // For each day in the loadData array
        for (String day : loadData) {

            // every day
            System.out.println(day);

            String textInFile = captData.readFile(day);
            System.out.println(textInFile);

        }

        //System.out.println(loadData.length);

        //String day1 = captData.readFile("2021-01-17.txt");




    };


    //will be used to log (store) current statistics
    public void logStatistics(){
        //Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        //System.out.println("Enter username");

        //String amountOfPlayers = myObj.nextLine();  // Read user input

    };

    //will be used to update statistics and make new file
    public void updateStatistics(){
        CapturedData newData = new CapturedData();
        newData.addDay();
    };
}

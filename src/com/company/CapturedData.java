package com.company;


import java.io.*;


//contains all functions on handling the data
public class CapturedData {

    public CapturedData(){};

    // get current user directory
    String pluginDirectory = System.getProperty("user.dir");

    //save in data directory
    String dataDirectory = pluginDirectory.concat("/src/com/company/data");

    // will add new textfile for new day
    public void addDay(){

        //currently hardcoded text, will use date extracting in future
        String fileName = "2021-01-17.txt";

        //concatenate to get full path
        String absolutePath = dataDirectory + File.separator + fileName;

        // Write the content in file
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath))) {

            //write example text to file, will be using current statistics in future
            String fileContent = "Amount of players: 17";
            bufferedWriter.write(fileContent);
        } catch (IOException e) {
            // Exception handling
        }

    };


    //get the available data
    public String[] getData() {

        // Creates an array in which we will store the names of files and directories
        String[] availableData;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(dataDirectory);

        // Populates the array with names of files and directories
        availableData = f.list();

        return availableData;

    };


    // print out the available data
    public void printData(){

        //make use of function above to get the data
        String[] availableData = getData();

        // For each day in the availableData array
        for (String day : availableData) {

            // Print the names of files and directories
            System.out.println(day);
        }

    };


    //read data out of file given filename and directory
    public String readFile(String fileName){

        //get absolute path
        String absolutePath = dataDirectory +"/"+fileName;
        //System.out.println(absolutePath);

        //get content of file
        String contentOfFile = "";


        // Read the content from file
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath))) {
            String line = bufferedReader.readLine();
            while(line != null) {
                //System.out.println(line);

                //add line to content of the file
                contentOfFile = contentOfFile + line;

                line = bufferedReader.readLine();


            }
        } catch (FileNotFoundException e) {
            // Exception handling
        } catch (IOException e) {
            // Exception handling
        }


        return contentOfFile;
    };


}

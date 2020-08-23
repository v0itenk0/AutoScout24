package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVReader {

    public static List<String[]> readerCSV(String path) {

        String csvFile = path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<String[]> csvData = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] paramsOfline = line.split(cvsSplitBy);
                csvData.add(paramsOfline);
            }
            System.out.println("File: " + csvFile +"was read successfully. Number of records - " + csvData.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return csvData;
    }
}

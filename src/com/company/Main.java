package com.company;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    public static List<String[]> listingsCSV = new ArrayList<>();
    public static List<String[]> contactsCSV = new ArrayList<>();

    public static void main(String[] args) {
          listingsCSV = CSVReader.readerCSV("C:/Autoscout24Voitenko/CSV/listings.csv");
          contactsCSV = CSVReader.readerCSV("C:/Autoscout24Voitenko/CSV/contacts.csv");
          generateReports(listingsCSV, contactsCSV);

//          //Tests
//          //---------------Report 1 ------------------------//
//          Tests.testReport1Generate_BasicSuccess();
//          Tests.testReport1Generate_EmptyFile();
//          Tests.testReport1Generate_noPrivateResults();
//          Tests.testReport1Generate_SmallRealData();
//          //---------------Report 2 ------------------------//
//          Tests.testReport2Generate_EmptyFile();
//          Tests.testReport2Generate_TwoRecords();
//          Tests.testReport2Generate_SmallRealData();
//          //---------------Report 3 ------------------------//
//          Tests.testReport3Generate_TwoRecords();
//          Tests.testReport3Generate_NoContactForListing();
//          Tests.testReport3Generate_EmptyFiles();
//          Tests.testReport3Generate_SmallRealData();
//          //---------------Report 4 ------------------------//
//          Tests.testReport4Generate_EmptyFiles();
//          Tests.testReport4Generate_WrongId();
//          Tests.testReport4Generate_SmallRealData();
    }


    /** Average Listing Selling Price per Seller Type. There are three Seller Types: private, dealer and other.
        For each of these types, it has been provided an average selling price.
        The average price is provided as rounded to integer value **/
    protected static List<String[]> report1Generate(List<String[]> csvRecords) {
        int sumPrivate, numPrivate, sumDealer, numDealer, sumOther, numOther;
        sumPrivate = numPrivate = sumDealer = numDealer = sumOther = numOther = 0;
        List<String[]> outputReport1 = new ArrayList<>();

        for(int i=1; i < csvRecords.size(); i++) {

            if (csvRecords.get(i)[4].contains("private")) {
                numPrivate++;
                sumPrivate += Integer.parseInt(csvRecords.get(i)[2]);
            }
            if (csvRecords.get(i)[4].contains("dealer")) {
                numDealer++;
                sumDealer += Integer.parseInt(csvRecords.get(i)[2]);
            }
            if (csvRecords.get(i)[4].contains("other")) {
                numOther++;
                sumOther += Integer.parseInt(csvRecords.get(i)[2]);
            }
        }

        double aPrivate = 0;
        if (numPrivate > 0) {
            aPrivate = Math.round((double)sumPrivate/numPrivate);
        }

        double aDealer = 0;
        if (numDealer > 0) {
            aDealer = Math.round((double)sumDealer/numDealer);
        }

        double aOther = 0;
        if (numOther > 0) {
            aOther = Math.round(((double)sumOther/numOther));
        }

        String[] output1 = {"private", "€ "+ aPrivate, Integer.toString(numPrivate)};
        String[] output2 = {"dealer", "€ "+aDealer, Integer.toString(numDealer)};
        String[] output3 = {"other", "€ "+aOther, Integer.toString(numOther)};
        outputReport1 = Arrays.asList(output1, output2, output3);
        report1FormatedOutput(outputReport1);
        return outputReport1;
    }

    /** Percentual distribution of available cars by Make
        For each make, it has been reported the percentual amount of listings.
        The report has been sorted by distribution, where makes with biggest numbers stays on top. **/
    protected static List<String[]> report2Generate(List<String[]> csvRecords) {
        List<String[]> outputReport2 = new ArrayList<>();
        if(csvRecords.size() > 1) {
            HashMap<String, Double> mapNumberOfRecords = countNumberOfAppearances(csvRecords, 1);
            HashMap<String, Double> mapPercents = new HashMap<>(mapNumberOfRecords);
            mapPercents.replaceAll ((key, value) -> value != null ? Math.round((value*100/(csvRecords.size()-1))*100.0)/100.0 : null);
            ValueComparator valueComparator = new ValueComparator(mapPercents);
            TreeMap<String, Double> sortedMap = new TreeMap<>(valueComparator);

            sortedMap.putAll(mapPercents);

            for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
                String[] output = {entry.getKey(), entry.getValue()+"%"};
                outputReport2.add(output);
            }
            report2FormatedOutput(outputReport2);
        } else {
            String[] output = {"error number of record", "-"};
            outputReport2.add(output);
        }

        return outputReport2;
    }

    /** Average price of the 30% most contacted listings
        Using the CSVcontacts, function reports the average price of the 30% most contacted listings. **/
    protected static String report3Generate(List<String[]> CSVlistings, List<String[]> CSVcontacts) {
        if ((CSVlistings.size() > 1) &&(CSVcontacts.size() > 1)) {
            HashMap<String, Double> mapNumberOfRecords = countNumberOfAppearances(CSVcontacts, 0);
            ValueComparator valueComparator = new ValueComparator(mapNumberOfRecords);
            TreeMap<String, Double> sortedMap = new TreeMap<>(valueComparator);
            sortedMap.putAll(mapNumberOfRecords);

            int threshold = 0;
            double previousValue = 0;

            List<String> theMostContacted = new ArrayList<>();
            for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
                if(threshold <= 0.3*sortedMap.size()) {
                    theMostContacted.add(entry.getKey());
                    previousValue = entry.getValue();
                } else {
                    if (entry.getValue() == previousValue) {
                        theMostContacted.add(entry.getKey());
                    }
                }
                threshold++;
            }
            //System.out.println(theMostContacted);
            double sum = 0;
            for (int i=0; i < theMostContacted.size(); i++) {
                for(int j=1; j < CSVlistings.size(); j++) {
                    if (CSVlistings.get(j)[0].equals(theMostContacted.get(i))) {
                        sum += Double.parseDouble(CSVlistings.get(j)[2]);
                    }
                }
            }

            String result = "€ "+(Math.round((sum/theMostContacted.size()) * 100.0) / 100.0);
            report3FormatedOutput(result);
            return result;
        } else {
            String result = "Error - incorrect data";
            report3FormatedOutput(result);
            return result;
        }
    }

    /** The Top 5 most contacted listings per Month Using the CSVcontacts,
        report which listing had more contacts in each month.
        Reported fields: Ranking, Listing Id, Make, Selling Price, Mileage, Total Amount of contacts etc  **/
    protected static List<String[]> report4Generate(List<String[]> CSVlistings, List<String[]> CSVcontacts) {
        List<String[]> outputReport4 = new ArrayList<>();
        if((CSVlistings.size() > 1 ) && (CSVcontacts.size() > 1) && checkIds(CSVlistings, CSVcontacts)){
            Map<String, List<String>> monthIds = new HashMap<>();
            Map<String, Map<String, Double>> monthIdsCounted = new HashMap<>();
            Map<String, TreeMap<String, Double>> results = new HashMap<>();

            for (int i=1; i < CSVcontacts.size(); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(CSVcontacts.get(i)[1]));
                int mMonth = calendar.get(Calendar.MONTH);
                monthIds.computeIfAbsent(theMonth(mMonth), k -> new ArrayList<>()).add(CSVcontacts.get(i)[0]);
            }

            for (Map.Entry<String, List<String>> entry : monthIds.entrySet()) {
                HashMap<String, Double> mapNumberOfRecords = new HashMap<>();
                for(int i=0; i < entry.getValue().size(); i++) {
                    if (mapNumberOfRecords.containsKey(entry.getValue().get(i))) {
                        mapNumberOfRecords.replace(entry.getValue().get(i), mapNumberOfRecords.get(entry.getValue().get(i))+1.0);
                    } else {
                        mapNumberOfRecords.put(entry.getValue().get(i), 1.0);
                    }
                }
                monthIdsCounted.put(entry.getKey(), mapNumberOfRecords);
            }

            for (Map.Entry<String, Map<String, Double>> entry : monthIdsCounted.entrySet()) {
                ValueComparator valueComparator = new ValueComparator(entry.getValue());
                TreeMap<String, Double> sortedMap = new TreeMap<>(valueComparator);
                sortedMap.putAll(entry.getValue());
                results.put(entry.getKey(), sortedMap);
            }

            int counter = 1;
            for (int monthNumber = 0; monthNumber <= 11; monthNumber++) {
                for (Map.Entry<String, TreeMap<String, Double>> result : results.entrySet()) {
                    if(result.getKey() == theMonth(monthNumber)) {
                        for (Map.Entry<String, Double> item : result.getValue().entrySet()) {
                            if (counter < 6) {
                                String[] lestingRecord = getRecord(CSVlistings, item.getKey());
                                String[] output = {result.getKey(), Integer.toString(counter), item.getKey(), Integer.toString(item.getValue().intValue()), lestingRecord[1], "€ "+ lestingRecord[2] + ",-", lestingRecord[3] +" KM"};
                                outputReport4.add(output);
                            }
                            counter++;
                        }
                        counter = 1;
                    }
                }
            }
            report4FormatedOutput(outputReport4);
            return outputReport4;
        } else {
            String[] output = {"-", "-", "-", "-", "-", "-", "-"};
            outputReport4.add(output);
            report4FormatedOutput(outputReport4);
            return outputReport4;
        }
    }

    private static void report1FormatedOutput(List<String[]> dataReport){
        String leftAlignFormat = "| %-15s | %-17s | %-19s |%n";
        System.out.format("+-----------------+-------------------+---------------------+%n");
        System.out.format("| Seller Type     | Average in Euro   | Number of records   |%n");
        System.out.format("+-----------------+-------------------+---------------------+%n");
        System.out.format(leftAlignFormat, dataReport.get(0));
        System.out.format(leftAlignFormat, dataReport.get(1));
        System.out.format(leftAlignFormat, dataReport.get(2));
        System.out.format("+-----------------+-------------------+---------------------+%n");
    }

    private static void report2FormatedOutput(List<String[]> dataReport){
        String leftAlignFormat = "| %-15s | %-17s | %n";

        System.out.format("+-----------------+-------------------+%n");
        System.out.format("| Make            | Distribution      |%n");
        System.out.format("+-----------------+-------------------+%n");
        for(int i=0; i < dataReport.size(); i++) {
            System.out.format(leftAlignFormat, dataReport.get(i));
        }
        System.out.format("+-----------------+-------------------+%n");
    }

    private static void report3FormatedOutput(String value){
        String leftAlignFormat = "| %-15s | %n";

        System.out.format("+-----------------+%n");
        System.out.format("| Average price   |%n");
        System.out.format("+-----------------+%n");

        String result = value;
        System.out.format(leftAlignFormat, result);

        System.out.format("+-----------------+%n");
    }


    private static void report4FormatedOutput(List<String[]> dataReport){
        String leftAlignFormat = "| %-8s | %-8s | %-10s | %-24s |%-18s |%-14s |%-10s | %n";
        System.out.format("+----------+----------+------------+--------------------------+-------------------+---------------+-----------+%n");
        System.out.format("| Month    | Ranking  | Listing Id | Total Amount of contacts | Make              | Selling Price | Mileage   |%n");
        System.out.format("+----------+----------+------------+--------------------------+-------------------+---------------+-----------+%n");
        goThroughListElements(dataReport, leftAlignFormat);
        System.out.format("+----------+----------+------------+--------------------------+-------------------+---------------+-----------+%n");
    }

    private static void goThroughListElements(List<String[]> dataList, String formatAlignment){
        for(int i=0; i< dataList.size(); i++) {
            if(i >=1) {
                if (!dataList.get(i)[0].equals(dataList.get(i-1)[0])){
                    System.out.format("|----------+----------+------------+--------------------------+-------------------+---------------+-----------|%n");
                }
            }
            System.out.format(formatAlignment, dataList.get(i));
        }
    }

    /** Generates 4 different reports based on the input data from the csv file **/
    private static void generateReports(List<String[]> CSVlistings, List<String[]> CSVcontacts) {
        System.out.println("1 - Average Listing Selling Price per Seller Type");
        report1Generate(CSVlistings);
        System.out.println("2 - Percentual distribution of available cars by Make");
        report2Generate(CSVlistings);
        System.out.println("3 - Average price of the 30% most contacted listings");
        report3Generate(CSVlistings, CSVcontacts);
        System.out.println("4 - The Top 5 most contacted listings per Month ");
        report4Generate(CSVlistings, CSVcontacts);
    }

    private static HashMap<String, Double> countNumberOfAppearances(List<String[]> csvRecords, int indexOfColumn) {
        HashMap<String, Double> mapNumberOfRecords = new HashMap<>();
        for(int i=1; i < csvRecords.size(); i++) {
            if (mapNumberOfRecords.containsKey(csvRecords.get(i)[indexOfColumn])) {
                mapNumberOfRecords.replace(csvRecords.get(i)[indexOfColumn], mapNumberOfRecords.get(csvRecords.get(i)[indexOfColumn])+1);
            } else {
                mapNumberOfRecords.put(csvRecords.get(i)[indexOfColumn], 1.0);
            }
        }
        return mapNumberOfRecords;
    }

    private static String[] getRecord(List<String[]> CSVlistings, String id){
        for (int i = 1; i < CSVlistings.size(); i++) {
            for ( int j = 0; j < CSVlistings.get(i).length; j++ ) {
                if (CSVlistings.get(i)[0].contains(id)) {
                    return CSVlistings.get(i);
                }
            }
        }
        return new String[]{};
    }

    private static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    private static Boolean checkIds(List<String[]> CSVlistings, List<String[]> CSVcontacts){
        Boolean flag = false;
        for(int i=0; i < CSVcontacts.size(); i++) {
            for(int k=0; k< CSVlistings.size(); k++){
                if (CSVcontacts.get(i)[0].equals(CSVlistings.get(k)[0])){
                    flag = true;
                }
            }
        }
        return flag;
    }
}

package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {

    //------------------------------------REPORT 1 ------------------------------------------//

    protected static Boolean testReport1Generate_BasicSuccess() {
        //test data:
        List<String[]> listings = new ArrayList<>();
        listings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        listings.add(new String[] {"1000", "\"Audi\"", "100", "100", "\"private\""});
        listings.add(new String[] {"1001", "\"Mazda\"", "300", "200", "\"private\""});
        listings.add(new String[] {"1002", "\"BWM\"", "400", "300", "\"dealer\""});
        listings.add(new String[] {"1003", "\"BWM\"", "150", "150", "\"other\""});

        List<String[]> outputReport1ExpectedResult = new ArrayList<>();
        outputReport1ExpectedResult.add(new String[]{"private", "€ 200.0", "2"});
        outputReport1ExpectedResult.add(new String[]{"dealer", "€ 400.0", "1"});
        outputReport1ExpectedResult.add(new String[]{"other", "€ 150.0", "1"});

        List<String[]> outputReport1Generate = Main.report1Generate(listings);
        return compareLists(outputReport1ExpectedResult, outputReport1Generate, 1, "testReport1Generate_BasicSuccess");
    }

    protected static Boolean testReport1Generate_EmptyFile() {
        //test data:
        List<String[]> listings = new ArrayList<>();
        listings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});

        List<String[]> outputReport1ExpectedResult = new ArrayList<>();
        outputReport1ExpectedResult.add(new String[]{"private", "€ 0.0", "0"});
        outputReport1ExpectedResult.add(new String[]{"dealer", "€ 0.0", "0"});
        outputReport1ExpectedResult.add(new String[]{"other", "€ 0.0", "0"});

        List<String[]> outputReport1Generate = Main.report1Generate(listings);
        return compareLists(outputReport1ExpectedResult, outputReport1Generate, 1, "testReport1Generate_EmptyFile");
    }

    protected static Boolean testReport1Generate_noPrivateResults() {
        //test data:
        List<String[]> listings = new ArrayList<>();
        listings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        listings.add(new String[] {"1002", "\"BWM\"", "400", "300", "\"dealer\""});
        listings.add(new String[] {"1003", "\"BWM\"", "150", "150", "\"other\""});

        List<String[]> outputReport1ExpectedResult = new ArrayList<>();
        outputReport1ExpectedResult.add(new String[]{"private", "€ 0.0", "0"});
        outputReport1ExpectedResult.add(new String[]{"dealer", "€ 400.0", "1"});
        outputReport1ExpectedResult.add(new String[]{"other", "€ 150.0", "1"});

        List<String[]> outputReport1Generate = Main.report1Generate(listings);
        return compareLists(outputReport1ExpectedResult, outputReport1Generate, 1, "testReport1Generate_noPrivateResults");
    }

    protected static Boolean testReport1Generate_SmallRealData() {
        //test data:
        List<String[]> CSVlistings = getListingsList();
        List<String[]> outputReport1ExpectedResult = new ArrayList<>();
        outputReport1ExpectedResult.add(new String[]{"private", "€ 33480.0", "5"});
        outputReport1ExpectedResult.add(new String[]{"dealer", "€ 20397.0", "5"});
        outputReport1ExpectedResult.add(new String[]{"other", "€ 37955.0", "3"});

        List<String[]> outputReport1Generate = Main.report1Generate(CSVlistings);
        return compareLists(outputReport1ExpectedResult, outputReport1Generate, 1, "testReport1Generate_SmallRealData");
    }

    //------------------------------------REPORT 2 ------------------------------------------//

    protected static Boolean testReport2Generate_EmptyFile() {
        List<String[]> CSVlistings = new ArrayList<>();

        List<String[]> outputReport2ExpectedResult = new ArrayList<>();
        outputReport2ExpectedResult.add(new String[]{"error number of record", "-"});
        List<String[]> outputReport2Generate = Main.report2Generate(CSVlistings);
        return compareLists(outputReport2ExpectedResult, outputReport2Generate, 2, "testReport2Generate_EmptyFile");
    }

    protected static Boolean testReport2Generate_TwoRecords() {
        //test data:
        List<String[]> CSVlistings = new ArrayList<>();
        CSVlistings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        CSVlistings.add(new String[] {"1000", "\"Audi\"", "49717", "6500", "\"private\""});

        List<String[]> outputReport2ExpectedResult = new ArrayList<>();
        outputReport2ExpectedResult.add(new String[]{"\"Audi\"", "100.0%"});
        List<String[]> outputReport2Generate = Main.report2Generate(CSVlistings);
        return compareLists(outputReport2ExpectedResult, outputReport2Generate, 2, "testReport2Generate_TwoRecords");
    }

    protected static Boolean testReport2Generate_SmallRealData() {
        //test data:
        List<String[]> CSVlistings = getListingsList();

        List<String[]> outputReport2ExpectedResult = new ArrayList<>();
        outputReport2ExpectedResult.add(new String[]{"\"Audi\"", "38.46%"});
        outputReport2ExpectedResult.add(new String[]{"\"VW\"", "15.38%"});
        outputReport2ExpectedResult.add(new String[]{"\"Mazda\"", "15.38%"});
        outputReport2ExpectedResult.add(new String[]{"\"Fiat\"", "7.69%"});
        outputReport2ExpectedResult.add(new String[]{"\"Toyota\"", "7.69%"});
        outputReport2ExpectedResult.add(new String[]{"\"Renault\"", "7.69%"});
        outputReport2ExpectedResult.add(new String[]{"\"BWM\"", "7.69%"});
        List<String[]> outputReport2Generate = Main.report2Generate(CSVlistings);
        return compareLists(outputReport2ExpectedResult, outputReport2Generate, 2, "testReport2Generate_SmallRealData");
    }

    //------------------------------------REPORT 3 ------------------------------------------//

    protected static Boolean testReport3Generate_TwoRecords() {
        //test data:
        List<String[]> CSVlistings = new ArrayList<>();
        CSVlistings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        CSVlistings.add(new String[] {"1000", "\"Audi\"", "49717", "6500", "\"private\""});

        List<String[]> CSVcontacts = new ArrayList<>();
        CSVcontacts.add(new String[] {"listing_id","contact_date","month"});
        CSVcontacts.add(new String[] {"1000","1561143364000","June"});

        String expectedResult = "€ 49717.0";
        boolean result = true;
        if (!expectedResult.equals(Main.report3Generate(CSVlistings, CSVcontacts))) {
            result = false;
            System.out.println("testReport3Generate_TwoRecords - fail");
            return result;
        }
        System.out.println("testReport3Generate_TwoRecords - correct");
        return result;
    }

    protected static Boolean testReport3Generate_NoContactForListing() {
        //test data:
        List<String[]> CSVlistings = new ArrayList<>();
        CSVlistings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        CSVlistings.add(new String[] {"1", "\"Audi\"", "49717", "6500", "\"private\""});

        List<String[]> CSVcontacts = new ArrayList<>();
        CSVcontacts.add(new String[] {"listing_id","contact_date","month"});
        CSVcontacts.add(new String[] {"1000","1561143364000","June"});

        String expectedResult = "€ 0.0";
        boolean result = true;
        if (!expectedResult.equals(Main.report3Generate(CSVlistings, CSVcontacts))) {
            result = false;
            System.out.println("testReport3Generate_NoContactForListing - fail");
            return result;
        }
        System.out.println("testReport3Generate_NoContactForListing - correct");
        return result;
    }

    protected static Boolean testReport3Generate_EmptyFiles() {
        //test data:
        List<String[]> CSVlistings = new ArrayList<>();
        List<String[]> CSVcontacts = new ArrayList<>();

        String expectedResult = "Error - incorrect data";
        boolean result = true;
        if (!expectedResult.equals(Main.report3Generate(CSVlistings, CSVcontacts))) {
            result = false;
            System.out.println("testReport3Generate_EmptyFiles - fail");
            return result;
        }
        System.out.println("testReport3Generate_EmptyFiles - correct");
        return result;
    }

    protected static Boolean testReport3Generate_SmallRealData() {
        //test data:
        List<String[]> CSVcontacts = getContactsList();
        List<String[]> CSVlistings = getListingsList();

        String expectedResult = "€ 26854.0";
        boolean result = true;
        if (!expectedResult.equals(Main.report3Generate(CSVlistings, CSVcontacts))) {
            result = false;
            System.out.println("testReport3Generate_SmallRealData - fail");
            return result;
        }
        System.out.println("testReport3Generate_SmallRealData - correct");
        return result;
    }

    //------------------------------------REPORT 4 ------------------------------------------//
    protected static Boolean testReport4Generate_EmptyFiles() {
        List<String[]> CSVlistings = new ArrayList<>();
        List<String[]> CSVcontacts = new ArrayList<>();

        List<String[]> outputReport4ExpectedResult = new ArrayList<>();
        outputReport4ExpectedResult.add(new String[]{"-", "-", "-", "-", "-", "-", "-"});

        List<String[]> outputReport4Generate = Main.report4Generate(CSVlistings, CSVcontacts);
        return compareLists(outputReport4ExpectedResult, outputReport4Generate, 4, "testReport4Generate_EmptyFiles");
    }

    protected static Boolean testReport4Generate_WrongId() {
        List<String[]> CSVlistings = new ArrayList<>();
        CSVlistings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        CSVlistings.add(new String[] {"1000", "\"Audi\"", "49717", "6500", "\"private\""});

        List<String[]> CSVcontacts = new ArrayList<>();
        CSVcontacts.add(new String[] {"listing_id","contact_date","month"});
        CSVcontacts.add(new String[] {"1004","1561143364000","June"});
        CSVcontacts.add(new String[] {"1003","1592498493000","June"});CSVcontacts.add(new String[] {"1003","1466535364000","June"});

        List<String[]> outputReport4ExpectedResult = new ArrayList<>();
        outputReport4ExpectedResult.add(new String[]{"-", "-", "-", "-", "-", "-", "-"});

        List<String[]> outputReport4Generate = Main.report4Generate(CSVlistings, CSVcontacts);
        return compareLists(outputReport4ExpectedResult, outputReport4Generate, 4, "testReport4Generate_WrongId");
    }

    protected static Boolean testReport4Generate_SmallRealData() {
        List<String[]> CSVlistings = getListingsList();
        List<String[]> CSVcontacts = getContactsList();
        List<String[]> outputReport4ExpectedResult = new ArrayList<>();
        outputReport4ExpectedResult.add(new String[]{"January", "1", "1008", "2", "\"VW\"", "€ 26350,-", "500 KM"});
        outputReport4ExpectedResult.add(new String[]{"January", "2", "1009", "1", "\"Audi\"", "€ 40070,-", "2500 KM"});
        outputReport4ExpectedResult.add(new String[]{"February", "1", "1010", "2", "\"Fiat\"", "€ 41201,-", "1500 KM"});
        outputReport4ExpectedResult.add(new String[]{"March", "1", "1001", "2", "\"Mazda\"", "€ 22031,-", "7000 KM"});
        outputReport4ExpectedResult.add(new String[]{"March", "2", "1002", "1", "\"BWM\"", "€ 17742,-", "6000 KM"});
        outputReport4ExpectedResult.add(new String[]{"March", "3", "1003", "1", "\"Toyota\"", "€ 11768,-", "0 KM"});
        outputReport4ExpectedResult.add(new String[]{"March", "4", "1004", "1", "\"Mazda\"", "€ 25219,-", "3000 KM"});
        outputReport4ExpectedResult.add(new String[]{"March", "5", "1005", "1", "\"Audi\"", "€ 43667,-", "500 KM"});
        outputReport4ExpectedResult.add(new String[]{"April", "1", "1012", "1", "\"Audi\"", "€ 10286,-", "3000 KM"});
        outputReport4ExpectedResult.add(new String[]{"June", "1", "1003", "4", "\"Toyota\"", "€ 11768,-", "0 KM"});
        outputReport4ExpectedResult.add(new String[]{"June", "2", "1001", "3", "\"Mazda\"", "€ 22031,-", "7000 KM"});
        outputReport4ExpectedResult.add(new String[]{"June", "3", "1002", "2", "\"BWM\"", "€ 17742,-", "6000 KM"});
        outputReport4ExpectedResult.add(new String[]{"June", "4", "1004", "1", "\"Mazda\"", "€ 25219,-", "3000 KM"});
        outputReport4ExpectedResult.add(new String[]{"June", "5", "1005", "1", "\"Audi\"", "€ 43667,-", "500 KM"});

        List<String[]> outputReport4Generate = Main.report4Generate(CSVlistings, CSVcontacts);
        return compareLists(outputReport4ExpectedResult, outputReport4Generate, 4, "testReport4Generate_SmallRealData");
    }



    private static Boolean compareLists(List<String[]> expectedResult, List<String[]> realResult, int number, String name){
        boolean result = true;

        for(int i=0; i<expectedResult.size(); i++) {
            for(int j=0; j < expectedResult.get(i).length; j++) {
                if (!expectedResult.get(i)[j].equals(realResult.get(i)[j])) {
                    result = false;
                    System.out.println(name+" "+number+" - fail");
                    return result;
                }
            }
        }
        System.out.println(name+" "+number+" - correct");
        return result;
    }

    private static List<String[]> getListingsList(){
        List<String[]> CSVlistings = new ArrayList<>();
        CSVlistings.add(new String[] {"name1", "name2", "name3", "name4", "name5"});
        CSVlistings.add(new String[] {"1000", "\"Audi\"", "49717", "6500", "\"private\""});
        CSVlistings.add(new String[] {"1001", "\"Mazda\"", "22031", "7000", "\"private\""});
        CSVlistings.add(new String[] {"1002", "\"BWM\"", "17742", "6000", "\"dealer\""});
        CSVlistings.add(new String[] {"1003", "\"Toyota\"", "11768", "0", "\"dealer\""});
        CSVlistings.add(new String[] {"1004", "\"Mazda\"", "25219", "3000", "\"other\""});
        CSVlistings.add(new String[] {"1005", "\"Audi\"", "43667", "500", "\"private\""});
        CSVlistings.add(new String[] {"1006", "\"Renault\"", "47446", "7500", "\"other\""});
        CSVlistings.add(new String[] {"1007", "\"VW\"", "25633", "8000", "\"private\""});
        CSVlistings.add(new String[] {"1008", "\"VW\"", "26350", "500", "\"private\""});
        CSVlistings.add(new String[] {"1009", "\"Audi\"", "40070", "2500", "\"dealer\""});
        CSVlistings.add(new String[] {"1010", "\"Fiat\"", "41201", "1500", "\"other\""});
        CSVlistings.add(new String[] {"1012", "\"Audi\"", "10286", "3000", "\"dealer\""});
        CSVlistings.add(new String[] {"1013", "\"Audi\"", "22121", "2000", "\"dealer\""});
        return CSVlistings;
    }

    private static List<String[]> getContactsList(){
        List<String[]> CSVcontacts = new ArrayList<>();
        CSVcontacts.add(new String[] {"listing_id","contact_date","month"});CSVcontacts.add(new String[] {"1004","1561143364000","June"});
        CSVcontacts.add(new String[] {"1003","1592498493000","June"});CSVcontacts.add(new String[] {"1003","1466535364000","June"});
        CSVcontacts.add(new String[] {"1003","1561143364000","June"});CSVcontacts.add(new String[] {"1003","1592498493000","June"});
        CSVcontacts.add(new String[] {"1002","1592498493000","June"});CSVcontacts.add(new String[] {"1002","1592498493000","June"});
        CSVcontacts.add(new String[] {"1001","1466535364000","June"});CSVcontacts.add(new String[] {"1001","1561143364000","June"});
        CSVcontacts.add(new String[] {"1001","1584070396000","March"});CSVcontacts.add(new String[] {"1001","1458586564000","March"});
        CSVcontacts.add(new String[] {"1002","1521658564000","March"});CSVcontacts.add(new String[] {"1001","1592498493000","June"});
        CSVcontacts.add(new String[] {"1005","1592498493000","June"});CSVcontacts.add(new String[] {"1006","1466535364000","June"});
        CSVcontacts.add(new String[] {"1009","1516474564000","January"});CSVcontacts.add(new String[] {"1003","1458586564000","March" });
        CSVcontacts.add(new String[] {"1004","1521658564000","March"});CSVcontacts.add(new String[] {"1005","1584070396000","March"});
        CSVcontacts.add(new String[] {"1008","1579546564000","January"});CSVcontacts.add(new String[] {"1008","1421780164000","January"});
        CSVcontacts.add(new String[] {"1010","1424458564000","February"});CSVcontacts.add(new String[] {"1010","1392922564000","February"});
        CSVcontacts.add(new String[] {"1012","1398020164000","April"});
        return CSVcontacts;
    }


}

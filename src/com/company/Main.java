package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

class TestCase {
    int weapons;
    int maxWeight;
    List<Integer> weightValues;
    List<Integer> damagevalues;
    Map<Integer, Double> ratio;

    TestCase(int weapons,int maxWeight,List<Integer> weightValues,List<Integer> damagevalues) {
        this.weapons = weapons;
        this.maxWeight = maxWeight;
        this.weightValues = weightValues;
        this.damagevalues = damagevalues;
    }

    public void calculateRatio()
    {
        //iterate weightvalues and damagevalues and add to ratio
        HashMap<Integer, Double> localRatio = new HashMap<>();
        for(int i=0; i<weightValues.size(); i++)
        {
            double currRatio = ((double)damagevalues.get(i))/(weightValues.get(i));
            localRatio.put(i, currRatio);
        }
        ratio = sortByValue(localRatio);
    }

    public void display() {
        System.out.println();
        System.out.println("<----------------------------------------------------------->");
        System.out.println(weapons);
        System.out.println(maxWeight);
        System.out.println(weightValues);
        System.out.println(damagevalues);
        System.out.println(ratio);
    }
    public String runValidationTest() {
        String error = "";
        if(weapons <= 0) {error = error.concat("Number of Weapons is Zero/Negative ");}
        if(maxWeight <= 0) {error = error.concat("Maximum Weight is Zero/Negative ");}
        for(int i=0;i<weightValues.size();i++){
            if(weightValues.get(i) <= 0 || damagevalues.get(i) <= 0) {
                error = error.concat("Weight/Damage of Ammunition is Zero/Negative. ");
                break;
            }
        }
        return error;
    }

    public Map<Integer, Double> sortByValue(HashMap<Integer, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double> > list = new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());
        // Sort the list using lambda expression
        Collections.sort(list, Comparator.comparing(Entry::getValue,Comparator.reverseOrder()));
        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
public class Main {
    static BufferedWriter writer;

    public static void main(String[] args) {
        Map<Integer,String> invalidTestcase;
        List<TestCase> testCases = new ArrayList<>();

        readBookList(testCases);  //for reading from inputPS4.txt

        testCases.forEach(TestCase::calculateRatio);

        invalidTestcase = fetchInvalidTestCase(testCases);
        System.out.println(invalidTestcase);

        for(int i=0;i<testCases.size();i++) {
            if(!invalidTestcase.containsKey(i)) {
                testCases.get(i).display();
            }
        }
        List<Double> maxSize = new ArrayList<>();
        testCases.forEach(testCase -> {
            //for each iter, we obtain total value from the knapsack function and keep adding it to the size list
            double size = getMaxValue(testCase);
            maxSize.add(size);
        });
        for(int i=0;i<testCases.size();i++) {
            if(invalidTestcase.containsKey(i)) {
                maxSize.add(-1.0);
            }
            else {
                maxSize.add(getMaxValue(testCases.get(i)));
            }
        }
            
    }

    private static void closeWriter() { // for closing the writer object
        try {
            writer.flush();
            writer.close(); // close the writer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initializeWriter() { // returns the singleton of BufferedWriter
        if (writer == null) {
            try {
                writer = new BufferedWriter(new FileWriter("outputPS4.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Operation 1
    public static void readBookList(List<TestCase> testCases) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); 
            String line = reader.readLine();
            if(line == null) {
                System.out.println("Empty file"); //exception
            }
            while (line != null) {
                while(!line.toLowerCase().contains("weapons")) {
                    line = reader.readLine();
                }
                String[] item1 = line.split(":"); 
                line = reader.readLine(); 
                String[] item2 = line.split(":"); 
                line = reader.readLine(); 
                
                String[] item;
                List<Integer> weightValues = new ArrayList<>();
                List<Integer> damageValues = new ArrayList<>();
                for(int i=0; i< Integer.parseInt(item1[1].trim()); i++) {
                    item = line.split("/");
                    weightValues.add(Integer.parseInt(item[1].trim()));
                    damageValues.add(Integer.parseInt(item[2].trim()));
                    line = reader.readLine();
                }
                testCases.add(new TestCase(Integer.parseInt(item1[1].trim()),Integer.parseInt(item2[1].trim()),weightValues,damageValues));
            }
            reader.close(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    private static Map<Integer,String> fetchInvalidTestCase(List<TestCase> t) { // for closing the writer object
        HashMap<Integer,String> m = new HashMap<>();
        for(int i=0;i<t.size();i++) {
            String error = t.get(i).runValidationTest();
            if(error.length() > 0) {
                m.put(i,error);
            }
            else {
                continue;
            }
        }
        return m;
    }

    public static void triggerFunctionforPromptsPS4File() { 
        String line;
        try { 
            FileReader file = new FileReader("promptsPS4.txt");
            BufferedReader br = new BufferedReader(file);

            while ((line = br.readLine()) != null) { 
                if(line.length()==0) { continue; } 
                String[] record = line.split(":"); 
                record[0] = record[0].trim(); 
                if(record[0].equalsIgnoreCase("checkOut") || record[0].equalsIgnoreCase("checkIn")){

                }
                else if(record[0].equalsIgnoreCase("findBook")){

                }
            }
            br.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fractional Knapsack Logic
    private static double getMaxValue(TestCase testcase)
    {
        int capacity = testcase.maxWeight;
 
        double totalValue = 0d;
 
        for (int i=0; i<testcase.weightValues.size();i++) {
 
            int curWt = testcase.weightValues.get(i);
            int curVal = testcase.damagevalues.get(i);
 
            if (capacity - curWt >= 0) {
                // this weight can be picked while
                capacity = capacity - curWt;
                totalValue += curVal;
            }
            else {
                // item cant be picked whole
                double fraction = ((double)capacity / (double)curWt);totalValue += (curVal * fraction);
                capacity = (int)(capacity - (curWt * fraction));
                break;
            }
        }
 
        return totalValue;
    }
}
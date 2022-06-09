package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.*;

class TestCase {
    int weapons;
    int maxWeight;
    List<Integer> weightValues;
    List<Integer> damagevalues;
    List<Double> ratio;

    TestCase(int weapons,int maxWeight,List<Integer> weightValues,List<Integer> damagevalues) {
        this.weapons = weapons;
        this.maxWeight = maxWeight;
        this.weightValues = weightValues;
        this.damagevalues = damagevalues;
    }

    public void calculateRatio()
    {
        //iterate weightvalues and damagevalues and add to ratio
        
        for(int i=0; i<weightValues.size(); i++)
        {
            double currRatio = (damagevalues.get(i))/(weightValues.get(i));
            ratio.add(currRatio);
        }
    }

    public void display() {
        System.out.println();
        System.out.println("<----------------------------------------------------------->");
        System.out.println(weapons);
        System.out.println(maxWeight);
        System.out.println(weightValues);
        System.out.println(damagevalues);

    }
}
public class Main {
    static BufferedWriter writer;

    public static void main(String[] args) {
        List<TestCase> testCases = new ArrayList<>();
        _readBookList(testCases);  //for reading from inputPS4.txt

        testCases.forEach(testcase ->{
            testcase.calculateRatio();
        });

        List<Double> maxSize = new ArrayList<>();
        testCases.forEach(testCase -> {
            //for each iter, we obtain total value from the knapsack function and keep adding it to the size list
            double size = getMaxValue(testCase);
            maxSize.add(size);
        });
            
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
    public static void _readBookList(List<TestCase> testCases) { 
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); 
            String line = reader.readLine(); 
            while (line != null) {
                String[] item1 = line.split(":"); 
                line = reader.readLine(); 
                String[] item2 = line.split(":"); 
                
                
                String[] item;
                List<Integer> weightValues = new ArrayList<>();
                List<Integer> damageValues = new ArrayList<>();
                for(int i=1; i<= Integer.parseInt(item1[1].trim()); i++) {
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
        // sorting items by value;
        Collections.sort(testcase.ratio, Collections.reverseOrder());
 
        double totalValue = 0d;
 
        for (Integer i : testcase.weightValues) {
 
            int curWt = (int)testcase.weightValues.get(i);
            int curVal = (int)testcase.damagevalues.get(i);
 
            if (capacity - curWt >= 0) {
                // this weight can be picked while
                capacity = capacity - curWt;
                totalValue += curVal;
            }
            else {
                // item cant be picked whole
                double fraction
                    = ((double)capacity / (double)curWt);
                totalValue += (curVal * fraction);
                capacity
                    = (int)(capacity - (curWt * fraction));
                break;
            }
        }
 
        return totalValue;
    }
}
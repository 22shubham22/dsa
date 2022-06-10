package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

class TestCase { // Model of one particular test case from inputps4.txt
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
class Output { // Model of one particular output for corresponding testcase
    double damage;
    double[] ratio;
    Output(double damage,double[] ratio) {
        this.damage = damage;
        this.ratio = ratio;
    }
}
public class Main {
    static BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        Map<Integer,String> invalidTestcase;
        List<TestCase> testCases = new ArrayList<>();
        List<Output> outputs = new ArrayList<>();

        readInputFile(testCases);  //for reading from inputPS4.txt

        testCases.forEach(TestCase::calculateRatio); // calculation of ratio of damage:weight and sort in reverse order

        invalidTestcase = fetchInvalidTestCase(testCases); // look for invalid test case with Negative/Zero values

        for(int i=0;i<testCases.size();i++) { // call greedy algo only for valid test cases
            if(invalidTestcase.containsKey(i)) {
                outputs.add(new Output(-1.0,new double[0])); // invalid
            }
            else {
                getMaxValue(testCases.get(i),outputs); // valid
            }
        }

        initializeWriter();
        for(int i=0;i<outputs.size();i++) { // display outputs
            if(invalidTestcase.containsKey(i)) { // appropriate message for invalid test case
                System.out.println();
                System.out.println("Test Case Number "+i+" in the input file is invalid due to following reason(s):");
                System.out.println(invalidTestcase.get(i));
            }
            else {
                writeToFile(outputs.get(i)); // valid outputs
            }
        }
        closeWriter();
            
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

    public static void readInputFile(List<TestCase> testCases) { // read input file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); 
            String line = reader.readLine();
            if(line == null) {
                System.out.println("Empty file"); //exception of empty file handled
            }
            while (line != null) {
                while(!line.toLowerCase().contains("weapons")) { // to skip through invalid test case
                    line = reader.readLine();
                }
                String[] item1 = line.split(":"); // read weapons
                line = reader.readLine(); 
                String[] item2 = line.split(":"); // read max weight
                line = reader.readLine();
                if(!isNumeric(item1[1].trim())) { item1[1] = "0"; } //exception of invalid number handled
                if(!isNumeric(item2[1].trim())) { item2[1] = "0"; } //exception of invalid number handled
                String[] item;
                List<Integer> weightValues = new ArrayList<>();
                List<Integer> damageValues = new ArrayList<>();
                for(int i=0; i< Integer.parseInt(item1[1].trim()); i++) {
                    item = line.split("/");
                    try{
                        weightValues.add(Integer.parseInt(item[1].trim())); //exception of invalid number handled
                        damageValues.add(Integer.parseInt(item[2].trim()));
                    } catch (NumberFormatException e) {
                        weightValues.add(-1);
                        damageValues.add(-1);
                    }
                    line = reader.readLine();
                }
                testCases.add(new TestCase(Integer.parseInt(item1[1].trim()),Integer.parseInt(item2[1].trim()),
                        weightValues,damageValues)); // create new testcase and add it to list
            }
            reader.close(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public static boolean isNumeric(String str) { // check for numeric character
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private static Map<Integer,String> fetchInvalidTestCase(List<TestCase> t) { // for testing validity of test case
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

    private static void getMaxValue(TestCase testcase, List<Output> outputs) // Fractional Knapsack Logic
    {
        double[] ratio = new double[testcase.weightValues.size()];
        Arrays.fill(ratio,0.0);
        int capacity = testcase.maxWeight;
        double totalValue = 0d;
        while (capacity > 0) { // while we have capacity to take ammo
            for (int i: testcase.ratio.keySet()) { //geting index from sorted ratio map
                int curWt = testcase.weightValues.get(i);
                int curVal = testcase.damagevalues.get(i);
                if (capacity - curWt >= 0) {// this weight can be picked whole
                    capacity = capacity - curWt;
                    totalValue += curVal;
                    ratio[i] = ratio[i] + 1.0;
                }
                else {// item can't be picked whole
                    double fraction = ((int)((double)capacity/curWt * 100))/100.0;
                    totalValue += (curVal * fraction);
                    capacity = (int)(capacity - (curWt * fraction));
                    ratio[i] = ratio[i] + fraction;
                    break; // capacity over so break
                }
            }
        }
        outputs.add(new Output(totalValue,ratio)); // save output of one test case to a model to write to file later
    }

    public static void writeToFile(Output op) throws IOException { // write to output file
        writer.write("Total Damage: ");
        writer.write(String.valueOf(op.damage));
        writer.newLine();
        writer.write("Ammunition Packs Selection Ratio:");
        writer.newLine();
        for(int i=0;i<op.ratio.length;i++) {
            writer.write("A");
            writer.write(String.valueOf(i+1));
            writer.write(" > ");
            if(op.ratio[i] > (int)op.ratio[i]) // check if 1.0 is same as 1
                writer.write(String.valueOf(op.ratio[i]));
            else
                writer.write(String.valueOf((int)op.ratio[i]));
            writer.newLine();
        }
    }
}
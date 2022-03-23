package com.company;

import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class Sample {
    int obj1;
    int obj2;

    Sample(int obj1,int obj2) {
        this.obj1=obj1;
        this.obj2=obj2;
    }

    public int getObj1() {
        return obj1;
    }

    public void setObj1(int obj1) {
        this.obj1 = obj1;
    }

    public int getObj2() {
        return obj2;
    }

    public void setObj2(int obj2) {
        this.obj2 = obj2;
    }
}

public class Main {

    static List<Sample> samples = new ArrayList<Sample>();
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("lines.txt"));
//          reader=  new BufferedReader(new FileReader("C://Users//hp//OneDrive//Documents//GitHub//dsa//lines.txt"));
            String line = reader.readLine();
            while (line != null) {
//                System.out.println(line);
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                samples.add(new Sample(Integer.parseInt(items.get(0)),Integer.parseInt(items.get(1))));
                // read next line
                line = reader.readLine();
            }
            for (int i=0 ; i<samples.size() ; i++) {
                System.out.print(samples.get(i).getObj1()+" "+samples.get(i).getObj2());
                System.out.println();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for printing to output file outputPS4.txt*/
        printToOutputFile(samples);
    }
    public static void printToOutputFile(List<Sample> samples){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(
                    "outputPS4.txt"));
            int i=1;
            for(Sample sample:samples) {
                writer.write("Top Books " + i++ + ": " + sample.getObj1() + ", " + sample.getObj2()+"\n");
            }
            writer.write("\nList of books not issued:\n");
            for(Sample sample:samples){
                writer.write("" + sample.getObj1()+"\n");
            }
            writer.write("\nBook id "+samples.get(0).getObj1()+" is available for checkout.\n");
            writer.write("\nAll available copies of the below books have been checked out:\n");
            writer.write(""+samples.get(1).getObj1());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
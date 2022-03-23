package com.company;

import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class attributeDetails{
    String status;
    int bkId;

    attributeDetails(String status, int bkId)
    {
        this.status = status;
        this.bkId = bkId;

    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getBookId()
    {
        return bkId;
    }

    public void setBookId(int bkId)
    {
        this.bkId = bkId;
    }
}
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
            reader = new BufferedReader(new FileReader(
                    "C://Users//hp//OneDrive//Documents//GitHub//dsa//lines.txt"));
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
    }

    public void inOutCheckCounter() {
        ArrayList<attributeDetails> inOutDetails = new ArrayList<>();
        String line;

        try {
            FileReader file = new FileReader("C:\\Users\\I526514\\Desktop\\DSA assignment\\promptsPS4.txt");
            BufferedReader br = new BufferedReader(file);

            while((line = br.readLine())!=null){
                String[] record = line.split(":");

                String status = record[0];
                int bkId = Integer.parseInt(record[1]);

                inOutDetails.add(new attributeDetails(status,bkId));
            }

            for(int i=0; i<=inOutDetails.size(); i++)
            {
                if(inOutDetails.get(i).getStatus().equals("checkOut"))
                {
                    /*int i = Search(obj1);
                    obj2-- for corresponding obj1;*/
                }
                else if(inOutDetails.get(i).getStatus().equals("checkin"))
                {
                    /*int i = Search(obj1);
                    obj2++ for corresponding obj1;*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
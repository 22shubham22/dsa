package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class TestCase {
    int weapons;
    int maxWeight;
    List<Integer> weightValues;
    List<Integer> damagevalues;

    TestCase(int weapons,int maxWeight,List<Integer> weightValues,List<Integer> damagevalues) {
        this.weapons = weapons;
        this.maxWeight = maxWeight;
        this.weightValues = weightValues;
        this.damagevalues = damagevalues;
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
        testCases.forEach(testCase -> {
            testCase.display();
        });
//            triggerFunctionforPromptsPS4File(); //for reading from promptsPS4.txt
//            closeWriter(); //for closing the writer to avoid resource leakage
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
    public static void _readBookList(List<TestCase> testCases) { // for reading the books present in the inputPS4.txt file & building the tree
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); // providing the name of file from the same directory
            String line = reader.readLine(); // reading one line of file
            while (line != null) {
                String[] item1 = line.split(":"); // splitting the line based on the expression into a list where list[0] is BookID and list[1] is number of books
                line = reader.readLine(); // read next line
                String[] item2 = line.split(":"); // splitting the line based on the expression into a list where list[0] is BookID and list[1] is number of books
                line = reader.readLine(); // read next line
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
            reader.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace(); // handling exception reading from file
        }
    }
//    public static void _printBooks() {  // prints all the books in the library
//        List<BookNode> listOfBooks = new ArrayList<BookNode>();
//        library.traverseBook(library.rootBook, listOfBooks); // traversing tree in inorder and adding books in the list
//        try {
//            initializeWriter();
//            if(listOfBooks.isEmpty()){  // if tree is empty and no books are there in the library print the apt msg to console
//                System.out.println("There are currently no books in the Library");
//            }
//            else {
//                writer.write("There are a total of "+listOfBooks.size()+" book titles in the library.\n");
//                for (BookNode book : listOfBooks) {
//                    writer.write(""+book.bookId+", "+book.availableCount+"\n"); // writing the BookId's and availableCounter of books present in the library to OutputPS4.txt
//                }
//            }
//            writer.write("\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void triggerFunctionforPromptsPS4File() { //for reading from promptsPS4.txt
        String line;
        try {  // to make sure of error handling while reading from file
            FileReader file = new FileReader("promptsPS4.txt");
            BufferedReader br = new BufferedReader(file);

            while ((line = br.readLine()) != null) { //here we read line by line from the input file which is promptsPS4.txt
                if(line.length()==0) { continue; } // if the current line in the promptsPS4.txt file is empty move to next line
                String[] record = line.split(":"); // split the line from file based on ':'
                record[0] = record[0].trim(); // .trim() removes any unnecessary leading and trailing spaces
                if(record[0].equalsIgnoreCase("checkOut") || record[0].equalsIgnoreCase("checkIn")){

                }
                else if(record[0].equalsIgnoreCase("findBook")){

                }
            }
            br.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
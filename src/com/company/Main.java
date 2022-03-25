package com.company;

import java.awt.print.Book;
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
class BookNode {
    public int bookId;
    public int availableCount;
    public int checkoutCounter;
    BookNode leftBook;
    BookNode rightBook;

    BookNode() { // empty constructor to set integers to 0 and reference nodes to null
        this.bookId=0;
        this.availableCount=0;
        this.checkoutCounter=0;
        this.leftBook=null;
        this.rightBook=null;
    }

    BookNode(int bookId,int availableCount, int checkoutCounter) { // parametrized constructor to set integers with parameters and reference nodes to null
        this.bookId=bookId;
        this.availableCount=availableCount;
        this.checkoutCounter=checkoutCounter;
        this.rightBook=null;
        this.leftBook=null;
    }
}

class LibraryManagement {
    BookNode rootBook;
    LibraryManagement() {
        rootBook= null;
    }

    public void insertBook(BookNode book) {
        if (this.rootBook==null) { // if root node is null, the root node is assigned the book
             this.rootBook=book;
        }
        else {
            BookNode currentBook=this.rootBook;
            BookNode previousBook;
            while (true) { // will be terminated internally
                previousBook=currentBook; // to keep track of previous book(node) , in case current book(node) is null
                if(book.bookId < currentBook.bookId ) { // then go left
                    currentBook=currentBook.leftBook;
                    if (currentBook == null) { //if left book (node) is null i.e empty
                        previousBook.leftBook=book; // then assign this place to current book
                        return;
                    }
                }
                else { // else , go right
                    currentBook=currentBook.rightBook;
                    if (currentBook == null) { //if right book (node) is null i.e empty
                        previousBook.rightBook=book; // then assign this place to current book
                        return;
                    }
                }
            }
        }
    }

    public BookNode findBook(int bookId) {
        BookNode currentBook= this.rootBook; // assign root node as current node to begin traversal for search
        while (currentBook.bookId != bookId) { // till we dont find book with given Book ID
            if (bookId < currentBook.bookId) { // if less the go to left book
                currentBook=currentBook.leftBook;
            }
            else {
                currentBook=currentBook.rightBook; // if less the go to right book
            }
            if(currentBook == null) { // if we reach end but haven't found return null book
                return null;
            }
        }
        return currentBook; // return the book that we found while traversal
    }

    public void traverseBook(BookNode book) { // inorder traversal coz as per assignment while traversing to print inventory BookId should be ascending order.
        if (book != null) {
            traverseBook(book.leftBook);
            //output to file between recursions so that traversal is inorder
            traverseBook(book.rightBook);
        }
    }
}

public class Main {

//    List<Sample> samples = new ArrayList<Sample>();
    LibraryManagement library = new LibraryManagement();

    public void main(String[] args) {
        //for reading from input1.txt
        readFromFile();
        //for printing to output file outputPS4.txt
//        printToOutputFile(samples);
    }

    public void readFromFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("lines.txt"));
            String line = reader.readLine();
            while (line != null) {
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                this.library.insertBook(new BookNode(Integer.parseInt(items.get(0)),Integer.parseInt(items.get(1)),0)); // insert the line (new book) to the library (tree)
                line = reader.readLine(); // read next line
            }
            reader.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // print ot output to be resolved by shubham mishra as per the new tree
//    public void printToOutputFile(List<Sample> samples){
//
//        BufferedWriter writer;
//        try {
//            writer = new BufferedWriter(new FileWriter("outputPS4.txt"));
//            int i=1;
//            for(Sample sample:samples) {
//                writer.write("Top Books " + i++ + ": " + sample.getObj1() + ", " + sample.getObj2()+"\n");
//            }
//            writer.write("\nList of books not issued:\n");
//            for(Sample sample:samples){
//                writer.write("" + sample.getObj1()+"\n");
//            }
//            writer.write("\nBook id "+samples.get(0).getObj1()+" is available for checkout.\n");
//            writer.write("\nAll available copies of the below books have been checked out:\n");
//            writer.write(""+samples.get(1).getObj1());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void inOutCheckCounter() {
        ArrayList<attributeDetails> inOutDetails = new ArrayList<>();
        String line;
        try {
            FileReader file = new FileReader("promptsPS4.txt");
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
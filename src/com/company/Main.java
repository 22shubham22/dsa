package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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

    public void print() {
        System.out.print(this.bookId+","+this.availableCount+","+this.checkoutCounter+"\n");
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
            book.print();
            //output to file between recursions so that traversal is inorder-> Note: this is done in traverseAndPrintToOutputFile()
            traverseBook(book.rightBook);
        }
    }

    public void traverseAndPrintToOutputFile(BookNode book,BufferedWriter writer) throws IOException {
        if (book != null) {
            traverseAndPrintToOutputFile(book.leftBook,writer);
            //output to file between recursions so that traversal is inorder
            writer.write("Book Id: "+book.bookId+", AvailableCounter: "+book.availableCount+", CheckOutCounter: "+book.checkoutCounter+"\n");
            traverseAndPrintToOutputFile(book.rightBook,writer);
        }
    }
}

public class Main {

//    List<Sample> samples = new ArrayList<Sample>();
    static LibraryManagement library = new LibraryManagement();

    public static void main(String[] args) {
        //for reading from input1.txt
        readFromFile();
        library.traverseBook(library.rootBook);
        //for reading from prompt.txt
        inOutCheckCounter();
        library.traverseBook(library.rootBook);
        //for printing to output file outputPS4.txt
        printToOutputFile(library.rootBook); // should be corrected by mishra for the new tree system
    }

    public static void readFromFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("lines.txt"));
            String line = reader.readLine();
            while (line != null) {
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                library.insertBook(new BookNode(Integer.parseInt(items.get(0).trim()),Integer.parseInt(items.get(1).trim()),0)); // insert the line (new book) to the library (tree)
                line = reader.readLine(); // read next line
            }
            reader.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // print ot output to outputPS4.txt
    public static void printToOutputFile(BookNode book){ // to be resolved by mishra
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("outputPS4.txt"));
            library.traverseAndPrintToOutputFile(book,writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * In this method, we are trying to read input from the PromptsPS4.txt
     * and we are checking the status whether it is checkIn or checkOut
     * Based on that, we update the available counter and the checkout counter
     *
     **/
    public static void inOutCheckCounter() {
        String line;
        BookNode book;
        try {
            FileReader file = new FileReader("promptsPS4.txt");
            BufferedReader br = new BufferedReader(file);

            //here we read line by line from the input file which is promptsPS4.txt
            while( (line = br.readLine())  !=  null) {
                String[] record = line.split(":");

                //here we simply fetch the first string which is the status of book
                String status = record[0];

                if(status.equalsIgnoreCase("checkOut"))
                {
                    /*if book status is checkout then we reduce the available count by 1 and increase the
                     checkout counter by 1 */

                    book = library.findBook(Integer.parseInt(record[1].trim()));
                        if (book != null && book.availableCount > 0) {
                            book.availableCount--;
                            book.checkoutCounter++;
                        }
                        else
                        {
                            System.out.println("Invalid Book ID found, No such book in Library");
                        }
                }
                else if(status.equalsIgnoreCase("checkIn"))
                {
                    /*here when the book is checked in then we simply increase the available counter by 1
                     but we do not modify the checkout counter here */

                    book = library.findBook(Integer.parseInt((record[1].trim())));
                        if (book != null) {
                            book.availableCount++;
                        }
                        else
                        {
                            System.out.println("Invalid Book ID found, No such book in Library");
                        }
                }
                else
                {
                    System.out.println("Invalid status in the PromptsPS4.txt file: It is neither checkIn or checkOut");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
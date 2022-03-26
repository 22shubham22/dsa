package com.company; // to be commented when given to ma'am


import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getAvailableCount() { return this.availableCount; }

    public int getCheckoutCounter() { return this.checkoutCounter; }

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

    public void traverseBook(BookNode book, List<BookNode> listOfBooks) { // inorder traversal coz as per assignment while traversing to print inventory BookId should be ascending order.
        if (book != null) {
            traverseBook(book.leftBook, listOfBooks);
            listOfBooks.add(book);
            traverseBook(book.rightBook,listOfBooks);
        }
    }
}

public class Main {

    static LibraryManagement library = new LibraryManagement();
    static List<String> prompts = new ArrayList<>();

    public static void main(String[] args) {

        _readBookList();  //for reading from input1.txt
        _chkInChkOut();  //for reading from prompt.txt
        triggerFunctionforPrompts(prompts);
//        printBooks(sortedUsers); //print tree nodes content sorted in ASC order of Book Id to outputPS4.txt
    }

    //Operation 1
    public static void _readBookList() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); // providing the name of file from the same directory
            String line = reader.readLine(); // reading one line of file
            while (line != null) {
                List<String> items = Arrays.asList(line.split("\\s*,\\s*")); // splitting the line based on the expression into a list where list[0] is BookID and list[1] is number of books
                library.insertBook(new BookNode(Integer.parseInt(items.get(0).trim()),Integer.parseInt(items.get(1).trim()),0)); // insert the line (new book) to the library (tree)
                line = reader.readLine(); // read next line
            }
            reader.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace(); // handling exception reading from file
        }
    }

    /**
     * In this method, we are trying to read input from the PromptsPS4.txt
     * and we are checking the status whether it is checkIn or checkOut
     * Based on that, we update the available counter and the checkout counter
     *
     **/
    //Operation 2
    public static void _chkInChkOut() {
        String line;
        BookNode book;
        try {  // to make sure of error handling while reading from file
            FileReader file = new FileReader("promptsPS4.txt");
            BufferedReader br = new BufferedReader(file);

            while( (line = br.readLine())  !=  null) { //here we read line by line from the input file which is promptsPS4.txt
                String[] record = line.split(":"); // split the line from file based on ':'
                if(record.length == 1) {
                    prompts.add(record[0].trim());
                    continue;
                }
                if(record[0].equalsIgnoreCase("checkOut")) { //if book status is 'checkout' then we reduce the available count by 1 and increase the checkout counter by 1
                    book = library.findBook(Integer.parseInt(record[1].trim()));
                        if (book != null && book.availableCount > 0) { // checking corner cases such as book is not in library or the book is out of stock
                            book.availableCount--;
                            book.checkoutCounter++;
                        }
                        else { //handling error case when we try to check out a book that is out of stock
                            System.out.println("Invalid Book ID found, No such book in Library");
                        }
                }
                else if(record[0].equalsIgnoreCase("checkIn")) {  //here when the book is checked in then we simply increase the available counter by 1 , checkout counter remains same
                    book = library.findBook(Integer.parseInt((record[1].trim()))); // taking care of any strings that might have space in it before parsing to integer
                        if (book != null) { // taking care if the book is not present in library
                            book.availableCount++;
                        }
                        else { // suitable message to denote that the book we're trying to cehck in was never present in library
                            System.out.println("Invalid Book ID found, No such book in Library");
                        }
                }
                else {  // finaly taking care of any invalid entried in promptsPS4.txt file
                    System.out.println("Invalid status in the PromptsPS4.txt file: It is neither checkIn or checkOut");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // error handling from reading from file
        }
    }

    //Operation 3
    public static void _getTopBooks(String prompt) {
        List<BookNode> listOfBooks= new ArrayList<BookNode> ();
        library.traverseBook(library.rootBook, listOfBooks);
        List<BookNode> sortedBooks = listOfBooks.stream().sorted(Comparator.comparing(BookNode::getCheckoutCounter).reversed()).limit(3).collect(Collectors.toList());
        writeToOutputFile(sortedBooks, prompt);
    }

    //Operation 4
    public static void _notIssued(String prompt) {
        List<BookNode> listOfBooks= new ArrayList<BookNode> ();
        library.traverseBook(library.rootBook, listOfBooks);
        List<BookNode> sortedUsers = listOfBooks.stream().filter((book)-> book.checkoutCounter == 0).collect(Collectors.toList());
        writeToOutputFile(sortedUsers, prompt);
    }

    //Operation 6
    public static void _stockOut(String prompt) {
        List<BookNode> listOfBooks= new ArrayList<BookNode> ();
        library.traverseBook(library.rootBook, listOfBooks);
        List<BookNode> sortedUsers = listOfBooks.stream().filter((book)-> book.availableCount == 0).collect(Collectors.toList());
        writeToOutputFile(sortedUsers, prompt);
    }

    //Operation 7
    public static void _printBooks(String prompt) {
        List<BookNode> listOfBooks= new ArrayList<BookNode> ();
        library.traverseBook(library.rootBook, listOfBooks);
        List<BookNode> sortedUsers = listOfBooks.stream().filter((book)-> book.availableCount == 0).collect(Collectors.toList());
        writeToOutputFile(sortedUsers,prompt);
    }


    public static void writeToOutputFile(List<BookNode> list, String prompt){ // printing list content to outputPS4.txt
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("outputPS4.txt"));
            for(BookNode book:list) {  // printing data of each book
                writer.write("Book Id: "+book.bookId+", AvailableCounter: "+book.availableCount+", CheckoutCounter: "+book.checkoutCounter+"\n");
            }
            writer.write("\n");
            writer.flush();
            writer.close();
        } catch (IOException e){ // error handling while writing to file
            e.printStackTrace();
        }
    }

    public static void triggerFunctionforPrompts(List<String> prompts) {
        for(String prompt:prompts) {
            if (prompt.equalsIgnoreCase("ListTopBooks")) {
                _getTopBooks(prompt);
            }
            else if(prompt.equalsIgnoreCase("BooksNotIssued")) {
                _notIssued(prompt);
            }
            else if(prompt.equalsIgnoreCase("ListStockOut")) {
                _stockOut(prompt);
            }
            else if(prompt.equalsIgnoreCase("printInventory")) {
                _printBooks(prompt);
            }
            else {
                System.out.println("Invalid Prompt Encountered in promptsPS4.txt");
            }
        }
    }
}

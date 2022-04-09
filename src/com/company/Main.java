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
    public int  getBookID() { return this.bookId; }

    public int getAvailableCount() { return this.availableCount; }

    public int getCheckoutCounter() { return this.checkoutCounter; }
}

class LibraryManagement {
    BookNode rootBook;
    LibraryManagement() {
        rootBook= null;
    }

    public void insertBook(BookNode currentBook,BookNode bookToInsert)
    {
        if (currentBook == null) {  // if root node is null, the root node is assigned the bookToInsert
            this.rootBook = bookToInsert;
            return;
        }
        if (bookToInsert.bookId < currentBook.bookId) { // if the given bookToInsert.bookId is less than the rootBook.bookId, recur for the left subtree
            if(currentBook.leftBook==null){
                currentBook.leftBook=bookToInsert;
            }
            else {
                insertBook(currentBook.leftBook, bookToInsert);
            }
        }
        else { // otherwise, recur for the right subtree i.e. when bookToInsert.bookId >= rootBook.bookId
            if(currentBook.rightBook==null){
                currentBook.rightBook=bookToInsert;
            }
            else {
                insertBook(currentBook.rightBook, bookToInsert);
            }
        }
    }

    public BookNode findBook(BookNode currentBook, int bookId) { // recursive approach coz its given in the function to do it recursively
        if(currentBook == null){ // if we reach end but haven't found return null book
            return null;
        }
        if(currentBook.bookId==bookId) {
            return currentBook; // return the book that we found while traversal
        }
        BookNode book;
        if (bookId < currentBook.bookId) {
            book=findBook(currentBook.leftBook, bookId);// if less then go to left book
        }
        else {
            book=findBook(currentBook.rightBook, bookId); // if more then go to right book
        }
        return book;
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
    static BufferedWriter writer;

    public static void main(String[] args) {
            _readBookList();  //for reading from inputPS4.txt
            triggerFunctionforPromptsPS4File(); //for reading from promptsPS4.txt
            closeWriter(); //for closing the writer to avoid resource leakage
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
    public static void _readBookList() { // for reading the books present in the inputPS4.txt file & building the tree
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("inputPS4.txt")); // providing the name of file from the same directory
            String line = reader.readLine(); // reading one line of file
            while (line != null) {
                List<String> items = Arrays.asList(line.split("\\s*,\\s*")); // splitting the line based on the expression into a list where list[0] is BookID and list[1] is number of books
                library.insertBook(library.rootBook,new BookNode(Integer.parseInt(items.get(0).trim()), Integer.parseInt(items.get(1).trim()),0)); // insert the line (new book) to the library (tree)
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
     **/
    //Operation 2
    public static void _chkInChkOut(int bkID, String inOut) {  // used for doing checkIn/checkOut
        BookNode book;
                if (inOut.equalsIgnoreCase("checkOut")) { //if book status is 'checkout' then we reduce the available
                    // count by 1 and increase the checkout counter by 1
                    book = library.findBook(library.rootBook, bkID);
                    if (book != null && book.availableCount > 0) { // checking corner cases such as book is not in library or the book is out of stock
                        book.availableCount--;
                        book.checkoutCounter++;
                    } else { //handling error case when we try to check out a book that is out of stock
                        System.out.println("Invalid Book ID found, No such book in Library");
                    }
                } else if (inOut.equalsIgnoreCase("checkIn")) {  //here when the book is checked in then we simply
                    // increase the available counter by 1 , checkout counter remains same
                    book = library.findBook(library.rootBook, bkID);
                    if (book != null) { // taking care if the book is not present in library
                        book.availableCount++;
                    } else { // suitable message to denote that the book we're trying to cehck in was never present in library
                        System.out.println("Invalid Book ID found, No such book in Library");
                    }
                } else { // i don't think this else is required anymore coz we already checked but still
                    // finaly taking care of any invalid entried in promptsPS4.txt file
                    System.out.println("Invalid status in the PromptsPS4.txt file: It is neither checkIn or checkOut");
                }
    }

    //Operation 3
    public static void _getTopBooks() {  // gets the top 3 books(on the basis of checkout counter) from the library
        List<BookNode> listOfBooks = new ArrayList<BookNode>();
        library.traverseBook(library.rootBook, listOfBooks); // traversing tree in inorder and adding books in the list
        // list
        List<BookNode> sortedBooks =
                // sorting books first acc to their checkout counter and if it's same then on the basis of their bookId
                listOfBooks.stream().sorted(Comparator.comparing(BookNode::getCheckoutCounter).thenComparing(BookNode::getBookID,(s1,s2) -> {
                    if(s1.compareTo(s2)>0) {
                        return s1;
                    }
                    return s2 ;
                }).reversed()).limit(3).collect(Collectors.toList()); // adding the top 3 books only after sorting to sortedBooks
        try {
            initializeWriter(); // initializing the writer object if not initialized otherwise using the same object
            int i=1;
            for(BookNode book : sortedBooks) {  // printing bookId and checkoutCounter of top 3 books
                if (book.checkoutCounter > 0) {
                    writer.write("Top Books " + (i++) + ": " + book.bookId + ", " + book.checkoutCounter + "\n");
                }
            }
            if(i!=4){ // when any of the books in sortedBooks(the top 3 books) have checkoutCounter=0 then we don't print them to OutputPS4.txt
                System.out.println("Checkout counter of "+(4-i)+ " Top Books was 0 hence not printing them to OutputPS4.txt");
            }
            writer.write("\n");  // taking control to the next line
        } catch (IOException e) { // error handling while writing to file
            e.printStackTrace();
        }
    }

    //Operation 4
    public static void _notIssued() { // displays the books not issued
        List<BookNode> listOfBooks = new ArrayList<BookNode>();
        library.traverseBook(library.rootBook, listOfBooks); // traversing tree in inorder and adding books in the list

        // adding those books to sortedUsers list whose checkoutCounter is 0
        List<BookNode> sortedUsers = listOfBooks.stream().filter((book) -> book.checkoutCounter == 0).collect(Collectors.toList());
        try {
            initializeWriter(); // initializing the writer object if not initialized otherwise using the same object
            if(sortedUsers.isEmpty()){ // if no book has checkoutCounter 0 then proper msg is displayed on the console
                System.out.println("All books are issued");
            }
            else {
                writer.write("List of books not issued:\n");
                for (BookNode book : sortedUsers) {
                    writer.write(""+book.bookId+"\n");  // writing bookId's of books having checkout Counter 0 to OutputPS4.txt
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //operation 5
    public static void _findBook(int bkID) { // searches the library for a particular book using the bookId
        BookNode book = library.findBook(library.rootBook,bkID); // finding the book in the tree
        try {
            initializeWriter();
            if (book != null) {
                if (book.availableCount > 0) {   // book is in stock
                    writer.write("Book id " + bkID + " is available for checkout\n");
                }
                else{                           // book is out if stock
                    writer.write("All copies of book id "+bkID+" have been checked out\n");
                }
            }
            else{                               // book does not exist in the library
                writer.write("Book id "+bkID+" does not exist\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Operation 6
    public static void _stockOut() { // prints the books that are out of stock
        List<BookNode> listOfBooks = new ArrayList<BookNode>();
        library.traverseBook(library.rootBook, listOfBooks); // traversing tree in inorder and adding books in the list

        // adding those books to sortedUsers list whose availableCounter is 0
        List<BookNode> sortedUsers = listOfBooks.stream().filter((book) -> book.availableCount == 0).collect(Collectors.toList());
        try {
            initializeWriter();
            if(sortedUsers.isEmpty()){ // if none of the book has availableCounter 0 print apt msg to console
                System.out.println("All books are in stock");
            }
            else {
                writer.write("All available copies of the below books have been checked out:\n");
                for (BookNode book : sortedUsers) {
                    writer.write(""+book.bookId+"\n"); // writing bookId's of books having availableCounter 0 to OutputPS4.txt
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Operation 7
    public static void _printBooks() {  // prints all the books in the library
        List<BookNode> listOfBooks = new ArrayList<BookNode>();
        library.traverseBook(library.rootBook, listOfBooks); // traversing tree in inorder and adding books in the list
        try {
            initializeWriter();
            if(listOfBooks.isEmpty()){  // if tree is empty and no books are there in the library print the apt msg to console
                System.out.println("There are currently no books in the Library");
            }
            else {
                writer.write("There are a total of "+listOfBooks.size()+" book titles in the library.\n");
                for (BookNode book : listOfBooks) {
                    writer.write(""+book.bookId+", "+book.availableCount+"\n"); // writing the BookId's and availableCounter of books present in the library to OutputPS4.txt
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                    if(record.length!=2) { // invalid checkin/checkout prompt format gives appt msg on console
                        System.out.println("Invalid "+record[0]+" format: The correct format is "+record[0]+": <BookId>");
                    }
                    else {  // if the prompt is of correct format the _chkInChkOut(bookId,inOut) method is called
                        _chkInChkOut(Integer.parseInt(record[1].trim()), record[0]); // .trim() takes care of any strings that might have space in it before parsing to integer
                    }
                }
                else if(record[0].equalsIgnoreCase("findBook")){
                    if(record.length!=2) { // invalid findBook prompt format gives appt msg on console
                        System.out.println("Invalid "+record[0]+" format: The correct format is "+record[0]+": <BookId>");
                    }
                    else { // if the prompt is of correct format the _findBook(bookId) method is called
                        _findBook(Integer.parseInt(record[1].trim()));
                    }
                }
                else if (record[0].equalsIgnoreCase("ListTopBooks")) {
                    _getTopBooks(); // gets the top 3 books in the library
                } else if (record[0].equalsIgnoreCase("BooksNotIssued")) {
                    _notIssued(); // gets the books that haven't been issued from the library
                } else if (record[0].equalsIgnoreCase("ListStockOut")) {
                    _stockOut(); // gets the books that are out of stock
                } else if (record[0].equalsIgnoreCase("printInventory")) {
                    _printBooks(); // displays all the books in the library
                } else {  // in case the prompt doesn't match the prompts given above
                    System.out.println("Invalid Prompt Encountered in promptsPS4.txt");
                }
            }
            br.close(); // close the reader
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
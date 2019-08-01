/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemsguildpc;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author lmski_000
 */
public class DatabaseQueries {
    
    private static int _numLines;
    private static ArrayList<String[]> _fileData;
    private static String _zip;
           
    protected static void connectToDatabase(){
        String host = "jdbc:sqlserver://mysqlserver0001.database.windows.net\\Server0001:1433;databaseName=myDatabase";
        String user = "azureuser";
        String pass = ";49Ductile";
                      
        try(Connection connection = DriverManager.getConnection(host, user, pass);) {
                                 
        } catch (SQLException ex) {
            Logger.getLogger(SystemsGuildPC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           
    private static void readFile(String fileName){              
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                                 
            int numLines = 0;      //number of lines in file
            String line;                 //current line in file
            ArrayList<String[]> fileData = new ArrayList<>(0);     //data from file
                                 
          //read in file, split into string arrays, and save in ArrayList lineData for future reference
            while((line = in.readLine()) != null){
                //System.out.println(line); //test
                String[] lineData = line.split(",");
                fileData.add(lineData);
                numLines++;
            }
            _fileData = fileData;   //set _fileData to data from most recently read file 
            _numLines = numLines;   //set _numLines to # of lines from most recently read file
            
            //System.out.println(fileData); //test
            
        }catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }
          
    
    
    //query database for transactions from zip code and time range defined by user
    protected static void getTransactionAverage(String zip, String startDate, String endDate){
      //read from SalesOrderHeader 'table' in database
        readFile("SalesOrderHeader.csv");
        _zip = zip;
      
        ArrayList<String> salesOrderIDArr = new ArrayList<>(0);
        ArrayList<String> customerIDArr = new ArrayList<>(0);
        
      //loop through data to find entries corresponding to proper date
      //start from index 1 (index 0 is column labels)
        for(int i=1; i<=_numLines-1; i++){
            String[] entry = _fileData.get(i); //retrieve array at index 1
            String dateSold = entry[2];        //retrieve date sold for entry
            
            
          //if orderDate (index 2) equals startDate, save salesorderID and customerID into arrays
          //fix to check range later
            if(dateSold.contains(startDate)){
                salesOrderIDArr.add(entry[0]);
                customerIDArr.add(entry[10]);
            }else{
                System.out.println("Not equal");
            }
        }
        
        //System.out.println(salesOrderIDArr); //test
        //System.out.println(customerIDArr); //test
        
      //call getAddressID to find proper entries in Address 'table'  
        getAddressID(customerIDArr);
        
      //replace all non necessary entries with "0" for later removal 
        for(int m=0; m<=customerIDArr.size()-1; m++){
            if(customerIDArr.get(m).equals("0")){
                salesOrderIDArr.set(m, "0");
            }
        }
        
      //remove all elements equal to "0" from arrays  
        customerIDArr.removeIf(n -> (n.equals("0")));
        salesOrderIDArr.removeIf(n -> (n.equals("0")));
        
        //System.out.println(customerIDArr); //test
        //System.out.println(salesOrderIDArr); //test
        
      //call getLineTotal method to retrieve total spent for each transaction
        double totalSpent = getTotal(salesOrderIDArr);
        double averageSpent = totalSpent / salesOrderIDArr.size();
        
        System.out.print("Average spent per transaction: ");
        System.out.format("%.2f", averageSpent);
        System.out.println("");
        
    }
    
    
    private static void getAddressID(ArrayList<String> customerIDArr){
        readFile("CustomerAddress.csv");
        
        ArrayList<String> addressIDArr = new ArrayList<>(0);
        
      //loop customerID array
        for(int i=0; i<customerIDArr.size(); i++){
                String customerID = customerIDArr.get(i);  //retrive customerID from list
                //System.out.println(customerID); //test
          
          //loop through CustomerAddress file to find matching entries
            for(int j=1; j<=_numLines-1; j++){
                String[] entry = _fileData.get(j); //retrieve entry
                String ID = entry[0];      //retrieve address ID for entry
                
                //if ID matches entry in customerID arraylist, save addressID into arraylist
                if(ID.equals(customerID)){
                    addressIDArr.add(entry[1]);
                }else{
                   //System.out.println("Not equal");
                }
            }
        }
        //System.out.println(addressIDArr); //test
      
      //call getZipCode method to find relavent entries
        getZipCode(addressIDArr);
        
      //replace all non necessary entries with "0" for later removal 
        for(int m=0; m<=addressIDArr.size()-1; m++){
            if(addressIDArr.get(m).equals("0")){
                customerIDArr.set(m, "0");
            }
        }
        //System.out.println(customerIDArr); //test
    }
         
    
    private static void getZipCode(ArrayList<String> addressIDArr){
        readFile("Address.csv");
        
        ArrayList<String> validEntries = new ArrayList<>(0); //a list of entries with proper zip code
        
      //loop customerID array
        for(int i=0; i<addressIDArr.size(); i++){
                String addressID = addressIDArr.get(i);  //retrive customerID from list
                //System.out.println(addressID); //test
          
          //loop through Address file to find matching entries
            for(int j=1; j<=_numLines-1; j++){
                String[] entry = _fileData.get(j); //retrieve entry
                String ID = entry[0];      //retrieve address ID for entry
                
                //if ID matches entry in addressID arraylist and zip code matches
                //save addressID into list of valid entries
                if(ID.equals(addressID)){
                  
                    if(entry[6].equals(_zip)){
                        validEntries.add(ID);
                  //if no match, set value in address to "0" for later removal      
                    }else{
                        addressIDArr.set(i, "0");
                    }
                }else{
                    //System.out.println("Not equal.");
                }
            }
        }
        
        //System.out.println(validEntries);
        //System.out.println(addressIDArr);
    }
    
    private static double getTotal(ArrayList<String> SalesOrderIDArr){
        readFile("SalesOrderDetail.csv");
        
        double totalSpent = 0;
        
      //loop SalesOrderID array
        for(int i=0; i<SalesOrderIDArr.size(); i++){
                String salesID = SalesOrderIDArr.get(i);  //retrive salesOrderID from list
                //System.out.println(salesID); //test
          
          //loop through SalesOrderDetail file to find matching entries
            for(int j=1; j<=_numLines-1; j++){
                String[] entry = _fileData.get(j); //retrieve entry
                String ID = entry[0];      //retrieve sales order ID for entry
                
                //if ID matches entry in customerID arraylist, save addressID into arraylist
                if(ID.equals(salesID)){
                    totalSpent += Double.parseDouble(entry[6]);
                }else{
                   //System.out.println("Not equal");
                }
            }
        }
        
        //System.out.println(totalSpent); //test
        
        return totalSpent;
    }
    
                      
                      
}

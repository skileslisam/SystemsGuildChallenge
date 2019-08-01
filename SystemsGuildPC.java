/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemsguildpc;


/**
 *
 * @author Lisa Skiles
 * Programming challenge for Systems Guild Inc.
 */
public class SystemsGuildPC {


           
           /**
            * @param args the command line arguments
            */
           public static void main(String[] args) {
                //Conect to database
                //Returns error: java.sql.SQLException: No suitable driver found
                //IDE can connect to database, but individual program cannot despite having proper drivers installed in library folder
                //DatabaseQueries.connectToDatabase(); 
                                  
                String zip = "93030";
                String startDate = "06-01";
                String endDate = "";
                                  
                DatabaseQueries.getTransactionAverage(zip, startDate, endDate);
                        
           }
           
}

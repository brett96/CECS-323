/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc.project;
import java.util.*;
import java.sql.*;
/**
 *
 * @author Brett
 */
public class JDBCProject 
{
    
    static String USER;
    static String PASS;
    static String DBNAME;
    public static Scanner input = new Scanner(System.in);
    
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/JDBC_Project;user=jdbc;password=jdbc";  //TODO

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        Connection connection = null;
        Statement stmt = null;
        
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection(DB_URL);
            
            stmt = connection.createStatement();
            
            boolean choice = false;
            String action = "";
            int actionNum = -1;
            while(!choice)
            {
                try{
                System.out.println("Welcome!\nWhat would you like to do? (Enter the number only)"
                        + "\n1.  List all the writing groups"
                        + "\n2.  List all the data for a group specified by the user"
                        + "\n3.  List all publishers"
                        + "\n4.  List all the data for a pubisher specified by the user."
                        + "\n5.  List all book titles"
                        + "\n6.  List all the data for a book specified by the user. "
                        + "\n7.  Insert a new book"
                        + "\n8.  Insert a new publisher and update all book published by one publisher to be published by the new pubisher."
                        + "\n9.  Remove a book specified by the user"
                        + "\n0.  Exit");
                
                    action = input.next();
                    actionNum = Integer.parseInt(action);
//                    if(action.contains("[[a-zA-Z]+"))
//                        System.out.println("Invalid entry");
                    if(actionNum >=0 && actionNum < 10)
                        choice = true;
                    else
                        System.out.println("Invalid Choice");
                    
                }
                catch(InputMismatchException i)
                {
                    System.out.println("Invalid input");
                }    
                catch(Exception e)
                {
                    System.out.println("Please only enter the number.  Do not include any letters or punctuation\n");
                }
            
            
                switch(actionNum)
                {
                    case 1:
                        getAllGroups(stmt);
                        choice = false;
                        break;
                    case 2:
                        getGroupData(stmt);
                        choice = false;
                        break;
                    case 3:
                        getAllPublishers(stmt);
                        choice = false;
                        break;
                    case 4:
                        getPublishers(stmt);
                        choice = false;
                        break;
                    case 5:
                        getAllTitles(stmt);
                        choice = false;
                        break;
                    case 6:
                        getBookData(stmt);
                        choice = false;
                        break;
                    case 7:
                        addBook(stmt);
                        choice = false;
                        break;
                    case 8:
                        updatePublishers(stmt);
                        choice = false;
                        break;
                    case 9:
                        remove(stmt);
                        choice = false;
                        break;
                    case 0:
                        choice = true;
                }
            }
        }
        catch (SQLException s)
        {
            System.out.println("An error occurred");
        }
        catch (Exception e)
        {
            System.out.println("An error occurred");;
        }
        
        finally
        {
            try
            {
                if(stmt != null)
                    stmt.close();
                if(connection != null)
                    connection.close();
            }
            catch(SQLException s)
            {
                System.out.println("An error occurred");
            }
        }
    }
    
    public static String isNull(String input)
    {
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static boolean isListed(ArrayList list, String s)
    { 
        for(int x = 0; x<list.size(); x++)
        {
            if(list.get(x).equals(s))
                return true;
        }
        return false;
    }
    
    //Option 1
    public static void getAllGroups(Statement stmt) throws SQLException
    {
        String sql;
        sql = "SELECT DISTINCT GroupName FROM WRITINGGROUP";
        ResultSet rs = stmt.executeQuery(sql);
        String groupName;
        int i = 1;
        while(rs.next())
        {
            groupName = rs.getString("GroupName");
            System.out.println(i + ".  " + groupName);
            i++;
        }
        rs.close();
        
        
//        String sql;
//        //the sql command 
//            //sql = "SELECT DISTINCT GroupName FROM ";
//            ResultSet rs = stmt.executeQuery("SELECT DISTINCT GroupName FROM WritingGroup");
//            int number = 0;//to list with numbers
//            
//            while (rs.next()) {
//                //Retrieve by column name
//                String groupname = rs.getString("GroupName");
//                number++;
//                //Display values
//               System.out.println(number+")"+isNull(groupname));                 
//            }
//            rs.close();
    }
    
    //Option 2
    public static void getGroupData(Statement stmt) throws SQLException
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get GroupName
        groups.add("GroupName");
        data += "GroupName: ";

        
        //Get Head Writer
            name = "HeadWriter";
            groups.add(name);
            data += " HeadWriter: ";

        
        //Get Year Formed
            name = "YearFormed";
            groups.add(name);
            data += " YearFormed: ";
            
        //Get subject
            name = "Subject";

        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        boolean done = false;
        while(!done)
        {
            try
            {
                //System.out.println(selections);
                System.out.println("What group would you like data on ?");
                getAllGroups(stmt);
                input.nextLine();
                String groupName = input.nextLine();
                String sql  = "SELECT DISTINCT * FROM WritingGroup, Publisher, Book WHERE WritingGroup.GroupName = '" + groupName +"'", attribute;
                //System.out.println(sql);
                //System.out.println("\nSQL = " + sql + "\n");
                ResultSet rs = stmt.executeQuery(sql);
                //System.out.println(data);
                while (rs.next()) 
                {
                  for(int i=0;i<groups.size() - 1 ;i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                      System.out.print(attribute+"  ,");  
                  }
                  attribute = rs.getString((String) groups.get(groups.size() - 1));
                  System.out.print(" " + attribute);  
                  System.out.println();
                }
                done = true;
                rs.close();
           }
            catch(Exception e)
           {
               System.out.println("An Error Occurred");
           }
        }
    }
    
    // OLD METHOD DON'T USE
    public static void getGroupData1(Statement stmt) throws SQLException
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get GroupName
        System.out.println("Would you like to select Group Name? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            if(!isListed(groups, "GroupName"))
            {
                groups.add("GroupName");
                data += "GroupName: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get Head Writer
        System.out.println("Would you like to select Head Writer? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "HeadWriter";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " HeadWriter: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get Year Formed
        System.out.println("Would you like to select Year Formed? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "YearFormed";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " YearFormed: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get subject
        System.out.println("Would you like to select Subject? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Subject";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Subject: ";
            }
            else System.out.println("Already selected");
        }
        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        
        try
        {
            System.out.println(selections);
            String sql  = "SELECT" + selections + " FROM WritingGroup", attribute;
            //System.out.println("\nSQL = " + sql + "\n");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(data);
            while (rs.next()) {
                  for(int i=0;i<groups.size();i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                      System.out.print(attribute+"  ,");  
                  }
                  System.out.println();  
            }
            rs.close();
       }catch(Exception e)
       {
           System.out.println("An error occurred");
       }
    }
    
    //Option 3
    public static void getAllPublishers(Statement stmt) throws SQLException
    {
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT PublisherName FROM PUBLISHER");
        String pubName;
        int i = 1;
        while(rs.next())
        {
            pubName = rs.getString("PublisherName");
            System.out.println(i + ".  " + pubName);
            i++;
        }
        rs.close();
    }
    
    // Option 4
    public static void getPublishers(Statement stmt)
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get Publisher Name
        name = "Publishername";
        groups.add(name);
        data += "Publisher Name: ";
        
        //Get address
        name = "Publisheraddress";
        groups.add(name);
        data += " Publisher Address: ";
        
        //Get phone
        name = "Publisherphone";
        groups.add(name);
        data += " Publisher Phone: ";

        //Get email
        name = "Publisheremail";
        groups.add(name);
        data += " Publisher Mail: ";
        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        
        boolean done = false;
        while(!done)
        {
            try
            {
                System.out.println("What Publisher Would You Like Data On?");
                getAllPublishers(stmt);
                input.nextLine();
                String publisher = input.nextLine();
                String sql  = "SELECT * FROM Publisher, Book, WritingGroup WHERE Publisher.PublisherName = '" + publisher + "'", attribute;
                //System.out.println("\nSQL = " + sql + "\n");
                ResultSet rs = stmt.executeQuery(sql);
                System.out.println(data);
                while (rs.next()) 
                {
                      for(int i=0;i<groups.size() - 1 ;i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                      System.out.print(attribute+"  ,");  
                  }
                  attribute = rs.getString((String) groups.get(groups.size() - 1));
                  System.out.print(" " + attribute);  
                  System.out.println();
                }
                done = true;
                rs.close();
           }catch(Exception e)
           {
               System.out.println("An Error Occurred");
           }
        }
    }
    
    //OLD METHOD DON'T USE
    public static void getPublishers1(Statement stmt)
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get Publisher Name
        System.out.println("Would you like to select Publisher Name? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Publishername";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += "Publisher Name: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get address
        System.out.println("Would you like to select Publisher Address? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Publisheraddress";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Publisher Address: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get phone
        System.out.println("Would you like to select Publisher Phone? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Publisherphone";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Publisher Phone: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get email
        System.out.println("Would you like to select Publisher Email? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Publisheremail";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Publisher Mail: ";
            }
            else System.out.println("Already selected");
        }
        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        
        try
        {
            String sql  = "SELECT" + selections + " FROM Publisher", attribute;
            System.out.println("\nSQL = " + sql + "\n");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(data);
            while (rs.next()) 
            {
                  for(int i=0;i<groups.size() - 1 ;i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                      System.out.print(attribute+"  ,");  
                  }
                  attribute = rs.getString((String) groups.get(groups.size() - 1));
                  System.out.print(" " + attribute);  
                  System.out.println();
            }
            rs.close();
       }catch(Exception e)
       {
           System.out.println("An error occurred");
       }
    }
    
    //Option 5
    public static void getAllTitles(Statement stmt) throws SQLException
    {
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT BOOKTITLE FROM BOOK");
        String title;
        int i = 0;
        while(rs.next())
        {
            title = rs.getString("BOOKTITLE");
            System.out.println(i + ".  " + title);
            i++;
        }
        rs.close();
    }
    
    //Option 6
    public static void getBookData(Statement stmt)
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get Name
        name = "Booktitle";
        groups.add(name);
        data += "Book Title: ";
        
        //Get year
        name = "yearpublished";
        groups.add(name);
        data += " Year Published: ";
        
        //Get pages
        name = "numberpages";
        groups.add(name);
        data += " Number Pages: ";
        
        //Get group name
        name = "groupname";
        groups.add(name);
        data += " Group Name: ";
        
        //Get publisher name
        name = "publishername";
        groups.add(name);
        data += " Publisher Name: ";
        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        
        try
        {
            System.out.println("What Book Would You Like Data On?");
            getAllTitles(stmt);
            input.nextLine();
            String title = input.nextLine();
            String sql  = "SELECT DISTINCT * FROM Book, Publisher, WritingGroup WHERE BookTitle = '" + title + "'", attribute;
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(data);
            while (rs.next()) {
                  for(int i=0;i<groups.size();i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                         System.out.print(attribute+"  ,");  
                  }
                  System.out.println();  
            }
            rs.close();
       }catch(Exception e)
       {
           System.out.println("An Error Occurred");
       }
    }
    
    //OLD METHOD DON'T USE
    public static void getBookData1(Statement stmt)
    {
        ArrayList groups = new ArrayList<>();
        String data = "", response = "", name = "";
        
        // Get Name
        System.out.println("Would you like to select Book Title? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "Booktitle";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += "Book Title: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get year
        System.out.println("Would you like to select Year Published? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "yearpublished";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Year Published: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get pages
        System.out.println("Would you like to select Pages? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "numberpages";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Number Pages: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get group name
        System.out.println("Would you like to select Group Name? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "groupname";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Group Name: ";
            }
            else System.out.println("Already selected");
        }
        
        //Get publisher name
        System.out.println("Would you like to select Publisher Name? (Y/N)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            name = "publishername";
            if(!isListed(groups, name))
            {
                groups.add(name);
                data += " Publisher Name: ";
            }
            else System.out.println("Already selected");
        }
        
        String selections = " " + groups.get(0);
        for(int i = 1; i < groups.size(); i++)
            selections += ", " + groups.get(i);
        
        try
        {
            String sql  = "SELECT" + selections + " FROM Book", attribute;
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(data);
            while (rs.next()) 
            {
                  for(int i=0;i<groups.size() - 1 ;i++)
                  {
                      attribute = rs.getString((String) groups.get(i));
                      System.out.print(attribute+"  ,");  
                  }
                  attribute = rs.getString((String) groups.get(groups.size() - 1));
                  System.out.print(" " + attribute);  
                  System.out.println();
            }
            rs.close();
       }catch(Exception e)
       {
           System.out.println("An error occurred");
       }
    }
    
    //Option 7
    public static void addBook(Statement stmt) throws SQLException
    {
        System.out.println("Enter the title of the book:");
        String title = input.next();
        System.out.println("Enter the Year Published:");
        String yearPublished = input.next();
        System.out.println("Enter the Number of Pages:");
        int numPages = input.nextInt();
        
        //input.nextLine();

        System.out.println("\nchoose one of these registered group names");

        getAllGroups(stmt);
        System.out.println("\nEnter the Group Name:");
        input.nextLine();
        String groupName = input.nextLine();
        
        //getting the Publisher name
        //System.out.println("\nchoose one of these registered group names");

        getAllPublishers(stmt);
        System.out.println("\nChoose Publisher Name:");
        //input.next();
        String pubName = input.nextLine();
        //getPublishers(stmt);
        

        try 
        {
            String sql = "INSERT INTO Book VALUES ('" + title + "', '" + yearPublished + "'," + numPages + ", '" + groupName +"', '"+ pubName +"') ";
            System.out.println("\nSQL = " + sql + "\n");
            stmt.executeUpdate(sql);
            System.out.println("New book have been inserted");
        }
        
        catch(SQLException s)
        {
            System.out.println("There was a problem.  Please try again");
        }
        catch(Exception e)
        {
            System.out.println("There was a problem.  Please try again");
        }
    }
    
    //Option 8
    public static void updatePublishers(Statement stmt) throws SQLException
    {
        input.nextLine();
        try{
            System.out.println("What is the publisher's name: ");
            String name = input.nextLine();
            
            System.out.println("What is the publisher's address: ");
            String address = input.nextLine();
            
            System.out.print("What is the publisher phone: ");
            String phone = input.nextLine();
            
            System.out.println("What is the publisher email: ");
            String email = input.nextLine();

            try 
            {
                 String sql = "INSERT INTO PUBLISHER VALUES('"+name+"' , '"+address+"' , '"+phone+"' , '"+email+"')";
                 //System.out.println(sql);
                 stmt.executeUpdate(sql);

                 System.out.println("Should the books published by one Publisher now be published by the new Publisher? (Y/N)");
                 String choice = input.next();
                 
                 if(choice.equalsIgnoreCase("Y"))
                 {
                     input.nextLine();
                    getAllPublishers(stmt);

                    System.out.println("What is the name of the publisher that will be updated: ");
                    String newPublisher = input.nextLine();

                    String update = "update BOOK set publishername= '"+name+"' where publishername= '"+ newPublisher+"'";
                    //System.out.println(sql);
                    stmt.execute(update);
                    System.out.println("Should be updated");
                 }
            }
            catch(SQLException e)
            {
                System.out.println("An Error Occured");
            }
            
        }
        catch(InputMismatchException e)
        {
            System.out.println("Invalid Input");
            updatePublishers(stmt);
        } 
        catch(Exception e)
        {
            System.out.println("An Error Occured");
            updatePublishers(stmt);
        }    
    }
    
    //Option 9
    public static void remove(Statement stmt) throws SQLException
    {
        System.out.println("1. Delete by Book title"
                          +"\n2. Delete by Year Published"
                          +"\n3. Delete by Number of Pages"
                          +"\n4. Delete by Group Name"
                          +"\n5. Delete by Publisher Name");
        
        int choice = input.nextInt();
        String attribute = null;
        String delete = null;
        int pageDelete = 0;
        input.nextLine();
        switch(choice)
        {
            case 1: 
                attribute = "BOOKTITLE";
                //show all the titles that can be deleted
                getAllTitles(stmt);
                
                System.out.println("Enter the title of the book you would like to delete");
                delete = input.nextLine();
                break;
            case 2:
                attribute = "YEARPUBLISHED";
                
                System.out.println("Enter the year published of the book you would like to delete");
                delete = input.nextLine();                
                break;                                
            case 3:
                attribute = "NUMBERPAGES";
                System.out.println("Enter the year number of pages of the book you would like to delete");
                pageDelete = input.nextInt();                
                break;
            case 4:
                attribute = "GROUPNAME";

                System.out.println("\nchoose one of these registered group names");

                getAllGroups(stmt);
                System.out.println("Enter the year name of the group of the book you would like to delete");
                delete = input.nextLine();                 
                break;
            case 5:
                attribute = "PUBLISHERNAME";

                System.out.println("\nchoose one of these registered group names");

                getAllPublishers(stmt);                
                System.out.println("Enter the year publisher's name of the book you would like to delete");
                delete = input.nextLine();                
                break;   
            default: 
                System.out.println("Invalid chose.");
                break;
        }
        if (attribute != null) 
        {
            try
            {
                if (delete != null) 
                {
                    String sql = "Delete from Book where "+attribute+"='"+delete+"'";
                    stmt.executeUpdate(sql);
                    System.out.println("Book have been deleted"); 
                } 
                else 
                {
                    String sql = "Delete from Book where "+attribute+"="+pageDelete;
                    stmt.executeUpdate(sql);
                    //System.out.print("Book has been deleted"); 
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Invalid input");
            }
            catch(SQLException s)
            {
                System.out.println("There was an error.  Please try again");
            }
        }
    }
}

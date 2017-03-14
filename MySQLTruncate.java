package championGG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MySQLTruncate {

  //Enter your own Database Name and Password
  String databaseName = "";
  String databasePassword = "";
	public static void truncateParticipantTable(){
		
    
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);

			String query = "Truncate Participant";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
	
		    preparedStmt.execute();

		    con.close();
		}catch(Exception e){
			System.err.println("Got an exception in MySQLTruncate.truncateParticipantsTable!");
		    System.err.println(e.getMessage());
			
		}
	}
	
	
	public static void truncateMidGameStats(){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
		
			String query = "Truncate MidGameStats";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
	
		    preparedStmt.execute();

		    con.close();
		}catch(Exception e){
			System.err.println("Got an exception in MySQLTruncate.truncateParticipantsTable!");
		    System.err.println(e.getMessage());
			
		}
	}
	public static void truncateGameTable(){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
		
			String query = "Truncate Match_lol";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
	
		    preparedStmt.execute();

		    con.close();
		}catch(Exception e){
			System.err.println("Got an exception in MySQLTruncate.truncateGameTable!");
		    System.err.println(e.getMessage());
			
		}
	}
	
	public static void truncateSummonerTable(){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
		
			String query = "Truncate Summoner";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
	
		    preparedStmt.execute();

		    con.close();
		}catch(Exception e){
			System.err.println("Got an exception in MySQLTruncate.truncateSummonerTable!");
		    System.err.println(e.getMessage());
			
		}
	}
}
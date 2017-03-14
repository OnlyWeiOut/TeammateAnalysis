package championGG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLInsert {

      String databaseName = "";
      String databasePassword = "";
	public static void insertSummoner(Summoner s) throws SQLException{
		
    
    //Enter your own Database Name and Passowrd

    
		Connection con = null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
		
			String query = " insert into Summoner (SummonerId, Region, SummonerName, Rank, GamesPlayed, WinPercent, avg_solo_kill, avg_solo_death, avg_cspm, avg_duration_delta)"
			        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.setLong(1, s.summonerId);
		    preparedStmt.setString (2, s.region);
		    preparedStmt.setString (3, s.summonerName);
		    preparedStmt.setString (4, s.summonerRank);
		    preparedStmt.setInt (5, s.numberOfGamesPlayed);
		    preparedStmt.setFloat(6, s.winPercent);
		    preparedStmt.setFloat(7, s.avgSoloKills);
		    preparedStmt.setFloat(8, s.avgSoloDeath);
		    preparedStmt.setFloat(9, s.avgCSPM);
		    preparedStmt.setFloat(10, s.avgDurationDelta);

		    
		    preparedStmt.execute();
		    con.close();

		}catch(Exception e){
			if(e.getMessage().contains("Duplicate entry")){
			    /*
				String query = "UPDATE Summoner SET summonerName = ?, soloRank = ?, GamesPlayed = ?, Victories = ?, Defeats = ?, Percent = ? where summonerId = ?";
			    
			    */

			}else{
				System.err.println("Got an exception in MySQLQuery.insertSummoner!");
		    	System.err.println(e.getMessage());
		    	con.close();
			}
		}
	}
	
	public static void insertMatch(Match m) throws SQLException{
		Connection con = null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
					
			String query = "INSERT INTO Match_lol (MatchId, Region, Season, Patch, CreationDate, Duration)" + "VALUES (?, ?, ?, ?, ?, ?)";
			
		    PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.setLong(1, m.matchId);
		    preparedStmt.setString(2, m.region);
		    preparedStmt.setString(3, m.season);
		    preparedStmt.setString(4, m.patch);
		    preparedStmt.setLong(5, m.creationDate);
		    preparedStmt.setInt(6, m.duration);
		    preparedStmt.execute();
		    
		    
		    con.close();
		}catch(Exception e){
			
			if(!e.getMessage().contains("Duplicate entry")){
				System.err.println("Got an exception in MySQLInsert.insertMatch!");
		    	System.err.println(e.getMessage());
			}
		    
			con.close();
		}
		
	}
	
	public static void insertParticipants(Match m){
		
		try{  
		    ArrayList<Participant> listOfPart = m.listOfParticipant;
		    for(Participant p: listOfPart){
		    	
		    Class.forName("com.mysql.jdbc.Driver");  
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);

				String query = "INSERT INTO Participant (MatchId, SummonerId, ChampionId, Lane, isWinner, SoloKills, SoloDeaths, CSPM, GPM)" + "VALUES (?,?,?,?,?,?,?,?,?)";

				PreparedStatement preparedStmt = con.prepareStatement(query);
				
			  preparedStmt.setLong(1, m.matchId);
			  preparedStmt.setLong(2, p.summonerId);
			  preparedStmt.setInt(3, p.championId);
			  preparedStmt.setString(4, p.lane);
			  preparedStmt.setBoolean(5, p.isWinner);
			  preparedStmt.setInt(6, p.soloKills);
			  preparedStmt.setInt(7, p.soloDeaths);
			  preparedStmt.setFloat(8, p.creepScorePerMinute);
			  preparedStmt.setFloat(9, p.goldPerMinute);
			  preparedStmt.execute();
			  con.close();
		   }
		}catch(Exception e){
			//System.out.println(e.getMessage());
			if(!e.getMessage().contains("Duplicate entry")){
				System.err.println("Got an exception in MySQLInsert.insertParticipants!");
		    	System.err.println(e.getMessage());
			}
		}
	}
  
	public static void insertChampion(Champion x) {
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName +"?useUnicode=true&characterEncoding=UTF-8","root", databasePassowrd);
		
			String query = " insert into Champion (championID, championName)"
			        + " values (?, ?)";
		    PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.setInt(1, x.championId);
		    preparedStmt.setString (2, x.championName);
	
		    preparedStmt.execute();

		    con.close();
		}catch(Exception e){
			if(!e.getMessage().contains("Duplicate entry")){
				System.err.println("Got an exception in MySQLQuery.insertMatch!");
		    	System.err.println(e.getMessage());
			}
		}		
	}
	
}
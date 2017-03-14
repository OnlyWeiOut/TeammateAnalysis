package championGG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.sql.ResultSet;


public class MySQLSelect {

  //Enter your own Database Name and Password
  String databaseName = "";
  String databasePassword = "";
  
	public static Summoner getSummonerByNameDAO(String summonerName){
		
    String databaseName = "";
    String databasePassword = "";
    Connection con = null;
		try{  
			summonerName = "\""+summonerName + "\"";
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=UTF-8","root", databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Summoner WHERE SummonerName = "+ summonerName);
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				con.close();
				return null;
			}
			rs.next();
			String sumName = rs.getString("SummonerName");
			String sumRank = rs.getString("SoloRank");
			long sumID = rs.getLong("SummonerId");
		
			Summoner s = new Summoner();
			s.summonerId = sumID;
			s.summonerName = sumName;
			s.summonerRank = sumRank;
			
		    con.close();
		    
		    return s;
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());
			
		}
		return null;
	}
	
	
	public static Summoner getSummonerByIDDAO(String summonerID){
		try{  
			summonerID = "\""+summonerID + "\"";
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=UTF-8","root", databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Summoner WHERE SummonerId = "+ summonerID);
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			rs.next();
		
			String sumName = rs.getString("SummonerName");
			String sumRank = rs.getString("SoloRank");
			long sumID = rs.getLong("SummonerId");
		
			Summoner s = new Summoner();
			s.summonerId = sumID;
			s.summonerName = sumName;
			s.summonerRank = sumRank;
			
		    con.close();
		    
		    return s;
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());
			
		}
		
		return null;
		
	}
	
	
	
	
	
	public static Match getMatchDAO(String matchID){
		try{  
			matchID = "\""+matchID + "\"";
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=UTF-8","root", databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Game WHERE GameId = "+ matchID);
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			rs.next();
		
			String patch = rs.getString("Patch");
			long date = rs.getLong("DateTime");
			String region = rs.getString("Region");

			Match m = new Match();
			m.patch = patch;
			m.creationDate = date;
			m.region = region;
			
		
		    con.close();
		    return m;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static ArrayList<Match> getListOfMatch(){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=UTF-8","root", databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Game");
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<Match> listOfMatch = new ArrayList<Match>();
			
			while(rs.next()){
				
				Match m = new Match();
				m.matchId = rs.getLong("GameId");				
				listOfMatch.add(m);	
			}
			
			return listOfMatch;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());
			
		}
		
		return null;
	}
	
	
	
	public static ArrayList<Summoner> getListOfSummonersInSummoners(){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=UTF-8","root", databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Summoner");
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<Summoner> listOfSummoner = new ArrayList<Summoner>();
			
			while(rs.next()){
				
				Summoner s = new Summoner();
				s.summonerId = rs.getLong("SummonerId");
				s.summonerName = rs.getString("SummonerName");

				listOfSummoner.add(s);	
			}
			
			return listOfSummoner;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());
			
		}
		
		return null;
	}
	
	public static ArrayList<Summoner> getListOfTopSummonersInSummoners(int numberOfSummoners, boolean isAscending){
		
		
		String ascendingOrDescending = "";
		if(isAscending == false){
			ascendingOrDescending = "DESC";
		}
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			
			
			ResultSet rs = stmt.executeQuery("SELECT SummonerId FROM championGG.Summoner ORDER BY Percent " + ascendingOrDescending);
			
			
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<Summoner> listOfSummoner = new ArrayList<Summoner>();
			
			int amountOfSummonerCount = 0;
			while(rs.next()){
				
				if(amountOfSummonerCount >= numberOfSummoners) break;

				Summoner s = new Summoner();
				s.summonerId = rs.getLong("SummonerId");
				amountOfSummonerCount++;
				//s.summonerName = rs.getString("SummonerName");
				
				listOfSummoner.add(s);
			}
			
			return listOfSummoner;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());	
		}
		return null;
	}
	
	
	public static float getTimeDelta(long summonerId){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT ChampionId, Lane, isWinner, Duration FROM championGG.Participant JOIN Match_lol ON Match_lol.MatchId = Participant.MatchId Where SummonerId = "+summonerId+" ORDER By Match_lol.CreationDate DESC");
			
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return 0;
			}
			
			float durationForWins = 0;
			float durationForLoss = 0;
			int winCount = 0;
			int lossCount = 0;
			
			float durationDelta = 0;
			
			int last25Games = 0;
			while(rs.next()){
				
				if(last25Games == 25) break;
				int duration = rs.getInt("Duration");
				boolean isWinner = rs.getBoolean("isWinner");

				if(isWinner == true){
					durationForWins += duration;
					winCount++;
				}else{
					durationForLoss += duration;
					lossCount++;
				}
				last25Games++;
			}
			
			durationForWins = durationForWins/winCount;
			durationForLoss = durationForLoss/lossCount;
			durationDelta = durationForLoss-durationForWins;
			durationDelta = durationDelta/60;
			
			DecimalFormat df = new DecimalFormat("##.###");
			String durationDeltaString = df.format(durationDelta);
			durationDelta = Float.parseFloat(durationDeltaString);
			

			return durationDelta;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLSelect.getTimeDelta!");
		    System.err.println(e.getMessage());	
		}
		
	
		return 0;
		
	}
	
	
	public static Summoner getSummonerAverageStat(Summoner s){
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			
			//String summonerName = "\""+ s.summonerName+"\"";
			//System.out.println(summonerName);
			ResultSet rs = stmt.executeQuery("SELECT AVG(SoloDeaths) From Participant Where SummonerId = "+s.summonerId +" UNION Select AVG(CSPM) From Participant Where SummonerId = "+s.summonerId+" And (Lane = 'TOP' OR Lane = 'MIDDLE' OR Lane = 'DUO_CARRY')");
			
			
			if(!rs.next()){
				return s;
			}
			
			
			
			
			DecimalFormat df = new DecimalFormat("##.###");
			
			float avgSoloDeath = rs.getFloat(1);
			avgSoloDeath = Float.parseFloat(df.format(avgSoloDeath));
			
			rs.next();
			float avgCSPM = rs.getFloat(1);
			avgCSPM = Float.parseFloat(df.format(avgCSPM));
			//float avgSoloKills = rs.
			//avgSoloKills = Float.parseFloat(df.format(avgSoloKills));
			//float avgSoloDeath = rs.getFloat("AVG(SoloDeaths)");
			//avgSoloDeath = Float.parseFloat(df.format(avgSoloDeath));
			
			s.avgSoloDeath = avgSoloDeath;
			s.avgCSPM = avgCSPM;
			//s.avgSoloKills = avgSoloKills;
			//s.avgDurationDelta = getTimeDelta(s.summonerId);
			
			return s;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLSelect.getSummonerAverageStat!");
		    System.err.println(e.getMessage());	
		}
		
	
		return null;
		
	}
	
	
	
	public static ArrayList<Participant> getParticipationDataOfSummoner(){
		
		
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			
			
			ResultSet rs = stmt.executeQuery("SELECT Summoner.SummonerId, SummonerName, Duration/60, Winner FROM championGG.Summoner JOIN Participant ON Summoner.SummonerId = Participant.SummonerId JOIN Match_lol ON Match_lol.Match_Id = Participant.MatchId");
			
			
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<Participant> listOfParticipants = new ArrayList<Participant>();
			
			while(rs.next()){
				
				Participant p = new Participant();
				p.summonerId = rs.getLong("SummonerId");
				
				
				//s.summonerName = rs.getString("SummonerName");
				
				listOfParticipants.add(p);
			}
			
			return listOfParticipants;
		    
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());	
		}
		return null;
	}
	
	public static ArrayList<Summoner> getListOfUniqueSummonersInParticipant(){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT(SummonerID) FROM  Participant");
			if(!rs.next()){
				System.out.println("No result was found");
				return null;
			}
			ArrayList<Summoner> listOfSummoner = new ArrayList<Summoner>();	
			while(rs.next()){		
				Summoner s = new Summoner();
				s.summonerId = rs.getLong("SummonerId");
				listOfSummoner.add(s);	
			}
			return listOfSummoner;
		}catch(Exception e){
			System.err.println("Got an exception in MySQLQuery.selectSummonerByName!");
		    System.err.println(e.getMessage());	
		}
		return null;
	}
	
	public static ArrayList<Participant> getParticipantForSummonerWithId(long summonerId){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM  Participant WHERE SummonerId = " + summonerId);
			if(!rs.next()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<Participant> listOfParticipantData = new ArrayList<Participant>();
			
			while(rs.next()){
				Participant p = new Participant();
				p.summonerId = rs.getLong("SummonerId");
				listOfParticipantData.add(p);	
			}
			return listOfParticipantData;
		}catch(Exception e){
			System.err.println("Got an exception in MySQLSelect.getParticipantForSummonerWithId!");
		    System.err.println(e.getMessage());	
		}
		return null;
	}
	
	
	public static ArrayList<String> getListOfregions(){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/championGG?useUnicode=true&characterEncoding=UTF-8","root", "Thunder101");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT RegionName FROM Region");
			if(!rs.isBeforeFirst()){
				System.out.println("No result was found");
				return null;
			}
			
			ArrayList<String> listOfRegions = new ArrayList<String>();
			
			while(rs.next()){
				
				String regionName = rs.getString("RegionName");
						
				listOfRegions.add(regionName);	
			}
			return listOfRegions;
		}catch(Exception e){
			System.err.println("Got an exception in MySQLSelect.getListOfRegion!");
		    System.err.println(e.getMessage());	
		}	
		return null;	
	}
}

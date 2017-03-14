package championGG;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;





public class Main {
	 //WeiWei ID = 40744937
	
	static String region = "na";
	static ArrayList<Long> listOfSummonerId = new ArrayList<Long>();
	static HashMap<Long, Boolean> mapOfSummonerId = new HashMap<Long, Boolean>();
	static Stack<Long> stackOfSummonerId = new Stack<Long>();
	
	
	public static void main(String args[]) throws IOException, JSONException, InterruptedException, SQLException{  

		
		long startTime = System.nanoTime();
		//code
		//truncateAllTables();
		while(true){
			main1();
		}
		/*
		Summoner s = Summoner.getSummonerWithName(region, "Sal Ali");
		ArrayList<Match> listOfMatches = Match.getMatchList(region, s.summonerId);
		
		
		for(int i = 0; i < 10; i++){
			Match m = listOfMatches.get(i);
			m = Match.getMatchWithId(region, m.matchId);
			for(Participant p: m.listOfParticipant){
				if(p.isWinner == false){
					stackOfSummonerId.push(p.summonerId);
				}
			}
		}
		
		int count = 0;
		float sum = 0;
		
		while(!stackOfSummonerId.isEmpty()){
			long summonerId = stackOfSummonerId.pop();
			Summoner checkThisSummoner = Summoner.getSummonerWithId(region, summonerId);
			ArrayList<Match> listOfMatches2 = Match.getMatchList(region, summonerId);
			for(int i = 0; i < 20; i++){
				Match m = listOfMatches2.get(i);
				if(!m.season.contains("2017"))break;
				m = Match.getMatchWithId(region, m.matchId);
				if(m == null) break;
				if(m.numberOfParticipant != 10) continue;
				if(m.duration < 300) continue;
				//MySQLInsert.insertMatch(m);
				MySQLInsert.insertParticipants(m);
			}
			checkThisSummoner = MySQLSelect.getSummonerAverageStat(checkThisSummoner);
			count++;
			sum = sum + checkThisSummoner.avgSoloDeath;
			
			if(count == 5){
				System.out.println("Losing Teams Total SoloDeaths: "+sum);
				sum = 0;
				count = 0;
			}
			
			
			//MySQLInsert.insertSummoner(checkThisSummoner);
		}
		*/
		
		//phase1Hash(s.summonerId);
		
		//phase2();
		
		//long endTime = System.nanoTime();
		//System.out.println("Took "+(endTime - startTime)/1000000 + "ms"); 

	}
	

	private static void main1() throws InterruptedException, JSONException, IOException, SQLException{
		Scanner sc = new Scanner(System.in); 
		System.out.print("Summoner's Username: ");
		String summonerName = sc.nextLine();
		
		Summoner s = Summoner.getSummonerWithName(region, summonerName);
		MySQLInsert.insertSummoner(s);
		System.out.println(summonerName + "'s summonerId is: " +s.summonerId);
		ArrayList<Match> listOfMatches = Match.getMatchList(region, s.summonerId);
		for(int i = 0; i < 20; i++){
			Match m = listOfMatches.get(i);
			m = Match.getMatchWithId(region, m.matchId);
			System.out.println("Looking at Match " + i);
			MySQLInsert.insertMatch(m);
			MySQLInsert.insertParticipants(m);
		}	
	}
	
	public static void phase1Hash(Long summonerId) throws InterruptedException, JSONException, IOException{
		
		while(mapOfSummonerId.size() < 20){
			
			if(mapOfSummonerId.containsKey(summonerId)){
				summonerId = stackOfSummonerId.pop();
				continue;
			}
			
			System.out.println(mapOfSummonerId.size() +" |Stack Size:" + stackOfSummonerId.size() + " | Checking SummonerId: " + summonerId);

			ArrayList<Summoner> listOfSummoners = Summoner.getListOfSummonersInLeagueWith(region, summonerId);
			if(listOfSummoners != null){
				if(listOfSummoners.size() == 0){
					mapOfSummonerId.put(summonerId, false);
				}else{
					for(Summoner s: listOfSummoners){
						mapOfSummonerId.put(s.summonerId, true);
					}
				}
					
				ArrayList<Match> listOfmatches = Match.getMatchList(region, summonerId);
				Match m = listOfmatches.get(0);
				m = Match.getMatchWithId(region, m.matchId);
					
				for(Participant p: m.listOfParticipant){
					if(!mapOfSummonerId.containsKey(p.summonerId) && !stackOfSummonerId.contains(p.summonerId) /*&& p.summonerId != summonerId*/){
						stackOfSummonerId.push(p.summonerId);	
					}
					
				}
			}else{
				mapOfSummonerId.put(summonerId, false);
			}
			
			if(stackOfSummonerId.isEmpty()){
				break;
			}else{
				summonerId = stackOfSummonerId.pop();
			}	
		}
	}
	
	public static void phase2() throws InterruptedException, JSONException, IOException, SQLException{
		
		for(long summonerId : mapOfSummonerId.keySet()){
			System.out.println(summonerId);
			boolean checkOrNah = mapOfSummonerId.get(summonerId);
			if(checkOrNah){
				ArrayList<Summoner> listOfSummoners = Summoner.getListOfSummonersInLeagueWith(region, summonerId);
				Summoner summonerA = null;
				for(Summoner s: listOfSummoners){
					if(s.summonerId == summonerId){
						summonerA = s;
						break;
					}
				}
				
				ArrayList<Match> listOfMatches = Match.getMatchList(region, summonerId);
				int gamesPlayedCount = 0;
				for(Match m: listOfMatches){
					if(!m.season.contains("2017"))break;
					if(gamesPlayedCount >= 20) break;
					
					
					m = Match.getMatchWithId(region, m.matchId);
					if(m == null) break;
					if(m.numberOfParticipant != 10) continue;
					if(m.duration < 300) continue;
					MySQLInsert.insertMatch(m);
					MySQLInsert.insertParticipants(m);
					gamesPlayedCount++;
				}
				
				summonerA = MySQLSelect.getSummonerAverageStat(summonerA);
				MySQLInsert.insertSummoner(summonerA);
				
			}
		}
		
	}
	
	public static void truncateAllTables(){
		MySQLTruncate.truncateGameTable();
		MySQLTruncate.truncateParticipantTable();
		MySQLTruncate.truncateSummonerTable();	
	}
	


}
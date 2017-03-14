package championGG;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Summoner {

	public long summonerId;
	public String summonerName;
	public String summonerLeague;
	public String summonerRank;
	public String region;
	int numberOfGamesPlayed;
	int numberOfWins;
	int numberOfLosses;
	float winPercent;
	
	float avgSoloDeath;
	float avgCSPM;
	float avgSoloKills;
	float avgDurationDelta;
	
	//Haven't Found a Use For This But I'll keep it for the potential value they have?
	int profileIconID;
	int revisionDate;
	int summonerLevel;	
	
	public static Summoner getSummonerWithId(String region, long summonerId) throws InterruptedException, JSONException, IOException{	
		TimeUnit.MILLISECONDS.sleep(1200);
		String apiKey = APIKey.getAPIKey();
		String urlString = "https://"+region+".api.pvp.net/api/lol/na/v1.4/summoner/"+summonerId+"?api_key=" + apiKey;
		URL url = new URL(urlString);
		
		Scanner scan = null;
		try{
			scan = new Scanner(url.openStream());

		} catch(IOException e){
			
			
			boolean errorCheck = ErrorCheck.errorHandlingForRiotAPI(urlString);
			if(errorCheck == false){
				System.err.println("Error With Url: " + urlString);
				return null;
			}
			if(errorCheck == true){
				return getSummonerWithId(region, summonerId);
			}
		}
		
		String str = new String();
		while(scan.hasNext()){
			str +=scan.nextLine();
		}
		scan.close();
		
		JSONObject fullJson = new JSONObject(str);
		JSONObject summonerInformation = fullJson.optJSONObject(Long.toString(summonerId));
		String summonerName = summonerInformation.getString("name");
		
		Summoner s = new Summoner();
		s.summonerName = summonerName;
		s.summonerId = summonerId;
		s.region = region; 
		return s;
	}
	
	public static Summoner getSummonerWithName(String region, String name) throws InterruptedException, JSONException, IOException{
		//TimeUnit.MILLISECONDS.sleep(1200);
		String apiKey = APIKey.getAPIKey();
		String namePercent20 = name.replaceAll(" ", "%20");
		namePercent20 = namePercent20.toLowerCase();
		String urlString = "https://"+region+".api.pvp.net/api/lol/"+region+"/v1.4/summoner/by-name/" + namePercent20 +"?api_key=" + apiKey;
		URL url = new URL(urlString);

		Scanner scan = null;
		try{
			scan = new Scanner(url.openStream());

		} catch(IOException e){
			
			boolean errorCheck = ErrorCheck.errorHandlingForRiotAPI(urlString);
			if(errorCheck == false){
				System.err.println("Error With Url: " + urlString);
				return null;
			}
			if(errorCheck == true){
				return getSummonerWithName(region, name);
			}
			
			/*if(e.toString().contains("FileNotFoundException")) return null;
			System.err.println(e.getMessage());

			System.err.println("Error With Url: " + urlString);
			return getSummonerWithName(region, name);*/
		}
		
		String str = new String();
		while(scan.hasNext()){
			str +=scan.nextLine();
		}
		scan.close();
		
		name = name.replaceAll(" ", "").toLowerCase();
		JSONObject fullJson = new JSONObject(str);
		JSONObject summonerInformation = fullJson.getJSONObject(name);
		String summonerName = summonerInformation.getString("name");
		long summonerId = summonerInformation.getLong("id");
		
		Summoner s = new Summoner();
		s.summonerName = summonerName;
		s.summonerId = summonerId;
		s.region = region;
		return s;
		
	}
	
	
	
	
	public static ArrayList<Summoner> getListOfSummonersInLeagueWith(String region, long summonerId) throws InterruptedException, JSONException, IOException{
		
		TimeUnit.MILLISECONDS.sleep(1200);
		ArrayList<Summoner> listOfSummoner = new ArrayList<Summoner>();

		String apiKey = APIKey.getAPIKey();
		String urlString = "https://"+ region +".api.pvp.net/api/lol/na/v2.5/league/by-summoner/"+summonerId +"?api_key=" + apiKey;
		URL url = new URL(urlString);
		
		Scanner scan = null;
		try {
			scan = new Scanner(url.openStream());
		} catch (IOException e) {
		
			boolean errorCheck = ErrorCheck.errorHandlingForRiotAPI(urlString);
			if(errorCheck == false){
				System.err.println("Error With Url: " + urlString);
				return null;
			}
			if(errorCheck == true){
				return getListOfSummonersInLeagueWith(region, summonerId);
			}    
		}
		String str = new String();
		while (scan.hasNext())
		    str += scan.nextLine();
		scan.close();
		
		
		JSONObject fullJson = new JSONObject(str);
		JSONArray queueArrays = fullJson.getJSONArray(Long.toString(summonerId));
		
		for(int i = 0; i < queueArrays.length(); i++){
			JSONObject queueJson = queueArrays.getJSONObject(i);
			String queueType = queueJson.getString("queue");
			
			if(!queueType.equals("RANKED_SOLO_5x5")){
				continue;
			}
			
			String divisionName = queueJson.getString("name");
			String tier = queueJson.getString("tier");
			
			JSONArray entriesArray = queueJson.getJSONArray("entries");
			
			for(int j = 0; j < entriesArray.length(); j++){
				
				JSONObject individualEntries = entriesArray.getJSONObject(j);
				String summonerName = individualEntries.getString("playerOrTeamName");
				long summoner_id = individualEntries.getLong("playerOrTeamId");
				String divisionNumber = individualEntries.getString("division");
				String sRank = tier + " " + divisionNumber;
				int wins = individualEntries.getInt("wins");
				int losses = individualEntries.getInt("losses");
				int gamesPlayed = wins + losses;
				float winPercentage = wins * 100 /gamesPlayed;
				
				Summoner s = new Summoner();
				s.summonerName = summonerName;
				s.summonerLeague = divisionName;
				s.region = region;
				s.summonerId = summoner_id;
				s.summonerRank = sRank;
				s.numberOfWins = wins;
				s.numberOfLosses = losses;
				s.numberOfGamesPlayed = gamesPlayed;
				s.winPercent = winPercentage;
				listOfSummoner.add(s);
			}	
		}
		return listOfSummoner;
	}
	
	
	
}
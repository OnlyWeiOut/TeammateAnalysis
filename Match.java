package championGG;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match {
	
	public long matchId;
	public String region;
	public String season;
	public String patch;
	public long creationDate;
	public int duration;
	public ArrayList<Participant> listOfParticipant;
	public int numberOfParticipant;
	
	ArrayList<Event> listOfEvents;
	
	public static Match getMatchWithId(String region, long matchId) throws InterruptedException, JSONException, IOException{
		TimeUnit.MILLISECONDS.sleep(1200);	
		String apiKey = APIKey.getAPIKey();

		String urlString = "https://"+region +".api.pvp.net/api/lol/"+region +"/v2.2/match/"+matchId+"?includeTimeline=true&api_key="+ apiKey;
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
			    return getMatchWithId(region, matchId);
			}   
			
		}
		
		String str = new String();
		while (scan.hasNext()){
		    str += scan.nextLine();
		}
		scan.close();
		
		
		Match m = new Match();

		JSONObject fullJson = new JSONObject(str);
		m.region = region;
		m.matchId = matchId;
		m.season = fullJson.getString("season");
		m.patch = fullJson.getString("matchVersion");	
		m.creationDate = fullJson.getLong("matchCreation");
		m.duration = fullJson.getInt("matchDuration");

		float durationOfPassiveGold = m.duration - 75;
		float passiveGold = (float) ((durationOfPassiveGold/6) * 20.4);
		
		JSONArray participantsArray = fullJson.getJSONArray("participants");
		JSONArray participantsIdArray = fullJson.getJSONArray("participantIdentities");
		
		ArrayList<Participant> listOfParticipants = new ArrayList<Participant>();

		for(int i = 0; i < participantsIdArray.length(); i++){
			Participant p = new Participant();

			JSONObject participantJSON = participantsArray.getJSONObject(i);
			JSONObject participantsIdJson = participantsIdArray.getJSONObject(i);
			JSONObject playerInfoJson = participantsIdJson.getJSONObject("player");
			
			p.summonerId = playerInfoJson.getInt("summonerId");
			p.championId = participantJSON.optInt("championId");
			JSONObject participantStats = participantJSON.optJSONObject("stats");
			p.matchId = matchId;
			p.isWinner = participantStats.getBoolean("winner");
			int creepScore = participantStats.optInt("minionsKilled") + participantStats.optInt("neutralMinionsKilled");
			DecimalFormat df = new DecimalFormat("#.#");
			String creepScorePerMinuteString = df.format((float)creepScore/((m.duration-75
					)/60));
			float creepScorePerMinute = Float.parseFloat(creepScorePerMinuteString);
			
			p.creepScorePerMinute = creepScorePerMinute;
			p.damageDealtToChampions = participantStats.optInt("totalDamageDealtToChampions");
			p.goldEarned = (int) (participantStats.optInt("goldEarned") - passiveGold);
			String goldEarnedPerMinuteString = df.format((float)p.goldEarned/((m.duration-75
					)/60));
			p.goldPerMinute = Float.parseFloat(goldEarnedPerMinuteString);
			

			JSONObject timelineJSON = participantJSON.optJSONObject("timeline");
			String role = timelineJSON.optString("role");
			String lane = timelineJSON.optString("lane");
			if(lane.equals("BOTTOM")){
				lane = role;
			}
			p.lane = lane;
			p.role = role;	
			listOfParticipants.add(p);	
		}
		
		
		
		ArrayList<Event> listOfEvents = new ArrayList<Event>();
		JSONObject timelineObject = fullJson.getJSONObject("timeline");
		JSONArray framesArray = timelineObject.getJSONArray("frames");
		
		for(int i = 0; i < framesArray.length(); i++){
			JSONObject framesObject = framesArray.getJSONObject(i);
			JSONArray eventsArray = framesObject.optJSONArray("events");
			if(eventsArray == null) continue;
			for(int j = 0; j < eventsArray.length(); j++){
				Event e = new Event();
				JSONObject eventObject = eventsArray.getJSONObject(j);
				String eventType = eventObject.getString("eventType");
				e.eventType = eventType;
				if(eventType.equals("BUILDING_KILL")){
					e.killerId = eventObject.getInt("killerId");
					e.timestamp = eventObject.getInt("timestamp");
					e.lane = eventObject.getString("laneType");
					e.buildingType = eventObject.getString("buildingType");
					e.towerType = eventObject.getString("towerType");
					JSONArray assistingParticipants = eventObject.optJSONArray("assistingParticipantIds");					
					ArrayList<Integer> listOfHelpers = new ArrayList<Integer>();
					if(assistingParticipants != null){
						e.amountOfAssist = assistingParticipants.length();
						for(int k = 0; k < e.amountOfAssist; k++){
							int id = assistingParticipants.getInt(k) -1;
							listOfHelpers.add(id);
						}
					}
					e.assistingParticipants = listOfHelpers;
					listOfEvents.add(e);
					

				}else if(eventType.equals("CHAMPION_KILL")){
					e.timestamp = eventObject.getInt("timestamp");
					e.killerId = eventObject.getInt("killerId");
					e.victimId = eventObject.getInt("victimId");
					
					JSONArray assistingParticipants = eventObject.optJSONArray("assistingParticipantIds");					
					ArrayList<Integer> listOfHelpers = new ArrayList<Integer>();
					if(assistingParticipants != null){
						e.amountOfAssist = assistingParticipants.length();
						for(int k = 0; k < e.amountOfAssist; k++){
							int id = assistingParticipants.getInt(k) - 1;
							listOfHelpers.add(id);
						}
					}
					e.assistingParticipants = listOfHelpers;
					listOfEvents.add(e);
				}else if(eventType.equals("ELITE_MONSTER_KILL")){
					listOfEvents.add(e);

				}
			}
		}
		
		float lastKillTime = 0;
		float currentKillTime = 0;
		
		
		for(Event e: listOfEvents){			
			

			if(e.eventType.equals("CHAMPION_KILL")){
				
				currentKillTime = (float)e.timestamp/60000;
				//System.out.println(currentKillTime);
				//System.out.println(lastKillTime);
				float differenceInTime = currentKillTime - lastKillTime;
				lastKillTime = (float) currentKillTime;

				
				if(differenceInTime > .166) continue;
				
				//System.out.println("Last kill was " +differenceInTime+ " minutes ago.");
				int victimId = e.victimId -1;
				listOfParticipants.get(victimId).soloDeaths += 1;
				
				if(e.amountOfAssist == 0){
					listOfParticipants.get(victimId).soloDeaths += 1;
				}
								
				//if(e.assistingParticipants.size() != 0) continue;
				//if(e.killerId == 0) break; 

				//int killerId = e.killerId - 1;
				//listOfParticipants.get(killerId).soloKills++;

			}else if(e.eventType.equals("BUILDING_KILL")){
				int killerId = e.killerId -1;
				int score = 0;
				
				if(e.buildingType.equals("TOWER_BUILDING")){
					
					if(e.towerType.equals("OUTER_TURRET")){
						score = 1;
					}else if(e.towerType.equals("INNER_TURRET")){
						score = 1;
					}else if(e.towerType.equals("BASE_TURRET")){
						score = 1;
					}
					
					if(killerId != -1){
						listOfParticipants.get(killerId).soloKills += score;
					}
					
					for(int id: e.assistingParticipants){
						listOfParticipants.get(id).soloKills += score;
					}
					
				}else if(e.buildingType.equals("INHIBITOR_BUILDING")){
					if(killerId != -1) listOfParticipants.get(killerId).soloKills += 1;
					
					
					for(int id: e.assistingParticipants){
						listOfParticipants.get(id).soloKills += score;
					}
				}
				
			}
		}
		

		m.listOfEvents = listOfEvents;
		m.listOfParticipant = listOfParticipants;
		m.numberOfParticipant = listOfParticipants.size();
				
		return m;
	}
	
	
	
	public static ArrayList<Match> getMatchList(String region, long summonerId) throws InterruptedException, JSONException, IOException{
		TimeUnit.MILLISECONDS.sleep(1200);

		String apiKey = APIKey.getAPIKey();
		String urlString = "https://"+region+".api.pvp.net/api/lol/"+region+"/v2.2/matchlist/by-summoner/"+ summonerId +"?api_key="+apiKey;
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
				return getMatchList(region, summonerId);
			}  
		}
		
		String str = new String();
		while (scan.hasNext())
		    str += scan.nextLine();
		scan.close();
		
		JSONObject fullJson = new JSONObject(str);
		JSONArray arrayOfMatches = fullJson.getJSONArray("matches");
		
		ArrayList<Match> listOfMatch = new ArrayList<Match>();
		for(int i = 0; i < arrayOfMatches.length(); i++){
			JSONObject matchData = arrayOfMatches.getJSONObject(i);
			Match m = new Match();
			m.matchId = matchData.getLong("matchId");
			m.season = matchData.getString("season");
			m.region = matchData.getString("region");
			listOfMatch.add(m);
		}
		return listOfMatch;
	}
	

}
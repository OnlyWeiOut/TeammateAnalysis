package championGG;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Champion {

	public int championId;
	public String championName;
	public String championKey;

	
	public static ArrayList<Champion> getChampionList() throws JSONException, InterruptedException, MalformedURLException{
		
		String apiKey = APIKey.getAPIKey();

		String urlString = "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?api_key="+ apiKey;
		URL url = new URL(urlString);
		
		Scanner scan = null;
		try {
			scan = new Scanner(url.openStream());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println("Error With Url: " + urlString);			
			return getChampionList();
		}		
		String str = new String();
		while (scan.hasNext())
		    str += scan.nextLine();
		scan.close();
		
		ArrayList<Champion> listOfChampions = new ArrayList<Champion>();
		
		JSONObject fullJson = new JSONObject(str);
		JSONObject dataJson = fullJson.getJSONObject("data");
		
		for(int i = 0; i < dataJson.length(); i++){
			Champion champ = new Champion();
			String championKey = dataJson.names().getString(i);
			int championId = dataJson.getJSONObject(championKey).getInt("id");
			String championName = dataJson.getJSONObject(championKey).getString("name");
			champ.championId = championId;
			champ.championName = championName;
			champ.championKey = championKey;
			listOfChampions.add(champ);
		}
		return listOfChampions;
	}
}
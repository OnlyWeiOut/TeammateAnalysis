package championGG;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class RiotAPI {
	public static final String apiKey = "a606c609-f4f4-4414-a4af-77c4c0e043a9";
	
	public static ArrayList<String> getPatchList() throws InterruptedException, MalformedURLException, JSONException{

		String urlString = "https://global.api.pvp.net/api/lol/static-data/na/v1.2/versions?api_key=" + apiKey;
		URL url = new URL(urlString);
		
		// read from the URL
		Scanner scan = null;
		try {
			scan = new Scanner(url.openStream());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println("Error With Url: " + urlString);
			return getPatchList();
		}		
		
		String str = new String();
		while (scan.hasNext())
		    str += scan.nextLine();
		scan.close();
		
		JSONArray fullJson = new JSONArray(str);
		ArrayList<String> listOfPatches = new ArrayList<String>();
		
		for(int i = 0; i < fullJson.length(); i++){
			String patchVersion = fullJson.getString(i);
			listOfPatches.add(patchVersion);
		}
		return listOfPatches;
	}

}
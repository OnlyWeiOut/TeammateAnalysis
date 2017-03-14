package championGG;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ErrorCheck {

	public static boolean errorHandlingForRiotAPI(String urlString) throws IOException{
		URL urlCatch = new URL(urlString);
		HttpURLConnection http = (HttpURLConnection)urlCatch.openConnection();
		int statusCode = http.getResponseCode();
		
		System.out.println(statusCode);
		
		if(statusCode == 200){
			return true;
		}
		
		if(statusCode == 400){
			return false;
		}
		
		if(statusCode == 401){
			return false;
		}
		
		if(statusCode == 403){
			return false;
		}
		
		if(statusCode == 404){
			return false;
		}
		
		if(statusCode == 429){
			return true;
		}
		
		if(statusCode == 500){
			return true;
		}
		
		if(statusCode == 503){
			return true;
		}
		
		
		return false;
		
	}
}
import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class invest
{
	public invest(){}
	public String getVolumes(String tikers)
	{
		URL url = null;
		try{
			url = new URL("https://tradernet.ru/securities/export?params=vol&tickers=" + tikers);
		}
		catch(MalformedURLException e){
			return "";
		}
		HttpURLConnection http = null;
		try{
			http = (HttpURLConnection)url.openConnection();
		}
		catch(IOException e){
			return "";
		}
		http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		String document = "";
		String line = "";
		try{
			///System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			while(true){
	            try{
	                line = in.readLine();
	            }
	            catch(Exception e){
	                System.err.println("IOException: " + e.getMessage());
	                return "";
	            }
	            if(line == null)
	                break;
	            document += line;
        	}
		}
		catch(IOException e){
			return "";
		}
		http.disconnect();
		return document;
	}
}
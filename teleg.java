import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class teleg
{
	String token;
	public teleg(String new_token){
		token = new_token;
	}
	public void sendMessage(String chat_id, String text, String keyboard)
	{
		URL url = null;
		try{
			url = new URL("https://api.telegram.org/bot" + token + "/sendmessage");
		}
		catch(MalformedURLException e){
			return;
		}
		HttpURLConnection http = null;
		try{
			http = (HttpURLConnection)url.openConnection();
		}
		catch(IOException e){
			return;
		}
		try{
			http.setRequestMethod("POST");
		}
		catch(ProtocolException e){
			return;
		}
		http.setDoOutput(true);
		http.setRequestProperty("Content-Type", "application/json");

		String data = "{\n\"chat_id\" : \"" + chat_id + "\",\n\"text\" : \"" + text + "\",\n\"reply_markup\" : " + keyboard + "\n}";
		//System.out.println(data);

		byte[] out = data.getBytes();
		try{
			OutputStream stream = http.getOutputStream();
			stream.write(out);
			System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
		}
		catch(IOException e){
			return;
		}
		http.disconnect();
	}
	public String getUpdates()
	{
		URL url = null;
		try{
			url = new URL("https://api.telegram.org/bot" + token + "/getupdates?offset=-1");
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
		String document = "";
		String line = "";
		try{
			//System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
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
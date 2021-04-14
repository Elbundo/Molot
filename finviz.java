import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.time.*;

public class finviz
{
	public finviz()
	{

	}
	public String getPage(String sector, int offset)
	{
		URL url = null;
		try{
			url = new URL("https://finance.yahoo.com/screener/predefined/" + sector + "?offset=" + offset + "&count=100");
		}
		catch(MalformedURLException e){
			return "";
		}
		HttpURLConnection http = null;
		try{
			http = (HttpURLConnection)url.openConnection();
			http.setConnectTimeout(10000);
		}
		catch(IOException e){
			return "";
		}
		String line = new String();
		String document = new String();
		try{
			//System.err.println(http.getResponseCode() + " " + http.getResponseMessage());
			if(http.getResponseCode() != 200)
				return "";
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			Instant start = Instant.now();
			while(true){
				Instant finish = Instant.now();
				long elapsed = Duration.between(start, finish).toMinutes();
				if(elapsed >= 2)
					return "";
	            int symbol = 0;
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

	public int getCount(String page)
	{
		if(page.equals(""))
			return 0;
		int begin = page.indexOf("Matching");
		int end = 0;
		begin = page.indexOf("<span", begin + 5);
		begin = page.indexOf("<span", begin + 5);
		begin = page.indexOf("<span", begin + 5);
		begin = page.indexOf(">", begin + 5);
		end = page.indexOf("<", begin + 1);
		String line = page.substring(begin + 1, end);
		begin = line.indexOf("of");
		end = line.indexOf("results");
		line = line.substring(begin + 3, end-1);
		int count = 0;
		try{
			count = Integer.parseInt(line);
		}catch(Exception e){
			return 0;
		}
		return count;
	}

	public String getTable(String page)
	{
		if(page.equals(""))
			return "";
		int begin = 0;
		int end = 1;
		begin = page.indexOf("<tbody");
		begin = page.indexOf(">", begin);
		end = page.indexOf("</tbody>");
		if(begin == -1 || end == -1 || begin >= end)
			return "";
		return page.substring(begin + 1, end);
	}

	public LinkedList<String> getVolumes(String table)
	{
		LinkedList<String> tiker = new LinkedList<>();
		if(table.equals(""))
			return tiker;
		int begin = 0;
		int end = 0;
		String tikerName = "";
		Instant start = Instant.now();
		while(table.indexOf("<a", end) != -1){
			Instant finish = Instant.now();
			long elapsed = Duration.between(start, finish).toMinutes();
			if(elapsed >= 3)
				break;
			begin = table.indexOf("<a", end);
			begin = table.indexOf(">", begin) + 1;
			end = table.indexOf("<", begin);
			if(begin == -1 || end == -1 || begin >= end)
				return tiker;
			tikerName = table.substring(begin, end);
			tiker.add(tikerName);
		}
		return tiker;
	}
	public int getOldVolume(String tiker, String today)
	{
		URL url = null;
		try{
			url = new URL("https://finance.yahoo.com/quote/" + tiker + "/history?p=" + tiker);
		}
		catch(MalformedURLException e){
			return 1;
		}
		HttpURLConnection http = null;
		try{
			http = (HttpURLConnection)url.openConnection();
			http.setConnectTimeout(10000);
		}
		catch(IOException e){
			return 1;
		}
		String line = new String();
		String document = new String();
		try{
			//System.err.println(http.getResponseCode() + " " + http.getResponseMessage());
			if(http.getResponseCode() != 200)
				return 1;
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			Instant start = Instant.now();
			while(true){
				Instant finish = Instant.now();
				long elapsed = Duration.between(start, finish).toMinutes();
				if(elapsed >= 2)
					return 1;
	            int symbol = 0;
	            try{
	                line = in.readLine();
	            }
	            catch(Exception e){
	                System.err.println("IOException: " + e.getMessage());
	                return 1;
	            }
	            if(line == null)
	                break;
	            document += line;
        	}
		}
		catch(IOException e){
			return 1;
		}
		http.disconnect();
		int begin = 0;
		int end = 0;
		begin = document.indexOf("<tbody");
		begin = document.indexOf("<tr", begin);
		begin = document.indexOf("<td", begin);
		begin = document.indexOf("<span", begin);
		begin = document.indexOf(">", begin);
		end = document.indexOf("</span", begin);
		if(begin == -1 || end == -1 || begin >= end)
				return 1;
		if(today.equals(document.substring(begin + 1, end))){
			begin = document.indexOf("</tr", begin);
			begin = document.indexOf("<td", begin + 1);
		}
		for(int i = 0; i < 6; i++)
			begin = document.indexOf("<td", begin + 1);
		begin = document.indexOf("<span", begin + 1);
		begin = document.indexOf(">", begin + 1);
		end = document.indexOf("</s", begin + 1);
		if(begin == -1 || end == -1 || begin >= end)
				return 1;
		String vol = document.substring(begin + 1, end);
		String parts[] = vol.split(",");
		vol = "";
		int oldVolume = 1;
		for(int i = 0; i < parts.length; i++)
			vol += parts[i];
		try{
			oldVolume = Integer.parseInt(vol);
		}catch(Exception e){
			oldVolume = 1;
		}
		return oldVolume;
	}
}
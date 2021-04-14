import java.lang.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class FinderVolume implements Runnable
{
	private Storage myStorage;
	private boolean isActive;

	public FinderVolume(Storage st)
	{
		myStorage = st;
		isActive = true;
	}

	public void disable()
	{
		isActive = false;
	}

	public void run()
	{
		invest find = new invest();
			while(isActive){
				// ArrayList<String> tikers = new ArrayList<>();
				 LinkedList<TikerVolumePair> response = new LinkedList<>();
				// if(myStorage.getHmap().isEmpty())
				// 	continue;
				// for(Map.Entry entry : myStorage.getHmap().entrySet()){
				// 	tikers.add((String)entry.getKey());
				// }
				// String request = "";
				// for(int i = 0; i < tikers.size(); i++){
				// 	request += tikers.get(i) + "+";
				// 	if(i % 50 == 0){
				// 		response.addAll(getFindedTikers(getEachTiker(getAllVolumes(find.getVolumes(request)))));
				// 		request = "";
				// 	}
				// }
				response.addAll(getFindedTikers(getEachTiker(getAllVolumes(find.getVolumes("AAPL.US+NVDA.US+GOGL.US+SPCE.US+M.US+TSLA.US+YNDX.US+CCL.US+JPM.US+BABA.US")))));
				if(response == null)
					continue;
				for (Iterator<TikerVolumePair> iter = response.iterator(); iter.hasNext(); ) {
					TikerVolumePair element = iter.next();
					Date dateNow = new Date();
				    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
					String today = formatForDateNow.format(dateNow);
					Tiker t = new Tiker(today, element.getVol());
					myStorage.putTiker(element.getTiker(), t);
				}
			}
	}

	private String getAllVolumes(String volumes)
	{
		if(volumes.equals(""))
			return "";
		return volumes.substring(1, volumes.length() - 1);
	}

	private LinkedList<String> getEachTiker(String allVols)
	{
		if(allVols.equals(""))
			return null;
		LinkedList<String> tikerVol = new LinkedList<>();
		int begin = 0, end = 0;
		for(int i = 0; i < allVols.length(); i++){ 
			if(allVols.charAt(i) == '{')
				begin = i;
			if(allVols.charAt(i) == '}'){
				end = i;
				tikerVol.add(allVols.substring(begin + 1, end));
			}
		}
		return tikerVol;
	}

	private LinkedList<TikerVolumePair> getFindedTikers(LinkedList<String> list)
	{
		if(list == null)
			return null;
		LinkedList<TikerVolumePair> result = new LinkedList<>();
		for (Iterator<String> iter = list.iterator(); iter.hasNext(); ) {
    		String element = iter.next();
    		String[] tikerandvolume = element.split(",");
    		String tiker = tikerandvolume[0].split(":")[1].substring(1, tikerandvolume[0].split(":")[1].length() - 1);
    		String vol = tikerandvolume[1].split(":")[1].substring(0, tikerandvolume[1].split(":")[1].length());
    		long volume = 0;
    		try{
    			volume = Long.parseLong(vol);
    		}catch(Exception e){
    			volume = -1;
    		}
    		TikerVolumePair pair = new TikerVolumePair(tiker, volume);
    		result.add(pair);
    	}
    	return result;
	}
}

class TikerVolumePair{
	private String tiker;
	private long volumes;

	public TikerVolumePair(String t, long v)
	{
		tiker = t;
		volumes = v;
	}

	public String getTiker(){
		return tiker;
	}

	public long getVol(){
		return volumes;
	}
}
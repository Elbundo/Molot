import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Storage
{
	private HashMap<String, LinkedList<Tiker>> TikerStorage;

	public Storage(){
		 TikerStorage = new HashMap<>();
	}

	synchronized public HashMap<String, LinkedList<Tiker>> getHmap()
	{
		return TikerStorage;
	}

	synchronized public void addTiker(String name)
	{
		if(!TikerStorage.containsKey(name)){
			TikerStorage.put(name, new LinkedList<>());
		}
	}

	synchronized public void putTiker(String name, Tiker vol)
	{
		if(!TikerStorage.containsKey(name)){
			TikerStorage.put(name, new LinkedList<>());
		}
		LinkedList<Tiker> list = TikerStorage.get(name);
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getDay().equals(vol.getDay())){
				list.set(i, vol);
				return;
			}
		}
		TikerStorage.get(name).addFirst(vol);
	}

	synchronized long getVolume(String name, String day)
	{
		if(!TikerStorage.containsKey(name))
			return -1;
		LinkedList<Tiker> list = TikerStorage.get(name);
		long vol = -1;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getDay().equals(day)){
				vol = list.get(i).getVol();
			}
		}
		return vol;
	}
}
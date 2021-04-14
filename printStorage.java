import java.lang.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class printStorage implements Runnable
{
	private Storage myStorage;
	private teleg myT;
	private String chat_id;

	public printStorage(Storage st, teleg t, String id)
	{
		myStorage = st;
		myT = t;
		chat_id = id;
	}
	public void run()
	{
		myT.sendMessage(chat_id, getMessage(myStorage), "");
	}

	private String getMessage(Storage st)
	{
		String result = "";
		HashMap<String, LinkedList<Tiker>> map = st.getHmap();
		for (Map.Entry entry : map.entrySet()) {
			if(entry.getValue() == null)
				continue;
   			result += entry.getKey() + "\n";
   			LinkedList<Tiker> list = (LinkedList<Tiker>)entry.getValue();
   			for (Iterator<Tiker> iter = list.iterator(); iter.hasNext(); ) {
				Tiker element = iter.next();
				result += element.getDay() + " - " + element.getVol() + "\n";
			}
		}
		return result;
	}
}
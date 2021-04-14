import java.lang.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class FinderTiker implements Runnable
{
	private Storage myStorage;

	public FinderTiker(Storage st)
	{
		myStorage = st;
	}
	public void run()
	{
		String sectors[] = {"ms_basic_materials", "ms_communication_services", "ms_consumer_cyclical", "ms_consumer_defensive", "ms_energy", "ms_financial_services", "ms_healthcare", "ms_industrials", "ms_real_estate", "ms_technology", "ms_utilities"};
		finviz myF = new finviz();
		while(true){
			LinkedList<String> list = getAllVolumes(sectors, myF);
			for (Iterator<String> iter = list.iterator(); iter.hasNext(); ) {
				String element = iter.next();
				myStorage.addTiker(element + ".US");
			}
		}
	}

	public static LinkedList<String> getAllVolumes(String sectors[], finviz myFin)
	{
		LinkedList<String> list = new LinkedList<>();
		for(int i = 0; i < sectors.length; i++){
			int n = myFin.getCount(myFin.getPage(sectors[i], 0));
			n = n / 100 + 1;
			for(int j = 0; j < n; j++){
				list.addAll(myFin.getVolumes(myFin.getTable(myFin.getPage(sectors[i], 100*j))));
				//System.out.println(sectors[i] + " is checked!");
				//System.out.println(list);
			}
		}
		return list;
	}
}
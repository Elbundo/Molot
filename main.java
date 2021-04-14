import java.lang.*;
import java.net.*;
import java.util.*;
import java.nio.*;
import java.io.*;

public class main{
	public static void main(String[] args){
		Storage tikerStorage = new Storage();

		TelegramBot myBot = new TelegramBot(tikerStorage);
		FinderVolume myFinder = new FinderVolume(tikerStorage);
		FinderTiker myFinderTiker = new FinderTiker(tikerStorage);

		Thread telegramThread = new Thread(myBot);
		Thread finderThread = new Thread(myFinder);
		Thread finderTikerThread = new Thread(myFinderTiker);

		Scanner in = new Scanner(System.in);

		telegramThread.start();
		finderThread.start();
		//finderTikerThread.start();
		
		String line = "";
		while(!line.equals("quit")){
			line  = in.nextLine();
			if(line.equals("restart TB")){
				myBot.disable();
				myBot = new TelegramBot(tikerStorage);
				new Thread(myBot).start();
			}else if(line.equals("restart FV")){
				myFinder.disable();
				myFinder = new FinderVolume(tikerStorage);
				new Thread(myFinder).start();
			}
		}
		System.out.println("Main thread is over!");
	}	
}
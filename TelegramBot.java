import java.lang.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class TelegramBot implements Runnable
{
	private boolean isActive;
	Storage myStorage;
	Message lastMsg = new Message();

	public TelegramBot(Storage ts)
	{
		myStorage = ts;
		isActive = true;
	}

	public void disable()
	{
		isActive = false;
	}

	public void run()
	{
		String token = null;
		try{	
			token = new String(Files.readAllBytes(Paths.get("token")));
		}
		catch(Exception e)
		{
			teleg myT = new teleg("ALl is bad!");
			return;
		}
		teleg myT = new teleg(token);
		System.out.println(token);
		while(isActive){
			Message curMsg = getCommand(myT.getUpdates());
			if(!lastMsg.equals(curMsg)){
				//curMsg.show();
				if(curMsg.text.equals("help")){
					String text = "Commands:\n\t-update\n\t-restart\n\t-checkAllTickers";
					String keys[][] = {{"update", "restart"}, {"checkTiker", "checkAllTickers"}, {"addTiker"}, {"help"}};
					ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
					myT.sendMessage(curMsg.chat_id, text, newKeyboard.toString());
				}else if(curMsg.text.equals("update")){
					myT.sendMessage(curMsg.chat_id, "doesn't work", "");
				}else if(curMsg.text.equals("restart")){
					myT.sendMessage(curMsg.chat_id, "doesn't work", "");
				}else if(curMsg.text.equals("checkAllTickers")){
					printStorage printSt = new printStorage(myStorage, myT, curMsg.chat_id);
					Thread printThread = new Thread(printSt);
					printThread.start();
				}else if(curMsg.text.equals("addTiker")){
					myT.sendMessage(curMsg.chat_id, "Enter the ticker name...", "");
				}else if(lastMsg.text.equals("addTiker")){
					if(myStorage.getHmap().containsKey(curMsg.text)){
						myT.sendMessage(curMsg.chat_id, "Ticker has already been added!", "");
					}else{
						myStorage.addTiker(curMsg.text);
						myT.sendMessage(curMsg.chat_id, "Ticker was successfully added!", "");
					}
				}else if(curMsg.text.equals("back")){
					String keys[][] = {{"update", "restart"}, {"checkTiker", "checkAllTickers"}, {"addTiker"}, {"help"}};
					ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
					myT.sendMessage(curMsg.chat_id, "main menu", newKeyboard.toString());
				}else if(curMsg.text.equals("checkTiker")){
					String text = "Choose ticker...";
					String keys[][];
					HashMap<String, LinkedList<Tiker>> hmap = myStorage.getHmap();
					keys = new String[hmap.size() + 1][];
					keys[0] = new String[1];
					keys[0][0] = "back";
					int i = 1;
					for(Map.Entry entry : hmap.entrySet()){
						keys[i] = new String[1];
						keys[i++][0] = (String)entry.getKey();
					}
					ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
					myT.sendMessage(curMsg.chat_id, text, newKeyboard.toString());
				}else if(lastMsg.text.equals("checkTiker") && myStorage.getHmap().containsKey(curMsg.text)){
					HashMap<String, LinkedList<Tiker>> hmap = myStorage.getHmap();
					LinkedList<Tiker> list = hmap.get(curMsg.text);
					if(list != null){
						String text = "Choose date...";
						String keys[][];
						keys = new String[list.size() + 1][];
						keys[0] = new String[1];
						keys[0][0] = "back";
						int i = 1;
						for (Iterator<Tiker> iter = list.iterator(); iter.hasNext(); ) {
							Tiker element = iter.next();
							keys[i] = new String[1];
							keys[i++][0] = element.getDay();
						}
						ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
						myT.sendMessage(curMsg.chat_id, text, newKeyboard.toString());
					}
					else{
						String keys[][] = {{"update", "restart"}, {"checkTiker", "checkAllTickers"}, {"addTiker"}, {"help"}};
						ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
						myT.sendMessage(curMsg.chat_id, "No data!", newKeyboard.toString());
					}
				}else if(myStorage.getHmap().containsKey(lastMsg.text)){
					HashMap<String, LinkedList<Tiker>> hmap = myStorage.getHmap();
					LinkedList<Tiker> list = hmap.get(lastMsg.text);
					long volume = -1;
					if(list != null){
						String text = "";
						String keys[][] = {{"update", "restart"}, {"checkTiker", "checkAllTickers"}, {"addTiker"}, {"help"}};
						ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
						for (Iterator<Tiker> iter = list.iterator(); iter.hasNext(); ) {
							Tiker element = iter.next();
							if(element.getDay().equals(curMsg.text)){
								volume = element.getVol();
								break;
							}
						}
						text = "Volume: " + volume;
						myT.sendMessage(curMsg.chat_id, text, newKeyboard.toString());
					}else{
						String keys[][] = {{"update", "restart"}, {"checkTiker", "checkAllTickers"}, {"addTiker"}, {"help"}};
						ReplyKeyboardMarkup newKeyboard = new ReplyKeyboardMarkup(keys);
						myT.sendMessage(curMsg.chat_id, "No data!", newKeyboard.toString());
					}
				}
				lastMsg = curMsg;
			}
		}	
	}

	public Message getCommand(String message)
	{
		String msg_id = "";
		String chat_id = "";
		String text = "";
		msg_id = parseMsg(message, "message_id");
		chat_id = parseMsg(message, "\"id\"");
		text = parseText(message, "text");
		Message msg = new Message(msg_id, chat_id, text);
		return msg;
	}
	public String parseMsg(String message, String key)
	{
		String result = "";
		int begin = 0;
		int end = 0;
		begin = message.indexOf(key);
		if(begin != -1){
			begin = message.indexOf(":", begin);
			end = message.indexOf(",", begin);
			if(end != -1)
				if(begin < end)
					result = message.substring(begin + 1, end);
		}
		return result;
	}

	public String parseText(String message, String key)
	{
		String result = "";
		int begin = 0;
		int end = 0;
		begin = message.indexOf(key);
		if(begin != -1){
			begin = message.indexOf(":", begin);
			begin = message.indexOf("\"", begin);
			end = message.indexOf("\"", begin + 1);
			if(end != -1)
				if(begin < end)
					result = message.substring(begin + 1, end);
		}
		return result;
	}
}

class Message
{
	private String message_id;
	public String chat_id;
	public String text;

	public Message()
	{
		message_id = "";
		chat_id = "";
		text = "";
	}

	public Message(String msg, String chat, String t)
	{
		message_id = msg;
		chat_id = chat;
		text = t;
	}

	public boolean equals(Message other)
	{
		return message_id.equals(other.message_id);
	}
	public void show()
	{
		System.out.println(message_id);
		System.out.println(chat_id);
		System.out.println(text);
		System.out.println();
	}
}

class KeyboardButton{
	public String text = "";

	public KeyboardButton(String t)
	{
		text = t;
	}

	public String toString()
	{
		return text;
	}
}

class ReplyKeyboardMarkup
{
	public KeyboardButton[][] keyboard;

	public ReplyKeyboardMarkup(String[][] keys)
	{
		keyboard = new KeyboardButton[keys.length][];
		for(int i = 0; i < keys.length; i++)
			keyboard[i] = new KeyboardButton[keys[i].length];
		for(int i = 0; i < keys.length; i++){
			for(int j = 0; j < keys[i].length; j++){
				keyboard[i][j] = new KeyboardButton("\"" + keys[i][j] + "\"");
			}
		}
	}

	public String toString()
	{
		return "{\"keyboard\" : " + Arrays.deepToString(keyboard) + "}";
	}
}
import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Tiker
{
	private String day;
	private long volume;

	public Tiker(String day, long vol)
	{
		this.day = day;
		this.volume = vol;
	}

	public String getDay()
	{
		return day;
	}

	public long getVol()
	{
		return volume;
	}

	public void putVol(long vol)
	{
		volume = vol;
	}
}
package fi.tapiiri.software;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EventHandler implements HttpHandler
{
	private DbConnection mDbc;

	EventHandler(DbConnection dbc)
	{
		mDbc=dbc;
	}

	HashMap<String,String> parsePostParameters(InputStream b)
	{
		Scanner s=new Scanner(b);
		HashMap<String,String> post=new HashMap<String, String>();
		StringBuilder sb=new StringBuilder();
		String key=null;
		String val=null;
		String next=null;
		if(s.hasNext())
		{
			next=s.next();
		}
		s.close();

		int len=next.length();
		for(int i=0; i<len; ++i)
		{
			char nextchar=next.charAt(i);
			if(nextchar=='=')
			{
				key=sb.toString();
				sb.setLength(0);
			}
			else if(nextchar=='&')
			{
				val=sb.toString();
				post.put(key,val);
				sb.setLength(0);
				key=null;
				val=null;
			}
			else sb.append(nextchar);
		}
		if(key!=null)
		{
			val=sb.toString();
			post.put(key,val);
			sb.setLength(0);
			key=null;
			val=null;
		}

		return post;
	}

	public void handle(HttpExchange t)
	{
		if(t.getRequestMethod().equals("POST"))
		{
			HashMap<String,String> params=parsePostParameters(t.getRequestBody());
			mDbc.InsertEvent(Integer.parseInt(params.get("playerid")),
							Integer.parseInt(params.get("matchid")),
							Integer.parseInt(params.get("itemid")));
		}
	}
}

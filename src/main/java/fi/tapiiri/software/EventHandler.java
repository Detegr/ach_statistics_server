package fi.tapiiri.software;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class EventHandler implements HttpHandler
{
	private DbConnection mDbc;

	EventHandler(DbConnection dbc)
	{
		mDbc=dbc;
	}

	HashMap<String,String> parseParameters(InputStream b)
	{
		Scanner s=new Scanner(b);
		HashMap<String,String> params=new HashMap<String, String>();
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
				params.put(key,val);
				sb.setLength(0);
				key=null;
				val=null;
			}
			else sb.append(nextchar);
		}
		if(key!=null)
		{
			val=sb.toString();
			params.put(key,val);
			sb.setLength(0);
			key=null;
			val=null;
		}

		return params;
	}

	public void handle(HttpExchange t)
	{
		Headers headers = t.getResponseHeaders();
		headers.add("Content-Type", "application/json");

		if(t.getRequestMethod().equals("POST"))
		{
			HashMap<String,String> params=parseParameters(t.getRequestBody());
			ResultSet rs=mDbc.InsertEvent(Integer.parseInt(params.get("playerid")),
							Integer.parseInt(params.get("matchid")),
							Integer.parseInt(params.get("itemid")));
			String response=new String();
			try
			{
				response=JSONConverter.toJSON(rs).toString();
				t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
				t.getResponseBody().write(response.getBytes());
			}
			catch(SQLException e)
			{
				System.out.println(e);
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
			catch(JSONException e)
			{
				System.out.println(e);
			}
		}
	}
}

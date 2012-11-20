package fi.tapiiri.software;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class RequestHandler implements HttpHandler
{
	private DbConnection mDbc;
	private String mFrom;

	RequestHandler(DbConnection dbc, String from)
	{
		mDbc=dbc;
		mFrom=from;
	}
	public void handle(HttpExchange t)
	{
		String method=t.getRequestMethod();
		if(method.equals("GET"))
		{
			try
			{
				t.getRequestBody().read();
				Headers headers = t.getResponseHeaders();
				headers.add("Content-Type", "application/json");
				String response = new String();
				ResultSet rs=mDbc.Get("*", mFrom);
				response += JSONConverter.toJSON(rs);
				t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
				t.getResponseBody().write(response.getBytes());
				t.close();
			} catch(IOException e)
			{
				System.out.println(e.toString());
			} catch(SQLException e)
			{
				System.out.println(e.toString());
			} catch(JSONException e)
			{
				System.out.println(e.toString());
			}
		}
		else if(method.equals("POST"))
		{
			InputStream b=t.getRequestBody();
			java.util.Scanner s=new java.util.Scanner(b);
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
		}
	}
}

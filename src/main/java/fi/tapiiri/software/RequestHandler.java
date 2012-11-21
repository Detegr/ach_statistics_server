package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		if(t.getRequestMethod().equals("GET"))
		{
			try
			{
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
	}
}

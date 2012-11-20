package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class PlayerHandler implements HttpHandler
{
	public void handle(HttpExchange t)
	{
		try
		{
			t.getRequestBody().read();
			Headers headers = t.getResponseHeaders();
			headers.add("Content-Type", "application/json");
			String response = new String();
			DbConnection dbc=DbConnection.Connect("jdbc:postgresql://localhost:5432/achdb");
			ResultSet rs=dbc.Get("*", "player");
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

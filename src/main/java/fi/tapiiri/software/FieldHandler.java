package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class FieldHandler implements HttpHandler
{
	public void handle(HttpExchange t)
	{
		try
		{
			t.getRequestBody().read();
			Headers headers = t.getResponseHeaders();
			headers.add("Content-Type", "text/html");
			String response = "<!DOCTYPE html><html><head></head><body><ul>";
			DbConnection dbc=DbConnection.Connect("jdbc:postgresql://localhost:5432/achdb");
			ResultSet rs=dbc.Get("*", "field");
			try
			{
				response+="<ul>";
				while(rs.next())
				{
					response += "<li>" + rs.getString("name") + "</li>";
				}
				response+="</ul>";
			}
			catch(SQLException e)
			{
			}
			response += "</body></html>";
			t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
			t.getResponseBody().write(response.getBytes());
			t.close();
		} catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
}

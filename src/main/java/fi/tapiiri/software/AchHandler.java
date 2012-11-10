package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AchHandler implements HttpHandler
{
	public void handle(HttpExchange t)
	{
		try
		{
			t.getRequestBody().read();
			String response = "<!DOCTYPE html><body>response</body></html>";
			t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			t.getResponseBody().write(response.getBytes());
			t.close();
		} catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
}

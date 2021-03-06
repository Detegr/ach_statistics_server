package fi.tapiiri.software;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class AchServer
{
	private static DbConnection dbc=null;
	public static void main(String[] args)
	{
		dbc=DbConnection.Connect("jdbc:postgresql://localhost:5432/achdb");
		try
		{
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/", new EventHandler(dbc));
			server.createContext("/delete", new DeleteHandler(dbc));
			server.createContext("/players", new RequestHandler(dbc, "player"));
			server.createContext("/matches", new RequestHandler(dbc, "match"));
			server.createContext("/items", new RequestHandler(dbc, "statistics_item"));
			server.createContext("/events", new RequestHandler(dbc, "statistics_event"));
			server.setExecutor(null);
			server.start();
		} catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
}

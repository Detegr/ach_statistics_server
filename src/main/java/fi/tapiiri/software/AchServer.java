package fi.tapiiri.software;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class AchServer
{
	public static void main(String[] args)
	{
		try
		{
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/", new FieldHandler());
			server.setExecutor(null);
			server.start();
		} catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
}

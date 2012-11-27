package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DeleteHandler implements HttpHandler
{
	private DbConnection mDbc;

	DeleteHandler(DbConnection dbc)
	{
		mDbc=dbc;
	}

	private void sendResponse(HttpExchange t, boolean value)
	{
		JSONObject responseobject=new JSONObject();
		String response=new String();
		try
		{
			try
			{
				responseobject.put("response", value);
				response=responseobject.toString();
				t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
			}
			catch(JSONException e) {}
			t.getResponseBody().write(response.getBytes());
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	public void handle(HttpExchange t)
	{
		if(t.getRequestMethod().equals("DELETE"))
		{
			String[] parts=t.getRequestURI().getPath().split("/");
			if(parts.length != 5) // First part before / is empty
			{
				sendResponse(t, false);
				return;
			}
			boolean ok=mDbc.DeleteEvent(Integer.parseInt(parts[2]),
							Integer.parseInt(parts[3]),
							Integer.parseInt(parts[4]));
			sendResponse(t, ok);
		}
	}
}

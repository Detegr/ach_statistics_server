package fi.tapiiri.software;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class PlayerHandler implements HttpHandler
{
	public void handle(HttpExchange t)
	{
		try
		{
			JSONArray arr = new JSONArray();
			t.getRequestBody().read();
			Headers headers = t.getResponseHeaders();
			headers.add("Content-Type", "application/json");
			String response = new String();
			DbConnection dbc=DbConnection.Connect("jdbc:postgresql://localhost:5432/achdb");
			ResultSet rs=dbc.Get("*", "player");
			ResultSetMetaData md = rs.getMetaData();
			int columns=md.getColumnCount();
			while(rs.next())
			{
				JSONObject o = new JSONObject();
				for(int i=1; i<columns+1; ++i)
				{
					String column=md.getColumnName(i);
					switch(md.getColumnType(i))
					{
						case java.sql.Types.ARRAY:
							o.put(column, rs.getArray(i));
							break;
						case java.sql.Types.BIGINT:
							o.put(column, rs.getInt(i));
							break;
						case java.sql.Types.BOOLEAN:
							o.put(column, rs.getBoolean(i));
							break;
						case java.sql.Types.BLOB:
							o.put(column, rs.getBlob(i));
							break;
						case java.sql.Types.DOUBLE:
							o.put(column, rs.getDouble(i));
							break;
						case java.sql.Types.FLOAT:
							o.put(column, rs.getFloat(i));
							break;
						case java.sql.Types.INTEGER:
							o.put(column, rs.getInt(i));
							break;
						case java.sql.Types.NVARCHAR:
							o.put(column, rs.getNString(i).trim());
							break;
						case java.sql.Types.VARCHAR:
							o.put(column, rs.getString(i).trim());
							break;
						case java.sql.Types.TINYINT:
							o.put(column, rs.getInt(i));
							break;
						case java.sql.Types.SMALLINT:
							o.put(column, rs.getInt(i));
							break;
						case java.sql.Types.DATE:
							o.put(column, rs.getDate(i));
							break;
						case java.sql.Types.TIMESTAMP:
							o.put(column, rs.getTimestamp(i));
							break;
						default:
							o.put(column, rs.getObject(i));
							break;
					}
				}
				arr.put(o);
			}
			JSONObject responseobject=new JSONObject();
			responseobject.put("response", arr);
			response += responseobject.toString();
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

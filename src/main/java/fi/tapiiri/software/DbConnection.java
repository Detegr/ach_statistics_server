package fi.tapiiri.software;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection
{
	private Connection mConnection;

	private DbConnection()
	{
		mConnection=null;
	}

	/**
	 * Initiates connection to the database
	 * @param connectionstring JDBC-style connection string to the database
	 * @return DbConnection object that can be used to read and write the database
	 */
	public static DbConnection Connect(String connectionstring)
	{
		DbConnection c=new DbConnection();
		try
		{
			c.mConnection = DriverManager.getConnection(connectionstring);
		}
		catch(SQLException e)
		{
			System.out.println("Unable to get connection to the database: " + e.toString());
		}
		
		return c;
	}

	/**
	 * Reads the database
	 * @param property Property to read
	 * @param from Table to read
	 * @return Result set of the query
	 */
	public ResultSet Get(String property, String from)
	{
		String[] arr={property};
		return Get(arr, from);
	}

	/**
	 * Reads the database
	 * @param properties Properties to read
	 * @param from Table to read
	 * @return Result set of the query
	 */
	public ResultSet Get(String[] properties, String from)
	{
		String query="SELECT ";
		int len=properties.length;
		for(int i=0; i<len-1; ++i) query += properties[i] + ", ";
		query += properties[len-1] + " FROM " + from;

		ResultSet rs=null;
		try
		{
			PreparedStatement ps=mConnection.prepareStatement(query);
			System.out.println(query);
			rs=ps.executeQuery();
		}
		catch(SQLException e)
		{
			System.out.println("Failed to execute query: " + e.toString());
		}
		return rs;
	}

	public boolean InsertEvent(int playerid, int matchid, int itemid)
	{
		String query="INSERT INTO statistics_event (player_id, match_id, item_id) VALUES(?,?,?)";
		try
		{
			PreparedStatement ps=mConnection.prepareStatement(query);
			ps.setInt(1, playerid);
			ps.setInt(2, matchid);
			ps.setInt(3, itemid);
			int rows=ps.executeUpdate();
			return rows>0;
		}
		catch(SQLException e)
		{
			System.out.println("Failed to execute query: " + e.toString());
		}
		return false;
	}

	public boolean DeleteEvent(int playerid, int matchid, int itemid)
	{
		String query="DELETE FROM statistics_event WHERE player_id=? AND match_id=? AND item_id=?";
		try
		{
			PreparedStatement ps=mConnection.prepareStatement(query);
			ps.setInt(1, playerid);
			ps.setInt(2, matchid);
			ps.setInt(3, itemid);
			int rows=ps.executeUpdate();
			return rows>0;
		}
		catch(SQLException e)
		{
			System.out.println("Failed to execute query: " + e.toString());
		}
		return false;
	}
}

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
}

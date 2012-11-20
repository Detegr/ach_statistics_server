package fi.tapiiri.software;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONConverter
{
	public static JSONObject toJSON(ResultSet rs) throws SQLException, JSONException
	{
		JSONArray arr = new JSONArray();
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
		JSONObject ret=new JSONObject();
		ret.put("response", arr);

		return ret;
	}
}

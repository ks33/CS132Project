package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class QueryDB {
	private Connection conn;
	String columnName[];
	
	public QueryDB(String pathToDatabase) throws Exception{
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + pathToDatabase);	
		String sqlStatement = "SELECT * FROM LapseTable";
		PreparedStatement stat = conn.prepareStatement(sqlStatement);
		ResultSet rs = stat.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		columnName = new String[count];
		for (int i = 1; i<= count; i++){
			columnName[i-1] = metaData.getColumnLabel(i);
		}
	}
	
	public ResultSet avgQuery() throws SQLException{
		String sqlStatement = "SELECT ";
		for (int i = 1; i<columnName.length-1; i++){
			System.out.println("columName: " + i + ": " + columnName[i]);
			sqlStatement = sqlStatement + "AVG(" + columnName[i] + ")";
			if (i!=(columnName.length-2)){
				sqlStatement = sqlStatement + ",";
			}
			sqlStatement = sqlStatement + " ";
		}
		sqlStatement = sqlStatement + "FROM LapseTable";
		System.out.println("sql statement is: " + sqlStatement);
		PreparedStatement stat = conn.prepareStatement(sqlStatement);
		ResultSet rs = stat.executeQuery();
		System.out.println(rs);
		return rs;
	}
}

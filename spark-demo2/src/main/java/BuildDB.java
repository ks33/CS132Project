package main.java;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;

public class BuildDB
{

	public static void createDB()throws Exception
	{
		
		String CSV_FILE = "Data.csv";
		String DB_FILE = "Database.db";
		String createTableSQL = "CREATE TABLE LapseTable("
				+ "ID int AUTOINCREMENT,"
				+ "tenure DOUBLE PRECISION,"
				+ "is30dPayer DOUBLE PRECISION,"
				+ "paymentAmount30d DOUBLE PRECISION," 
				+ "engmtLastWk DOUBLE PRECISION,"
				+ "engmtWk_1 DOUBLE PRECISION,"
				+ "engmtWk_2 DOUBLE PRECISION,"
				+ "engmtWk_3 DOUBLE PRECISION,"
				+ "engmt28d DOUBLE PRECISION,"
				+ "deltaEngmt4wk DOUBLE PRECISION,"
				+ "socialActionsReceivedLastWk DOUBLE PRECISION,"
				+ "socialActorsReceivedLastWk DOUBLE PRECISION,"
				+ "socialActionsSentLastWk DOUBLE PRECISION,"
				+ "socialActorsSentLastWk DOUBLE PRECISION,"
				+ "daysSinceLastSend DOUBLE PRECISION,"
				+ "daysSinceLastReceive DOUBLE PRECISION,"
				+ "trueLapse VARCHAR(50));";
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
		
		Statement stat = conn.createStatement();
		stat.executeUpdate(createTableSQL);
		System.out.println("Made Table");
		stat.executeUpdate("PRAGMA synchronous = OFF;");
		stat.executeUpdate("PRAGMA journal_mode = MEMORY;");
		
		
		String insertdata = "INSERT OR IGNORE INTO LapseTable" +
				" (tenure, is30dPayer, paymentAmount30d, engmtLastWk, engmtWk_1, engmtWk_2, engmtWk_3, engmt28d, deltaEngmt4wk, socialActionsReceivedLastWk, socialActorsReceivedLastWk, socialActionsSentLastWk, socialActorsSentLastWk, daysSinceLastSend, daysSinceLastReceive, trueLapse)" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prep = conn.prepareStatement(insertdata);
		CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
		boolean firstline = true;
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
				if(firstline){
					nextLine = reader.readNext();
					firstline = false;
				}
				 prep.setDouble(1, Double.parseDouble(nextLine[0]));
				 prep.setDouble(2, Double.parseDouble(nextLine[1]));
				 prep.setDouble(3, Double.parseDouble(nextLine[2]));
				 prep.setDouble(4, Double.parseDouble(nextLine[3]));
				 prep.setDouble(5, Double.parseDouble(nextLine[4]));
				 prep.setDouble(6, Double.parseDouble(nextLine[5]));
				 prep.setDouble(7, Double.parseDouble(nextLine[6]));
				 prep.setDouble(8, Double.parseDouble(nextLine[7]));
				 prep.setDouble(9, Double.parseDouble(nextLine[8]));
				 prep.setDouble(10, Double.parseDouble(nextLine[9]));
				 prep.setDouble(11, Double.parseDouble(nextLine[10]));
				 prep.setDouble(12, Double.parseDouble(nextLine[11]));
				 prep.setDouble(13, Double.parseDouble(nextLine[12]));
				 prep.setDouble(14, Double.parseDouble(nextLine[13]));
				 prep.setDouble(15, Double.parseDouble(nextLine[14]));
				 prep.setString(16, nextLine[15]);
				 prep.addBatch(); 	

		 }
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);		
		reader.close();
		conn.close();
	}

}
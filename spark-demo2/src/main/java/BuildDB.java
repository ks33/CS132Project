package main.java;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVReader;

public class BuildDB
{

	public static void createDB()throws Exception
	{
		
		String CSV_FILE = "Data.csv";
		String DB_FILE = "Database.db";
		Scanner sc = new Scanner(new FileReader(new File("Data.csv")));
		String attributes = "";
		if(sc.hasNext())
			attributes = sc.nextLine();
		attributes = attributes.replaceAll("-", "_");
		List<String> attributesList = Arrays.asList(attributes.split(","));
		String createTableSQL = "CREATE TABLE LapseTable("
				+ "ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		for (int i = 0; i < attributesList.size() - 1; i++)
		{
			createTableSQL = createTableSQL + attributesList.get(i) + " DOUBLE PRECISION, ";
		}
		createTableSQL = createTableSQL + attributesList.get(attributesList.size()-1) + " VARCHAR(50));";
				System.out.println(createTableSQL);
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
		
		Statement stat = conn.createStatement();
		stat.executeUpdate(createTableSQL);
		System.out.println("Made Table");
		stat.executeUpdate("PRAGMA synchronous = OFF;");
		stat.executeUpdate("PRAGMA journal_mode = MEMORY;");
		
		
		String values = "";
		String attrString = "";
		for (int i = 0; i < attributesList.size()-1; i++)
		{
			attrString = attrString + attributesList.get(i) + ", ";
			values = values + "?, ";
		}
		attrString = attrString + attributesList.get(attributesList.size()-1);
		values = values + "?";
		String insertData = "INSERT OR IGNORE INTO LapseTable (" + attrString + ") VALUES (" + values+")";

		PreparedStatement prep = conn.prepareStatement(insertData);
		CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
		boolean firstline = true;
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
				if(firstline){
					nextLine = reader.readNext();
					firstline = false;
				}
				int i = 0;
				for (i = 0; i < attributesList.size()-1; i++)
					prep.setDouble(i+1, Double.parseDouble(nextLine[i]));
				 prep.setString(i+1, nextLine[i]);
				 prep.addBatch(); 	
		 }
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);		
		reader.close();
		conn.close();
	}

}
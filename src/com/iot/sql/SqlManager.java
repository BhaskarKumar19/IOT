package com.iot.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlManager {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/test";
	static final String USER = "ODBC@localhost";
	static final String PASS = "";
	private static PreparedStatement preparedStatement = null;

	public void updateSqlTable(String switchId) {
		Connection conn = null;
		Statement stmt = null;
		int id = 6;
		int age = 29;
		String first = "test";
		String last = "entry";

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			preparedStatement = conn
					.prepareStatement("insert into  emp (id, first, last, age) values (?, ?, ?, ?)");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, switchId);
			preparedStatement.setString(3, last);
			preparedStatement.setInt(4, age);
			preparedStatement.executeUpdate();

			String sql = "SELECT id, first, last, age FROM emp";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt("id");
				age = rs.getInt("age");
				first = rs.getString("first");
				last = rs.getString("last");

				System.out.print("ID: " + id);
				System.out.print(", Age: " + age);
				System.out.print(", First: " + first);
				System.out.println(", Last: " + last);
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}
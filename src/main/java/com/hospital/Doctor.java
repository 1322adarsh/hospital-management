package com.hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

	private Connection connection;

	public Doctor(Connection connection) {
		this.connection = connection;
	}

//	To view the doctors list
	public void viewDoctors() {
		String query = "SELECT * FROM doctors";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet resultSet = pstmt.executeQuery();
			System.out.println("Doctors: ");
			System.out.println("+------------+--------------------+------------------+");
			System.out.println("| Doctor Id  | Name               | Specialization   |");
			System.out.println("+------------+--------------------+------------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String specialization = resultSet.getString("specialization");
				System.out.printf("| %-10s | %-18s | %-16s |\n".formatted(id, name, specialization));
				System.out.println("+------------+--------------------+------------------+");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	It returns true/false if the particular doctor is present in the list or not
	public boolean getDoctorById(int id) {
		String query = "SELECT * FROM doctors WHERE id = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, id);
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}

package com.hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

//	add new patient in the database by users
	public void addPatient() {
		System.out.println("Enter Patient Name: ");
		String name = scanner.next();
		System.out.println("Enter Patient Age: ");
		int age = scanner.nextInt();
		System.out.println("Enter Patient Gender: ");
		String gender = scanner.next();

		String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
		int rowsAffected;
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, name);
			pstmt.setInt(2, age);
			pstmt.setString(3, gender);
			rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0)
				System.out.println("Patient Added Successfully!!");
			else
				System.out.println("Failed to add Patient!!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	view patients list(id, name, age, gender)
	public void viewPatients() {
		String query = "SELECT * FROM patients";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet resultSet = pstmt.executeQuery();
			System.out.println("Patients: ");
			System.out.println("+------------+--------------------+--------+-----------+");
			System.out.println("| Patient Id | Name               | Age	   | Gender    |");
			System.out.println("+------------+--------------------+--------+-----------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				System.out.printf("| %-10s | %-18s | %-6s | %-9s |\n", id, name, age, gender);
				System.out.println("+------------+--------------------+--------+-----------+");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	return true/false if the patient is registered in the database by their id
	public boolean getPatientById(int id) {
		String query = "SELECT * FROM patients WHERE id = ?";
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

package com.hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagement {
	private static final String url = "jdbc:mysql://localhost:3306/hospitalmanagement";
	private static final String username = "root";
	private static final String password = "Adarsh@1322#";

	public static void main(String[] args) {
		// -> I didn't call driver here because with the help of maven dependency it will be
		// automatically done

//		Scanner object to input by the users during runtime
		Scanner scanner = new Scanner(System.in);

		try {
			// creates a connection to the database to save and retrieve data from the database
			Connection connection = DriverManager.getConnection(url, username, password); 

			Patient patient = new Patient(connection, scanner);

			Doctor doctor = new Doctor(connection);
			while (true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter Your Choice: ");
				int choice = scanner.nextInt();

				switch (choice) {
				case 1:
					// Add Patient
					patient.addPatient();
					System.out.println();
					break;
				case 2:
					// View Patients
					patient.viewPatients();
					System.out.println();
					break;
				case 3:
					// View Doctors
					doctor.viewDoctors();
					System.out.println();
					break;
				case 4:
					// Book Appointment
					bookAppointment(patient, doctor, connection, scanner);
					System.out.println();
					break;
				case 5:
					// Exit
					System.out.println("THANK YOU!  For Using Hospital Management System!!!");
					return;
				default:
					System.out.println("Enter Valid Choice!!!");
					break;
				}
			}

		} catch (SQLException e) {
		}

	}

//	method to book appointment if doctor is available at that particular date.
	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.println("Enter Patient Id: ");
		int patientId = scanner.nextInt();
		System.out.println("Enter Doctor Id: ");
		int doctorId = scanner.nextInt();
		System.out.println("Enter Appointment Date (YYYY-MM-DD: ");
		String appointmentDate = scanner.next();
		if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
			if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
				String appointmentQuery = "INSERT INTO appointments(patient_id, doctors_id, appointment_date) VALUES(?, ?, ?)";
				try {
					PreparedStatement pstmt = connection.prepareStatement(appointmentQuery);
					pstmt.setInt(1, patientId);
					pstmt.setInt(2, doctorId);
					pstmt.setString(3, appointmentDate);
					int rowsAffected = pstmt.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Appointment Booked!!");
					} else {
						System.out.println("Failed To Book Appointment!!!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Doctor not available on this date!!");
			}
		} else {
			System.out.println("Either doctor or patient doesn't exist!!!");
		}
	}

//	method to check that doctor is available or not at that particular date.
	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id = ? AND appointment_date = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, doctorId);
			pstmt.setString(2, appointmentDate);
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count == 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

package progexec;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class StaffManagementApp extends Application {

    // Database connection details
    TextField tfUrl = new TextField("jdbc:mysql://localhost:3306/javatestdb");
    TextField tfUser = new TextField("root");
    PasswordField pfPassword = new PasswordField(); // Leave blank for default XAMPP setup


    // UI Components
    private TextField tfId = new TextField();
    private TextField tfLastName = new TextField();
    private TextField tfFirstName = new TextField();
    private TextField tfMi = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfTelephone = new TextField();
    private TextField tfEmail = new TextField();

    @Override
    public void start(Stage primaryStage) {
        // Create a GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add labels and text fields to the grid
        gridPane.add(new Label("ID:"), 0, 0);
        gridPane.add(tfId, 1, 0);
        gridPane.add(new Label("Last Name:"), 0, 1);
        gridPane.add(tfLastName, 1, 1);
        gridPane.add(new Label("First Name:"), 0, 2);
        gridPane.add(tfFirstName, 1, 2);
        gridPane.add(new Label("MI:"), 0, 3);
        gridPane.add(tfMi, 1, 3);
        gridPane.add(new Label("Address:"), 0, 4);
        gridPane.add(tfAddress, 1, 4);
        gridPane.add(new Label("City:"), 0, 5);
        gridPane.add(tfCity, 1, 5);
        gridPane.add(new Label("State:"), 0, 6);
        gridPane.add(tfState, 1, 6);
        gridPane.add(new Label("Telephone:"), 0, 7);
        gridPane.add(tfTelephone, 1, 7);
        gridPane.add(new Label("Email:"), 0, 8);
        gridPane.add(tfEmail, 1, 8);

        // Buttons
        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");

        // Add buttons to the grid
        gridPane.add(btnView, 0, 9);
        gridPane.add(btnInsert, 1, 9);
        gridPane.add(btnUpdate, 2, 9);

        // Button Actions
        btnView.setOnAction(e -> viewRecord());
        btnInsert.setOnAction(e -> insertRecord());
        btnUpdate.setOnAction(e -> updateRecord());

        // Set the scene and show the stage
        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setTitle("Staff Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // View a record by ID
    private void viewRecord() {
        String id = tfId.getText();

        String query = "SELECT * FROM Staff WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tfLastName.setText(resultSet.getString("lastName"));
                tfFirstName.setText(resultSet.getString("firstName"));
                tfMi.setText(resultSet.getString("mi"));
                tfAddress.setText(resultSet.getString("address"));
                tfCity.setText(resultSet.getString("city"));
                tfState.setText(resultSet.getString("state"));
                tfTelephone.setText(resultSet.getString("telephone"));
                tfEmail.setText(resultSet.getString("email"));
            } else {
                showAlert("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a new record
    private void insertRecord() {
        String query = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tfId.getText());
            preparedStatement.setString(2, tfLastName.getText());
            preparedStatement.setString(3, tfFirstName.getText());
            preparedStatement.setString(4, tfMi.getText());
            preparedStatement.setString(5, tfAddress.getText());
            preparedStatement.setString(6, tfCity.getText());
            preparedStatement.setString(7, tfState.getText());
            preparedStatement.setString(8, tfTelephone.getText());
            preparedStatement.setString(9, tfEmail.getText());

            preparedStatement.executeUpdate();
            showAlert("Record inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing record
    private void updateRecord() {
        String query = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tfLastName.getText());
            preparedStatement.setString(2, tfFirstName.getText());
            preparedStatement.setString(3, tfMi.getText());
            preparedStatement.setString(4, tfAddress.getText());
            preparedStatement.setString(5, tfCity.getText());
            preparedStatement.setString(6, tfState.getText());
            preparedStatement.setString(7, tfTelephone.getText());
            preparedStatement.setString(8, tfEmail.getText());
            preparedStatement.setString(9, tfId.getText());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Record updated successfully!");
            } else {
                showAlert("No record found with ID: " + tfId.getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Utility method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

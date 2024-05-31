/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbiblioteca;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Clases.Prestamo;

public class PrestamosController implements Initializable {
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtISBN;
    @FXML
    private TextField txtFechaVencimiento;
    @FXML
    private TextField txtIdPrestamo;
    @FXML
    private TableView<Object[]> tablaPrestamos;

    private Prestamo objetoPrestamo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        objetoPrestamo = new Prestamo();
        objetoPrestamo.mostrarPrestamos(tablaPrestamos);
    }
@FXML
private void agregarPrestamo(ActionEvent event) {
    objetoPrestamo.agregarPrestamo(txtIdCliente, txtISBN, txtFechaVencimiento);
    objetoPrestamo.mostrarPrestamos(tablaPrestamos);
}

@FXML
private void devolverPrestamo(ActionEvent event) {
    objetoPrestamo.devolverPrestamo(txtIdPrestamo);
    mostrarPrestamos();
}

private void mostrarPrestamos() {
    tablaPrestamos.getItems().clear();
    objetoPrestamo.mostrarPrestamos(tablaPrestamos);
}


    @FXML
    private void volver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Principal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Biblioteca");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



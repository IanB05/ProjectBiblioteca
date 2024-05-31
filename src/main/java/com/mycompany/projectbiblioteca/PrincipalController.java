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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Clases.Prestamo;

public class PrincipalController implements Initializable {
    @FXML
    private TextField txtISBN;
    @FXML
    private TextField txtautor;
    @FXML
    private TextField txtanopublicacion;
    @FXML
    private TextField txtcantidad;
    @FXML
    private TextField txteditorial;
    @FXML
    private TextField txttitulo;
    @FXML
    private TableView<Object[]> tablalibro;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtFechaVencimiento;
    @FXML
    private TextField txtIdPrestamo;

    @FXML
private TextField txtBuscar;
    private Clases.Libro objetoLibro;
    private Clases.Prestamo objetoPrestamo;
    private Clases.CConnection objetoConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        objetoLibro = new Clases.Libro();
        objetoPrestamo = new Clases.Prestamo();
        objetoConexion = new Clases.CConnection();
        objetoLibro.MostrarLibros(tablalibro);
    }


@FXML
public void buscarLibro(ActionEvent event) {
    String criterio = txtBuscar.getText();
    if (criterio.isEmpty()) {
        objetoLibro.MostrarLibros(tablalibro); // Mostrar todos los libros si el campo de búsqueda está vacío
    } else {
        objetoLibro.buscarLibros(criterio, tablalibro);
    }
}

    
    @FXML
    private void irAClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Clientes.fxml"));
            Parent root = loader.load();

            ClientesController clientesController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Clientes");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void guardarlibro(ActionEvent event) {
        objetoLibro.AgregarLibro(txtISBN, txttitulo, txtautor, txtanopublicacion, txteditorial, txtcantidad);
        tablalibro.getColumns().clear();
        tablalibro.getItems().clear();
        objetoLibro.MostrarLibros(tablalibro);
    }

    @FXML
    public void modificarLibro(ActionEvent event) {
        objetoLibro.ModificarLibro(txtISBN, txttitulo, txtautor, txtanopublicacion, txteditorial, txtcantidad);
        tablalibro.getColumns().clear();
        tablalibro.getItems().clear();
        objetoLibro.MostrarLibros(tablalibro);
    }

    @FXML
    public void eliminarLibro(ActionEvent event) {
        objetoLibro.EliminarLibro(txtISBN);
        txtISBN.clear();
        txttitulo.clear();
        txtautor.clear();
        txtanopublicacion.clear();
        txteditorial.clear();
        txtcantidad.clear();
        tablalibro.getColumns().clear();
        tablalibro.getItems().clear();
        objetoLibro.MostrarLibros(tablalibro);
    }

    @FXML
    private void seleccionarLibro(MouseEvent event) {
        objetoLibro.seleccionarLibro(tablalibro, txtISBN, txttitulo, txtautor, txtanopublicacion, txteditorial, txtcantidad);
    }

    @FXML
    public void agregarPrestamo(ActionEvent event) {
        objetoPrestamo.agregarPrestamo(txtIdCliente, txtISBN, txtFechaVencimiento);
    }

    @FXML
    public void devolverPrestamo(ActionEvent event) {
        objetoPrestamo.devolverPrestamo(txtIdPrestamo);
                }

    @FXML
    private void irAPrestamos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Prestamos.fxml"));
            Parent root = loader.load();

            PrestamosController prestamosController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Préstamos");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbiblioteca;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ClientesController implements Initializable {
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private CheckBox chkMulta;
    @FXML
    private TableView<Object[]> tablaClientes;

    @FXML
private TextField txtBuscar;
    private Clases.Cliente objetoCliente;
    private Clases.CConnection objetoConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     objetoCliente = new Clases.Cliente() ;
   objetoConexion = new Clases.CConnection();
        actualizarTablaClientes();
    }



    
    
    @FXML
    public void guardarCliente(ActionEvent event) {
    objetoCliente.AgregarCliente(txtIdCliente, txtNombre, txtDireccion, txtTelefono);
        limpiarCampos();
       actualizarTablaClientes();
    }
@FXML
public void modificarCliente(ActionEvent event) {
    if (txtIdCliente.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
        showAlert("Error", "Por favor, complete todos los campos.");
        return;
    }

 objetoCliente.modificarCliente(txtIdCliente, txtNombre, txtDireccion, txtTelefono, chkMulta);
  
    limpiarCampos();
    actualizarTablaClientes();
}

    @FXML
    public void eliminarCliente(ActionEvent event) {
        objetoCliente.eliminarCliente(Integer.parseInt(txtIdCliente.getText()));
        
        limpiarCampos();
        
        actualizarTablaClientes(); 
    }

    @FXML
    private void seleccionarCliente(MouseEvent event) {
objetoCliente.seleccionarCliente(tablaClientes, txtIdCliente, txtNombre, txtDireccion, txtTelefono, chkMulta);
    }

  private void limpiarCampos() {
       txtIdCliente.clear();
       txtNombre.clear();
         txtDireccion.clear();
         txtTelefono.clear();
        chkMulta.setSelected(false);
    }

    private void actualizarTablaClientes() {
        tablaClientes.getItems().clear(); 
        objetoCliente.mostrarClientes(tablaClientes); 
    }

@FXML
public void buscarCliente(ActionEvent event) {
    String criterio = txtBuscar.getText();
    if (criterio.isEmpty()) {
        objetoCliente.mostrarClientes(tablaClientes); // Mostrar todos los libros si el campo de búsqueda está vacío
    } else {
        objetoCliente.buscarClientes(criterio, tablaClientes);
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



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Statement;

public class Libro implements Serializable {

    private String ISBN;
    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String editorial;
    private int cantidad;
    
    public String getISBN() { 
        return ISBN; }
    
    public void setISBN(String ISBN) { 
        this.ISBN = ISBN; }

    public String getTitulo() { 
        return titulo; }
    public void setTitulo(String titulo) { 
        this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAñoPublicacion() { 
        return añoPublicacion; } 
    
    public void setAñoPublicacion(int añoPublicacion) { 
        this.añoPublicacion = añoPublicacion; }

    public String getEditorial() { return editorial; }
    
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getCantidad() { return cantidad; }
    
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public Libro() {
    }

    public void AgregarLibro(TextField isbn, TextField titulo, TextField autor, TextField añoPublicacion, TextField editorial, TextField cantidad) {
        CConnection objetoConexion = new CConnection();
        String consulta = "INSERT INTO Libros (isbn, titulo, autor, año_publicacion, editorial, cantidad_disponible) VALUES (?, ?, ?, ?, ?, ?)";
        try {
         int cantidadValue = Integer.parseInt(cantidad.getText());
        if (cantidadValue < 0) {
           showAlert("Error", "La cantidad no puede ser menor a 0");
            return;
            }
            try (CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta)) {
     cs.setString(1, isbn.getText());
     cs.setString(2, titulo.getText());
     cs.setString(3, autor.getText());
     cs.setInt(4, Integer.parseInt(añoPublicacion.getText()));
     cs.setString(5, editorial.getText());
     cs.setInt(6, cantidadValue);
               cs.execute();

    try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("Libros.dat", true))) {
                    Libro libro = new Libro();
                    libro.setISBN(isbn.getText());
                    libro.setTitulo(titulo.getText());
                    libro.setAutor(autor.getText());
                    libro.setAñoPublicacion(Integer.parseInt( añoPublicacion.getText())); 
                    libro.setEditorial(editorial.getText());
                    libro.setCantidad(cantidadValue);
                    salida.writeObject(libro);
  }

          showAlert("Información", "Libro registrado correctamente");
            } 
        } catch (Exception e) {
        showAlert("Error", "Error al registrar el libro: " + e.toString());
        } finally {
        objetoConexion.cerrarConexion();
        }
    }
    
    public void ModificarLibro(TextField isbn, TextField titulo, TextField autor, TextField añoPublicacion, TextField editorial, TextField cantidad) {
        CConnection objetoConexion = new CConnection();
        String consulta = "UPDATE Libros SET titulo = ?, autor = ?, año_publicacion = ?, editorial = ?, cantidad_disponible = ? WHERE isbn = ?";
        try {
     int cantidadValue = Integer.parseInt(cantidad.getText());
      if (cantidadValue < 0) {
         showAlert("Error", "La cantidad no puede ser menor a 0");
        return;
         }
    try (CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta)) {
         cs.setString(1, titulo.getText());
          cs.setString(2, autor.getText());
          cs.setInt(3, Integer.parseInt(añoPublicacion.getText())); // Cambiado a int
         cs.setString(4, editorial.getText());
          cs.setInt(5, cantidadValue);
          cs.setString(6, isbn.getText());
          cs.execute();

       showAlert("Información", "Libro modificado correctamente");
     }
    } catch (Exception e) {
     showAlert("Error", "Error al modificar el libro: " + e.toString());
   } finally {
      objetoConexion.cerrarConexion();
      }
    }

    public void EliminarLibro(TextField isbn) {
     CConnection objetoConexion = new CConnection();
    String consulta = "DELETE FROM Libros WHERE isbn = ?";

   try (CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta)) {
       cs.setString(1, isbn.getText());
      cs.execute();

      showAlert("Información", "Libro eliminado correctamente");
   } catch (Exception e) {
      showAlert("Error", "Error al eliminar el libro: " + e.toString());
        } finally {
    objetoConexion.cerrarConexion();
       }
    }

    public void buscarLibros(String criterio, TableView<Object[]> tableView) {
    CConnection objetoConexion = new CConnection();
    String consulta = "SELECT * FROM Libros WHERE isbn LIKE ? OR titulo LIKE ?";
    try (PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta)) {
        ps.setString(1, "%" + criterio + "%");
        ps.setString(2, "%" + criterio + "%");
        try (ResultSet rs = ps.executeQuery()) {
            tableView.getColumns().clear();
            tableView.getItems().clear();
            TableColumn<Object[], String> colISBN = new TableColumn<>("ISBN");
            colISBN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].toString()));
            TableColumn<Object[], String> colTitulo = new TableColumn<>("Título");
            colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].toString()));
            TableColumn<Object[], String> colAutor = new TableColumn<>("Autor");
            colAutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].toString()));
            TableColumn<Object[], String> colAno = new TableColumn<>("Año de Publicación");
            colAno.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].toString()));
            TableColumn<Object[], String> colEditorial = new TableColumn<>("Editorial");
            colEditorial.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4].toString()));
            TableColumn<Object[], String> colCantidad = new TableColumn<>("Cantidad Disponible");
            colCantidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5].toString()));
            tableView.getColumns().addAll(colISBN, colTitulo, colAutor, colAno, colEditorial, colCantidad);
            while (rs.next()) {
         Object[] row = {
      rs.getString("isbn"),
     rs.getString("titulo"),
     rs.getString("autor"),
      rs.getInt("año_publicacion"),
       rs.getString("editorial"),
       rs.getInt("cantidad_disponible")  };
   tableView.getItems().add(row);  }
  }
    } catch (Exception e) {
        showAlert("Error", "Error al buscar los libros: " + e.toString());
    } finally {
        objetoConexion.cerrarConexion();
    }
}

    
    public void MostrarLibros(TableView<Object[]> tableView) {
     CConnection objetoConexion = new CConnection();
     String consulta = "SELECT * FROM Libros";
     try (Statement st = objetoConexion.estableceConexion().createStatement(); ResultSet rs = st.executeQuery(consulta)) {
          tableView.getColumns().clear();
          tableView.getItems().clear();
            TableColumn<Object[], String> colISBN = new TableColumn<>("ISBN");
            colISBN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].toString()));
            TableColumn<Object[], String> colTitulo = new TableColumn<>("Título");
            colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].toString()));
            TableColumn<Object[], String> colAutor = new TableColumn<>("Autor");
            colAutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].toString()));
            TableColumn<Object[], String> colAno = new TableColumn<>("Año de Publicación");
            colAno.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].toString()));
            TableColumn<Object[], String> colEditorial = new TableColumn<>("Editorial");
            colEditorial.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4].toString()));
            TableColumn<Object[], String> colCantidad = new TableColumn<>("Cantidad Disponible");
            colCantidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5].toString()));
            tableView.getColumns().addAll(colISBN, colTitulo, colAutor, colAno, colEditorial, colCantidad);
            while (rs.next()) {
               Object[] row = {
            rs.getString("isbn"),
            rs.getString("titulo"),
            rs.getString("autor"),
             rs.getInt("año_publicacion"),
             rs.getString("editorial"),
             rs.getInt("cantidad_disponible")
                };
                tableView.getItems().add(row);
            }
        } catch (Exception e) {
       showAlert("Error", "Error al mostrar los libros: " + e.toString());
      } finally {
      objetoConexion.cerrarConexion();
     }
    }

    public void seleccionarLibro(TableView<Object[]> tableView, TextField isbn, TextField titulo, TextField autor, TextField añoPublicacion, TextField editorial, TextField cantidad) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Object[] filaSeleccionada = tableView.getSelectionModel().getSelectedItem();
            isbn.setText(filaSeleccionada[0].toString());
            titulo.setText(filaSeleccionada[1].toString());
            autor.setText(filaSeleccionada[2].toString());
            añoPublicacion.setText(filaSeleccionada[3].toString());
            editorial.setText(filaSeleccionada[4].toString());
            cantidad.setText(filaSeleccionada[5].toString());
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


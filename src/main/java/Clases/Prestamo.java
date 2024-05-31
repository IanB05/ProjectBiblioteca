/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Prestamo implements Serializable {
    private int idPrestamo;
   private int idCliente;
    private String isbn;
    private LocalDate fechaPrestamo  ;
    
    private LocalDate fechaDevolucion;
    private LocalDate fechaVencimiento;
  private double multa;

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    
    
    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }

    public Prestamo() {}

    public void agregarPrestamo(TextField idCliente, TextField isbn, TextField fechaVencimiento) {
      CConnection objetoCConnection = new CConnection();
      Cliente cliente = new Cliente();
      int idClienteInt = Integer.parseInt(idCliente.getText());

      if (!clienteExiste(idClienteInt)) {
          showAlert("Error", "El cliente con ID " + idClienteInt + " no existe.");
            return; }

      if (clienteTieneMulta(idClienteInt)) {
       showAlert("Error", "El cliente tiene una multa activa y no puede realizar nuevos préstamos.");
            return;}

     if (cliente.clienteTienePrestamo(idClienteInt)) {
            showAlert("Error", "El cliente ya tiene un préstamo activo.");
            return;}

   int cantidadDisponible = obtenerCantidadLibro(isbn.getText());
    if (cantidadDisponible <= 0) {
        showAlert("Error", "No hay suficientes ejemplares del libro disponibles.");
        return;}

     String consulta = "INSERT INTO Prestamos (id_cliente, isbn, fecha_prestamo, fecha_vencimiento) VALUES (?, ?, ?, ?)";
     try (CallableStatement cs = objetoCConnection.estableceConexion().prepareCall(consulta)) {
        cs.setInt(1, idClienteInt);
        cs.setString(2, isbn.getText());
        cs.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
         cs.setDate(4, java.sql.Date.valueOf(LocalDate.parse(fechaVencimiento.getText())));
         cs.execute();

    cliente.actualizarPrestamoCliente(idClienteInt, true);
    actualizarCantidadLibro(isbn.getText(), -1);

    showAlert("Información", "Préstamo registrado correctamente");
        } catch (Exception e) {
            showAlert("Error", "Error al registrar el préstamo: " + e.toString());
        } finally {
            objetoCConnection.cerrarConexion();
        }
    }

    private boolean clienteExiste(int idCliente) {
     CConnection objetoCConnection = new CConnection();
     String consulta = "SELECT COUNT(*) FROM Clientes WHERE id_cliente = ?";
      try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
         ps.setInt(1, idCliente);
     ResultSet rs = ps.executeQuery();
         if (rs.next()) {
           return rs.getInt(1) > 0;    }
   } catch (Exception e) {
       showAlert("Error", "Error al verificar la existencia del cliente: " + e.toString());
     } finally {
       objetoCConnection.cerrarConexion();  }
 return false;
 
    }

    private int obtenerCantidadLibro(String isbn) {
        CConnection objetoCConnection = new CConnection();
        String consulta = "SELECT cantidad_disponible FROM Libros WHERE isbn = ?";
        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
       ps.setString(1, isbn);
      ResultSet rs = ps.executeQuery();
        if (rs.next()) {    return rs.getInt("cantidad_disponible");}
    } catch (Exception e) {
     showAlert("Error", "Error al obtener la cantidad del libro: " + e.toString());
      } finally {
      objetoCConnection.cerrarConexion();  }
        return 0;
    }

    private boolean clienteTieneMulta(int idCliente) {
       CConnection objetoCConnection = new CConnection();
       String consulta = "SELECT multa FROM Clientes WHERE id_cliente = ?";
       try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
      ps.setInt(1, idCliente);
          ResultSet rs = ps.executeQuery();
    if (rs.next()) {
           return rs.getBoolean("multa");}
    } catch (Exception e) {
     showAlert("Error", "Error al verificar la multa del cliente: " + e.toString());
      } finally {
        objetoCConnection.cerrarConexion(); }
     return false;
     
    }

    public void devolverPrestamo(TextField idPrestamo) {
    CConnection objetoCConnection = new CConnection();
      Cliente cliente = new Cliente();
        int idPrestamoInt = Integer.parseInt(idPrestamo.getText());
        String isbn = obtenerIsbnDePrestamo(idPrestamoInt);
     String consulta = "UPDATE Prestamos SET fecha_devolucion = ? WHERE id_prestamo = ?";
     try (CallableStatement cs = objetoCConnection.estableceConexion().prepareCall(consulta)) {
         cs.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
         cs.setInt(2, idPrestamoInt);
       cs.execute();
         int idCliente = obtenerIdClienteDePrestamo(idPrestamoInt);

    LocalDate fechaVencimiento = obtenerFechaVencimiento(idPrestamoInt);
      if (LocalDate.now().isAfter(fechaVencimiento)) {
    cliente.actualizarMultaCliente(idCliente, true); }

    cliente.actualizarPrestamoCliente(idCliente, false);
     actualizarCantidadLibro(isbn, 1);

  eliminarPrestamo(idPrestamoInt);

   showAlert("Información", "Préstamo devuelto y eliminado correctamente");
     } catch (Exception e) {
        showAlert("Error", "Error al devolver el préstamo: " + e.toString());
     } finally {
     objetoCConnection.cerrarConexion();
    }
    }

   private String obtenerIsbnDePrestamo(int idPrestamo) {
     CConnection objetoCConnection = new CConnection();
     String consulta = "SELECT isbn FROM Prestamos WHERE id_prestamo = ?";
     try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
     ps.setInt(1, idPrestamo);
     ResultSet rs = ps.executeQuery();
      if (rs.next()) {
         return rs.getString("isbn");  }
        } catch (Exception e) {   
 showAlert("Error", "Error al obtener el ISBN del préstamo: " + e.toString());
        } finally {
    objetoCConnection.cerrarConexion();
        }
        return null; 
    }

    private LocalDate obtenerFechaVencimiento(int idPrestamo) {
     CConnection objetoCConnection = new CConnection();
      String consulta = "SELECT fecha_vencimiento FROM Prestamos WHERE id_prestamo = ?";
     try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
         ps.setInt(1, idPrestamo);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
       return rs.getDate("fecha_vencimiento").toLocalDate(); }
        } catch (Exception e) {
      showAlert("Error", "Error al obtener la fecha de vencimiento del préstamo: " + e.toString());
        } finally {
     objetoCConnection.cerrarConexion();
        }
        return null; // Devuelve null si no se encuentra el préstamo
    }

    private void eliminarPrestamo(int idPrestamo) {
        CConnection objetoCConnection = new CConnection();
        String consulta = "DELETE FROM Prestamos WHERE id_prestamo = ?";

        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
         ps.setInt(1, idPrestamo);
         ps.execute();
        } catch (Exception e) {
      showAlert("Error", "Error al eliminar el préstamo: " + e.toString());
        } finally {
      objetoCConnection.cerrarConexion();
        }
    }

    private int obtenerIdClienteDePrestamo(int idPrestamo) {
        CConnection objetoCConnection = new CConnection();
        String consulta = "SELECT id_cliente FROM Prestamos WHERE id_prestamo = ?";
        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
         ps.setInt(1, idPrestamo);
        ResultSet rs = ps.executeQuery();
         if (rs.next()) {
        return rs.getInt("id_cliente");  }
        } catch (Exception e) {
        showAlert("Error", "Error al obtener el ID del cliente del préstamo: " + e.toString());
        } finally {
       objetoCConnection.cerrarConexion();
        }  return 0; 
    }

    private void actualizarCantidadLibro(String isbn, int cantidadCambio) {
        CConnection objetoCConnection = new CConnection();
        String consulta = "UPDATE Libros SET cantidad_disponible = cantidad_disponible + ? WHERE isbn = ?";
        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
      ps.setInt(1, cantidadCambio);
    ps.setString(2, isbn);
    ps.execute();
    } catch (Exception e) {
   showAlert("Error", "Error al actualizar la cantidad del libro: " + e.toString());
   } finally {
    objetoCConnection.cerrarConexion();
   }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void mostrarPrestamos(TableView<Object[]> tableView) {
        CConnection objetoConexion = new CConnection();
        String consulta = "SELECT * FROM Prestamos";

        try (Statement st = objetoConexion.estableceConexion().createStatement(); ResultSet rs = st.executeQuery(consulta)) {
        tableView.getColumns().clear();
         tableView.getItems().clear();
       TableColumn<Object[], String> colIdPrestamo = new TableColumn<>("ID Préstamo");
       colIdPrestamo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].toString()));

       TableColumn<Object[], String> colIdCliente = new TableColumn<>("ID Cliente");
       colIdCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].toString()));

         TableColumn<Object[], String> colIsbn = new TableColumn<>("ISBN");
         colIsbn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].toString()));

         TableColumn<Object[], String> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
         colFechaPrestamo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].toString()));

      TableColumn<Object[], String> colFechaVencimiento = new TableColumn<>("Fecha Vencimiento");
       colFechaVencimiento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4].toString()));

       tableView.getColumns().addAll(colIdPrestamo, colIdCliente, colIsbn, colFechaPrestamo, colFechaVencimiento);

    while (rs.next()) {
      Object[] row = {
        rs.getInt("id_prestamo"),
       rs.getInt("id_cliente"),
        rs.getString("isbn"),
        rs.getDate("fecha_prestamo").toLocalDate(),
         rs.getDate("fecha_vencimiento").toLocalDate(),};
     tableView.getItems().add(row);
 }
    } catch (Exception e) {
    showAlert("Error", "Error al mostrar los préstamos: " + e.toString());
   } finally {
   objetoConexion.cerrarConexion();}
    }
}





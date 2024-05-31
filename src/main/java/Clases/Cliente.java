/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Cliente implements Serializable {
 private int id_cliente;
 private String nombre;
   private String direccion ;
  private String telefono;
 private boolean multa ;

 private boolean prestamo;

    public Cliente(int id_cliente, String nombre, String direccion, String telefono, boolean multa, boolean prestamo) {
        
        this.id_cliente = id_cliente;
      this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        
        this.multa = multa;
        this.prestamo = prestamo;
    }

    public Cliente() {
        this.multa = false;
        this.prestamo = false ; 
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isMulta() {
        return multa;
    }

    public void setMulta(boolean multa) {
        this.multa = multa;
    }

    public boolean isPrestamo() {
        return prestamo;
    }

    public void setPrestamo(boolean prestamo) {
        this.prestamo = prestamo;
    }


    public void AgregarCliente(TextField id_cliente, TextField nombre, TextField direccion, TextField telefono) {
        
        CConnection objetoCConnection = new CConnection();
if (clienteExists(Integer.parseInt(id_cliente.getText()))) {
     showAlert("Error", "El cliente ya existe en la base de datos.");
     return;}

    String Consulta = "INSERT INTO Clientes (id_cliente, nombre, direccion, telefono, multa, prestamo) VALUES (?,?,?,?,?,?)";

     try (CallableStatement cs = objetoCConnection.estableceConexion().prepareCall(Consulta)) {
            cs.setInt(1, Integer.parseInt(id_cliente.getText()));
            cs.setString(2, nombre.getText()) ;
            
            cs.setString(3, direccion.getText() );
            cs.setString(4, telefono.getText());
            cs.setBoolean(5, false);
            cs.setBoolean(6, false); 
            cs.execute();
            showAlert("Información", "Se agrego un cliente ");
           if (cs.getUpdateCount() > 0) {
                try {
                  
           FileOutputStream fileOut = new FileOutputStream("cliente_serializado.ser");
           ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                    
           objectOut.writeObject(this);
              objectOut.close();
            fileOut.close();
             showAlert("Información", "Cliente guardado y serializado correctamente");
            } catch (Exception ex) {
             showAlert("Error", "Error al serializar el cliente: " + ex.getMessage());
         }
            } else 
           { showAlert("Información", "No se pudo guardar el cliente en la base de datos.");
            }

        } catch (Exception e) {
            showAlert("Información", "Error al guardar cliente: " + e.toString());
        } finally {
            objetoCConnection.cerrarConexion();
        }
    }

public void modificarCliente(TextField id_cliente, TextField nombre, TextField direccion, TextField telefono, CheckBox multa) {
    CConnection objetoCConnection = new CConnection();
    String consulta = "UPDATE Clientes SET nombre = ?, direccion = ?, telefono = ?, multa = ? WHERE id_cliente = ?";

    try (CallableStatement cs = objetoCConnection.estableceConexion().prepareCall(consulta)) {
        cs.setString(1, nombre.getText());
        cs.setString(2, direccion.getText());
        cs.setString(3, telefono.getText());
        cs.setBoolean(4, multa.isSelected());
        cs.setInt(5, Integer.parseInt(id_cliente.getText()));
        int rowsAffected = cs.executeUpdate();
        if (rowsAffected > 0) {
            showAlert("Información", "Se modifico al cliente");
        } else {
            showAlert("Error", "Este cliente no existe.");
        }
    } catch (Exception e) {
        showAlert("Error", "Error al modificar el cliente: " + e.toString());
    } finally {
        objetoCConnection.cerrarConexion();
    }
}
    public void eliminarCliente(int id_cliente) {
        
        
        CConnection objetoCConnection = new CConnection();
        
        String Consulta = "DELETE FROM Clientes WHERE id_cliente=?";

        
        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(Consulta)) {
            ps.setInt(1, id_cliente);
            ps.executeUpdate();
   showAlert("Información", "Cliente eliminado correctamente");
        } catch (Exception e) {
            
            showAlert("Información", "Error al eliminar el cliente: " + e.toString());
        } finally {
            objetoCConnection.cerrarConexion();
        }
    }

public void mostrarClientes(TableView<Object[]> tablaClientes) {
    CConnection objetoConexion = new CConnection();
    String consulta = "SELECT * FROM Clientes";

    try (Statement st = objetoConexion.estableceConexion().createStatement(); ResultSet rs = st.executeQuery(consulta)) {
        tablaClientes.getColumns().clear();
        tablaClientes.getItems().clear();

        TableColumn<Object[], String> colIdCliente = new TableColumn<>("ID Cliente");
        colIdCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].toString()));

        TableColumn<Object[], String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].toString()));

        TableColumn<Object[], String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].toString()));

        TableColumn<Object[], String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].toString()));

        TableColumn<Object[], String> colMulta = new TableColumn<>("Multa");
        colMulta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4].toString()));

        TableColumn<Object[], String> colPrestamo = new TableColumn<>("Préstamo");
        colPrestamo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5].toString()));

        tablaClientes.getColumns().addAll(colIdCliente, colNombre, colDireccion, colTelefono, colMulta, colPrestamo);

        while (rs.next()) {
            Object[] row = {
         rs.getInt("id_cliente"),
         rs.getString("nombre"),
         rs.getString("direccion"),
         rs.getString("telefono"),
         rs.getBoolean("multa"),
         rs.getBoolean("prestamo")
            };
            tablaClientes.getItems().add(row);
        }
    } catch (Exception e) {
        showAlert("Error", "Error al mostrar los clientes: " + e.toString());
    } finally {
        objetoConexion.cerrarConexion();
    }
}


    public void seleccionarCliente(TableView<Object[]> TablaTotalCliente, TextField id_cliente, TextField nombre, TextField direccion, TextField telefono, CheckBox multa) {
        int fila = TablaTotalCliente.getSelectionModel().getSelectedIndex();
        if (fila >= 0) {
    Object[] filaSeleccionada = TablaTotalCliente.getItems().get(fila);
    id_cliente.setText(filaSeleccionada[0].toString());
    nombre.setText(filaSeleccionada[1].toString());
    direccion.setText(filaSeleccionada[2].toString());
     telefono.setText(filaSeleccionada[3].toString());
     multa.setSelected(Boolean.parseBoolean(filaSeleccionada[4].toString()));}
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean clienteExists(int id_cliente) {
        CConnection objetoCConnection = new CConnection();
        String query = "SELECT COUNT(*) FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(query)) {
       ps.setInt(1, id_cliente);
       ResultSet rs = ps.executeQuery();
         if (rs.next()) {
          return rs.getInt(1) > 0;}
    }    catch (Exception e) {
      showAlert("Error", "Error al verificar la existencia del cliente: " + e.toString());
     } finally {
    objetoCConnection.cerrarConexion();
      }
    return false;
    }

    public void actualizarPrestamoCliente(int idCliente, boolean prestamo) {
    CConnection objetoCConnection = new CConnection();
    String consulta = "UPDATE Clientes SET prestamo = ? WHERE id_cliente = ?";
    try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
   ps.setBoolean(1, prestamo);
   ps.setInt(2, idCliente);
   ps.executeUpdate();
    } catch (Exception e) {
        showAlert("Error", "Error al actualizar el estado del préstamo del cliente: " + e.toString());
    } finally {
        objetoCConnection.cerrarConexion();
    }
}

public boolean clienteTienePrestamo(int idCliente) {
    CConnection objetoCConnection = new CConnection();
    String consulta = "SELECT prestamo FROM Clientes WHERE id_cliente = ?";
    try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
    ps.setInt(1, idCliente);
   ResultSet rs = ps.executeQuery();
   if (rs.next()) {
      return rs.getBoolean("prestamo");
        }
    } catch (Exception e) {
        showAlert("Error", "Error al verificar el estado del préstamo del cliente: " + e.toString());
    } finally {
        objetoCConnection.cerrarConexion();
    }
    return false;
}

public void actualizarMultaCliente(int idCliente, boolean multa) {
    CConnection objetoCConnection = new CConnection();
    String consulta = "UPDATE Clientes SET multa = ? WHERE id_cliente = ?";
    try (PreparedStatement ps = objetoCConnection.estableceConexion().prepareStatement(consulta)) {
        ps.setBoolean(1, multa);
        ps.setInt(2, idCliente);
        ps.executeUpdate();
    } catch (Exception e) {
        showAlert("Error", "Error al actualizar el estado de la multa del cliente: " + e.toString());
    } finally {
        objetoCConnection.cerrarConexion();
    }
}

public void buscarClientes(String criterio, TableView<Object[]> tableView) {
    CConnection objetoConexion = new CConnection();
    String consulta = "SELECT * FROM Clientes WHERE id_cliente::text LIKE ? OR nombre LIKE ?";
    
    try (PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta)) {
        ps.setString(1, "%" + criterio + "%");
        ps.setString(2, "%" + criterio + "%");
        
        try (ResultSet rs = ps.executeQuery()) {
            tableView.getColumns().clear();
            tableView.getItems().clear();

            TableColumn<Object[], String> colIdCliente = new TableColumn<>("ID Cliente");
            colIdCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].toString()));

            TableColumn<Object[], String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].toString()));

            TableColumn<Object[], String> colDireccion = new TableColumn<>("Dirección");
            colDireccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].toString()));

            TableColumn<Object[], String> colTelefono = new TableColumn<>("Teléfono");
            colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].toString()));

            TableColumn<Object[], String> colMulta = new TableColumn<>("Multa");
            colMulta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4].toString()));

            tableView.getColumns().addAll(colIdCliente, colNombre, colDireccion, colTelefono, colMulta);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getBoolean("multa") ? "Sí" : "No"
                };
                tableView.getItems().add(row);
            }
        }
    } catch (Exception e) {
        showAlert("Error", "Error al buscar los clientes: " + e.toString());
    } finally {
        objetoConexion.cerrarConexion();
    }
}

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author barre
 */
import java.io.Serializable;

public class LibroSer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String titulo;
    private String autor;
    private int anopublicacion;
    private String editorial;
    private int cantidad;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnopublicacion() {
        return anopublicacion;
    }

    public void setAnopublicacion(int anopublicacion) {
        this.anopublicacion = anopublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LibroSer(String isbn, String titulo, String autor, int anopublicacion, String editorial, int cantidad) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anopublicacion = anopublicacion;
        this.editorial = editorial;
        this.cantidad = cantidad;
    }

}
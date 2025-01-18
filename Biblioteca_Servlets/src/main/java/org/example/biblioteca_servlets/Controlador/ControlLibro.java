package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.biblioteca_servlets.Modelo.DAOGenerico;
import org.example.biblioteca_servlets.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ControlLibro {
    String isbn;
    String titulo;
    String autor;
    public static DAOGenerico daoLibro;

    public ControlLibro() {
        daoLibro = new DAOGenerico(Libro.class, String.class);
    }
    public ControlLibro(String isbn, String titulo, String autor) {
        this();
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
    }

    public boolean existeLibro(String isbn) {
        if(daoLibro.getById(isbn) != null) {
            return true;
        }
        return false;
    }

    public void mostrarLibros(ObjectMapper conversorJson, PrintWriter out) throws IOException {
        List<Libro> libros = daoLibro.getAll();
        String json_response = conversorJson.writeValueAsString(libros);
        out.println(json_response);
    }

    public void obtenerLibro(String isbn, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(!existeLibro(isbn))
            out.println("ERROR: EL LIBRO NO EXISTE");
        else {
            Libro libro = (Libro) daoLibro.getById(isbn);
            String json_response = conversorJson.writeValueAsString(libro);
            out.println(json_response);
        }
    }

    public void creaLibro(String isbn, String titulo, String autor, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(existeLibro(isbn)) {
            out.println("ERROR: EL LIBRO YA EXISTE");
        }else{
            Libro libro = new Libro(isbn, titulo, autor);
            daoLibro.insert(libro);
            String json_response = conversorJson.writeValueAsString(libro);
            out.println(json_response);
        }
    }

    public void actualizaLibro(String isbn, String titulo, String autor, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(!existeLibro(isbn)) {
            out.println("ERROR: EL LIBRO NO EXISTE");
        }
        else {
            Libro libro = (Libro) daoLibro.getById(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            daoLibro.update(libro);
            String json_response = conversorJson.writeValueAsString(libro);
            out.println(json_response);
        }
    }

    public void borraLibro(String isbn, PrintWriter out) {
        if(!existeLibro(isbn)) {
            out.println("ERROR: EL LIBRO NO EXISTE");
        }else {
            Libro libro = (Libro) daoLibro.getById(isbn);
            daoLibro.delete(libro);
            out.println("LIBRO ELIMINADO CORRECTAMENTE");
        }
    }
}

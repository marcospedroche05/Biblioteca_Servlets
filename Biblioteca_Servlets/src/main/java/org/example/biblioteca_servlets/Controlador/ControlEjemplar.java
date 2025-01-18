package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.biblioteca_servlets.Modelo.DAOEjemplar;
import org.example.biblioteca_servlets.Modelo.DAOGenerico;
import org.example.biblioteca_servlets.Modelo.Ejemplar;
import org.example.biblioteca_servlets.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ControlEjemplar {
    String isbn;
    String estado;
    DAOGenerico daoEjemplar;

    public ControlEjemplar(){daoEjemplar = new DAOGenerico(Ejemplar.class, Integer.class);}
    public ControlEjemplar(String isbn, String estado) {
        this();
        this.isbn = isbn;
        this.estado = estado;
    }

    public boolean existeEjemplar(Integer id) {
        if(daoEjemplar.getById(id) != null) {
            return true;
        }
        return false;
    }

    public void muestraEjemplares(ObjectMapper conversorJson, PrintWriter out) throws IOException {
        List<Ejemplar> ejemplares = daoEjemplar.getAll();
        String json_response = conversorJson.writeValueAsString(ejemplares);
        out.println(json_response);
    }


    public void eliminaEjemplar(Integer id, PrintWriter out){
        if(existeEjemplar(id)){
            Ejemplar ejemplar = (Ejemplar)daoEjemplar.getById(id);
            daoEjemplar.delete(ejemplar);
            out.println("EJEMPLAR ELIMINADO");
        }
        else out.println("ERROR: EL EJEMPLAR NO EXISTE");
    }

    public void creaEjemplar(String isbn, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        ControlLibro controlLibro = new ControlLibro();
        Libro libro = (Libro)controlLibro.daoLibro.getById(isbn);
        Ejemplar nuevoEjemplar = new Ejemplar(libro);
        daoEjemplar.insert(nuevoEjemplar);
        out.println(conversorJson.writeValueAsString(nuevoEjemplar));

    }

    public void obtenerEjemplar(Integer id, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(existeEjemplar(id)){
            Ejemplar ejemplar = (Ejemplar)daoEjemplar.getById(id);
            out.println(conversorJson.writeValueAsString(ejemplar));
        }else out.println("ERROR: EL EJEMPLAR NO EXISTE");
    }

    public void actualizaEjemplar(Integer id, String isbn, String estado, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        ControlLibro controlLibro = new ControlLibro();
        if(!existeEjemplar(id)){
            out.println("ERROR: EL EJEMPLAR NO EXISTE");
        }else if(!controlLibro.existeLibro(isbn)) {
            out.println("ERROR: EL ISBN QUE TRATA DE ASIGNAR NO PERTENECE A NINGÚN LIBRO");
        }else {
            Libro libro = (Libro)controlLibro.daoLibro.getById(isbn);
            Ejemplar ejemplar = (Ejemplar)daoEjemplar.getById(id);
            ejemplar.setIsbn(libro);
            ejemplar.setEstado(estado);
            daoEjemplar.update(ejemplar);
            out.println(conversorJson.writeValueAsString(ejemplar));
        }
    }
}

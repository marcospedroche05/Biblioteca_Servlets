package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.biblioteca_servlets.Modelo.DAOGenerico;
import org.example.biblioteca_servlets.Modelo.Ejemplar;
import org.example.biblioteca_servlets.Modelo.Prestamo;
import org.example.biblioteca_servlets.Modelo.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ControlPrestamo {
    DAOGenerico daoPrestamo;

    public ControlPrestamo(){
        daoPrestamo = new DAOGenerico(Prestamo.class, Integer.class);
    }

    private boolean existePrestamo(Integer id){
        if(daoPrestamo.getById(id) != null){
            return true;
        }return false;
    }

    public void muestraPrestamos(ObjectMapper conversorJson, PrintWriter out) throws IOException {
        List<Prestamo> prestamos = daoPrestamo.getAll();
        out.println(conversorJson.writeValueAsString(prestamos));
    }

    public void obtenerPrestamo(Integer id, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(!existePrestamo(id)){
            out.println("ERROR: ESTE PRESTAMO NO EXISTE");
        }else {
            Prestamo prestamo = (Prestamo) daoPrestamo.getById(id);
            out.println(conversorJson.writeValueAsString(prestamo));
        }
    }

    public void crearPrestamo(Integer usuario_id, Integer ejemplar_id, LocalDate fechaInicio, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        ControlUsuario controlUsuario = new ControlUsuario();
        ControlEjemplar controlEjemplar = new ControlEjemplar();
        if(!controlUsuario.existeUsuario(usuario_id)){
            out.println("ERROR: EL USUARIO AL QUE LE INTENTA ASIGNAR EL PRÉSTAMO NO EXISTE");
        }else if(!controlEjemplar.existeEjemplar(ejemplar_id)){
            out.println("ERROR: EL EJEMPLAR AL QUE LE INTENTA ASIGNAR EL PRÉSTAMO NO EXISTE");
        }else {
            Ejemplar ejemplar = (Ejemplar) controlEjemplar.daoEjemplar.getById(ejemplar_id);
            if(!ejemplar.getEstado().equalsIgnoreCase("Disponible")){
                out.println("ERROR: EL EJEMPLAR NO ESTÁ DISPONIBLE");
            }else {
                Usuario usuario = (Usuario) controlUsuario.daoUsuario.getById(usuario_id);
                LocalDate fechaDevolucion = fechaInicio.plusDays(15);
                Prestamo prestamo = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
                ejemplar.setEstado("Prestado");
                controlEjemplar.daoEjemplar.update(ejemplar);
                daoPrestamo.insert(prestamo);
                out.println(conversorJson.writeValueAsString(prestamo));
            }
        }
    }

    public void borrarPrestamo(Integer id, PrintWriter out) {
        ControlEjemplar controlEjemplar = new ControlEjemplar();
        if(!existePrestamo(id)){
            out.println("ERROR: EL PRESTAMO NO EXISTE");
        }else {
            Prestamo prestamo = (Prestamo) daoPrestamo.getById(id);
            daoPrestamo.delete(prestamo);
            Ejemplar ejemplar = prestamo.getEjemplar();
            ejemplar.setEstado("Disponible");
            controlEjemplar.daoEjemplar.update(ejemplar);
            out.println("PRESTAMO ELIMINADO CORRECTAMENTE");
        }
    }

    public void actualizaPrestamo(Integer id, Integer usuario_id, Integer ejemplar_id, LocalDate fechaInicio, LocalDate fechaDevolucion, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        ControlUsuario controlUsuario = new ControlUsuario();
        ControlEjemplar controlEjemplar = new ControlEjemplar();
        if(!existePrestamo(id)){
            out.println("ERROR: EL PRESTAMO NO EXISTE");
        }else if(!controlUsuario.existeUsuario(usuario_id)){
            out.println("ERROR: EL USUARIO AL QUE LE INTENTA ASIGNAR EL PRÉSTAMO NO EXISTE");
        }else if(!controlEjemplar.existeEjemplar(ejemplar_id)){
            out.println("ERROR: EL EJEMPLAR AL QUE LE INTENTA ASIGNAR EL PRÉSTAMO NO EXISTE");
        }else {
            Usuario usuario = (Usuario) controlUsuario.daoUsuario.getById(usuario_id);
            Ejemplar ejemplar = (Ejemplar) controlEjemplar.daoEjemplar.getById(ejemplar_id);
            if(!ejemplar.getEstado().equalsIgnoreCase("Disponible")){
                out.println("ERROR: EL EJEMPLAR NO ESTÁ DISPONIBLE");
            }else {
                Prestamo prestamo = (Prestamo)daoPrestamo.getById(id);
                Ejemplar ejemplarAnterior = prestamo.getEjemplar();
                ejemplarAnterior.setEstado("Disponible");
                controlEjemplar.daoEjemplar.update(ejemplarAnterior);
                prestamo.setUsuario(usuario);
                prestamo.setEjemplar(ejemplar);
                ejemplar.setEstado("Prestado");
                controlEjemplar.daoEjemplar.update(ejemplar);
                prestamo.setFechaInicio(fechaInicio);
                prestamo.setFechaDevolucion(fechaDevolucion);
                daoPrestamo.update(prestamo);
                out.println(conversorJson.writeValueAsString(prestamo));
            }

        }
    }


}

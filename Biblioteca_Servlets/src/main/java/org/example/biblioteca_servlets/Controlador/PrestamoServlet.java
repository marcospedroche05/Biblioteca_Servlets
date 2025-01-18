package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "prestamo_servlet", value = "/prestamo_servlet")
public class PrestamoServlet extends HttpServlet {
    ObjectMapper conversorJson;

    public void init(){conversorJson = new ObjectMapper();}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        conversorJson.registerModule(new JavaTimeModule());


        //Esto es terrible, Java no me permite introducir estos datos vacíos por cuestiones de error de conversiones ya que los recibe vacios, por lo que tengo que hacer el lio que ves a continuación para poder introducir parámetros vacios en el caso de que quiera hacer un getAll (porque no necesita en ese caso parámetros).
        Integer id;
        Integer usuario_id;
        Integer ejemplar_id;
        LocalDate fechaInicio;
        LocalDate fechaDevolucion;
        if(request.getParameter("id") != null){
            id = Integer.parseInt(request.getParameter("id"));
        }else id = 0;
        if(request.getParameter("usuario_id") != null){
            usuario_id = Integer.parseInt(request.getParameter("usuario_id"));
        }else usuario_id = 0;
        if(request.getParameter("ejemplar_id") != null){
            ejemplar_id = Integer.parseInt(request.getParameter("ejemplar_id"));
        } else ejemplar_id = 0;
        if(request.getParameter("fecha_inicio") != null){
            fechaInicio = LocalDate.parse(request.getParameter("fecha_inicio"));
        } else fechaInicio = null;
        if(request.getParameter("fecha_devolucion") != null){
            fechaDevolucion = LocalDate.parse(request.getParameter("fecha_devolucion"));
        }else fechaDevolucion = null;


        String accion = request.getParameter("accion");

        ControlPrestamo controlPrestamo = new ControlPrestamo();

        switch (accion){
            case "getAll": controlPrestamo.muestraPrestamos(conversorJson, out); break;
            case "getPrestamo": controlPrestamo.obtenerPrestamo(id, conversorJson, out); break;
            case "crearPrestamo": controlPrestamo.crearPrestamo(usuario_id, ejemplar_id, fechaInicio, conversorJson, out); break;
            case "deletePrestamo": controlPrestamo.borrarPrestamo(id, out); break;
            case "updatePrestamo": controlPrestamo.actualizaPrestamo(id, usuario_id, ejemplar_id, fechaInicio, fechaDevolucion, conversorJson, out); break;
            default: break;
        }

    }
}

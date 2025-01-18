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

@WebServlet(name = "usuario_servlet", value = "/usuario_servlet")
public class UsuarioServlet extends HttpServlet {
    ObjectMapper conversorJson;

    public void init(){conversorJson = new ObjectMapper(); }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        conversorJson.registerModule(new JavaTimeModule());

        Integer id = Integer.parseInt(request.getParameter("id"));
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String tipo = request.getParameter("tipo");
        LocalDate penalizacionHasta;
        if(request.getParameter("penalizacionHasta") != null){
            penalizacionHasta = LocalDate.parse(request.getParameter("penalizacionHasta"));
        }else penalizacionHasta = null;

        String accion = request.getParameter("accion");

        ControlUsuario controlUsuario = new ControlUsuario(dni, nombre, email, password, tipo);

        switch (accion){
            case "getAll": controlUsuario.mostrarUsuarios(conversorJson, out);break;
            case "getUsuario": controlUsuario.obtenerUsuario(id, conversorJson, out);break;
            case "crearUsuario": controlUsuario.creaUsuario(dni, nombre, email, password, tipo, conversorJson, out); break;
            case "deleteUsuario": controlUsuario.borraUsuario(id, out); break;
            case "updateUsuario": controlUsuario.actualizaUsuario(id, dni, nombre, email, password, tipo, penalizacionHasta, conversorJson, out); break;
            default:break;
        }

    }
}

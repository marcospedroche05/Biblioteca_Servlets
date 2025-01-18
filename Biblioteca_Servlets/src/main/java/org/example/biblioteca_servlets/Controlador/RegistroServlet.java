package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.biblioteca_servlets.Modelo.Usuario;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "registro_servlet", value = "/registro_servlet")
public class RegistroServlet extends HttpServlet {
    ObjectMapper conversorJson;

    public void init(){
        conversorJson = new ObjectMapper();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String tipo = request.getParameter("tipo");

        ControlUsuario controlUsuario = new ControlUsuario(dni, nombre, email, password, tipo);

        if(controlUsuario.usuarioCorrecto(email, password)){
            out.println("ERROR: EL  USUARIO INTRODUCIDO YA EXISTE");
        }else{
            Usuario usuario = new Usuario(dni, nombre, email, password, tipo);
            controlUsuario.creaUsuario(usuario);
            out.println(conversorJson.writeValueAsString(usuario));
        }
    }

}

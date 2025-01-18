package org.example.biblioteca_servlets.Controlador;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "login_servlet", value = "/login_servlet")
public class LoginServlet extends HttpServlet {
    private String message;

    ObjectMapper conversorJson;

    public void init() {
        message = "Hello World!";
        conversorJson = new ObjectMapper();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        ControlUsuario controlUsuario = new ControlUsuario(email, password);
        conversorJson.registerModule(new JavaTimeModule());

        if(controlUsuario.usuarioCorrecto(email, password)) {
            out.println("LOGIN CORRECTO");
        }else out.println("ERROR: CORREO O CONTRASEÑA INCORRECTOS, ESTE USUARIO NO EXISTE");

    }

    public void destroy() {
    }
}
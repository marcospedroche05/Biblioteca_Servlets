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

@WebServlet(name = "libro_servlet", value = "/libro_servlet")
public class LibroServlet extends HttpServlet {

    ObjectMapper conversorJson;

    public void init(){conversorJson = new ObjectMapper();}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        conversorJson.registerModule(new JavaTimeModule());

        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String accion = request.getParameter("accion");

        ControlLibro controlLibro = new ControlLibro(isbn, titulo, autor);


        switch (accion){
            case "getTodosLibros": controlLibro.mostrarLibros(conversorJson, out); break;
            case "getLibro": controlLibro.obtenerLibro(isbn, conversorJson, out); break;
            case "insertLibro": controlLibro.creaLibro(isbn, titulo, autor, conversorJson, out); break;
            case "updateLibro": controlLibro.actualizaLibro(isbn, titulo, autor, conversorJson, out); break;
            case "deleteLibro": controlLibro.borraLibro(isbn, out); break;
            default: break;
        }


    }
}

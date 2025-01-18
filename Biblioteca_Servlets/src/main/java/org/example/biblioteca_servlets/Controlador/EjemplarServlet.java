package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.biblioteca_servlets.Modelo.Ejemplar;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ejemplar_servlet", value = "/ejemplar_servlet")
public class EjemplarServlet extends HttpServlet {
    ObjectMapper conversorJson;

    public void init() {conversorJson = new ObjectMapper();}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        conversorJson.registerModule(new JavaTimeModule());

        Integer id = Integer.parseInt(request.getParameter("id"));
        String isbn = request.getParameter("isbn");
        String estado = request.getParameter("estado");
        String accion = request.getParameter("accion");

        ControlEjemplar controlEjemplar = new ControlEjemplar(isbn, estado);

        switch (accion){
            case "getAll": controlEjemplar.muestraEjemplares(conversorJson, out); break;
            case "getEjemplar": controlEjemplar.obtenerEjemplar(id, conversorJson, out); break;
            case "insertEjemplar": controlEjemplar.creaEjemplar(isbn, conversorJson, out); break;
            case "deleteEjemplar": controlEjemplar.eliminaEjemplar(id, out); break;
            case "updateEjemplar": controlEjemplar.actualizaEjemplar(id, isbn, estado, conversorJson, out); break;
            default: break;
        }
    }

}

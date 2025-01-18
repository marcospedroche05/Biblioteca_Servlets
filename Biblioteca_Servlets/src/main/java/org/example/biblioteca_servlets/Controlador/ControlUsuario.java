package org.example.biblioteca_servlets.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.biblioteca_servlets.Modelo.DAOUsuario;
import org.example.biblioteca_servlets.Modelo.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ControlUsuario {
    String dni;
    String nombre;
    String email;
    String pass;
    String tipo;
    DAOUsuario daoUsuario;

    public ControlUsuario() {daoUsuario = new DAOUsuario();}
    public ControlUsuario(String email, String pass) {
        this();
        this.email = email;
        this.pass = pass;
    }
    public ControlUsuario(String dni, String nombre, String email, String pass, String tipo){
        this(email, pass);
        this.dni = dni;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    private boolean existeUsuario(String email) {
        if(daoUsuario.getUsuarioByEmail(email) != null) {
            return true;
        }
        return false;
    }
    public boolean existeUsuario(Integer id) {
        if(daoUsuario.getById(id) != null) {
            return true;
        }
        return false;
    }

    public boolean usuarioCorrecto(String email, String pass) {
        if(existeUsuario(email)){
            Usuario usuario = daoUsuario.getUsuarioByEmail(email);
            if(usuario.getPassword().equals(pass)) {
                return true;
            }
            return false;
        }
        return false;
    }

    //Ámbos métodos sirven para crear un usuario, solo que el segundo primero crea el objeto a partir de los parámetros para llamar al primero pasandole ya el usuario para insertarlo en la base de datos
    public void creaUsuario(Usuario usuario){
        daoUsuario.insert(usuario);
    }
    public void creaUsuario(String dni, String nombre, String email, String pass, String tipo, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(!existeUsuario(email)){
            Usuario usuario = new Usuario(dni, nombre, email, pass, tipo);
            creaUsuario(usuario);
            out.println(conversorJson.writeValueAsString(usuario));
        }else out.println("ERROR: EL USUARIO YA EXISTE");
    }


    public void mostrarUsuarios(ObjectMapper conversorJson, PrintWriter out) throws IOException {
        List<Usuario> usuarios = daoUsuario.getAll();
        out.println(conversorJson.writeValueAsString(usuarios));
    }

    public void obtenerUsuario(Integer id, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(existeUsuario(id)){
            Usuario usuario = (Usuario)daoUsuario.getById(id);
            out.println(conversorJson.writeValueAsString(usuario));
        }else out.println("ERROR: EL USUARIO NO EXISTE");
    }

    public void borraUsuario(Integer id, PrintWriter out) throws IOException {
        if(existeUsuario(id)){
            Usuario usuario = (Usuario)daoUsuario.getById(id);
            daoUsuario.delete(usuario);
            out.println("USUARIO ELIMINADO CORRECTAMENTE");
        }else out.println("ERROR: EL USUARIO NO EXISTE");
    }

    public void actualizaUsuario(Integer id, String dni, String nombre, String email, String pass, String tipo, LocalDate penalizacionHasta, ObjectMapper conversorJson, PrintWriter out) throws IOException {
        if(!existeUsuario(id)){
            out.println("ERROR: EL USUARIO NO EXISTE");
        }else {
            Usuario usuario = (Usuario)daoUsuario.getById(id);
            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(pass);
            usuario.setTipo(tipo);
            usuario.setPenalizacionHasta(penalizacionHasta);
            daoUsuario.update(usuario);
            out.println(conversorJson.writeValueAsString(usuario));
        }
    }

}

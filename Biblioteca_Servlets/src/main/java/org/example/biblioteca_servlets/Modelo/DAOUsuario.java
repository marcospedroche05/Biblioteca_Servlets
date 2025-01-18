package org.example.biblioteca_servlets.Modelo;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class DAOUsuario extends DAOGenerico{
    public DAOUsuario() {
        super(Usuario.class, Integer.class);
    }
    public Usuario getUsuarioByEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Query consulta = em.createNativeQuery(sql, Usuario.class);
        consulta.setParameter(1, email);
        try {
            return (Usuario) consulta.getSingleResult();
        } catch (NoResultException e){
            return null;
        }

    }

}

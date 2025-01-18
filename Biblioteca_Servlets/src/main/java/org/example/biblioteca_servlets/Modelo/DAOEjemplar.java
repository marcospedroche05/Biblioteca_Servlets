package org.example.biblioteca_servlets.Modelo;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.List;

public class DAOEjemplar extends DAOGenerico {
    public DAOEjemplar() {
        super(Ejemplar.class, Integer.class);
    }

    public List<Ejemplar> getAllByIsbn(String isbn){
        String sql = "SELECT * FROM ejemplar WHERE isbn LIKE ?";
        Query consulta = em.createNativeQuery(sql, Ejemplar.class);
        consulta.setParameter(1, isbn);
        try {
            return consulta.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public List<Ejemplar> getAllDisponible(String isbn){
        String sql = "SELECT * FROM ejemplar WHERE isbn LIKE ? AND estado LIKE 'Disponible'";
        Query consulta = em.createNativeQuery(sql, Ejemplar.class);
        consulta.setParameter(1, isbn);
        try {
            return consulta.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }
}

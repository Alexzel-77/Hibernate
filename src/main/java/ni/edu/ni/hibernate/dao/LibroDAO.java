package ni.edu.ni.hibernate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.ni.hibernate.Libro;
import ni.edu.ni.hibernate.database.JPAUtil;

import java.util.List;

public class LibroDAO {
    public void guardar(Libro libro){

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            em.persist(libro);
            tx.commit();
        } catch(RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;

        }finally {
            em.close();
        }
    }

    public List<Libro> obtenerTodos(){

        EntityManager em = JPAUtil.getEntityManager();
        try{
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        }finally {
            em.close();
        }
    }
}
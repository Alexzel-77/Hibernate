package ni.edu.ni.hibernate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.ni.hibernate.Categoria;
import ni.edu.ni.hibernate.Libro;
import ni.edu.ni.hibernate.database.JPAUtil;

import java.util.List;

public class LibroDAO {

    // Método auxiliar
    private Categoria obtenerCategoria(EntityManager em, Long categoriaId) {
        return em.find(Categoria.class, categoriaId);
    }

    public void guardar(Libro libro){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            // 1. Extraemos el ID de la categoría que viene en el libro
            Long idCat = libro.getCategoria().getId();

            // 2. Buscamos esa categoría en la BD usando el método auxiliar
            Categoria categoriaBD = obtenerCategoria(em, idCat);

            // 3. Le asignamos la categoría gestionada al libro
            libro.setCategoria(categoriaBD);

            // 4. Guardamos el libro
            em.persist(libro);

            tx.commit();
        } catch(RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Libro> obtenerTodos(){
        EntityManager em = JPAUtil.getEntityManager();
        try{
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean modificar(Libro libro){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libroBD = em.find(Libro.class, libro.getId());
            if(libroBD == null){
                tx.rollback();
                return false;
            }
            libroBD.setTitulo(libro.getTitulo());
            libroBD.setAutor(libro.getAutor());
            libroBD.setAnio_publicacion(libro.getAnio_publicacion());
            libroBD.setPrecio(libro.getPrecio());

            // Actualizar también la categoría si fue modificada
            if (libro.getCategoria() != null) {
                Categoria catBD = em.find(Categoria.class, libro.getCategoria().getId());
                libroBD.setCategoria(catBD);
            }

            tx.commit();
            return true;
        } catch (RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean eliminar(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Libro libroBD = em.find(Libro.class, id);
            if(libroBD == null){
                tx.rollback();
                return false;
            }
            em.remove(libroBD);
            tx.commit();
            return true;
        } catch (RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.service;

import ch.hesge.etu.eddy.cdi.model.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

@Stateless// définit que cette classe est un EJB
public class BookService {

    @PersistenceContext
    private EntityManager em;

    public ArrayList<Book> readAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        cq.from(Book.class);
        TypedQuery<Book> q = em.createQuery(cq);
        ArrayList<Book> books = new ArrayList<>();
        books.addAll(q.getResultList());
        return books;
    }

    public void save(Book b){ em.persist(b); }

    public void update(Book b) { em.merge(b); }

    public void delete(Book b){
        Book book = em.find(Book.class,b.getId());
        em.remove(book);
    }

}
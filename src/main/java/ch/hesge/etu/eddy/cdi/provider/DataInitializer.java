/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */

package ch.hesge.etu.eddy.cdi.provider;

import ch.hesge.etu.eddy.cdi.model.Book;
import ch.hesge.etu.eddy.cdi.service.BookService;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Arrays;
import java.util.List;

@WebListener
public class DataInitializer implements ServletContextListener {

    private final BookService bookService;

    @Inject
    public DataInitializer(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        List<Book> books = Arrays.asList(
                new Book("La grande vadrouille","Louis de Funès", "TomCat",1966),
                new Book("Harry Potter","J.K. Rowling", "Griffondor",1997),
                new Book("Les fleurs du mal","Baudelaire", "Maison Baffort",1857),
                new Book("Bel-Ami","Guy de Maupassant", "Ollendorff",1885),
                new Book("Le temple du soleil","Hergé", "Tintin",1948)
        );
        if (bookService.readAll().isEmpty()) { // assure que la liste est remplie une seule fois uniquement au démarrage de l'application
            for(Book book : books){
                bookService.save(book);
            }
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // NOTHING TO DO HERE
    }

}

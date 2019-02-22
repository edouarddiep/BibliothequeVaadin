/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.views;

import ch.hesge.etu.eddy.cdi.model.Book;
import ch.hesge.etu.eddy.cdi.service.BookService;
import ch.hesge.etu.eddy.cdi.validation.Validate;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import javax.inject.Inject;
import java.util.ArrayList;

@Route(layout = ParentView.class, value = "liste")
public class ListBookView extends VerticalLayout {

    private BookService service;
    private Grid<Book> grid = new Grid<>();
    private Button btnNouveau = new Button("Nouveau livre");
    private ArrayList<Book> books = new ArrayList<>();

    private final String TITLE_ERR = "Veuillez saisir un titre !";
    private final String AUTH_ERR = "Veuillez saisir un auteur !";
    private final String EDITOR_ERR = "Veuillez saisir un éditeur !";
    private final String YEAR_NULL_ERR = "Veuillez saisir une année !";
    private final String YEAR_VALID_ERR = "L'année doit être comprise entre -999 et 2019 !";
    private final int TITLE_ID = 1;
    private final int AUTH_ID = 2;
    private final int EDITOR_ID = 3;
    private final int YEAR_ID = 4;

    public ListBookView() {}

    @Inject
    public ListBookView(BookService service){
        this.service = service;
        initView();
    }

    private void initView(){
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        boolean logged = Boolean.TRUE.equals(req.getSession().getAttribute("logged"));
        books = service.readAll();
        if(logged) {
            addTextFields();
            addButtons();
            add(grid, btnNouveau);
        } else {
            addLabels();
            add(grid);
        }
    }

    private void addTextFields(){
        grid.addComponentColumn(book -> {
            TextField tf = new TextField();
            fillTextField(TITLE_ID, tf, book);
            addListener(tf, book, TITLE_ERR, "Le titre", TITLE_ID);
            return tf;
        }).setHeader("Titre");
        grid.addComponentColumn(book -> {
            TextField tf = new TextField();
            fillTextField(AUTH_ID, tf, book);
            addListener(tf, book, AUTH_ERR, "L'auteur", AUTH_ID);
            return tf;
        }).setHeader("Auteur");
        grid.addComponentColumn(book -> {
            TextField tf = new TextField();
            fillTextField(EDITOR_ID, tf, book);
            addListener(tf, book, EDITOR_ERR, "L'éditeur", EDITOR_ID);
            return tf;
        }).setHeader("Editeur");
        grid.addComponentColumn(book -> {
            TextField tf = new TextField();
            fillTextField(YEAR_ID, tf, book);
            addListener(tf, book, YEAR_NULL_ERR, "L'année", YEAR_ID);
            tf.addFocusListener(event -> tf.getStyle().set("color", "black"));
            return tf;
        }).setHeader("Année");
        grid.setItems(books);
    }

    private void addButtons(){
        grid.addColumn(new NativeButtonRenderer<>("Supprimer", clickedItem -> {
            Book b = clickedItem;
            service.delete(b);
            Notification.show("Le livre \""+b.getTitre()+"\" a bien été supprimé !", 4000, Notification.Position.MIDDLE);
            updateList(service);
        }));
        btnNouveau.setSizeFull();
        btnNouveau.getStyle().set("font-size", "3vh");
        btnNouveau.addClickListener(e -> btnNouveau.getUI().ifPresent(ui -> ui.navigate("newBook")));
    }

    private void addLabels(){
        grid.addColumn(Book::getTitre).setHeader("Titre");
        grid.addColumn(Book::getAuteur).setHeader("Auteur");
        grid.addColumn(Book::getEditeur).setHeader("Editeur");
        grid.addColumn(Book::getAnnee).setHeader("Année");
        grid.setItems(books);
    }

    private void fillTextField(int id, TextField tf, Book book) {
        switch(id){
            case TITLE_ID:tf.setValue(book.getTitre());
                break;
            case AUTH_ID:tf.setValue(book.getAuteur());
                break;
            case EDITOR_ID:tf.setValue(book.getEditeur());
                break;
            case YEAR_ID:tf.setValue(String.valueOf(book.getAnnee()));
                break;
            default: break;
        }
    }

    private void addListener(TextField tf, Book book, String errorMsg, String confirmMsg, int id){
        String v = tf.getValue(); // on stocke la valeur courante du TextField avant d'ajouter le listener
        tf.addValueChangeListener(t ->{
            if(t.getValue().isEmpty()){
                fillTextField(id, tf, book); // dans le cas où l'utilisateur a laissé un champ vide, on remet les données avant modification
                Notification.show(errorMsg, 4000, Notification.Position.MIDDLE);
                return;
            } else {
                if(v.equals(t.getValue())) {
                    return; // on ne fait rien car il n'y a aucune modification effectuée
                }
                updateBook(id, book, t, tf, confirmMsg);
            }
        });
    }

    private void updateBook(int id, Book book, AbstractField.ComponentValueChangeEvent<TextField, String> t, TextField tf, String confirmMsg){
        switch(id){
            case TITLE_ID: book.setTitre(t.getValue());
                break;
            case AUTH_ID: book.setAuteur(t.getValue());
                break;
            case EDITOR_ID: book.setEditeur(t.getValue());
                break;
            case YEAR_ID:
                Validate validate = new Validate();
                if(validate.isYearValid(tf.getValue())){
                    book.setAnnee(Integer.parseInt(t.getValue()));
                } else {
                    Notification.show(YEAR_VALID_ERR, 4000, Notification.Position.MIDDLE);
                    tf.getStyle().set("color", "red");
                    return;
                }
                break;
            default:break;
        }
        service.update(book);
        Notification.show(confirmMsg+" du livre \""+(id==TITLE_ID?t.getOldValue():book.getTitre())+"\" a bien été modifié !", 4000, Notification.Position.MIDDLE);
    }

    public void updateList(BookService service){
        this.service = service;
        books = service.readAll();
        grid.setItems(books);
    }
}
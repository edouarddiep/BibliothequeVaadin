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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.inject.Inject;

@Route(layout = ParentView.class, value = "newBook")
public class NewBookView extends VerticalLayout {

    private Label errorLabel = new Label("Vous devez être connecté à l'application pour voir le contenu de cette page !");
    private Label lblTitre = new Label("Titre");
    private Label lblAuteur = new Label("Auteur");
    private Label lblEditeur = new Label("Editeur");
    private Label lblAnnee = new Label("Annee");
    private TextField titre = new TextField();
    private TextField auteur = new TextField();
    private TextField editeur = new TextField();
    private TextField annee = new TextField();
    private Button btnAdd = new Button("Créer");

    private Book book;
    private BookService service;
    private ListBookView listBookView = new ListBookView();
    private Binder<Book> binder = new Binder<>(Book.class);

    @Inject
    public NewBookView(BookService service) {
        this.service = service;
        initView();
    }

    private void initView() {
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        if(!Boolean.TRUE.equals(req.getSession().getAttribute("logged"))){
            add(errorLabel);
            return; // dans le cas où l'utilisateur n'est pas loggé, on ne fait rien
        } else {
            setStyle(); // formatage visuel
            binder.forField(annee)
                    .asRequired("Veuillez saisir une année !")
                    .withConverter(
                            new StringToIntegerConverter("Valeur incorrecte (Ex: 2014)"))
                    .bind(Book::getAnnee, Book::setAnnee);
            binder.bindInstanceFields(this);
            binder.forField(titre).asRequired("Veuillez saisir un titre !").bind(Book::getTitre, Book::setTitre);
            binder.forField(auteur).asRequired("Veuillez saisir un auteur !").bind(Book::getAuteur, Book::setAuteur);
            binder.forField(editeur).asRequired("Veuillez saisir un éditeur !").bind(Book::getEditeur, Book::setEditeur);
            add(lblTitre, titre, lblAuteur, auteur, lblEditeur, editeur, lblAnnee, annee, btnAdd);
            btnAdd.addClickListener(buttonClickEvent -> {
                addBook();
            });
            annee.addFocusListener(event -> annee.getStyle().set("color", "black"));
        }
    }


    private void setStyle(){
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.setAlignItems(Alignment.START);
        titre.setWidth("25%");
        auteur.setWidth("25%");
        editeur.setWidth("25%");
        annee.setWidth("25%");
        btnAdd.setWidth("12.5%");
    }

    private void addBook(){
        Validate validate = new Validate();
        if(validate.isFieldValid(titre, auteur, editeur, annee)){
            if(validate.isYearValid(annee.getValue())){
                createBook();
            } else {
                Notification.show("L'année doit être comprise entre -999 et 2019 !", 4000, Notification.Position.MIDDLE);
                annee.getStyle().set("color", "red");
            }
        } else {
            Notification.show("Veuillez remplir tous les champs !", 4000, Notification.Position.MIDDLE);
        }
    }

    private void createBook(){
        book = new Book(titre.getValue(), auteur.getValue(), editeur.getValue(), Integer.parseInt(annee.getValue()));
        service.save(book);
        listBookView.updateList(service);
        btnAdd.getUI().ifPresent(ui -> ui.navigate("liste"));
        Notification.show("Le livre \""+book.getTitre()+"\" a bien été créé !", 4000, Notification.Position.MIDDLE);
    }
}

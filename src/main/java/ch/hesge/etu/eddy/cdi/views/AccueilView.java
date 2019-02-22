/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route(layout = ParentView.class, value = "")
public class AccueilView extends HorizontalLayout {

    private String url = "https://i.pinimg.com/originals/a5/8d/47/a58d4712b77ffa3b9e3bc3e96041d524.jpg";
    private TextArea description = new TextArea();

    public AccueilView(){
        initView();
    }

    private void initView(){
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        description.setValue("Bienvenue sur notre Bibliothèque Vaadin ! Ici vous trouverez un petit lot de livres originaux et dignes d'une attention toute particulière à leur égard ! N'oubliez pas de soutenir les développeurs qui ont codé cette bibliothèque dynamique avec amour, patience, sincérité et passion afin de leur apporter une motivation et une détermination à toute épreuve de la vie quotidienne ! Signé Hugo et Edouard.");
        description.getStyle().set("font-size", "150%");
        description.getStyle().set("font-style", "italic");
        description.getStyle().set("width", "50%");
        description.getStyle().set("height", "25vw");
        description.getStyle().set("margin", "3rem 5rem");
        description.setReadOnly(true);
        Image img = new Image();
        img.setWidth("20%");
        img.setHeight("30%");
        img.getStyle().set("margin", "2rem 10rem");
        img.setSrc(url);
        add(description, img);
    }
}

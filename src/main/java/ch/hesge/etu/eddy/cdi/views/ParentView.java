/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class ParentView extends VerticalLayout implements RouterLayout {

    public ParentView() {
        initView();
    }

    private void initView(){
        HorizontalLayout barreMenu = new BarreMenuView();
        setStyle();
        add(barreMenu);
    }

    private void setStyle(){
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.getStyle().set("border", "1px solid black");
        this.getStyle().set("width", "100%");
    }
}

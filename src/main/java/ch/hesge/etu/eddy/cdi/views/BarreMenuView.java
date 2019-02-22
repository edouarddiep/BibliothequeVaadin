/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.servlet.ServletException;

@ParentLayout(value = ParentView.class)
public class BarreMenuView extends HorizontalLayout {

    private Label lblMenu = new Label("AppBibliotheque");
    private RouterLink linkLivres = new RouterLink("Livres", ListBookView.class);
    private Button btnConnexion = new Button("Connexion");
    private Button btnDeconnexion = new Button("Se déconnecter");
    private final String LOGOUT_CONFIRM = "Vous avez été déconnecté de l'application !";

    public BarreMenuView(){
        initView();
    }

    private void initView(){
        Div divBtn = new Div();
        setStyle();
        btnConnexion.addClickListener(e -> {
            btnConnexion.getUI().ifPresent(ui -> ui.navigate("login"));
            btnConnexion.setVisible(false);
        });
        btnDeconnexion.addClickListener(e -> logout());
        divBtn.add(btnConnexion, btnDeconnexion);
        linkLivres.setHighlightAction((e, p) -> updateState());
        add(lblMenu,linkLivres, divBtn);
        updateState();
    }

    private void setStyle(){
        this.setWidth("100%");
        this.setHeight("75px");
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.getStyle().set("border", "1px solid black");
        linkLivres.getStyle().set("background", "transparent");
        linkLivres.getStyle().set("font-style", "normal");
        linkLivres.getStyle().set("margin", "0 30vw 0");
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        lblMenu.getStyle().set("font-size", "3vh");
        linkLivres.getStyle().set("font-size", "3vh");
        btnConnexion.getStyle().set("font-size", "3vh");
    }

    private void logout(){
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        req.getSession().setAttribute("logged", Boolean.FALSE);
        try{
            req.logout();
            btnDeconnexion.getUI().ifPresent(ui -> ui.navigate(""));
            Notification.show(LOGOUT_CONFIRM, 3000, Notification.Position.MIDDLE);
        } catch (ServletException ex){
            ex.printStackTrace();
        }
    }

    public void updateState(){
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        if(Boolean.TRUE.equals(req.getSession().getAttribute("logged"))){
            btnDeconnexion.setVisible(true);
            btnConnexion.setVisible(false);
        } else {
            btnConnexion.setVisible(true);
            btnDeconnexion.setVisible(false);
        }
        if(req.getPathInfo().equals("/login")){
            btnConnexion.setVisible(false);
        }
    }

}

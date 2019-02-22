/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.views;
import com.google.common.eventbus.EventBus;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import javax.servlet.ServletException;


@Route(layout = ParentView.class, value = "login")
public class LoginView extends VerticalLayout{


    private Label lblId = new Label("Identifiant :");
    private Label lblPassword = new Label("Password :");
    private Label errorLabel = new Label("Vous êtes déjà connecté à l'application !");
    private TextField username = new TextField();
    private PasswordField pass = new PasswordField();
    private Button btnLogin = new Button("Se connecter");

    // Le rôle défini pour login
    private final String ROLE = "manager";
    private final String NULL_FIELD_MSG = "Veuillez remplir tous les champs !";
    private final String CONFIRM_MSG = "Connexion réussie !";
    private final String FAIL_CONNECT_MSG = "Identifiant / mot de passe erroné";

    public LoginView() {
        initView();
    }

    private void initView(){
        reset();
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if(Boolean.TRUE.equals(request.getSession().getAttribute("logged"))){
            add(errorLabel);
            return; // dans le cas où l'utilisateur est déjà loggé, on ne fait rien
        }
        setStyle();
        btnLogin.addClickListener(e -> doLogin());
        add(lblId, username, lblPassword, pass, btnLogin);
    }

    private void setStyle(){
        this.getStyle().set("margin", "4em 20%");
        username.setRequired(true);
        username.setErrorMessage("Nom d'utilisateur requis");
        pass.setRequired(true);
        pass.setErrorMessage("Mot de passe requis");
    }

    private void doLogin(){
        VaadinServletRequest req = VaadinServletRequest.getCurrent();
        if(!req.isUserInRole(ROLE)) {
            if(username.isEmpty() || pass.isEmpty()){
                reset();
                Notification.show(NULL_FIELD_MSG,3000,Notification.Position.MIDDLE);
                return;
            }
            try {
                String login = username.getValue();
                String password = pass.getValue();
                req.login(login, password);
                req.getSession().setAttribute("logged", Boolean.TRUE);
                btnLogin.getUI().ifPresent(ui -> ui.navigate(""));
                Notification.show(CONFIRM_MSG, 3000, Notification.Position.MIDDLE);
            } catch (ServletException ex) {
                reset();
                Notification.show(FAIL_CONNECT_MSG,3000,Notification.Position.MIDDLE);
            }
        } else {
            try{
                req.logout();
                req.getSession().setAttribute("logged", Boolean.FALSE);
            } catch(ServletException ex){
                ex.printStackTrace();
            }
        }
    }

    private void reset() {
        username.focus();
        username.setValue("");
        pass.setValue("");
    }
}
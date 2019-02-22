/**
 *
 *
 * Projet Bibliotheque Vaadin
 *
 * @author : Edouard Diep
 * */
package ch.hesge.etu.eddy.cdi.validation;

import com.vaadin.flow.component.textfield.TextField;

public class Validate {

    // méthode de validation des champs vides (pour update et nouveau)
    public boolean isFieldValid(TextField field1, TextField field2, TextField field3, TextField field4){
        if(field1.isEmpty() || field2.isEmpty() || field3.isEmpty() || field4.isEmpty()){
            return false;
        }
        return true;
    }

    // méthode de validation de l'année (pour update et nouveau)
    public boolean isYearValid(String year){
        try{
            int intYear = Integer.parseInt(year);
            if(intYear < -999 || intYear > 2019){
                return false;
            }
            return true;
        } catch (NumberFormatException nbe){
            return false;
        }
    }
}

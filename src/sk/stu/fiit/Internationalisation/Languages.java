/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.Internationalisation;

import java.util.Locale;

/**
 *
 * @author Adam Bublavý
 */
public enum Languages {
    
    SK(new Locale("sk", "SK")), 
    US(Locale.US);
    
    private final Locale locale;
    
    private Languages(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
    
}

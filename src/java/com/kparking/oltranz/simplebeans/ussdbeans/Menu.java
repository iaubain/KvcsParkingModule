/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.ussdbeans;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hp
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Menu {
    @XmlElement(name = "MENU")
    private List<String> menu;

    public Menu() {
    }

    public Menu(List<String> menu) {
        this.menu = menu;
    }

    public List<String> getMenu() {
        return menu;
    }

    public void setMenu(List<String> menu) {
        this.menu = menu;
    }
    
}

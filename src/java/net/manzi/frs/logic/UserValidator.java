/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.manzi.frs.databeans.userbeans.UserBean;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class UserValidator {
    @EJB
    ApInterface apInterface;
    public UserBean isUserValid(String msisdn){
        return apInterface.getUserByTel(msisdn);
    }
}

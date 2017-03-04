/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.utilities;

import com.kparking.oltranz.config.AppDesc;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import javax.ejb.Stateless;

/**
 *
 * @author Hp
 */
@Stateless
public class IdGenerator {
    public String generate(){
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        String generatedSeed = Long.toString(l, Character.MAX_RADIX);
        out.print(AppDesc.APP_DESC+" ID Generator generated seed"+generatedSeed);
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest(generatedSeed.getBytes());
            String generatedId = hexEncode(result);
            out.print(AppDesc.APP_DESC+" ID Generator generated ID:"+generatedId);
            return generatedSeed;
        }catch(NoSuchAlgorithmException e){
            out.print(AppDesc.APP_DESC+" Error generating contract ID due to: "+e.getLocalizedMessage());
            return generatedSeed;
        }       
    }
    
    public String genContractId(){
        try{
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.toString(prng.nextInt());
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest(randomNum.getBytes());
            String generatedId = hexEncode(result);
            out.print(AppDesc.APP_DESC+" ID Generator generated Contract_ID:"+generatedId);
            return generatedId;
        }catch(NoSuchAlgorithmException e){
            out.print(AppDesc.APP_DESC+" Error generating contract ID due to: "+e.getLocalizedMessage());
            return String.valueOf(UUID.randomUUID());
        }
    }
    static private String hexEncode(byte[] aInput){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[ (b&0xf0) >> 4 ]);
            result.append(digits[ b&0x0f]);
        }
        return result.toString();
    }
}

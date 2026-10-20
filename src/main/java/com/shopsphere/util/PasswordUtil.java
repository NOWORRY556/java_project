package com.shopsphere.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int ITERATIONS=120_000;
    private static final int KEY_LENGTH=256;
    private static final int SALT_LENGTH=16;
    private PasswordUtil(){}
    public static String hash(String password) {
        try {
            byte[] salt=new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);
            byte[] hash=derive(password.toCharArray(),salt);
            return "PBKDF2$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(hash);
        } catch(Exception e){ throw new IllegalStateException("Unable to hash password",e); }
    }
    public static boolean verify(String password,String stored) {
        if(password==null || stored==null) return false;
        if(!stored.startsWith("PBKDF2$")) return stored.equals(password); // legacy educational data
        try {
            String[] p=stored.split("\\$");
            if(p.length!=3) return false;
            byte[] salt=Base64.getDecoder().decode(p[1]);
            byte[] expected=Base64.getDecoder().decode(p[2]);
            byte[] actual=derive(password.toCharArray(),salt);
            if(actual.length!=expected.length) return false;
            int diff=0; for(int i=0;i<actual.length;i++) diff|=actual[i]^expected[i];
            return diff==0;
        } catch(Exception e){ return false; }
    }
    private static byte[] derive(char[] password,byte[] salt)throws NoSuchAlgorithmException,InvalidKeySpecException{
        PBEKeySpec spec=new PBEKeySpec(password,salt,ITERATIONS,KEY_LENGTH);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    }
}
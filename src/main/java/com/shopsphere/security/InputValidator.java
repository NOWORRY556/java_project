package com.shopsphere.security;

import java.util.regex.Pattern;

public final class InputValidator {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PINCODE = Pattern.compile("^\\d{6}$");
    private static final Pattern MOBILE = Pattern.compile("^\\+?[0-9]{10,15}$");

    private InputValidator() {}

    public static String required(String value, String field) {
        String v = value == null ? "" : value.trim();
        if (v.isEmpty()) throw new IllegalArgumentException(field + " is required.");
        return v;
    }

    public static String maxLength(String value, String field, int max) {
        String v = value == null ? "" : value.trim();
        if (v.length() > max) throw new IllegalArgumentException(field + " must be at most " + max + " characters.");
        return v;
    }

    public static String email(String value) {
        String v = required(value, "Email").toLowerCase();
        if (v.length() > 150 || !EMAIL.matcher(v).matches()) throw new IllegalArgumentException("Enter a valid email address.");
        return v;
    }

    public static String pincode(String value) {
        String v = required(value, "Pincode");
        if (!PINCODE.matcher(v).matches()) throw new IllegalArgumentException("Pincode must contain exactly 6 digits.");
        return v;
    }

    public static String mobile(String value) {
        String v = value == null ? "" : value.trim();
        if (!v.isEmpty() && !MOBILE.matcher(v).matches()) throw new IllegalArgumentException("Enter a valid mobile number.");
        return v;
    }

    public static int nonNegativeInt(String value,String field){try{int n=Integer.parseInt(required(value,field));if(n<0)throw new NumberFormatException();return n;}catch(NumberFormatException e){throw new IllegalArgumentException(field+" must be zero or a positive number.");}}

    public static int positiveInt(String value, String field) {
        try {
            int n = Integer.parseInt(required(value, field));
            if (n <= 0) throw new NumberFormatException();
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " must be a positive number.");
        }
    }
}

package com.kshprimeindustries.tuneupme.model;

public class Validations {
    public static boolean isEmailValid(String email) {

        return email.matches("^[a-zA-Z0-9_\\!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    }
    public static boolean isMobileNumberValid(String mobile) {

        return mobile.matches("^07[01245678]{1}[0-9]{7}$");

    }

    public static boolean isDouble(String price) {

        return price.matches("^\\d+(\\.\\d{2})?$");

    }

    public static boolean isInteger(String value) {

        return value.matches("^\\d+$");

    }
}

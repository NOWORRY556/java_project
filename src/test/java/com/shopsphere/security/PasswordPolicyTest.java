package com.shopsphere.security;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class PasswordPolicyTest{@Test void strongPasswordAccepted(){assertTrue(PasswordPolicy.isStrong("ShopSphere1"));}@Test void weakPasswordsRejected(){assertFalse(PasswordPolicy.isStrong("password"));assertFalse(PasswordPolicy.isStrong("Short1"));assertFalse(PasswordPolicy.isStrong("Password"));}@Test void validateThrowsForWeak(){assertThrows(IllegalArgumentException.class,()->PasswordPolicy.validate("abc123"));}}

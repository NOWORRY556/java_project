package com.shopsphere.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    @Test void validatesEmailAndPincode() {
        assertEquals("user@example.com", InputValidator.email(" User@Example.com "));
        assertEquals("302001", InputValidator.pincode("302001"));
    }

    @Test void rejectsInvalidEmailAndPincode() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.email("bad-email"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.pincode("12345"));
    }

    @Test void validatesMobileAndPositiveIntegers() {
        assertEquals("+919876543210", InputValidator.mobile("+919876543210"));
        assertEquals(3, InputValidator.positiveInt("3", "Quantity"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.positiveInt("0", "Quantity"));
    }
}

package com.shopsphere.admin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class OrderStatusValidatorTest{
 @Test void acceptsKnownStatus(){assertTrue(OrderStatusValidator.isAllowed("SHIPPED"));assertEquals("DELIVERED",OrderStatusValidator.normalize("delivered"));}
 @Test void acceptsProcessing(){assertTrue(OrderStatusValidator.isAllowed("PROCESSING"));}
 @Test void rejectsUnknownStatus(){assertFalse(OrderStatusValidator.isAllowed("HACKED"));assertThrows(IllegalArgumentException.class,()->OrderStatusValidator.normalize("HACKED"));}
}

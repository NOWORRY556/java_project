package com.shopsphere.util;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class PasswordUtilTest{@Test void hashCanBeVerified(){String h=PasswordUtil.hash("secret123");assertNotEquals("secret123",h);assertTrue(PasswordUtil.verify("secret123",h));assertFalse(PasswordUtil.verify("wrong",h));}}
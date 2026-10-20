package com.shopsphere.security;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LoginRateLimiterTest{
 @Test void allowsFiveAttemptsThenBlocks(){String k="test-"+System.nanoTime();for(int i=0;i<5;i++)assertTrue(LoginRateLimiter.allow(k));assertFalse(LoginRateLimiter.allow(k));}
}
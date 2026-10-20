package com.shopsphere.security;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
public final class LoginRateLimiter{
 private static final ConcurrentHashMap<String,Window> WINDOWS=new ConcurrentHashMap<>();
 private static final int MAX=5; private static final long WINDOW_MS=60_000L;
 private LoginRateLimiter(){}
 public static boolean allow(String key){long now=System.currentTimeMillis();Window w=WINDOWS.computeIfAbsent(key,k->new Window(now));synchronized(w){if(now-w.started>=WINDOW_MS){w.started=now;w.count.set(0);}return w.count.incrementAndGet()<=MAX;}}
 private static final class Window{long started;AtomicInteger count=new AtomicInteger();Window(long s){started=s;}}
}
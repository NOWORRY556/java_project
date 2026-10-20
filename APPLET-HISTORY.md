# Applets — historical RTU syllabus demonstration

The RTU guide treats Applets as a short historical/concept demonstration and not a dependency of the modern application.

ShopSphere therefore does not add the obsolete java.applet API to the Java 17 web build. For viva purposes explain that an applet historically ran inside a host/container and used lifecycle methods such as init, start, stop and destroy. Modern Java web applications use Servlets/JSP and a web container instead.

This keeps the project aligned with the guide without introducing an obsolete runtime dependency.

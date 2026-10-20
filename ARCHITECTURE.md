# ShopSphere Architecture

## Main web flow

    Browser
       |
    JSP / HTML / JavaScript
       |
    Servlet Controller
       |
    Service Layer
       |
    DAO Layer
       |
    JDBC
       |
    MySQL

Filters and listeners surround the web request lifecycle. Swing, Socket, RMI, serialization, URLConnection, i18n and JNDI remain focused academic modules.

## Main functional flow

    Customer
       |
    CheckoutServlet
       |
    OrderService
       |
    OrderDAO
       |
    MySQL transaction
       |
    Order confirmation

## RTU modules

The browser workflow stays layered. Socket, RMI, serialization, Swing, URLConnection, internationalization and JNDI are kept as focused educational modules so they demonstrate the syllabus without coupling unrelated concerns into checkout.

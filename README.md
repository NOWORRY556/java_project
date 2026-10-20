# ShopSphere — Advanced Java E-Commerce Project

> **3rd Year Advanced Java Project | RTU Syllabus | B.Tech CSE**

ShopSphere is a fully functional Java-based e-commerce web application demonstrating advanced Java concepts including Servlets, JSP, JDBC, RMI, Sockets, Swing, Serialization, JNDI, and Internationalization.

🌐 **Live Demo:** [https://shopsphere-online.azurewebsites.net](https://shopsphere-online.azurewebsites.net)

---

## 📁 Repository Structure

```
Repository (java_project)/
├── Advanced_JAVA_LAB_Experiment/
│   ├── LAB_No.1/   → Java Swing GUI — Student Registration Form
│   ├── LAB_No.2/   → Java Swing MVC Architecture — Calculator App
│   ├── LAB_No.3/   → JDBC with MySQL — Database Operations
│   ├── LAB_No.4/   → Java Networking — Socket / ServerSocket (Client-Server)
│   ├── LAB_No.5/   → Java RMI — Remote Calculator (Interface, Impl, Server, Client)
│   ├── LAB_No.6/   → Servlet Basics — StudentServlet with HTML form
│   └── LAB_No.7/   → Servlets Advanced — Filters, Listeners, Session, Login Flow
├── src/            → ShopSphere Web Application Source Code
├── database/       → MySQL Schema & Seed Data
├── pom.xml         → Maven Build Configuration
└── README.md       → This file
```

---

## 🛒 ShopSphere — Complete Project Details

### 📌 Project Overview

ShopSphere is a full-stack e-commerce platform built with Java EE technologies. It supports two user roles — **Customer** and **Admin** — with secure authentication, product management, order processing, and analytics.

### 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Web Framework | Jakarta Servlet 6.1 + JSP/JSTL |
| Database | MySQL 8 with JDBC |
| Build Tool | Apache Maven |
| Server | Apache Tomcat 11 |
| Desktop Module | Java Swing |
| Networking | java.net Socket / ServerSocket |
| RMI | Java RMI + RMI Registry |
| Directory | JNDI DataSource |
| Serialization | Java Object Serialization |
| Security | PBKDF2, CSRF Tokens, Security Headers |
| Deployment | Azure App Service (WAR) |

### 🏗️ Architecture

```
Browser → JSP/HTML/JS → Servlet (Controller) → Service Layer → DAO → JDBC → MySQL
```

**Package Structure:**
```
com.shopsphere
├── dao/           → Data Access Objects (ProductDAO, UserDAO, OrderDAO, WishlistDAO...)
├── model/         → Entity classes (Product, User, Order, Cart...)
├── web/           → Servlets (Controllers)
├── filter/        → Auth, CSRF, Security Headers Filters
├── security/      → Password hashing, session management
├── network/       → Socket-based product query server
├── rmi/           → RMI inventory service & registry
├── serialization/ → Object serialization demos
├── desktop/       → Swing MVC desktop dashboard
└── jndi/          → JNDI DataSource lookup
```

---

## ✨ Features

### 👤 Customer Features
- User registration with OTP verification & secure login/logout
- Product listing with search, filter, sorting & pagination
- Product details with multi-image gallery, reviews & ratings
- Shopping cart & wishlist / favourites
- Saved address management
- Coupon codes & discount checkout
- Mock payment recording
- Order history with invoice download (PDF)
- Order return & refund requests

### 🔧 Admin Features
- Dashboard with live statistics
- Product management (add, edit, enable/disable)
- Category & inventory management with low-stock alerts
- Customer account management (suspend / activate)
- Order management & status updates
- Review moderation
- Sales analytics with Chart.js (bar, pie, line charts)
- Coupon creation & management
- CSV export (sales & inventory) + CSV import for bulk products

### 🔒 Security Features
- PBKDF2WithHmacSHA256 password hashing
- CSRF token protection on all forms
- Session fixation defense (regenerate session on login)
- Security headers (CSP, X-Frame-Options, HSTS, etc.)
- SQL injection prevention via PreparedStatements
- Login attempt throttling
- Input validation (server-side)
- Role-based access control (Admin / Customer)

---

## 🧪 Lab Experiments (RTU Syllabus Coverage)

| Lab | Topic | Key Files |
|---|---|---|
| **LAB_No.1** | Java Swing GUI — Student Registration Form | `StudentRegistration.java` |
| **LAB_No.2** | Java Swing MVC Architecture — Calculator | `ModelsCalculator.java`, `ViewsCalculator.java`, `ControllerCalculator.java`, `RunCalculator.java` |
| **LAB_No.3** | JDBC with MySQL — Database CRUD Operations | `LAB_No.3/jdbc/src/` (JDBC demo) |
| **LAB_No.4** | Java Networking — Socket / ServerSocket | `server.java`, `client.java` |
| **LAB_No.5** | Java RMI — Remote Method Invocation | `Calculator.java`, `CalculatorImpl.java`, `Server.java`, `Client.java` |
| **LAB_No.6** | Servlet Basics — Form Handling | `StudentServlet.java`, `index.html` |
| **LAB_No.7** | Servlets Advanced — Filters, Listeners, Session, Login | `AuthFilter.java`, `AppListener.java`, `HomeServlet.java`, `LoginServletextends.java` |

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17+
- Apache Maven 3.8+
- Apache Tomcat 11
- MySQL 8

### 1. Clone Repository
```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd ShopSphere
```

### 2. Database Setup
```bash
mysql -u root -p
```
```sql
CREATE DATABASE shopsphere;
USE shopsphere;
SOURCE database/shopsphere.sql;
SOURCE database/seed.sql;
```

### 3. Configure Database Connection
Set environment variables or edit `db.properties`:
```
SHOPSPHERE_DB_URL=jdbc:mysql://localhost:3306/shopsphere
SHOPSPHERE_DB_USER=root
SHOPSPHERE_DB_PASSWORD=your_password
```

### 4. Build & Deploy
```bash
mvn clean package
# Copy target/shopsphere.war to Tomcat webapps/
```

### 5. Access Application
| URL | Description |
|---|---|
| `http://localhost:8080/shopsphere/` | Customer storefront |
| `http://localhost:8080/shopsphere/admin/dashboard` | Admin panel |
| Default Admin | `admin@shopsphere.com` / `Admin@1234` |

---

## 🚀 Live Deployment

The application is live on **Microsoft Azure App Service**:

🔗 **[https://shopsphere-online.azurewebsites.net](https://shopsphere-online.azurewebsites.net)**

- Hosted on Azure App Service (Linux)
- MySQL 8 on Azure Database for MySQL Flexible Server
- Deployed as WAR via GitHub Actions CI/CD pipeline
- Secured with HTTPS (Azure-managed TLS certificate)

---

## 🗄️ Database Files

| File | Description |
|---|---|
| `database/shopsphere.sql` | Full schema (tables, constraints, indexes) |
| `database/seed.sql` | Sample data (products, categories, users) |

---

## 📄 Documentation

| Document | Description |
|---|---|
| `ARCHITECTURE.md` | Detailed architecture & package design |
| `RTU-MAPPING.md` | RTU syllabus topic-to-code mapping |
| `FINAL-REPORT.md` | Complete project report |
| `VIVA-CHECKLIST.md` | Viva preparation checklist |
| `AUDIT-REPORT.md` | Security audit findings & fixes |
| `DEPLOYMENT-CHECKLIST.md` | Production deployment steps |

---

## 👨‍💻 Student Details

| Field | Details |
|---|---|
| **Name** | Krishna Saraf |
| **Roll No.** | 24EGJAD028|
| **Branch** | B.Tech — Artificial Intelligence & Data science |
| **Year / Semester** | 3rd Year / 5th Semester |
| **University** | Rajasthan Technical University (RTU) |
| **Subject** | Advanced Java Programming |
| **Faculty** | *Janvi Goyal* |

## 📜 License

This project is developed for educational purposes as part of the RTU Advanced Java curriculum.

---

*ShopSphere — Built with ❤️ using Java EE | RTU Advanced Java Project*

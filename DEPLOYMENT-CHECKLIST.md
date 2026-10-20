# ShopSphere — Deployment Checklist

This document is the deployment handoff checklist for moving the tested ShopSphere application from local Tomcat/MySQL to a hosted environment.

## 1. Required before deployment

### Application

- Java 17 runtime
- Apache Tomcat 11
- Maven 3.9+ for build, or a prebuilt WAR
- ShopSphere WAR: `target/shopsphere.war`

### Database

- MySQL 8.x-compatible server
- Database named `shopsphere`
- Database schema from `database/shopsphere.sql`
- Seed/demo data from `database/seed.sql`
- A dedicated database user is preferred for production rather than using MySQL `root`

### Server

- Linux/Windows server capable of running Java 17 + Tomcat 11
- Network access from Tomcat to MySQL
- Firewall configured for required application traffic
- SSH/RDP/admin access for deployment

## 2. Database setup

Create the production database and import:

```text
database/shopsphere.sql
database/seed.sql
```

Create a database user with only the permissions required by ShopSphere.

Do not commit the production username/password to GitHub.

## 3. Tomcat JNDI configuration

The application expects the datasource name:

```text
java:comp/env/jdbc/ShopSphereDB
```

Use the repository template:

```text
deploy/tomcat/context.xml
```

Copy/configure it in the Tomcat host configuration, preferably outside the Git repository.

Replace:

```text
CHANGE_ME
```

with the production database password.

For a production server, keep credentials outside source control.

## 4. Build

From the repository root:

```bash
mvn clean test package
```

Expected artifact:

```text
target/shopsphere.war
```

## 5. Deploy WAR

Copy the WAR to Tomcat's `webapps` directory:

```text
$CATALINA_HOME/webapps/shopsphere.war
```

Start/restart Tomcat.

Expected application context:

```text
/shopsphere
```

## 6. First smoke tests

Check:

```text
/shopsphere/
/shopsphere/products
/shopsphere/health
```

The health endpoint should report database status as `UP`.

Then test:

- Registration
- Login/logout
- Product search/sort/pagination
- Cart
- Saved address
- Coupon
- Checkout
- Order history
- Product review
- Admin login
- Admin product/category management
- Admin orders
- Admin analytics
- CSV export

## 7. Production security checklist

- [ ] HTTPS enabled
- [ ] Production DB password not committed
- [ ] Dedicated DB user created
- [ ] Database not publicly exposed unnecessarily
- [ ] Firewall configured
- [ ] Tomcat management interfaces restricted
- [ ] Default/demo admin credentials changed
- [ ] Error pages do not expose stack traces
- [ ] Production logging configured
- [ ] Backups configured
- [ ] Old/test data removed if not required
- [ ] Real payment integration only if required

## 8. Recommended production topology

```text
Internet
   |
HTTPS / Domain
   |
Tomcat 11
   |
ShopSphere WAR
   |
JNDI DataSource
   |
MySQL 8
```

The database should normally remain private to the application server/network.

## 9. Deployment information needed from the project owner

Before the actual deployment command sequence, provide/choose:

1. **Hosting provider**
   - VPS/cloud provider, or another Java-capable host.
2. **Server access**
   - SSH access for Linux, or equivalent server access.
3. **Server OS**
   - Ubuntu/Debian/Windows/etc.
4. **Database hosting**
   - Same server MySQL or managed MySQL.
5. **Domain**
   - Optional for first deployment; recommended for public HTTPS.
6. **DNS access**
   - Needed if connecting a custom domain.
7. **Production database credentials**
   - Supplied privately during server configuration; never committed to GitHub.
8. **Admin account**
   - Production admin credentials to be created/verified.
9. **HTTPS**
   - Recommended before public launch.

## 10. Important credential rule

The repository intentionally uses a deployment template with:

```text
password="CHANGE_ME"
```

Do not replace this with a real production password and commit it.

The actual production credential belongs in the deployment environment/JNDI configuration.

## 11. Deployment completion criteria

Deployment is considered complete when all of the following pass:

- [ ] WAR starts without Tomcat deployment errors
- [ ] `/health` returns database `UP`
- [ ] Home page loads
- [ ] Products load
- [ ] Customer registration/login works
- [ ] Cart and checkout work
- [ ] Order is persisted
- [ ] Admin pages work
- [ ] Review flow works
- [ ] CSV export works
- [ ] Security headers are present
- [ ] HTTPS works for public deployment
- [ ] Logs contain no startup/database errors
- [ ] Production database is backed up

## Current status

**Application:** locally tested and reported working.  
**Repository:** deployment-ready at the application level.  
**Next blocker:** selecting the hosting/server environment and configuring production MySQL + Tomcat.

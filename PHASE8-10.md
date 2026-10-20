# Phases 8–10

## Phase 8 — Address Management
Saved shipping addresses can be created, listed and deleted. Delete operations are scoped by both user ID and address ID.

## Phase 9 — Customer/Admin Management
Admins can view users, enable/disable accounts and promote customers to ADMIN. The existing AdminFilter protects /admin/*.

## Phase 10 — Deployment Hardening
Database credentials now support SHOPSPHERE_DB_URL, SHOPSPHERE_DB_USER and SHOPSPHERE_DB_PASSWORD. A Tomcat JNDI Resource example is included under deploy/tomcat/context.xml.

## Verification
mvn clean test package; then test addresses, admin users and DB environment configuration in Tomcat.

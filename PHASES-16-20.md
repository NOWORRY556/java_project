# ShopSphere — Phases 16–20

## Phase 16 — Database Runtime Hardening
- DBConnection first attempts Tomcat JNDI `java:comp/env/jdbc/ShopSphereDB`.
- Environment-variable JDBC remains the local fallback.
- This supports Tomcat-managed DataSource pooling without changing the existing architecture.

## Phase 17 — Customer Order Details
- Ownership-scoped order lookup by user ID and order ID.
- Customer sees status, payment, shipping address and line items.
- Cross-customer order access returns 404.

## Phase 18 — Health Endpoint
- `/health` checks database connectivity.
- HTTP 200 indicates healthy database connectivity; HTTP 503 indicates unavailable/degraded database state.

## Phase 19 — Password Policy
- Passwords must be 8–128 characters.
- Must contain uppercase, lowercase and a digit.
- Policy is isolated and unit tested.

## Phase 20 — Automated Verification
- Added password-policy tests.
- Existing Maven test lifecycle remains the verification path.
- Before merge: run `mvn clean test package`, then verify `/health` and order-detail ownership on Tomcat.

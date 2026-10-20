# ShopSphere — Phases 11–15

## Phase 11 — Review Moderation
- Admin-only review listing and deletion.
- Reviews are joined with customer and product names for moderation context.
- Existing `/admin/*` authorization remains the access boundary.

## Phase 12 — Order Management
- Admin order listing with customer, payment and status information.
- Controlled status values: PLACED, PROCESSING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED.
- Status changes are performed through a dedicated servlet and DAO.

## Phase 13 — Admin Analytics
- Summary counts for users, active products, orders and reviews.
- Revenue excludes cancelled orders.
- Seven-day order/revenue aggregation.

## Phase 14 — Production Observability
- `CorrelationIdFilter` creates or reuses `X-Request-ID`.
- The response echoes the request ID.
- Method, URI, HTTP status and elapsed time are logged.
- This is lightweight servlet-level observability, not a centralized production logging platform.

## Phase 15 — Verification
- Added unit coverage for order-status validation.
- Java 17 build target remains unchanged.
- Before merge run `mvn clean test package` and manually verify review moderation, order status updates, analytics and the `X-Request-ID` response header on Tomcat.

## Scope note
The project remains an educational ShopSphere implementation. It does not add a live payment gateway or claim production compliance certification.

# Phases 26–45 — Complete Hardening Batch

This batch contains 20 milestones delivered together.

| Phase | Scope | Result |
|---|---|---|
| 26 | Catalog search | Server-side product name/brand search |
| 27 | Catalog sorting | Newest, price ascending/descending, stock |
| 28 | Catalog pagination | Bounded 12-item pages and safe SQL LIMIT/OFFSET |
| 29 | Catalog query safety | Whitelisted sort expressions and parameterized filters |
| 30 | Cart validation | Positive product/quantity validation at servlet boundary |
| 31 | Login throttling | Five attempts per minute per IP/email key |
| 32 | Session hardening | 30-minute timeout and cookie-only tracking |
| 33 | Sensitive response caching | Session-backed responses receive Cache-Control no-store |
| 34 | Admin reporting | CSV export for order records |
| 35 | Admin export navigation | Export added to admin dashboard navigation |
| 36 | Numeric validation | Added non-negative integer validator |
| 37 | Password security | Existing PBKDF2/password-policy flow retained and covered by tests |
| 38 | Address ownership | Existing ownership-scoped saved-address checkout integration retained |
| 39 | Order ownership | Existing customer order-detail authorization retained |
| 40 | Admin safety | Existing self-lockout and last-admin protections retained |
| 41 | Order UX | Existing order-detail navigation from order history retained |
| 42 | Health monitoring | Existing DB-backed /health endpoint retained |
| 43 | Observability | Existing correlation/request logging retained |
| 44 | Advanced Java modules | Existing Socket, RMI, JNDI, serialization, Swing and i18n modules retained |
| 45 | Verification | Added login-throttling automated test and consolidated phase documentation |

## Verification

Target command: mvn clean test package

Manual checks:
- Search/sort/paginate products.
- Submit invalid cart quantities.
- Attempt repeated failed logins and verify throttling.
- Confirm session timeout configuration.
- Sign in and verify sensitive pages are not cacheable.
- Open Admin → Export orders and verify CSV download.
- Verify existing checkout saved-address ownership checks.
- Verify customer order details remain ownership-scoped.
- Verify admin self-disable/self-demotion protection.

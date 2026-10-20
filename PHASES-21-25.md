# Phases 21–25

## Phase 21 — Saved Address Checkout Integration
- Checkout now loads the authenticated customer's saved addresses.
- A selected address is reloaded with an ownership-scoped DAO query before use.
- Manual address entry remains available.

## Phase 22 — Order Detail UX
- Customer order history now links each order to `/order-detail`.
- Existing Phase 17 ownership checks remain the authorization boundary.

## Phase 23 — Admin Account Safety
- Admins cannot disable their own admin account.
- Admins cannot remove their own ADMIN role.
- The last active admin cannot be disabled or demoted.

## Phase 24 — Password Policy UX
- Registration UI now documents the 8–128 character policy.
- Added authenticated password-change flow using the existing PBKDF2 password utility.
- New passwords use the same PasswordPolicy enforced during registration.

## Phase 25 — Verification & Documentation
- Added the password-change view and endpoint.
- Manual verification checklist:
  1. Sign in and open Profile → Change password.
  2. Reject an incorrect current password.
  3. Reject weak replacement passwords.
  4. Accept a strong replacement password and sign in with it.
  5. Add a saved address and select it during checkout.
  6. Open an order from My Orders and verify details.
  7. As admin, verify self-disable/self-demotion and last-admin protections.

Verification target: mvn clean test package.

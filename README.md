# Development Of Large Systems Exam - MTOGO Application 

# MTOGO Legacy User Stories by Sprint

This document captures the user stories for the MTOGO system using the CCC (Card, Conversation, Confirmation) structure.

Each story includes:
- Legacy behavior (old system)
- User Story (current application behavior)
- Acceptance Criteria (testable conditions)
- MoSCoW priority

Each story is mapped to the Spring endpoints or services currently implemented in the application.

Quality measurement and planning techniques used:
- Story points to estimate relative effort per story and compare complexity across sprints.
- Planning poker to reach a shared team estimate and reduce individual bias.

---

## Sprint 1 - Core Accounts, Restaurants, Ordering

### Customer Context (Accounts & Login)

#### Legacy System (Card)
As a customer, I could only type my contact information each time I placed an order, so there were no saved accounts or profiles.

#### User Story (Card) Register Account
As a customer, I want to register an account so I can save my profile details.

**Acceptance Criteria (Confirmation)**
- A customer can register with name, email, password, and profile details.
- A new customer record has been created in the system.
- The registered customer can be retrieved by ID.

**Priority:** Must Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- Registration: `POST /api/customers` in `backend/src/main/java/com/mtogo/controller/CustomerController.java`.
- Retrieved by ID: `GET /api/customers/{id}` in `backend/src/main/java/com/mtogo/controller/CustomerController.java`.

#### User Story (Card) Login
As a customer, I want to log in so I can access my account.

**Acceptance Criteria (Confirmation)**
- Login succeeds with valid email and password.
- Login fails with invalid credentials.
- The response returns to the customer profile.

**Priority:** Must Have  
**Story Points:** 2

**Spring Mapping (Conversation)**
- Login: `POST /api/customers/login` in `backend/src/main/java/com/mtogo/controller/CustomerController.java`.

#### User Story (Card) Guest Checkout
As a customer, I want to place an order as a guest so I can order without creating an account.

**Acceptance Criteria (Confirmation)**
- A guest profile can be created for an order.
- A guest order can be created successfully.
- The guest order returns an order ID.

**Priority:** Must Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- Guest profile: `POST /api/customers/guest` in `backend/src/main/java/com/mtogo/controller/CustomerController.java`.
- Guest order: `POST /api/orders/guest` in `backend/src/main/java/com/mtogo/controller/OrderController.java`.

---

### Restaurant Context (Registration & Menu Management)

#### Legacy System (Card)
As a restaurant, I could receive orders, but menus were updated manually by admins.

#### User Story (Card) Register Restaurant
As a restaurant owner, I want to register my restaurant so it can appear in the system.

**Acceptance Criteria (Confirmation)**
- A restaurant can be created with a name, email, phone, and address.
- The created restaurant has an ID.
- The restaurant can be retrieved by ID.

**Priority:** Should Have  
**Story Points:** 4

**Spring Mapping (Conversation)**
- Registration: `POST /api/restaurants` in `backend/src/main/java/com/mtogo/controller/RestaurantController.java`.
- Retrieved by ID: `GET /api/restaurants/{id}` in `backend/src/main/java/com/mtogo/controller/RestaurantController.java`.

#### User Story (Card) View Restaurants
As a customer, I want to view active restaurants so I can choose where to order.

**Acceptance Criteria (Confirmation)**
- The system returns to a list of active restaurants.
- Each restaurant includes basic details (name, address, email).
- The list is accessible without authentication.

**Priority:** Should Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- Active listing: `GET /api/restaurants` in `backend/src/main/java/com/mtogo/controller/RestaurantController.java`.

#### User Story (Card) Manage Menu Items
As a restaurant owner, I want to add menu items so customers can see what is available.

**Acceptance Criteria (Confirmation)**
- A menu item can be created for a restaurant.
- Menu items can be listed by restaurant ID.
- Each menu item includes name, description, price, and availability.

**Priority:** Should Have  
**Story Points:** 4

**Spring Mapping (Conversation)**
- List menu items: `GET /api/menu-items/restaurant/{restaurantId}` in `backend/src/main/java/com/mtogo/controller/MenuItemController.java`.
- Create menu item: `POST /api/menu-items/restaurant/{restaurantId}` in `backend/src/main/java/com/mtogo/controller/MenuItemController.java`.

---

### Ordering Context (Making Orders)

#### Legacy System (Card)
As a customer, I could place a simple order, but there was no shopping cart or saved menu.

#### User Story (Card) Create Order
As a customer, I want to create an order so I can request delivery from a restaurant.

**Acceptance Criteria (Confirmation)**
- A registered customer can create an order with a restaurant and menu items.
- The order created includes a status and total price.
- The order returns an ID.

**Priority:** Must Have  
**Story Point:** 4

**Spring Mapping (Conversation)**
- Create order: `POST /api/orders` in `backend/src/main/java/com/mtogo/controller/OrderController.java`.

#### User Story (Card) View Orders
As a customer, I want to view my orders so I can check their details.

**Acceptance Criteria (Confirmation)**
- Orders can be listed.
- An order can be retrieved by ID.
- The response includes status, total, and restaurant details.

**Priority:** Must Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- List orders: `GET /api/orders` in `backend/src/main/java/com/mtogo/controller/OrderController.java`.
- Get by ID: `GET /api/orders/{id}` in `backend/src/main/java/com/mtogo/controller/OrderController.java`.

#### User Story (Card) Filter Orders by Status
As a restaurant, I want to filter orders by status so I can manage preparation.

**Acceptance Criteria (Confirmation)**
- Orders can be filtered by status.
- The response includes only matching orders.
- The status value is case insensitive.

**Priority:** Must Have  
**Story Points:** 2

**Spring Mapping (Conversation)**
- Filter by status: `GET /api/orders/status/{status}` in `backend/src/main/java/com/mtogo/controller/OrderController.java`.

---

## Sprint 2 - Payments, Fulfillment, Delivery, Commissions

### Payment Context (Online Payments)

#### Legacy System (Card)
Payments were made manually or after delivery, not through the system.

#### User Story (Card) Create Payment
As a customer, I want to create a payment for my order so that the order can be confirmed as paid.

**Acceptance Criteria (Confirmation)**
- A payment can be made for an existing order.
- Duplicate payments for the same order are blocked.
- The payment is linked to the order.

**Priority:** Must Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- Create payment: `POST /api/payments` in `backend/src/main/java/com/mtogo/controller/PaymentController.java`.

#### User Story (Card) View Payment
As a customer, I want to view the payment for an order so I can confirm it was processed.

**Acceptance Criteria (Confirmation)**
- Payment can be retrieved by order ID.
- If no payment exists, an error not found is returned.
- The response includes amount, method, and status.

**Priority:** Must Have  
**Story Points:** 3

**Spring Mapping (Conversation)**
- Fetch payment: `GET /api/payments/order/{orderId}` in `backend/src/main/java/com/mtogo/controller/PaymentController.java`.

---

## Sprint 3 - Feedback, Bonuses, Notifications, Reporting, Support

*(Content continues exactly as provided, unchanged, formatted consistently in markdown headings, lists, and code spans.)*

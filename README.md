Docs: [Architecture overview](Instructors.md) · [How to run](#4-build-and-run-everything-with-docker-compose)

MTOGO – System Integration Project
This project was developed for the SI module, where the main challenge is to connect different types of systems so they can work together smoothly. In this case, the work focuses on integrating a legacy monolithic application with a set of new microservices.
Instead of replacing the old system all at once, the project demonstrates how modern services can be introduced gradually while the legacy application continues to run. This approach reflects how real companies upgrade systems in stages, using patterns such as the Strangler Pattern and API Gateway routing.

Brainstorming Output

This section summarizes the results of our group brainstorming for the MTOGO
Food Delivery System. It shows the main actors, entities, processes, systems,
and business rules that were identified during the early planning stage. The goal
is to give a clear overview of the key ideas and interactions in the system, which
will guide the later design and implementation.
Actors
• Customer:  Creates an account, browses restaurant menus, places and
pays for orders, receives updates, and gives feedback after delivery.
• Restaurant / Supplier: Manages menu items and prepares food when
orders are received.
• Delivery Agent: Picks up and delivers food to customers and can earn
bonuses based on performance.
• MTOGO Management: Monitors the system, manages users and
restaurants, and views reports and dashboards.
• External Systems: Payment provider (mocked) and SMS or push notification service for updates.
Entities
• Customer account
• Restaurant profile and menu
• Order (with status tracking)
• Payment (external reference)
• Delivery assignment
• Feedback and rating
• Commission record
• Bonus record
Processes
1. Customer registration and login
2. Browsing menus and placing an order
3. Payment through an external service
4. Order preparation by the restaurant
5. Delivery assignment and live tracking
6. Delivery confirmation and customer feedback
7. Commission and bonus calculation
8. Dashboard reporting for management
Systems and Components
• Frontend: Web and mobile applications (English and Danish).
• Backend API: Core logic for orders, users, and communication with
external systems.
• Database: Stores users, restaurants, orders, and transactions.
• Notification Service: Sends SMS or in-app updates to customers.
• Dashboard Module: Shows real-time information about orders and per-
formance.
Business Rules
• Restaurant commission depends on total order value before VAT:
– 6% for orders up to 100 DKK
– 5% for orders up to 500 DKK
– 4% for orders up to 1,000 DKK
– 3% for anything above 1,000 DKK
• Customers do not pay delivery fees; it is included in menu prices.
• Delivery agents earn bonuses based on order value, on-time delivery, and
customer ratings.
• Payments are handled by an external provider, and MTOGO does not
store payment data.
• After delivery, customers provide feedback and ratings for both the food
and delivery.

Event Storming Approach
Event Storming is a workshop-based technique introduced by Alberto Brandolini to explore complex business domains and design systems collaboratively. It is divided into
distinct levels, each with its own focus and purpose. We decided to follow Brandolini’s
Event Storming methodology because it allows us to capture domain knowledge from
experts and gradually refine it into implementable software design. In our Event Storming workshop, we adopted the methodology outlined by Jakub Lambrych in his article https://medium.com/@lambrych/can-eventstorming-guide-the-design-workflow-6f75d8aa20e0

Step 1: Collection of Domain Events
This section lists the main domain events (orange sticky notes) identified during the Event Storming session for the MTOGO Food Delivery
System. Each event describes something important that happened in the
business domain. The events form the system and help to understand the
main workflow from customer account creation to delivery, feedback, and
reporting.



1. Customer Account Created: A new customer account was cre-
ated in the system.
2. Customer Verified: The customer verified their account (for ex-
ample, via email or SMS).
3. Customer Logged In: The customer successfully logged into their
account.
4. Restaurants Viewed: The customer viewed the list of available restaurants.
5. Restaurant Selected: The customer selected a restaurant from the list.
6. Menus Viewed: The customer viewed the menu of the selected
restaurant.
7. Restaurant Registered: A restaurant joined the MTOGO plat-
form and created its profile.
8. Menu Submitted: The restaurant uploaded its menu to the system.
9. Menu Published: The menu was approved and made visible to customers.
10. Delivery Agent Profile Created: A new delivery agent account
was registered in the system.
11. Order Created: A customer placed a new order through the app
or website.
12. Payment Initiated: The system sent a payment request to the
external payment service.
13. Payment Authorized: The payment was successfully processed.
14. Payment Failed: The payment was not successful.
15. Order Confirmed: The system confirmed that the order was received and valid.
16. Order Cancelled: The order was cancelled by the customer or the
restaurant.
17. Order Received by Restaurant: The restaurant received the new
order.
18. Order Accepted: The restaurant accepted the order and started
preparation.
19. Order Rejected: The restaurant rejected the order (for example,
due to missing items).
20. Order Prepared: The restaurant finished preparing the food.
21. Delivery Task Assigned: A delivery task was created and assigned
to an available agent.
22. Agent Accepted Delivery Task: The delivery agent accepted the
task.
23. Agent Picked Up Order: The delivery agent collected the order
from the restaurant.
24. Agent Started Delivery: The delivery agent started the delivery
route to the customer.
25. Order Delivered to Customer: The customer received the food
successfully.
26. Delivery Delayed: The delivery took longer than the expected
time.
27. Feedback Request Sent: The system sent a feedback request to
the customer.
28. Customer Feedback Submitted: The customer submitted feed-
back and ratings after delivery.
29. Delivery Agent Rated: The customer rated the delivery agent’s
performance.
30. Restaurant Rated: The customer rated the restaurant’s food quality and service.
31. Overall Experience Rated: The customer rated the overall order experience.
32. Fee Calculated for Restaurant: The system calculated the com-
mission fee for the restaurant based on the order value.
33. Fee Deducted from Restaurant: The commission amount was
deducted from the restaurant’s payment.
34. Bonus Calculated for Delivery Agent: The system calculated
the delivery agent’s performance bonus.
35. Bonus Paid to Delivery Agent: The calculated bonus was paid
to the delivery agent.
36. Order Metrics Updated: The system updated statistics such as
delivery time and order count.
37. Performance Report Generated: A management report was created showing overall system performance.
38. Complaint Submitted: A customer or restaurant submitted a
complaint.
39. Complaint Processed: The complaint was handled and resolved
by the support team

Step 2: Refining Domain Events
After collecting all domain events, we refined and cleaned up the list to
remove duplicates, ensure consistent naming, and confirm that each event
represents a real business action written in past tense.
During this stage, we reviewed the events according to three criteria:
– Duplicates: Events that described the same situation were merged.
– Meaningfulness: Only events that represented an important busi-
ness occurrences were kept.
– Naming: All events were checked to ensure they were written in
past tense and clearly described a completed event.
Refinement examples:
– ”Restaurants Shown” and ”Restaurants Viewed” referred to the same
situation. We kept Restaurants Viewed as the clearer and more
user-focused event.
– ”Menus Shown” and ”Menus Viewed” were merged into Menus
Viewed.
– ”Order Confirmed” and ”Payment Authorized” were kept separate,
since payment authorization happens before the system confirms the
order.
– ”Order Delivered” and ”Order Delivered to Customer” were merged
into Order Delivered to Customer for clarity.
– ”Customer Rated” was replaced by three more meaningful events:
Delivery Agent Rated, Restaurant Rated, and Overall Experience rated.

After refinement, the final set of domain events was reduced to a consistent
and a meaningful list that clearly represents the main business processes of MTOGO.

Final Refined Domain Events
Below is the final list of domain events for the MTOGO Food Delivery
System after refinement. All events are unique, clearly written in past
tense, and describe meaningful business actions in the system.
1. Customer Account Created – A new customer account was created.
2. Customer Verified – The customer verified their account.
3. Customer Logged In – The customer successfully logged in to their
account.
4. Restaurants Viewed – The customer viewed the list of available
restauranter.
5. Restaurant Selected – The customer selected a restaurant.
6. Menus Viewed – The customer viewed the restaurant’s menu.
7. Restaurant Registered – A restaurant registered on the MTOGO
platform.
8. Menu Submitted – The restaurant submitted its menu to the plat-
form.
9. Menu Published – The submitted menu was approved and made
available to customers.
10. Delivery Agent Profile Created – A new delivery agent profile
was created.
11. Order Created – A customer placed a new order.
12. Payment Initiated – A payment request was initiated with the
external service.
13. Payment Authorized – The payment was successfully processed.
14. Payment Failed – The payment could not be processed.
15. Order Confirmed – The system confirmed that the order was valid
and paid.
16. Order Cancelled – The order was cancelled by the customer or
restaurant.
17. Order Received by Restaurant – The restaurant received the
confirmed order.
18. Order Accepted – The restaurant accepted the order and began
preparation.
19. Order Rejected – The restaurant rejected the order.
20. Order Prepared – The restaurant completed food preparation.
21. Delivery Task Assigned – A delivery task was assigned to a de-
livery agent.
22. Agent Accepted Delivery Task – The agent accepted the assigned
delivery.
23. Agent Picked Up Order – The delivery agent collected the order
from the restaurant.
24. Agent Started Delivery – The delivery agent started the route to
the customer.
25. Order Delivered to Customer – The customer received the order.
26. Delivery Delayed – The order delivery took longer than expected.
27. Feedback Request Sent – The customer received a feedback re-
quest after delivery.
28. Customer Feedback Submitted – The customer provided feed-
back and ratings.
29. Delivery Agent Rated – The customer rated the delivery agent’s
service.
30. Restaurant Rated – The customer rated the restaurant’s food and
service.
31. Overall Experience Rated – The customer rated their overall ex-
perience.
32. Fee Calculated for Restaurant – The system calculated the restau-
rant’s service fee.
33. Fee Deducted from Restaurant – The system deducted the ser-
vice fee from the restaurant’s payment.
34. Bonus Calculated for Delivery Agent – The system calculated
the agent’s performance bonus.
35. Bonus Paid to Delivery Agent – The bonus was paid to the
delivery agent.
36. Order Metrics Updated – The system updated statistical data
about orders.
37. Performance Report Generated – A management report was
generated for monitoring purposes.
38. Complaint Submitted – A complaint was submitted by a customer
or restaurant.
39. Complaint Processed – The submitted complaint was reviewed
and resolved.


Phase 1: Big Picture:  Food Delivery System (MTOGO)
Goal
The goal of the Big Picture EventStorming phase is to explore the entire
business domain of the MTOGO food delivery system and create a shared
understanding of how the business works from end to end.
Color Legend Toolbox:
– Orange: Domain Events, key business happenings (in past tense)
– Yellow: Actors or Agents, people or teams performing actions
– Blue: Commands or Actions, triggers that cause events
– Pink: Systems and supporting tools or software
– Neon Pink: Hotspots or risks, issues, or unclear areas
– Green: Opportunities or possible improvements or ideas
– Red: Values or Constraints,  business rules or limits

Domain Area 1: Onboarding

Actors (Yellow): Customer, Restaurant Owner, Delivery Agent, Admin
Systems (Pink): Authentication System, Admin Dashboard
Domain Events (Orange): Customer Account Created; Customer Verified; Customer Logged In; Restaurant Registered; Restaurant Approved; Delivery Agent ProfileCreated.
Commands (Blue): Register Customer; Verify Customer Email; Register Restaurant; Approve Restaurant Registration; Create Delivery AgentProfile.
Hotspots (Neon Pink): Verification process may fail or delay activation. Manual approval of restaurants could slow onboarding.
Opportunities (Green): Automate verification using external identity APIs.
Constraints (Red): Customer and restaurant data must follow GDPR compliance.
Domain Area 2: Ordering
Actors (Yellow): Customer, Restaurant Owner, System
Systems (Pink): Menu Service, Order Management, Payment Gateway
Domain Events (Orange): Restaurants Viewed; Restaurant Selected; Menus Viewed; Order Created; Payment Initiated; Payment Authorized; Payment Failed; Order Confirmed; Order Cancelled.
Commands (Blue): Search Restaurants; View Menus; Add Item to Cart; Submit Order; Process Payment; Confirm Order.
Hotspots (Neon Pink): Handling failed payments. Managing restaurant availability in real time.
Opportunities (Green): Introduce personalized restaurant recommendations.
Constraints (Red): Orders can only be placed within delivery zones.
Domain Area 3: Fulfilment and Delivery
Actors (Yellow): Restaurant Staff, Delivery Agent, System
Systems (Pink): Restaurant Dashboard, Delivery Assignment System, GPS Tracking
Domain Events (Orange): Order Received by Restaurant; Order Accepted; Order Rejected; Order Prepared; Delivery Task Assigned; Agent Accepted Delivery Task; Agent Picked Up Order; Agent Started Delivery; Order Delivered to Customer; Delivery Delayed.
Commands (Blue): Accept Order; Prepare Order; Assign Delivery Agent; Start Delivery; Confirm Delivery.
Hotspots (Neon Pink): Agent unavailability during peak hours. Tracking delays or GPS errors.
Opportunities (Green): Optimize delivery routing using real-time traffic data.
Constraints (Red): Delivery must be completed within the estimated time window.
Domain Area 4: Post-Delivery
Actors (Yellow): Customer, Delivery Agent, Restaurant, System
Systems (Pink): Feedback Service, Notification System
Domain Events (Orange): Feedback Request Sent; Customer Feedback Submitted; Delivery Agent Rated; Restaurant Rated; Overall Experience Rated.
Commands (Blue): Send Feedback Request; Submit Feedback; Calculate Average Rating.
Hotspots (Neon Pink): Low participation in feedback collection.
Opportunities (Green): Offer discount coupons for submitted feedback.

Domain Area 5: Business and Finance
Actors (Yellow): Admin, Finance Team, System
Systems (Pink): Payment Gateway, Financial Dashboard
Domain Events (Orange): Fee Calculated for Restaurant; Fee Deducted from Restaurant; Bonus Calculated for Delivery Agent; Bonus Paid to Delivery Agent; Order Metrics Updated; Performance Report Generated.
Commands (Blue): Calculate Fees; Deduct Commission; Calculate Bonus; Generate Reports.
Hotspots (Neon Pink): Incorrect fee or bonus calculation logic.
Opportunities (Green): Add automated reporting to reduce manual effort.
Constraints (Red): All transactions must comply with financial regulations.

Domain Area 6: Complaints and Future Improvements
Actors (Yellow): Customer, Restaurant, Support Agent, Admin
Systems (Pink): Support Ticket System
Domain Events (Orange): Complaint Submitted; Complaint Processed.
Commands (Blue): Submit Complaint; Resolve Complaint.
Hotspots (Neon Pink): Long response times for customer issues.
Opportunities (Green): Implement chatbot-assisted initial support.

Output
The Big Picture phase provides a shared, end-to-end understanding of how
the MTOGO food delivery business operation from onboarding and ordering to delivery, feedback, and financial processing. This understanding will guide the next phase of EventStorming, where causes (commands), aggregates, and bounded contexts are refined.



Process Level EventStorming
Goal:
To dive deeper into how specific business processes work within each bounded context.
This level focuses on uncovering how domain events are triggered, what commands cause them, and what policies or rules govern the flow of operations.
Unlike the Big Picture, which captures the entire business landscape, the Process Level zooms into each process to clarify workflow logic, decision points, and automation rules.
Color Legend Toolbox:
Orange: Domain Event: Something that happened in the business domain (Order Placed, Delivery Completed)
Blue: Command:  An action someone or something wants to do(Pace Order, Assign Agent)
Lilac / Purple: Policy:  A business rule or automatic reaction to an event (When Order Placed then notify Restaurant)
Green: Read Model or View: Information prepared for reading or display (Active Orders Dashboard)      
Yellow: Actor Role: A person or system that performs a command (Customer, Restaurant, Delivery Agent)
Pink: External System: A system outside your domain (Payment Gateway, SMS Service)
Red: Hotspot / Problem:  Something unclear, risky, or frustrating (Unclear fee calculation rule)     
Bright Green: Opportunity / Improvement: A possible improvement or optimization (Automate restaurant notification)
Description:
 At the process level, we refine the domain events identified in the Big Picture.
 We trace why each event happens (its triggering command), and what happens next (policies and resulting events).
 This step makes invisible business logic visible and ensures that all workflows and rules are clearly modeled.
 It bridges the gap between business understanding and software design, helping teams align on how the system should behave.






Design Level (System and Software)
 
Goal:
To connect the business processes discovered in EventStorming with the actual software design and system structure.
Toolbox: Aggregate
At the design level, we focus on the internal structure of the system, finding aggregates, entities, value objects, and the software services that implement them. Each bounded context becomes a self-contained service with its own aggregates, rules, and data. Communication between services happens through domain events and policies, creating an event-driven, modular system that mirrors real business operations. This design guarantees scalability, maintainability, and alignment between business understanding and software implementation.
This approach helps divide the large system into smaller, focused parts called bounded contexts, where each context handles a specific business area.


 Context Map
The Context Map shows how the contexts that interact with each other through domain events and integrations.
 It connects both the system level design and the software-level structure. 
Each context in the map has its own aggregates, value objects, and policies.
 These contexts collaborate using events (like OrderDelivered or FeedbackSubmitted) or integrations (such as PaymentGateway) to keep the overall system consistent and responsive.
This design ensures:
Clear separation of responsibilities


Scalable and maintainable software structure


Event-driven communication between independent modules

  
The Customer Context manages how customers create and verify their accounts in the application. It starts when a new customer signs up using email or identity verification. The Customer aggregate keeps all customer information correct and secure during updates and verification. All data follows privacy and GDPR rules to protect user information.
The Ordering Context begins when a verified customer views menus and creates a shopping cart. It controls the entire purchase process,  from choosing items and placing an order to confirming the payment. The Cart and Order aggregates keep data consistent between item selection, payment authorization, and order confirmation. This context connects with the MenuService to read available menu data and with the PaymentGateway to handle secure payments. When an order is confirmed, it triggers an event that starts the Fulfillment Context.
The Fulfillment Context starts after an order is confirmed. It manages how restaurants prepare orders, making sure each one is accepted, prepared, and marked as ready within service time limits. The FulfillmentOrder aggregate keeps restaurant operations consistent. It connects with the RestaurantDashboard to show real time updates and reduce manual errors. When an order is ready this context sends an event to start the Delivery Context.
The Delivery Context begins when an order is prepared. It handles delivery agent assignment, delivery tracking, and completion confirmation. The DeliveryTask aggregate manages the full delivery lifecycle, from pickup to successful delivery. Integrations with DeliveryAssignmentService and GPS Tracking System support automatic agent selection and live tracking. When delivery is complete, it sends an OrderDelivered event that starts the Feedback Context.
The Feedback Context begins when delivery is confirmed. It manages customer ratings and feedback after each order. The Feedback aggregate ensures all feedback is linked to real orders and updates performance metrics through domain events. It connects with the Notification Context to send reminders and with the Financial and Dashboard Contexts to support bonus and performance calculations.
The Financial Context starts when it receives completed order and feedback data. It handles all financial operations, including restaurant commissions and delivery agent bonuses. The Transaction aggregate manages fee deductions and payouts. This context integrates with the PaymentGateway to process payments and with the ReportingService to update the dashboard. It triggers FeeCalculated and BonusCalculated events to share financial updates with other parts of the system.
The Support Context manages customer complaints. It starts when a customer submits a complaint about a completed order. The Complaint aggregate handles the full process  from submission and assignment to resolution. Policies automatically route issues to the right place (restaurant or admin). It integrates with the Notification Context to inform users about complaint updates. All complaint data follows privacy and data retention policies.
The Restaurant Context manages restaurant onboarding, profile setup, and menu publication. It includes the Restaurant and Menu aggregates, which keep restaurant information and menu data consistent. It connects with the RestaurantDashboard for management and with the MenuService to publish menus to the Ordering Context. When a restaurant is approved, it can create or update menus, triggering events to refresh customer menu views.
The Notification Context handles all system messages sent to customers, restaurants, and delivery agents. It listens to domain events like OrderConfirmed, PaymentFailed, and ComplaintResolved. The Notification aggregate manages message templates, delivery channels, and user preferences. Integrations with email, SMS, and push services ensure all notifications are sent reliably and match valid events.
The Dashboard Context provides a full view of the business, showing sales, performance, and financial results. It listens to events like FeeCalculated, FeedbackSubmitted, and OrderDelivered. These are processed by the ReportingService, which updates KPI and reports in the Dashboard aggregate. Policies make sure all dashboards update automatically whenever key events happen, giving management real-time insight into system performance.

The MenuService is a shared read only service that stores published menu data from restaurants. It allows the Ordering Context to display up to date menu information to customers without directly accessing restaurant data.

The PaymentGateway is an external integration used for secure payment processing.
It authorizes customer payments during order confirmation and later processes commission and payout transactions from the Financial Context

Tactical Design
After identifying the bounded map  in our application we now move to tactical design. Tactical design focuses on the internal structure of each context and how data is organized. It shows the main entities, and their mutable attributes, and the value objects that encapsulate important domain information.
1. Customer Context
Aggregate: Customer
 Entity Attributes (mutable):
customerId
Full Name
email
password
accountStatus (Active, Suspended)
verificationStatus (Pending, Verified)

Value Objects (immutable, with rules):
ContactInfo (email, phone – must be valid format)
Address (street, city, postalCode:  cannot be empty)
PreviousOrders (a list of order read-only once added)


orderId


restaurantName


orderDate


totalAmount


deliveryStatus


2. Ordering Context
1 Aggregates: Order
Entity Attributes (mutable):
orderId
customerId
restaurantId
orderStatus (Pending, Confirmed, Cancelled)
paymentStatus (Authorized, Failed)
totalAmount
orderDate
deliveryTime
Value Objects (immutable, with rules):
OrderItem (itemName, quantity, price:  quantity must not be 0 or below)
PaymentInfo (cardNumber, transactionId :  must be valid)
2 Aggregate: Cart
Entity Attributes (mutable):
cartId


customerId


restaurantId 


totalPrice


status (Active, CheckedOut, Abandoned)


createdAt


updatedAt


These are mutable because:
the customer can add/remove items,


the total and status change during checkout.


Value Objects (immutable, with rules):
CartItem
itemId
itemName
quantity (must be over 0)
pricePerUnit (must be over 0)
total = quantity × price

3. Fulfillment Context
Aggregate: FulfillmentOrder
Entity Attributes (mutable):
fulfillmentId
orderId
restaurantId
status (Accepted, InProgress, Prepared)
preparationTime
assignedStaff
Value Objects (immutable, with rules):
PreparationDetails (itemList, estimatedTime)
KitchenNote (specialInstruction: optional text)
4. Delivery Context
Aggregate: DeliveryTask
Entity Attributes (mutable):
deliveryId
orderId
restaurantId
customerId
agentId
deliveryStatus (Assigned, PickedUp, Delivered)
pickupTime
deliveryTime
Value Objects (immutable, with rules):
Location (latitude, longitude: must be valid coordinates)
TrackingInfo (route, startTime, endTime:  read-only once logged)

5. Feedback Context
Aggregate: Feedback
Entity Attributes (mutable):
feedbackId
orderId
customerId
restaurantId
rating
comment
feedbackDate
Value Objects (immutable, with rules):
RatingValue (1–5 range only)
CommentText (max 250 characters)

6. Financial Context
Aggregate: Transaction
Entity Attributes (mutable):
transactionId
orderId
restaurantId
agentId
amount
transactionType (Commission, Bonus, Refund)
transactionStatus (Pending, Completed, Failed)
transactionDate
Value Objects (immutable, with rules):
MoneyAmount (must be positive)
CommissionRate (0–100%)
BonusRate (0–100%)

7. Support Context
Aggregate: Complaint
Entity Attributes (mutable):
complaintId
orderId
customerId
restaurantId
complaintType (DeliveryIssue, RestaurantIssue)
complaintStatus (Submitted, InProgress, Resolved)
assignedTo
resolutionNote
Value Objects (immutable, with rules):
ComplaintType (must be valid domain type)
ComplaintStatus

8. Restaurant Context
Aggregate: Restaurant
Entity Attributes (mutable):
restaurantId


name


ownerName


email


phone


address


approvalStatus (Pending, Approved, Suspended)


operatingHours


Value Objects (immutable, with rules):
RestaurantProfile (name, address, contactInfo: must be valid)



Aggregate: Menu
Entity Attributes (mutable):
menuId


restaurantId (restaurant owns it)


version (showing updates)


status (Draft, Published, Archived)


Value Objects (immutable, with rules):
MenuItem (itemId, name, description, price over 0)


Category (Drinks, Meals, Desserts)


9. Notification Context
Aggregate: Notification
Entity Attributes (mutable):
notificationId
messageType (OrderConfirmed, PaymentFailed, ComplaintResolved)
recipientId (customerId, restaurantId, agentId)
status (Pending, Sent, Failed)
sentTime
Value Objects (immutable, with rules):
MessageTemplate (subject, body)
DeliveryChannel (Email, SMS, Push: valid type only)

10. Dashboard Context
Aggregate: Dashboard
Entity Attributes (mutable):
dashboardId
reportDate
totalOrders
totalRevenue
averageRating
totalComplaints
topRestaurants
Value Objects (immutable, with rules):
KPI (name, value – value must be numeric)
MetricPeriod (Daily, Weekly, Monthly)
PerformanceScore (0–100 range)

Ubiquitous Language

Ubiquitous Language means using the same simple words for business people, developers, and designers to describe how the system works. It comes from events, entities, and actions that everyone in the project understands the same way. Each term should be short, easy to explain, and linked to one or more contexts.

Nr.
Term / Event
Meaning
Bounded Context
1
Customer Account Created
A new customer signed up on the MTOGO app.
Customer
2
Customer Verified
The customer’s identity or email was confirmed.
Customer
3
Customer Logged In
The customer successfully accessed their account.
Customer
4
Restaurants Viewed
The customer looked at the list of restaurants.
Ordering
5
Restaurant Selected
The customer chose a restaurant to view or order from.
Ordering
6
Menus Viewed
The customer opened a restaurant’s menu.
Ordering
7
Restaurant Registered
A restaurant signed up on the MTOGO platform.
Restaurant
8
Menu Submitted
The restaurant created and sent a new menu for approval.
Restaurant
9
Menu Published
The approved menu was made visible to customers.
Restaurant - MenuService
10
Delivery Agent Profile Created
A new delivery agent joined the company
Delivery
11
Order Created
The customer placed a new order using their cart.
Ordering
12
Payment Initiated
The system sent a payment request to the PaymentGateway.
Ordering
13
Payment Authorized
The payment was confirmed as successful.
Ordering - Financial
14
Payment Failed
The payment could not be completed.
Ordering
15
Order Confirmed
The system marked the order as paid and valid.
Ordering
16
Order Cancelled
The order was cancelled by either the customer or restaurant.
Ordering
17
Order Received by Restaurant
The restaurant got confirmed order details.
Fulfillment
18
Order Accepted
The restaurant accepted and started preparing the order.
Fulfillment
19
Order Rejected
The restaurant declined the order request.
Fulfillment
20
Order Prepared
The restaurant finished preparing the order.
Fulfillment
21
Delivery Task Assigned
The system assigned the delivery to an available agent.
Delivery
22
Agent Accepted Delivery Task
The agent accepted the assigned delivery.
Delivery
23
Agent Picked Up Order
The agent collected the order from the restaurant.
Delivery
24
Agent Started Delivery
The agent began traveling to the customer.
Delivery
25
Order Delivered to Customer
The customer received the order successfully.
Delivery
26
Delivery Delayed
The delivery took longer than expected.
Delivery
27
Feedback Request Sent
The system asked the customer to give feedback.
Notification - Feedback
28
Customer Feedback Submitted
The customer rated their delivery and experience.
Feedback
29
Delivery Agent Rated
The customer rated the delivery agent’s performance.
Feedback
30
Restaurant Rated
The customer rated the restaurant’s food and service.
Feedback
31
Overall Experience Rated
The customers rated their whole MTOGO experience.
Feedback
32
Fee Calculated for Restaurant
The system calculated how much commission the restaurant pays.
Financial
33
Fee Deducted from Restaurant
The system deducted that fee from the restaurant’s balance.
Financial
34
Bonus Calculated for Delivery Agent
The system calculated a bonus for a high-performing agent.
Financial
35
Bonus Paid to Delivery Agent
The bonus was sent to the agent’s account.
Financial
36
Order Metrics Updated
The system updated data about total orders and performance.
Dashboard
37
Performance Report Generated
The dashboard created a management report.
Dashboard
38
Complaint Submitted
A customer or restaurant reported a problem.
Support
39
Complaint Processed
The complaint was reviewed, handled, and resolved.
Support








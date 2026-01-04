## Software Quality Exam - MTOGO Application 

MTOGO Software Solutions develops, tests, and maintains all software entirely in-house to ensure full control over quality, performance, security, and maintainability.
Documents containing the Test Strategy, Test Design, and two separate Test Plans for both the Legacy Monolithic System and the new Microservice Platform of the MTOGO application can be found in this repository.
Both systems share the same business goals, but they are different in architecture and testing focus:

*	Legacy Monolithic System:
 Legacy system: is tested to find weaknesses, improve stability, and prepare for migration to the new microservice system.
*	Microservice Platform:
 Designed for scalability, automation, and continuous testing through CI/CD pipelines.

This document demonstrates how a single in-house Software Quality Assurance (SQA) framework can be applied to different architectures while maintaining consistent quality standards.
Regular checkpoints, automated tests, and review meetings ensure that changes are verified before release and that no new issues are introduced.
By using multiple testing strategies and tracking metrics like coverage, defect density, and response time, the team maintains transparency and control over software quality.
This structured SQA process helps reduce long-term maintenance costs, increase reliability, and support continuous improvement for both the legacy and microservice systems.




## Equivalence Partitioning and Boundary Value Analysis – The Commission Model

### Overview
The `CommissionCalculatorTest` class applies Equivalence Partitioning (EP) and Boundary Value Analysis (BVA) to verify that commission and fee calculations in the Financial Microservice behave correctly under both normal and edge-case conditions.

### Testing Approach
- **Testing Level:** Unit Testing
- **Techniques Applied:**
  - Equivalence Partitioning (EP)
  - Boundary Value Analysis (BVA)
- **Framework:** JUnit 5
- **Scope:** Commission calculation logic only, with no external dependencies

### Commission Model Explanation
The commission calculation used by the MTOGO system is based on an incremental fee structure rather than a flat percentage.

The total amount for each order is divided into defined ranges, and within each range, is charged a different commission rate:

- The first 100 of the order amount is charged at 6%
- The portion from 101 to 500 is charged at 5%
- The portion from 501 to 1000 is charged at 4%
- Any amount above 1000 is charged at 3%

#### Example: Order Amount = 700
- 6% is applied to the first 100
- 5% is applied to the portion from 101 to 500
- 4% is applied to the portion from 501 to 700

### Equivalence Partitions (EP)

| EP  | Order Amount Range |
|-----|--------------------|
| EP1 | ≤ 100 |
| EP2 | 101 – 500 |
| EP3 | 501 – 1000 |
| EP4 | > 1000 |

Random values from each partition are tested to ensure correct commission calculations within each range.

### Boundary Value Analysis (BVA)

| Boundary Case | Order Amount |
|--------------|--------------|
| Edge Case 1 | 100 |
| Edge Case 2 | 500 |
| Edge Case 3 | 1000 |

The tests include:
- Boundary values
- Values just below the boundary
- Values just above the boundary

This ensures correct behavior when commission calculation rules changes. 

# RedPulse - Blood Donor Management Backend

Backend service powering the **RedPulse** voluntary blood donation platform. Built with Java and Spring Boot to manage donor profiles, blood inventory records, urgent donation requests, and geographical matching.

## Key Features

- **Donor Registration & Profiles**: Secure donor onboarding with blood type, contact details, and last donation history.
- **Emergency Blood Requests**: Hospital and patient request logging with urgency flags.
- **Search & Filtering**: Query available donors by blood group (A+, A-, B+, B-, AB+, AB-, O+, O-) and district/location.
- **RESTful Endpoints**: Clean JSON API architecture following REST conventions.

## Tech Stack

- **Language**: Java 17+
- **Framework**: Spring Boot / Maven (`pom.xml`)
- **Data Layer**: Spring Data JPA / Hibernate
- **Build Tool**: Apache Maven (`mvnw`)

## Getting Started

```bash
# Clone the repository
git clone https://github.com/aminur167/project-redpulse-new.git
cd project-redpulse-new

# Build with Maven wrapper
./mvnw clean compile

# Run the backend service
./mvnw spring-boot:run
```

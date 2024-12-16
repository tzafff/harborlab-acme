# Harbor Lab Acme Booking Service

This is a Spring Boot application designed to handle room booking management. Users can create, retrieve, and cancel room bookings. The project includes the ability to validate booking requests, check for room availability, and prevent booking conflicts.

## Prerequisites

To run this application, you need the following installed on your machine:

- **JDK 17 or higher**
- **Maven** 3.8.5 or higher
- **Database** MySQL

## Installation

### Step 1: Clone the repository

Clone the project from GitHub to your local machine.

```bash
git clone https://github.com/tzafff/harborlab-acme.git
cd harborlab-acme
```

### Step 2: Set up the Database

Make sure you have a database such as MySQL running. Create a new database for this project (e.g., acme).

### Step 3: Configure the Database connection
In the src/main/resources/application.properties file, update the database connection properties:

```bash
spring.application.name=harborlab-acme
spring.datasource.url=jdbc:mysql://localhost:3306/acme
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Step 4: Build the application
To build the application, run the following command in the root directory of the project:

```bash
mvn clean install
```

### Step 5: Run the application
After the build is successful, you can run the application with the following command:

```bash
mvn spring-boot:run
```


## Running the Application with Docker Compose

You can use Docker Compose to run both the MySQL database and the application together. Follow these steps:

## To build and start both the MySQL and the application services, run the following command:

```bash
docker-compose up --build
```

### And then deploy: 

```bash
docker-compose up 
```

### If that not working, you could first run the mysql and then the application

### Step 1:
```bash
docker-compose up mysql
```

### Step 2:

```bash
docker-compose up app
```



## API Documentation

### Endpoints:

1. **Create Booking:**
    - **POST** `/api/bookings/create`
    - Request body:
      ```json
      {
        "room": "Room A",
        "email": "example@example.com",
        "date": "2024-12-14",
        "timeFrom": "09:00",
        "timeTo": "10:00"
      }
      ```
    - Description: Creates a new booking for a room.

2. **Get Bookings by Room and Date:**
    - **GET** `/api/bookings`
    - Query parameters:
        - `room` (string) - The name of the room.
        - `date` (date) - The date of the booking in `YYYY-MM-DD` format.
    - Example:
      ```
      /api/bookings?room=Room A&date=2024-12-14
      ```
    - Description: Retrieves all bookings for a specific room on a specific date.

3. **Get All Bookings:**
    - **GET** `/api/bookings/all`
    - Description: Retrieves all bookings in the system.

4. **Cancel Booking:**
    - **DELETE** `/api/bookings/{id}`
    - Path variable: `id` - The ID of the booking to cancel.
    - Description: Cancels an existing booking based on its ID.

### Example Responses:

1. **Create Booking Response:**
    - Status: `201 Created`
    - Response body:
      ```json
      {
        "email": "example@example.com",
        "timeFrom": "09:00",
        "timeTo": "10:00"
      }
      ```

2. **Get Bookings Response:**
    - Status: `200 OK`
    - Response body (example):
      ```json
      [
        {
          "email": "example@example.com",
          "timeFrom": "09:00",
          "timeTo": "10:00"
        },
        {
          "email": "another@example.com",
          "timeFrom": "10:00",
          "timeTo": "11:00"
        }
      ]
      ```

3. **Get All Bookings Response:**
    - Status: `200 OK`
    - Response body:
      ```json
      [
        {
          "email": "example@example.com",
          "timeFrom": "09:00",
          "timeTo": "10:00"
        },
        {
          "email": "another@example.com",
          "timeFrom": "10:00",
          "timeTo": "11:00"
        }
      ]
      ```

4. **Cancel Booking Response:**
    - Status: `200 OK`
    - Description: The booking was successfully canceled, no content returned.

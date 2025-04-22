# 🧑‍💼 WorkSphere - Employee Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blueviolet.svg)](https://www.mysql.com/)

Welcome to **WorkSphere**, a modern Employee Management System built using Spring Boot. This system is developed to streamline HR operations, making it easier to manage employee data, track attendance and leaves, and handle user accounts in an organized, secure, and scalable way.

![Image](https://github.com/user-attachments/assets/4e9f26f2-fb5d-40bc-9e5a-5b30907be41f)

---

## 📋 Project Description

**WorkSphere** provides a centralized solution for employee-related tasks including profile management, leave tracking, system access provisioning, and attendance reporting. With a clean UI and dynamic functionalities, it ensures a smooth HR workflow for any organization.

This standalone web application includes real-time features like dynamic birthday highlights, role-based access control, email-integrated account creation, and more — all built on a solid backend powered by Spring Boot and MySQL.

---

## 🌟 Key Features

### 👥 Employee Profile Management
- Add, update, and delete employee records.
- Store comprehensive personal details including name, contact information, department, position, salary, birthday, and profile photo.

### 📆 Leave Management
- Employees can view their leave balance, apply for leaves, and check the status of their requests.
- Managers can manage and approve/reject leave requests.
- Filter and track leave usage, balance, and different leave types (SICK, CASUAL, ANNUAL).
- Dynamic updates to leave summaries upon approval or rejection, powered by backend integration.

### 🎂 Birthday Reminders
- A dedicated section on the dashboard displays upcoming employee birthdays in a styled table for easy visibility.
- A modal popup provides a comprehensive list of all employee birthdays.

### 🕓 Attendance Filtering
- Allows users to filter attendance records based on a specified date range.
- Results are dynamically appended to HTML tables, providing an interactive way to review attendance data.

### 👤 User Account Provisioning
- Enables administrators to create new user accounts for employees who do not yet have login credentials.
- Automatically generates secure, random passwords for new accounts.
- Sends the generated password securely to the employee's registered email address.
- Implements role-based access management (Admin and User) to control system functionalities based on user roles.

### 📈 Dashboard Overview *(Optional Enhancement)*
- *(Planned)* A future enhancement to include a dashboard displaying key HR metrics such as the total number of employees, department-wise employee counts, overall leave statistics, and more.

---

## 🛠️ Technologies Used

- **Backend**:
    - [Spring Boot](https://spring.io/projects/spring-boot) - A powerful framework for building Java-based web applications.
    - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Simplifies data access and interaction with the database.
    - [Hibernate](https://hibernate.org/) - An Object-Relational Mapping (ORM) tool for database interaction.
- **Frontend**:
    - HTML - The standard markup language for creating web pages.
    - CSS - Used for styling the HTML elements and providing a visually appealing interface.
    - [Bootstrap](https://getbootstrap.com/) - A popular CSS framework for responsive and mobile-first web development.
    - JavaScript (Vanilla JS) - For implementing dynamic and interactive frontend functionalities without relying on external libraries.
- **Database**:
    - [MySQL](https://www.mysql.com/) - A widely used open-source relational database management system.
- **Security**:
    - [Spring Security](https://spring.io/projects/spring-security) - Provides comprehensive security features for Spring applications.
    - BCrypt password encoding - A strong hashing algorithm used to securely store user passwords.
- **Email Service**:
    - [JavaMailSender](https://javaee.github.io/javamail/) (via SMTP configuration) - Enables the application to send emails, such as for account creation and password resets.
- **Architecture**:
    - Layered Architecture (Controller, Service, Repository, DTO) - Ensures a well-organized and maintainable codebase by separating concerns.

---

## 🚀 Getting Started

Follow these steps to set up and run the WorkSphere Employee Management System on your local machine.

### 📦 Clone the Repository

First, clone the project repository from GitHub:

```bash
git clone [https://github.com/your-username/worksphere-employee-management.git](https://github.com/your-username/worksphere-employee-management.git)
cd worksphere-employee-management
```

## ⚙️ Backend Setup

To set up the backend of WorkSphere, follow these steps:

1.  **Open the Project:** Open the cloned project directory in your preferred Java IDE, such as IntelliJ IDEA or Eclipse.

2.  **Create MySQL Database:** Ensure you have MySQL installed and running. Create a new database for the application. For example:

    ```sql
    CREATE DATABASE worksphere_db;
    ```

3.  **Configure Database Credentials:** Navigate to the `src/main/resources` directory and open the `application.properties` file. Update the database connection properties with your MySQL server details, username, and password:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/worksphere_db?serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.show-sql=true
    spring.jpa.format_sql=true
    ```

    **Note:** Adjust the `spring.datasource.url` if your MySQL server is running on a different host or port. The `spring.jpa.hibernate.ddl-auto=update` property will automatically update your database schema based on the entity definitions. In a production environment, consider using `validate` or migrations for schema management.

4.  **Run the Application:** You can run the Spring Boot application in several ways:

    -   **Using Maven:** Open a terminal in the project root directory and run the following command:

        ```bash
        ./mvnw spring-boot:run
        ```

    -   **From your IDE:** Most Java IDEs (IntelliJ IDEA, Eclipse) provide a straightforward way to run Spring Boot applications by locating the main application class (usually annotated with `@SpringBootApplication`) and running it.

5.  **Access the Application:** Once the application has started successfully, open your web browser and navigate to the default address:

    ```
    http://localhost:8080
    ```

---

## 📸 Screenshots

#### 🧑‍💼 Employee Dashboard

![Image](https://github.com/user-attachments/assets/08f1a002-e2b0-44a8-b9d9-677f0761ca97)

#### 📋 Attendance

![Image](https://github.com/user-attachments/assets/e094b5a6-7c95-4e1f-a346-60cbeaf61e87)

#### 📆 Leave Summary

![Image](https://github.com/user-attachments/assets/0772b916-e6ea-49ec-ae22-761439392d6b)

#### 👨🏻‍💻 Admin Dashboard

![Image](https://github.com/user-attachments/assets/25e43de8-c484-4d28-93ea-813b07a3d49c)

### 📧 Emails

![Image](https://github.com/user-attachments/assets/6f578985-0ea9-4f14-ac03-c76d2b791e66)

### 🔐 Forgot Password

![Image](https://github.com/user-attachments/assets/af0fb2e3-2f1d-427c-a723-ac29ecf002a2)

---

## 🎥 Demo Video

Watch a full walkthrough and demonstration of the WorkSphere Employee Management System on YouTube:

https://youtu.be/a_9rHUQzvUM

---

## 🎯 Objectives

This project was developed with the following objectives in mind:

✔️   Apply real-world Object-Relational Mapping (ORM) concepts effectively using Spring Data JPA and Hibernate.

✔️   Build reusable and maintainable backend components following a layered architecture (Controller, Service, Repository, DTO).

✔️   Implement secure development practices, including password encryption using BCrypt and input validation.

✔️   Deliver a clean, intuitive, and user-friendly HR management tool.

---

## 💡 Future Enhancements

The following enhancements are planned for future versions of WorkSphere:

* 📊   **Analytics Dashboard with Charts:** Implement visual representations of HR data, such as leave trends, department-wise employee distributions, and other relevant metrics.
* 📂   **Export Reports:** Allow users to export various reports in common formats like PDF and Excel.
* 📆   **Integration with Google Calendar:** Integrate employee birthdays and approved leaves with Google Calendar for better scheduling and awareness.
* 🧑‍🍳   **Multilingual Support and Theming:** Add support for multiple languages and allow users to customize the application's theme.

---

## 🧠 Learning Outcomes

By exploring and contributing to this project, you can gain practical experience in:

* Implementing CRUD (Create, Read, Update, Delete) operations with Spring Boot and Hibernate.
* Utilizing Spring Data JPA repositories for simplified data access.
* Performing dynamic Document Object Model (DOM) manipulation using JavaScript.
* Implementing real-time UI updates driven by backend API interactions.
* Implementing role-based security using Spring Security.
* Integrating email functionality using JavaMailSender and SMTP.

---

## 💬 Contact

Feel free to connect with me for any suggestions, improvements, or potential collaboration opportunities:

* 🌐   Facebook
* 🔗   LinkedIn
* 👾   Reddit

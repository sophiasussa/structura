# Marble Business Management System

An open-source web application for marble and stone business management, focused on operational administration and workflow organization.

Built with Java and Vaadin, the platform helps businesses manage service orders, projects, products, clients, suppliers, employees, and daily operations through a centralized system.

---

## Key Features

- Main Dashboard with: daily service orders, scheduled tasks, low-stock products
- CRUD Modules: service orders, projects, products, tasks, clients, suppliers, employees

---
 
## Technologies Used

- Java 17
- Spring Boot
- Vaadin
- MySQL
- Maven

---

## Video Demo

Watch a demo of the system in action:
[https://youtu.be/ikaWbtQyMxw](https://youtu.be/ikaWbtQyMxw)

---

## System Screenshots

Here are some screenshots of the application.

### Main Dashboard
![Home](docs/assets/dashboard.png)

### Suppliers
![View all suppliers](docs/assets/fornecedores.png)

### Product
![Product registration](docs/assets/produtos.png)

### Service Orders
![View all service orders](docs/assets/os.png)

---

## Project Structure

```
src/main/java/com/example/application/
├── controller/     # Handles HTTP requests and application flow
├── model/          # Domain models and entities
├── repository/     # Database access layer
├── utils/          # Helper and utility classes
├── views/          # Vaadin UI components and views
└── Application.java  # Spring Boot application entry point
```

---

## How to Run

This is a standard Maven project. To run it from the terminal, type `mvnw` (Windows) or `./mvnw` (Mac & Linux), then open http://localhost:8080 in your browser.
You can also import the project into your preferred IDE like any Maven project.

## Database

The script to create the database is available in the [`/database`](./database/schema.sql) folder.
You can import this file into any local MySQL instance or container to run the project correctly.

## Production Build

To generate a production build, run `mvnw clean package -Pproduction` (Windows) or `./mvnw clean package -Pproduction` (Mac & Linux).
This will create a JAR file containing all dependencies and front-end resources, ready for deployment. The file will be in the `target` folder after the build completes.

After the JAR is generated, you can run it using:
`java -jar target/myapp-1.0-SNAPSHOT.jar`
(Note: replace `myapp-1.0-SNAPSHOT.jar` with your actual JAR file name.)

---
## Contributing

Contributions are welcome. Feel free to open issues, submit pull requests, or suggest improvements.

---
## Project History

This project was originally developed as a university Capstone Project (TCC) and later released as an open-source initiative.

---
## License
This project is licensed under the MIT License.

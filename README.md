# CityQuest

## About
CityQuest is an interactive webapp designed to enhance the experience of tourists and locals exploring Vienna. By gamifying the exploration process, CityQuest offers a unique way to discover the city's key spots, track progress through a personal profile view, and earn rewards for each visited location.

https://github.com/ProjectCityQuest/CityQuest/assets/39237785/382408f0-28f0-43ff-938a-cf95f9f517a1

## Getting Started

### Prerequisites
- JDK 11 or later
- Maven
- Node.js and npm
- An IDE like IntelliJ IDEA or VSCode

### Backend Setup
1. Navigate to the `backend` directory:
    ```bash
    cd backend
    ```
2. Use Maven to install the dependencies:
    ```bash
    mvnw clean install
    ```
3. Run the application:
    ```bash
    mvnw spring-boot:run
    ```
4. The backend server will start on `http://localhost:8080`.

### Frontend Setup
1. Navigate to the `frontend` directory:
    ```bash
    cd frontend
    ```
2. Install the dependencies using npm:
    ```bash
    npm install
    ```
3. Start the development server:
    ```bash
    npm run dev
    ```
4. Access the frontend application on `http://localhost:3000`.

## Project Structure
- `backend/` - Contains Spring Boot application with REST API endpoints.
  - `.mvn/` - Maven wrapper for building the project.
  - `src/` - Source code for the backend application.
    - `main/java/com/example/backend/` - Application source files.
    - `main/resources/` - Configuration files and static resources.
    - `test/` - Unit and integration tests.
- `frontend/` - Vue.js application for the user interface.
  - `public/` - Public assets like images and icons.
  - `src/` - Source code for the frontend application.
    - `assets/` - Static assets (images, fonts, styles).
    - `components/` - Vue components.
    - `router/` - Vue Router configurations.
    - `views/` - Vue components for pages.

## API Documentation
Refer to `openapi.yaml` for the OpenAPI specifications detailing available REST endpoints and their usage.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any bugs, features, or improvements.

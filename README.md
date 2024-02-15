<h1>Description</h1>

The backend part of the application is implemented using the Spring Boot framework, providing a robust and scalable architecture for processing business logic and managing data. The backend consists of a RESTful API that facilitates communication between the user interface and the database.

For user authentication and authorization, Spring Security is implemented, providing security mechanisms for managing access to application resources. Users access the API through JWT (JSON Web Token) authentication, enabling secure and efficient authentication via tokens sent in the HTTP request headers. Additionally, support for OAuth2 authentication is implemented, allowing users to log in to the system using their GitHub account. This provides users with additional flexibility and security when logging in to the application.

Furthermore, support for CORS (Cross-Origin Resource Sharing) and CSRF (Cross-Site Request Forgery) protection is implemented, ensuring secure interaction between the frontend and backend parts of the application, as well as preventing CSRF attacks.

Every request passing through the backend part of the application undergoes validation for SQL injection and XSS (Cross-Site Scripting) attacks. This ensures that the application is resilient to these types of attacks, safeguarding user data and system integrity.

HTTPS connection is enabled to ensure security during communication between the client and server.

In case of attack detection, the user's JWT token is automatically blacklisted. Tokens are automatically deleted from the database after their expiration time, ensuring that unnecessary expired tokens are not stored and maintaining the cleanliness of the database.

All security-sensitive events are logged using the SIEM (Security Information and Event Management) component. This enables tracking and analysis of all security-sensitive information in the application to detect and respond to potential threats.

# E-COMMERCE REST API
## Descripción
Este proyecto es una API REST en desarrollo para un sistema de e-commerce.
## Tecnologías
### unitarias, integracion, rendimiento, funcionales, 
- **Java** 
  - **Spring Boot**: Framework para el desarrollo de aplicaciones web.
    - **Spring Data JPA**: Framework para el mapeo objeto-relacional ( Hibernate ).
    - **Spring Security**: Modulo para la seguridad de la aplicación (Autenticación, Autorización, Vulnerabilidades, etc).
    - **Spring Web**: Modulo para soporte de aplicaciones web (MVC, REST, etc).
- 

### Testing
- **Java**
  - **JUnit**: Framework para realizar pruebas unitarias, de integración, etc.
  - **AssertJ**: Framework para realizar aserciones.
  - **Mockito**: Framework para realizar pruebas unitarias con mocks.
  - **Spring Boot Test**: Framework para realizar pruebas de integración.

## Estructura del proyecto


```mermaid
graph TD
    A[Cliente] --> B[Controlador]
    B --> C[Servicio]
    C --> D[Repositorio]
    D --> E[Base de datos]
    E --> D
    D --> C
    C --> B
    B --> A
```
# E-COMMERCE REST API

## Como ejecutarlo

## Descripción

Este proyecto es una API REST en desarrollo para un sistema de e-commerce.

## Tecnologías

- **Java**
    - **Spring Boot**: Framework para el desarrollo de aplicaciones web.
        - **Spring Data JPA**: Framework para el mapeo objeto-relacional ( Hibernate ).
        - **Spring Security**: Modulo para la seguridad de la aplicación (Autenticación, Autorización, Vulnerabilidades,
          etc).
        - **Spring Web**: Modulo para soporte de aplicaciones web (MVC, REST, etc).
    - **Testing**
        - **JUnit**: Framework para realizar pruebas unitarias, de integración, etc.
        - **AssertJ**: Framework para realizar aserciones.
        - **Mockito**: Framework para hacer mock de dependencias.
        - **Spring Boot Test**: Framework para realizar pruebas de integración.


- **Base de datos**
    - **PostgreSQL**: RDBMS.
    - **Cloud Storage for Firebase**: Servicio cloud para almacenar archivos binarios (imagenes, videos, etc).


- **Documentación**
    - **OpenAPI Specification**: Especificación estandar para documentar APIs HTTP.

## Estructura del proyecto

### Simple

![simple](docs/svg/simple-component-diagram.svg)

#### Modulos y su funcionalidad:

- **_bootstrapping_**:
    - Llevantamiento de la aplicacion
    - Configuracion de entornos
        - produccion
        - desarrollo
        - pruebas
    - Mapeo de codigos de error
    - Entrada y salida HTTP
    - Pruebas Funcionales


- **_application_**:
    - Logica de negocio
        - Coordinacion entre los modulos (de bajo nivel)


- **_user_**:
    - Gestion de usuarios
        - Creacion
        - Actualizacion
        - Eliminacion
        - Consultas en general


- **_product_**:
    - Gestion de productos
        - Creacion
        - Actualizacion
        - Eliminacion
        - Consultas en general


- **_email_**:
    - Envio de emails
    - Gestion de codigo de verificacion


- **_security_**:
    - Autenticacion
    - Autorizacion
    - Proteccion contra vulnerabilidades
    - Gestion de sesiones


- **_payment_**:
    - Gestion de pagos
        - ~~Integracion con pasarelas de pago~~
        - Creditos internos

- **_order_**:
    - Gestion de ordenes


- **_file_**:
    - Gestion de archivos
        - imagenes
            - de productos


- **_deposit_**:
    - Gestion de depositos
        - Recarga de creditos internos por transferencia bancaria


- **_cart_**:
    - Gestion de carritos de compras

### Ejemplo de flujo de aplicacion

- Diagrama de clases de [EmailController](bootstrapping/src/main/java/org/cris6h16/Controllers/EmailController.java)

```mermaid
classDiagram
    class EmailController {
    }
    class EmailFacade {
        <<interface>>
    }
    class EmailFacadeImpl {
    }
    class EmailComponent {
        <<interface>>
    }
    class UserComponent {
        <<interface>>
    }

    EmailController --> EmailFacade
    EmailFacade <|-- EmailFacadeImpl
    EmailFacadeImpl --> UserComponent
    EmailFacadeImpl --> EmailComponent
```

- Diagrama de clases
  de [UserController](bootstrapping/src/main/java/org/cris6h16/Controllers/UserController.java) & [AuthenticationController](bootstrapping/src/main/java/org/cris6h16/Controllers/AuthenticationController.java) (
  completo)

```mermaid
classDiagram
    class UserController {
    }
    class AuthenticationController {
    }

    class UserFacade {
        <<interface>>
    }

    class UserFacadeImpl {
    }

    class EmailComponent {
        <<interface>>
    }

    class EmailComponentImpl {
    }

    class UserComponent {
        <<interface>>
    }

    class UserComponentImpl {
    }

    class SecurityComponent {
        <<interface>>
    }

    class SecurityComponentImpl {
    }

    class UserValidator {
    }

    class EmailSender {
    }
    class EmailValidator {
    }
    class VerificationCodeRepository {
        <<interface>>
    }
    class VerificationCodeGenerator {
    }

    class VerificationCodeEntity {
    }

    class UserRepository {
        <<interface>>
    }

    class AuthorityRepository {
        <<interface>>
    }

    class UserEntity {
    }
    class AuthorityEntity {
    }
    class PasswordEncoder {
    }
    class JwtUtils {
    }
    UserController --> UserFacade
    AuthenticationController --> UserFacade
    UserFacadeImpl --> EmailComponent
    UserFacadeImpl --> UserComponent
    UserFacadeImpl --> SecurityComponent
    EmailComponent <|-- EmailComponentImpl
    UserComponent <|-- UserComponentImpl
    UserComponentImpl --> UserRepository
    UserComponentImpl --> AuthorityRepository
    UserComponentImpl --> UserValidator
    UserComponentImpl --> UserEntity
    UserComponentImpl --> AuthorityEntity
    SecurityComponent <|-- SecurityComponentImpl
    SecurityComponentImpl --> JwtUtils
    SecurityComponentImpl --> PasswordEncoder
    EmailComponentImpl --> EmailSender
    EmailComponentImpl --> EmailValidator
    EmailComponentImpl --> VerificationCodeRepository
    EmailComponentImpl --> VerificationCodeGenerator
    EmailComponentImpl --> VerificationCodeEntity
    UserFacade <|-- UserFacadeImpl
```

PD:

- Por hacerlo mas legible, Estos diagramas excluyen:
    - _DTOs_: Objetos de entrada y salida HTTP
    - _Inputs_: Objetos de entradas a componentes
    - _Outputs_:Objetos de salida de componentes

## Requisitos Funcionales Cumplidos

![PDF](docs/requisitos.pdf)

<hr>
DOCS INCOMPLETOS desde aqui
<hr>

## Dudas que probablemente tengas

### Aplicación

### Desarrollo

<hr>

[//]: # (## Acerca del autor)










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

## Kanban

| por hacer           | En progreso | Terminado |
|---------------------|-------------|-----------|
| Registro de usuario |             |           |

## Requisitos Funcionales

### 1. Registro de usuario

#### Descripción:

El sistema permitirá a cualquier persona registrarse como usuario, creando una cuenta en la plataforma.

| Datos obligatorios | Desc                                                    |
|--------------------|---------------------------------------------------------|
| Primer nombre      | Campo alfanumérico obligatorio.                         |
| Primer apellido    | Campo alfanumérico obligatorio.                         |
| Correo electrónico | Debe cumplir con la expresión regular: `^\S+@\S+\.\S+$` |
| Contraseña         | mínimode 8 caracteres                                   |

#### Reglas de negocio adicionales:

- El correo electrónico debe ser único dentro del sistema; no se permitirán registros con correos ya existentes.
  Restricciones:
- En caso de error, el sistema debe informar claramente al usuario la causa del rechazo, y el código de error unico.

#### Respuesta esperada:

Si el registro es exitoso, el sistema responderá con un mensaje de confirmación y creará el usuario en la base de
datos.
En caso de error, el sistema devolverá un mensaje detallado con el código de error correspondiente (por ejemplo, 400
Bad Request para datos inválidos o 409 Conflict para email duplicado) basado en los estandares de la especificación _RFC
9110_.

## Testing

### Funcional

#### Registro de usuario

1. Caso de prueba: Registro exitoso

- En una creacion segun [RFC 9110](https://www.rfc-editor.org/rfc/rfc9110.html#name-post)
  - **Http Method**: POST
  - **Http Status**: 201 Created
  - **Http Headers**: `Location: ...`

- Por mi parte:
  - mockear la dependencia de envio de email para no enviar emails reales en las pruebas funcionales.


### ENDPOINTS antes
> PD: Esto es temporaneo mientras termine mi OpenAPI Description

| Path                                           | Metodo   | Descripcion                                                                                          |
|------------------------------------------------|----------|------------------------------------------------------------------------------------------------------|
| `/auth/signup`                                 | `POST`   | Crear una cuenta de usuario                                                                          |
| `/auth/login`                                  | `POST`   | Iniciar sesion <br/>(obtener tokens de acceso)                                                       |
| `/auth/verify-email`                           | `POST`   | Verificar email ( necesario codigo de email )                                                        |
| `/auth/reset-password`                         | `POST`   | Resetar password ( necesario codigo de email )                                                       |
| `/auth/refresh-token`                          | `POST`   | refrescar token de accesso                                                                           |
| `/users/{id}`                                  | `GET`    | Obtener un usuario por id                                                                            |
| `/users/{id}/products`                         | `GET`    | Obtener productos de un usuario por id<br/>(Spring pageable), probablemente no indispensable         |
| `/users/{id}/balance`                          | `PATCH`  | Ajustar balance (sumar o restar el _delta_ (Δ))                                                      |
| `/users/{id}/authorities`                      | `PUT`    | Actualizar roles (PUT)                                                                               |
| `/carts/{id}`                                  | `GET`    | Obtener my carro de compras (NO IMPLEMENTADO AUN)                                                    |
| `/carts/{id}/items/add`                        | `POST`   | Agregar un item al carrito de ompras                                                                 |
| `/carts/{id}/items/{id}/amount`                | `PUT`    | Actualizar cantidad de un item de carrito                                                            |
| `/carts/{id}/items/{id}`                       | `DELETE` | Borrar un item de carrito                                                                            |
| `/email/send-email-verification`               | `POST`   | Enviar un email de verificacio<br/>necesario especificar que tipo<br/>de permiso se esta concediendo |
| `/payments`                                    | `POST`   | Procesar un pago                                                                                     |
| `/products/create-product`                     | `POST`   | Crear un producto                                                                                    |
| `/products/{id}`                               | `GET`    | Obteber un producto                                                                                  |
| `/products/{id}`                               | `PUT`    | actualizar un producto                                                                               |
| `/products/{id}`                               | `DELETE` | borrae un producto                                                                                   |
| `/products/categories//create-category`        | `POST`   | crear categoria                                                                                      |
| `/products/categories`                         | `GET`    | obtener categoria ( no pageable )                                                                    |
| `/products`<br/>`?page=0&size=10&sort=id,desc` | `GET`    | Obtener todos los productos<br/>(Spring pageable)                                                    |

<hr>
DOCS INCOMPLETOS desde aqui
<hr>

## Dudas que probablemente tengas

### Aplicación

1. ¿Porque no se integra con pasarelas de pago?   
   // todo: explicar: necesito ruc, hisotrial crediticio, bancos internacionales(strype), mesualidad

2. ¿Si no se integram pasarelas de pago como se realizan los pagos?
   // explcair creditos internos de app

3. como integrar asarelas de pago
   // todo: explicar extension de la clase abstracta base y impl en el componente mediante una interface

### Desarrollo

<hr>

## Acerca del autor
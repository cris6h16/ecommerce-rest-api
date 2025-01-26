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

## Requisitos Funcionales


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










<ul _ngcontent-ktg-c330=""><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 1</p><label _ngcontent-ktg-c330=""  for="Selecciona tres aplicaciones de los algoritmos hash en el almacenamiento online.">Selecciona tres aplicaciones de los algoritmos hash en el almacenamiento online.</label><!----><div _ngcontent-ktg-c330="" class="ng-star-inserted"><section _ngcontent-ktg-c330=""><mat-checkbox _ngcontent-ktg-c330="" class="mat-checkbox mat-accent ng-star-inserted" id="mat-checkbox-98"><label class="mat-checkbox-layout" for="mat-checkbox-98-input"><span class="mat-checkbox-inner-container"><input type="checkbox" class="mat-checkbox-input cdk-visually-hidden" id="mat-checkbox-98-input" tabindex="0" aria-checked="false"><span matripple="" class="mat-ripple mat-checkbox-ripple mat-focus-indicator"><span class="mat-ripple-element mat-checkbox-persistent-ripple"><span class="mat-checkbox-frame"><span class="mat-checkbox-background"><svg version="1.1" focusable="false" viewBox="0 0 24 24" xml:space="preserve" aria-hidden="true" class="mat-checkbox-checkmark"><path fill="none" stroke="white" d="M4.1,12.7 9,17.6 20.3,6.3" class="mat-checkbox-checkmark-path"></path></svg><span class="mat-checkbox-mixedmark"><span class="mat-checkbox-label"><span style="display: none;">&nbsp;<span _ngcontent-ktg-c330="">Encriptar conexiones remotas</label></mat-checkbox><mat-checkbox _ngcontent-ktg-c330="" class="mat-checkbox mat-accent ng-star-inserted" id="mat-checkbox-99"><label class="mat-checkbox-layout" for="mat-checkbox-99-input"><span class="mat-checkbox-inner-container"><input type="checkbox" class="mat-checkbox-input cdk-visually-hidden" id="mat-checkbox-99-input" tabindex="0" aria-checked="false"><span matripple="" class="mat-ripple mat-checkbox-ripple mat-focus-indicator"><span class="mat-ripple-element mat-checkbox-persistent-ripple"><span class="mat-checkbox-frame"><span class="mat-checkbox-background"><svg version="1.1" focusable="false" viewBox="0 0 24 24" xml:space="preserve" aria-hidden="true" class="mat-checkbox-checkmark"><path fill="none" stroke="white" d="M4.1,12.7 9,17.6 20.3,6.3" class="mat-checkbox-checkmark-path"></path></svg><span class="mat-checkbox-mixedmark"><span class="mat-checkbox-label"><span style="display: none;">&nbsp;<span _ngcontent-ktg-c330="">Enviar archivos encriptados</label></mat-checkbox><mat-checkbox _ngcontent-ktg-c330="" class="mat-checkbox mat-accent ng-star-inserted" id="mat-checkbox-100"><label class="mat-checkbox-layout" for="mat-checkbox-100-input"><span class="mat-checkbox-inner-container"><input type="checkbox" class="mat-checkbox-input cdk-visually-hidden" id="mat-checkbox-100-input" tabindex="0" aria-checked="false"><span matripple="" class="mat-ripple mat-checkbox-ripple mat-focus-indicator"><span class="mat-ripple-element mat-checkbox-persistent-ripple"><span class="mat-checkbox-frame"><span class="mat-checkbox-background"><svg version="1.1" focusable="false" viewBox="0 0 24 24" xml:space="preserve" aria-hidden="true" class="mat-checkbox-checkmark"><path fill="none" stroke="white" d="M4.1,12.7 9,17.6 20.3,6.3" class="mat-checkbox-checkmark-path"></path></svg><span class="mat-checkbox-mixedmark"><span class="mat-checkbox-label"><span style="display: none;">&nbsp;<span _ngcontent-ktg-c330="">Identificar malware </label></mat-checkbox><mat-checkbox _ngcontent-ktg-c330="" class="mat-checkbox mat-accent ng-star-inserted" id="mat-checkbox-101"><label class="mat-checkbox-layout" for="mat-checkbox-101-input"><span class="mat-checkbox-inner-container"><input type="checkbox" class="mat-checkbox-input cdk-visually-hidden" id="mat-checkbox-101-input" tabindex="0" aria-checked="false"><span matripple="" class="mat-ripple mat-checkbox-ripple mat-focus-indicator"><span class="mat-ripple-element mat-checkbox-persistent-ripple"><span class="mat-checkbox-frame"><span class="mat-checkbox-background"><svg version="1.1" focusable="false" viewBox="0 0 24 24" xml:space="preserve" aria-hidden="true" class="mat-checkbox-checkmark"><path fill="none" stroke="white" d="M4.1,12.7 9,17.6 20.3,6.3" class="mat-checkbox-checkmark-path"></path></svg><span class="mat-checkbox-mixedmark"><span class="mat-checkbox-label"><span style="display: none;">&nbsp;<span _ngcontent-ktg-c330="">Identificar archivos</label></mat-checkbox><mat-checkbox _ngcontent-ktg-c330="" class="mat-checkbox mat-accent ng-star-inserted" id="mat-checkbox-102"><label class="mat-checkbox-layout" for="mat-checkbox-102-input"><span class="mat-checkbox-inner-container"><input type="checkbox" class="mat-checkbox-input cdk-visually-hidden" id="mat-checkbox-102-input" tabindex="0" aria-checked="false"><span matripple="" class="mat-ripple mat-checkbox-ripple mat-focus-indicator"><span class="mat-ripple-element mat-checkbox-persistent-ripple"><span class="mat-checkbox-frame"><span class="mat-checkbox-background"><svg version="1.1" focusable="false" viewBox="0 0 24 24" xml:space="preserve" aria-hidden="true" class="mat-checkbox-checkmark"><path fill="none" stroke="white" d="M4.1,12.7 9,17.6 20.3,6.3" class="mat-checkbox-checkmark-path"></path></svg><span class="mat-checkbox-mixedmark"><span class="mat-checkbox-label"><span style="display: none;">&nbsp;<span _ngcontent-ktg-c330="">Proteger mensajes</label></mat-checkbox><!----></section></div><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 2</p><label _ngcontent-ktg-c330=""  for="¿Para qué se utiliza el siguiente comando? #passwd -l usuario1">¿Para qué se utiliza el siguiente comando? #passwd -l usuario1</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-192" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Habilitar la contraseña del usuario</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-192" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Desbloquear a un usuario</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-192" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Bloquear a un usuario</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 3</p><label _ngcontent-ktg-c330=""  for="Si un administrador desea codificar información privada de los usuarios para ocultarla, ¿cuál es el método más apropiado?">Si un administrador desea codificar información privada de los usuarios para ocultarla, ¿cuál es el método más apropiado?</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-196" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Utilizar un algoritmo hash</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-196" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Usar una encriptación simétrica</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-196" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Emplear una encriptación híbrida</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 4</p><label _ngcontent-ktg-c330=""  for="¿Con qué comando se agrega al usuario_2 al grupo Administración?">¿Con qué comando se agrega al usuario_2 al grupo Administración?</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-200" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">gpasswd -a usuario_2 Administración</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-200" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">gpasswd -a Administración usuario_2 </p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-200" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">groupadd -a usuario_2 Administración</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 5</p><label _ngcontent-ktg-c330=""  for="El comando para agregar usuarios al sistema es:">El comando para agregar usuarios al sistema es:</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-204" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">groupadd</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-204" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">useradd</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-204" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">passwd</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 6</p><label _ngcontent-ktg-c330=""  for="<p>&amp;iquest;Qu&amp;eacute; debes realizar para transformar una cuenta normal en administradora?</p>"><p>¿Qué debes realizar para transformar una cuenta normal en administradora?</p></label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-208" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Cambiar los permisos de la carpeta del usuario a sin restricciones</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-208" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Habilitar alias de comandos en la configuración de visudo</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-208" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Iniciar sesión en un cuenta de root para ejecutar comandos</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 7</p><label _ngcontent-ktg-c330=""  for="¿Qué comando necesitas ejecutar para realizar cambios en la configuración del servidor sin ser un usuario administrador?">¿Qué comando necesitas ejecutar para realizar cambios en la configuración del servidor sin ser un usuario administrador?</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-212" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">sudo</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-212" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">root</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-212" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">yum</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 8</p><label _ngcontent-ktg-c330=""  for="¿Qué debes realizar antes de configurar el comando sudo?">¿Qué debes realizar antes de configurar el comando sudo?</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-216" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Crear un nuevo usuario administrador para configurarlo</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-216" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Verificar por comandos la instalación de los paquetes</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-216" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Modificar los permisos de la carpeta de instalación</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 9</p><label _ngcontent-ktg-c330=""  for="¿Qué información puedes obtener si ejecutas el siguiente comando? #groups usuario_3">¿Qué información puedes obtener si ejecutas el siguiente comando? #groups usuario_3</label><div _ngcontent-ktg-c330="" class="ng-star-inserted"><input  tabindex="0" name="mat-radio-group-220" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Los permisos del usuario_3</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-220" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">Los usuarios del grupo al que pertenece el usuario_3</p><!----></label></mat-radio-button><input  tabindex="0" name="mat-radio-group-220" value="[object Object]"><span mat-ripple="" class="mat-ripple mat-radio-ripple mat-focus-indicator"><span class="mat-ripple-element mat-radio-persistent-ripple"><span class="mat-radio-label-content"><span style="display: none;">&nbsp;<!----><!----><span _ngcontent-ktg-c330="" class="ng-star-inserted"><p _ngcontent-ktg-c330="">El grupo al que pertenece el usuario_3</p><!----></label></mat-radio-button><!----></mat-radio-group></div><!----><!----><!----><!----><!----></mat-card></li><li _ngcontent-ktg-c330="" class="question-card ng-star-inserted"><mat-card _ngcontent-ktg-c330="" class="mat-card mat-focus-indicator question" ><p _ngcontent-ktg-c330="">Pregunta 10</p><label _ngcontent-ktg-c330=""  for="Ordena las actividades que realiza un administrador de sistemas para implementar la seguridad en el servidor Linux.">Ordena las actividades que realiza un administrador de sistemas para implementar la seguridad en el servidor Linux.</label><!----><!----><div _ngcontent-ktg-c330="" class="ng-star-inserted"><div _ngcontent-ktg-c330="" cdkdroplist="" class="cdk-drop-list example-list hljs-h1" id="cdk-drop-list-73"><div _ngcontent-ktg-c330="" cdkdrag="" class="cdk-drag example-box hljs-h1 ng-star-inserted"><mat-icon _ngcontent-ktg-c330="" role="img" class="mat-icon notranslate material-icons mat-icon-no-color" aria-hidden="true" data-mat-icon-type="font">unfold_more</mat-icon><span _ngcontent-ktg-c330="">Analizar las tendencias de nuevas metodologías y amenazas</div><!----><div _ngcontent-ktg-c330="" cdkdrag="" class="cdk-drag example-box hljs-h1 ng-star-inserted"><mat-icon _ngcontent-ktg-c330="" role="img" class="mat-icon notranslate material-icons mat-icon-no-color" aria-hidden="true" data-mat-icon-type="font">unfold_more</mat-icon><span _ngcontent-ktg-c330="">Gestionar el uso de los recursos de software y hardware</div><!----><div _ngcontent-ktg-c330="" cdkdrag="" class="cdk-drag example-box hljs-h1 ng-star-inserted"><mat-icon _ngcontent-ktg-c330="" role="img" class="mat-icon notranslate material-icons mat-icon-no-color" aria-hidden="true" data-mat-icon-type="font">unfold_more</mat-icon><span _ngcontent-ktg-c330="">Diseñar un plan de gestión de riesgos asociados al área de TI</div><!----><div _ngcontent-ktg-c330="" cdkdrag="" class="cdk-drag example-box hljs-h1 ng-star-inserted"><mat-icon _ngcontent-ktg-c330="" role="img" class="mat-icon notranslate material-icons mat-icon-no-color" aria-hidden="true" data-mat-icon-type="font">unfold_more</mat-icon><span _ngcontent-ktg-c330="">Verificar las conexiones de red para evitar intrusiones</div><!----><div _ngcontent-ktg-c330="" cdkdrag="" class="cdk-drag example-box hljs-h1 ng-star-inserted"><mat-icon _ngcontent-ktg-c330="" role="img" class="mat-icon notranslate material-icons mat-icon-no-color" aria-hidden="true" data-mat-icon-type="font">unfold_more</mat-icon><span _ngcontent-ktg-c330="">Administrar las cuentas y autenticaciones de lo usuarios</div><!----><!----></div></div><!----><!----><!----></mat-card></li><!----></ul>
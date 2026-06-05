========================================================
  DEVSU CHALLENGE - Instrucciones de Ejecucion
  Automatizacion E2E y API
========================================================

========================================================
  ESPAÑOL
========================================================

PREREQUISITOS
-------------
- Java 21 o superior
- Maven 3.x
- Google Chrome o Mozilla Firefox instalado
- Conexion a internet (requerida para pruebas API contra petstore.swagger.io)

VERIFICAR INSTALACION
---------------------
  java -version
  mvn -version

CLONAR EL REPOSITORIO
---------------------
  git clone https://github.com/FedeLagna1983/Devsu-challenge.git
  cd Devsu-challenge

INSTALAR DEPENDENCIAS
---------------------
  mvn clean install -DskipTests

========================================================
  EJERCICIO 1 - AUTOMATIZACION E2E — OPCION 2 (SauceDemo)
========================================================

Opcion seleccionada del challenge: AUTOMATIZACION E2E — OPCION 2.

El framework utiliza Selenium WebDriver + Cucumber + JUnit Platform.
El driver del navegador es gestionado automaticamente por WebDriverManager.

Por que Selenium WebDriver:
  Se eligio Selenium WebDriver por ser la herramienta estandar en entornos
  corporativos, con amplio soporte de la industria, mayor comunidad, integracion
  nativa con CI/CD y mejor escalabilidad para proyectos de mayor tamano.

Por que JUnit Platform:
  Se eligio JUnit como runner para Cucumber con el objetivo de unificar el
  motor de ejecucion con el ejercicio de API, donde Karate corre sobre JUnit 5.
  Esto permite ejecutar ambas suites bajo el mismo ecosistema de testing.

EJECUTAR PRUEBA E2E (Chrome):
  mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome"

EJECUTAR PRUEBA E2E (Firefox):
  mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=firefox"

La prueba automatiza el siguiente flujo completo:
  1. Login con standard_user / secret_sauce
  2. Agregar dos productos al carrito desde el listado
  3. Visualizar el carrito y verificar 2 productos
  4. Completar el formulario de compra (nombre, apellido, codigo postal)
  5. Confirmar la orden hasta el mensaje "Thank you for your order!"

REPORTES E2E:
  - HTML: reports/ui/cucumber-report.html
  - JSON: reports/ui/cucumber-report.json
  - Screenshots de fallos: screenshots/

========================================================
  EJERCICIO 2 - AUTOMATIZACION APIS — OPCION 3 (PetStore)
========================================================

Opcion seleccionada del challenge: AUTOMATIZACION APIS — OPCION 3.

El framework utiliza Karate DSL sobre JUnit 5.

EJECUTAR PRUEBAS API:
  mvn clean test "-Dtest=ApiTestRunner"

La prueba cubre el ciclo de vida completo de un usuario:
  1. POST   /user             -> Crear usuario
  2. GET    /user/{username}  -> Buscar usuario creado
  3. PUT    /user/{username}  -> Actualizar nombre y email
  4. GET    /user/{username}  -> Buscar usuario actualizado
  5. DELETE /user/{username}  -> Eliminar usuario

REPORTES API:
  - Directorio: reports/api/
  - Karate genera reportes HTML y JSON automaticamente

========================================================
  EJECUTAR AMBAS SUITES JUNTAS
========================================================

  mvn clean test

========================================================
  ESTRUCTURA DEL PROYECTO
========================================================

  src/test/java/devsu/
    core/
      config/     -> ConfigReader (lectura de config.properties)
      driver/     -> DriverFactory (gestion del WebDriver)
      utils/      -> ScreenshotUtils (captura de evidencias)
    ui/
      hooks/      -> Hooks Cucumber (setup/teardown por escenario)
      pages/      -> Page Objects (login, inventory, cart, checkout)
      runners/    -> UiTestRunner
      stepdefinitions/ -> Step Definitions de Cucumber
    api/
      runners/    -> ApiTestRunner

  src/test/resources/
    config.properties        -> Configuracion de browser y timeout
    karate-config.js         -> Configuracion base de Karate (URL de API)
    features/
      ui/checkout/           -> Feature file Cucumber (flujo de compra E2E)
      api/petstore/          -> Feature file Karate (ciclo de vida de usuario)

========================================================
  ENGLISH
========================================================

PREREQUISITES
-------------
- Java 21 or higher
- Maven 3.x
- Google Chrome or Mozilla Firefox installed
- Internet connection (required for API tests against petstore.swagger.io)

VERIFY INSTALLATION
-------------------
  java -version
  mvn -version

CLONE THE REPOSITORY
--------------------
  git clone https://github.com/FedeLagna1983/Devsu-challenge.git
  cd Devsu-challenge

INSTALL DEPENDENCIES
--------------------
  mvn clean install -DskipTests

========================================================
  EXERCISE 1 - E2E AUTOMATION — OPTION 2 (SauceDemo)
========================================================

Selected challenge option: E2E AUTOMATION — OPTION 2.

The framework uses Selenium WebDriver + Cucumber + JUnit Platform.
The browser driver is managed automatically by WebDriverManager.

Why Selenium WebDriver:
  Selenium WebDriver was chosen for being the standard tool in corporate
  environments, with broad industry support, large community, native
  CI/CD integration and better scalability for larger projects.

Why JUnit Platform:
  JUnit was chosen as the Cucumber runner in order to unify the execution
  engine with the API exercise, where Karate runs on JUnit 5. This allows
  both suites to run under the same testing ecosystem.

RUN E2E TEST (Chrome):
  mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome"

RUN E2E TEST (Firefox):
  mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=firefox"

The test automates the following complete flow:
  1. Login with standard_user / secret_sauce
  2. Add two products to the cart from the product listing
  3. View the cart and verify 2 products
  4. Complete the purchase form (first name, last name, zip code)
  5. Confirm the order until the message "Thank you for your order!"

E2E REPORTS:
  - HTML: reports/ui/cucumber-report.html
  - JSON: reports/ui/cucumber-report.json
  - Failure screenshots: screenshots/

========================================================
  EXERCISE 2 - API AUTOMATION — OPTION 3 (PetStore)
========================================================

Selected challenge option: API AUTOMATION — OPTION 3.

The framework uses Karate DSL on JUnit 5.

RUN API TESTS:
  mvn clean test "-Dtest=ApiTestRunner"

The test covers the complete user lifecycle:
  1. POST   /user             -> Create user
  2. GET    /user/{username}  -> Find created user
  3. PUT    /user/{username}  -> Update name and email
  4. GET    /user/{username}  -> Find updated user
  5. DELETE /user/{username}  -> Delete user

API REPORTS:
  - Directory: reports/api/
  - Karate automatically generates HTML and JSON reports

========================================================
  RUN BOTH SUITES TOGETHER
========================================================

  mvn clean test

========================================================
  PROJECT STRUCTURE
========================================================

  src/test/java/devsu/
    core/
      config/     -> ConfigReader (reads config.properties)
      driver/     -> DriverFactory (WebDriver management)
      utils/      -> ScreenshotUtils (evidence capture)
    ui/
      hooks/      -> Cucumber Hooks (setup/teardown per scenario)
      pages/      -> Page Objects (login, inventory, cart, checkout)
      runners/    -> UiTestRunner
      stepdefinitions/ -> Cucumber Step Definitions
    api/
      runners/    -> ApiTestRunner

  src/test/resources/
    config.properties        -> Browser and timeout configuration
    karate-config.js         -> Karate base configuration (API URL)
    features/
      ui/checkout/           -> Cucumber feature file (E2E purchase flow)
      api/petstore/          -> Karate feature file (user lifecycle)

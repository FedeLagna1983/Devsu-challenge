# Devsu Challenge — Automatización E2E y API | E2E and API Automation

---

## Español

Proyecto de automatización que cubre un flujo de compra E2E en [SauceDemo](https://www.saucedemo.com/) y pruebas de API REST sobre [PetStore](https://petstore.swagger.io/), usando Selenium, Cucumber, JUnit Platform y Karate.

## Tabla de contenidos

- [Stack técnico](#stack-técnico)
- [Instalación](#instalación)
- [Ejercicio 1 — E2E (SauceDemo)](#ejercicio-1--e2e-saucedemo)
- [Ejercicio 2 — API (PetStore)](#ejercicio-2--api-petstore)
- [Ejecutar ambas suites](#ejecutar-ambas-suites)
- [Ejecución por tags (Smoke / Regression)](#ejecución-por-tags-smoke--regression)
- [Pipeline Jenkins — CI/CD](#pipeline-jenkins--cicd)
- [Reportes y evidencias](#reportes-y-evidencias)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Hallazgos y conclusiones](#hallazgos-y-conclusiones)

---

## Stack técnico

| Herramienta | Uso | Versión |
|---|---|---|
| Java | Lenguaje base | 21 |
| Maven | Gestión de dependencias y ejecución | 3.x |
| Selenium WebDriver | Automatización UI | 4.31.0 |
| WebDriverManager | Gestión automática de drivers | 5.8.0 |
| Cucumber | BDD para pruebas UI | 7.18.1 |
| JUnit Platform Suite | Runner de pruebas UI | 1.11.4 |
| Karate | Automatización API | 1.5.2 |

---

## Instalación

1. Clonar el repositorio:

```bash
git clone https://github.com/FedeLagna1983/Devsu-challenge.git
cd Devsu-challenge
```

2. Verificar Java y Maven:

```bash
java -version
mvn -version
```

3. Instalar dependencias:

```bash
mvn clean install -DskipTests
```

---

## Ejercicio 1 — E2E (SauceDemo) · Opción 2

> **Opción seleccionada del challenge:** AUTOMATIZACIÓN E2E — OPCIÓN 2

**¿Por qué Selenium WebDriver?**
Se eligió Selenium WebDriver por ser el estándar en entornos corporativos: mayor cobertura de navegadores, amplia comunidad, integración nativa con pipelines CI/CD y mejor escalabilidad para proyectos de mayor envergadura frente a otras alternativas más ligadas a ecosistemas específicos.

**¿Por qué JUnit Platform?**
Se eligió JUnit como runner de Cucumber para unificar el motor de ejecución con el ejercicio de API, donde Karate corre sobre JUnit 5. Ambas suites conviven en el mismo ecosistema sin dependencias adicionales ni configuraciones divergentes.

---

Automatización del flujo completo de compra en [https://www.saucedemo.com/](https://www.saucedemo.com/):

1. Login con `standard_user` / `secret_sauce`
2. Agregar dos productos al carrito desde el listado
3. Visualizar el carrito y verificar los 2 productos
4. Completar el formulario de compra (nombre, apellido, código postal)
5. Confirmar la orden hasta el mensaje **"Thank you for your order!"**

### Ejecutar en Chrome

```bash
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome"
```

### Ejecutar en Firefox

```bash
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=firefox"
```

---

## Ejercicio 2 — API (PetStore) · Opción 3

> **Opción seleccionada del challenge:** AUTOMATIZACIÓN APIS — OPCIÓN 3

Pruebas del ciclo de vida completo de un usuario sobre [https://petstore.swagger.io/v2](https://petstore.swagger.io/v2):

| Paso | Método | Endpoint | Descripción |
|---|---|---|---|
| 1 | `POST` | `/user` | Crear usuario |
| 2 | `GET` | `/user/{username}` | Buscar usuario creado |
| 3 | `PUT` | `/user/{username}` | Actualizar nombre y email |
| 4 | `GET` | `/user/{username}` | Buscar usuario actualizado |
| 5 | `DELETE` | `/user/{username}` | Eliminar usuario |

### Ejecutar pruebas API

```bash
mvn clean test "-Dtest=ApiTestRunner"
```

---

## Ejecutar ambas suites

```bash
mvn clean test
```

---

## Ejecución por tags (Smoke / Regression)

Los escenarios están etiquetados para permitir ejecuciones selectivas:

| Tag | Escenario | Suite |
|---|---|---|
| `@smoke` | Flujo E2E completo de compra | Smoke |
| `@smoke` | Ciclo de vida de usuario (API) | Smoke |
| `@regression` | Flujo E2E completo de compra | Regression |
| `@regression` | Ciclo de vida de usuario (API) | Regression |

### Solo smoke tests

```bash
# UI
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome" "-Dcucumber.filter.tags=@smoke"

# API
mvn clean test "-Dtest=ApiTestRunner" "-Dcucumber.filter.tags=@smoke"

# Ambas suites
mvn clean test "-Dcucumber.filter.tags=@smoke"
```

### Solo regression tests

```bash
# UI
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome" "-Dcucumber.filter.tags=@regression"

# API
mvn clean test "-Dtest=ApiTestRunner" "-Dcucumber.filter.tags=@regression"

# Ambas suites
mvn clean test "-Dcucumber.filter.tags=@regression"
```

> Actualmente los escenarios llevan ambos tags (`@smoke @regression`). Al crecer el proyecto, los
> tests más lentos o de menor criticidad se etiquetan solo con `@regression`, manteniendo `@smoke`
> para el subconjunto de verificación rápida.

---

## Pipeline Jenkins — CI/CD

### Parámetros del pipeline

El `Jenkinsfile` expone dos parámetros configurables en cada ejecución:

| Parámetro | Opciones | Descripción |
|---|---|---|
| `SUITE` | `smoke` / `regression` / `all` | Suite a ejecutar. `all` corre sin filtro de tags. |
| `BROWSER` | `chrome` / `firefox` | Navegador para los tests UI. |

### Trigger automático desde GitHub

El pipeline se activa automáticamente con cada `push` al repositorio. Configuración única (por entorno):

1. **Jenkins — instalar plugin**: _Manage Jenkins_ → _Plugins_ → instalar **GitHub plugin**.
2. **GitHub — crear webhook**:
   - Ir a _Settings_ → _Webhooks_ → _Add webhook_
   - **Payload URL**: `http://<JENKINS_URL>/github-webhook/`
   - **Content type**: `application/json`
   - **Events**: seleccionar _Just the push event_
3. **Jenkins job**: en _Build Triggers_, habilitar **"GitHub hook trigger for GITScm polling"**.

El bloque `triggers { githubPush() }` en el `Jenkinsfile` declara este comportamiento en código.

### Ejecutar manualmente en Jenkins

1. Abrir el job en Jenkins.
2. Clic en **"Build with Parameters"**.
3. Seleccionar `SUITE` (`smoke`, `regression` o `all`) y `BROWSER`.
4. Clic en **Build**.

### Comandos equivalentes en local

```bash
# Smoke (Chrome)
mvn clean test "-Dcucumber.filter.tags=@smoke" "-Dbrowser=chrome"

# Regression (Firefox)
mvn clean test "-Dcucumber.filter.tags=@regression" "-Dbrowser=firefox"

# All (sin filtro)
mvn clean test
```

---

## Reportes y evidencias

| Tipo | Ubicación |
|---|---|
| Reporte HTML Cucumber (E2E) | `reports/ui/cucumber-report.html` |
| Reporte JSON Cucumber (E2E) | `reports/ui/cucumber-report.json` |
| Reportes Karate (API) | `reports/api/` |
| Screenshots de fallos (E2E) | `screenshots/` |

---

## Estructura del proyecto

```
src/test/java/devsu/
├── core/
│   ├── config/          ConfigReader — lectura de config.properties
│   ├── driver/          DriverFactory — gestión del WebDriver
│   └── utils/           ScreenshotUtils — captura de evidencias
├── ui/
│   ├── hooks/           Hooks Cucumber (setup/teardown por escenario)
│   ├── pages/           Page Objects (login, inventory, cart, checkout)
│   ├── runners/         UiTestRunner
│   └── stepdefinitions/ Step Definitions de Cucumber
└── api/
    └── runners/         ApiTestRunner

src/test/resources/
├── config.properties        Configuración de browser y timeout
├── karate-config.js         URL base de la API PetStore
└── features/
    ├── ui/checkout/         Feature file Cucumber — flujo de compra E2E
    └── api/petstore/        Feature file Karate — ciclo de vida de usuario
```

---

## Hallazgos y conclusiones

### Ejercicio 1 — E2E

**1. Estabilidad del sitio bajo prueba**
SauceDemo está diseñado específicamente para automatización. Sus atributos `data-test` en el HTML facilitan locators robustos y resistentes a cambios de estilo.

**2. Flujo de compra completo**
Todos los pasos del flujo fueron automatizados con assertions explícitas en cada transición de página, validando URL, visibilidad de elementos y contenido esperado.

**3. Compatibilidad con React y Selenium**
SauceDemo usa React. Se identificó que el método nativo `sendKeys()` de Selenium actualiza visualmente los campos del formulario de checkout pero no dispara el evento `onChange` de React. Como consecuencia, React mantiene los campos como vacíos en su estado interno y bloquea la navegación al hacer submit.

La solución fue usar el setter nativo de `HTMLInputElement` vía `JavascriptExecutor` para actualizar el valor y disparar el evento `input` con `bubbles: true`, permitiendo a React reconocer el cambio.

**4. Botones de navegación y JavascriptExecutor**
Los botones *Continue* y *Finish* del checkout tampoco responden al click nativo de Selenium en todos los contextos. Se resolvió ejecutando el click directamente con `JavascriptExecutor`.

**5. Captura automática de evidencias**
Se implementó captura automática de screenshots ante escenarios fallidos mediante un `@After` Hook de Cucumber, facilitando el diagnóstico en entornos CI/CD.

**Conclusiones E2E**
- Page Object Model garantiza mantenibilidad: un cambio en la UI solo requiere modificar el Page Object.
- Cucumber + Gherkin actúa como documentación viva del comportamiento del sistema.
- WebDriverManager elimina la gestión manual de drivers.
- En aplicaciones con frameworks reactivos (React, Angular, Vue) es necesario usar estrategias de interacción compatibles con su sistema de eventos sintéticos.

---

### Ejercicio 2 — API

**1. API pública con comportamiento permisivo**
PetStore es un entorno de demo. No aplica validaciones estrictas de unicidad: permite crear usuarios con el mismo ID múltiples veces. Comportamiento esperado en una API de demostración.

**2. Estructura de respuestas inconsistente**
`POST`, `PUT` y `DELETE` devuelven un envelope genérico `{ code, type, message }`, mientras que `GET /user/{username}` devuelve directamente el objeto. En una API productiva esto se reportaría como deuda técnica de diseño.

**3. Idempotencia garantizada con UUID dinámico**
El username se genera dinámicamente con UUID en cada ejecución, evitando conflictos entre corridas sucesivas y garantizando datos frescos en todo momento.

**4. Ciclo de vida encadenado en un único escenario**
Los 5 casos de prueba se implementaron como pasos encadenados en un único escenario Karate, reflejando la naturaleza secuencial del flujo donde cada operación depende del resultado de la anterior.

**5. Disponibilidad del servidor**
Al ser una API pública sin SLA, puede presentar latencia variable. Se recomienda considerar reintentos o aumentar timeouts en entornos CI/CD.

**Conclusiones API**
- Karate DSL permite definir request, response, assertions y variables en el mismo feature file, sin código Java adicional.
- El encadenamiento de llamadas HTTP en un escenario es ideal para flujos con dependencias de estado.
- El uso de UUID garantiza idempotencia y facilita la ejecución en paralelo.
- Para APIs productivas se recomienda complementar con validación de schemas JSON y contract testing.

---

### Conclusión general

El challenge integra dos enfoques complementarios de automatización: pruebas funcionales de interfaz de usuario con Selenium + Cucumber para el flujo de compra E2E, y pruebas de servicios REST con Karate para el ciclo de vida de usuarios en PetStore. Ambos frameworks coexisten en un único proyecto Maven, ejecutables de forma independiente o conjunta.

---

## English

E2E and API automation project covering a complete purchase flow on [SauceDemo](https://www.saucedemo.com/) and REST API tests on [PetStore](https://petstore.swagger.io/), using Selenium, Cucumber, JUnit Platform and Karate.

## Table of contents

- [Tech stack](#tech-stack)
- [Installation](#installation)
- [Exercise 1 — E2E (SauceDemo)](#exercise-1--e2e-saucedemo--option-2)
- [Exercise 2 — API (PetStore)](#exercise-2--api-petstore--option-3)
- [Run both suites](#run-both-suites)
- [Tag-based execution (Smoke / Regression)](#tag-based-execution-smoke--regression)
- [Jenkins Pipeline — CI/CD](#jenkins-pipeline--cicd)
- [Reports and evidence](#reports-and-evidence)
- [Project structure](#project-structure)
- [Findings and conclusions](#findings-and-conclusions)

---

## Tech stack

| Tool | Purpose | Version |
|---|---|---|
| Java | Base language | 21 |
| Maven | Dependency management and execution | 3.x |
| Selenium WebDriver | UI automation | 4.31.0 |
| WebDriverManager | Automatic driver management | 5.8.0 |
| Cucumber | BDD for UI tests | 7.18.1 |
| JUnit Platform Suite | UI test runner | 1.11.4 |
| Karate | API automation | 1.5.2 |

---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/FedeLagna1983/Devsu-challenge.git
cd Devsu-challenge
```

2. Verify Java and Maven:

```bash
java -version
mvn -version
```

3. Install dependencies:

```bash
mvn clean install -DskipTests
```

---

## Exercise 1 — E2E (SauceDemo) · Option 2

> **Selected challenge option:** E2E AUTOMATION — OPTION 2

**Why Selenium WebDriver?**
Selenium WebDriver was chosen for being the standard tool in corporate environments: broader browser coverage, large community, native CI/CD pipeline integration and better scalability for larger projects compared to other alternatives more tied to specific ecosystems.

**Why JUnit Platform?**
JUnit was chosen as the Cucumber runner to unify the execution engine with the API exercise, where Karate runs on JUnit 5. Both suites coexist in the same ecosystem without additional dependencies or divergent configurations.

---

Complete purchase flow automation on [https://www.saucedemo.com/](https://www.saucedemo.com/):

1. Login with `standard_user` / `secret_sauce`
2. Add two products to the cart from the product listing
3. View the cart and verify 2 products
4. Complete the purchase form (first name, last name, zip code)
5. Confirm the order until the message **"Thank you for your order!"**

### Run on Chrome

```bash
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome"
```

### Run on Firefox

```bash
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=firefox"
```

---

## Exercise 2 — API (PetStore) · Option 3

> **Selected challenge option:** API AUTOMATION — OPTION 3

Complete user lifecycle tests on [https://petstore.swagger.io/v2](https://petstore.swagger.io/v2):

| Step | Method | Endpoint | Description |
|---|---|---|---|
| 1 | `POST` | `/user` | Create user |
| 2 | `GET` | `/user/{username}` | Find created user |
| 3 | `PUT` | `/user/{username}` | Update name and email |
| 4 | `GET` | `/user/{username}` | Find updated user |
| 5 | `DELETE` | `/user/{username}` | Delete user |

### Run API tests

```bash
mvn clean test "-Dtest=ApiTestRunner"
```

---

## Run both suites

```bash
mvn clean test
```

---

## Tag-based execution (Smoke / Regression)

Scenarios are tagged to allow selective runs:

| Tag | Scenario | Suite |
|---|---|---|
| `@smoke` | Complete E2E purchase flow | Smoke |
| `@smoke` | User lifecycle (API) | Smoke |
| `@regression` | Complete E2E purchase flow | Regression |
| `@regression` | User lifecycle (API) | Regression |

### Smoke tests only

```bash
# UI
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome" "-Dcucumber.filter.tags=@smoke"

# API
mvn clean test "-Dtest=ApiTestRunner" "-Dcucumber.filter.tags=@smoke"

# Both suites
mvn clean test "-Dcucumber.filter.tags=@smoke"
```

### Regression tests only

```bash
# UI
mvn clean test "-Dtest=UiTestRunner" "-Dbrowser=chrome" "-Dcucumber.filter.tags=@regression"

# API
mvn clean test "-Dtest=ApiTestRunner" "-Dcucumber.filter.tags=@regression"

# Both suites
mvn clean test "-Dcucumber.filter.tags=@regression"
```

> Both scenarios currently carry both tags (`@smoke @regression`). As the project grows, slower or lower-priority tests are tagged only with `@regression`, keeping `@smoke` for the quick sanity-check subset.

---

## Jenkins Pipeline — CI/CD

### Pipeline parameters

The `Jenkinsfile` exposes two configurable parameters per run:

| Parameter | Options | Description |
|---|---|---|
| `SUITE` | `smoke` / `regression` / `all` | Suite to run. `all` runs without tag filter. |
| `BROWSER` | `chrome` / `firefox` | Browser for UI tests. |

### Automatic trigger from GitHub

The pipeline triggers automatically on every push to the repository. One-time setup (per environment):

1. **Jenkins — install plugin**: _Manage Jenkins_ → _Plugins_ → install **GitHub plugin**.
2. **GitHub — create webhook**:
   - Go to _Settings_ → _Webhooks_ → _Add webhook_
   - **Payload URL**: `http://<JENKINS_URL>/github-webhook/`
   - **Content type**: `application/json`
   - **Events**: select _Just the push event_
3. **Jenkins job**: in _Build Triggers_, enable **"GitHub hook trigger for GITScm polling"**.

The `triggers { githubPush() }` block in the `Jenkinsfile` declares this behavior in code.

### Manual execution in Jenkins

1. Open the job in Jenkins.
2. Click **"Build with Parameters"**.
3. Select `SUITE` (`smoke`, `regression` or `all`) and `BROWSER`.
4. Click **Build**.

### Local equivalent commands

```bash
# Smoke (Chrome)
mvn clean test "-Dcucumber.filter.tags=@smoke" "-Dbrowser=chrome"

# Regression (Firefox)
mvn clean test "-Dcucumber.filter.tags=@regression" "-Dbrowser=firefox"

# All (no filter)
mvn clean test
```

---

## Reports and evidence

| Type | Location |
|---|---|
| Cucumber HTML report (E2E) | `reports/ui/cucumber-report.html` |
| Cucumber JSON report (E2E) | `reports/ui/cucumber-report.json` |
| Karate reports (API) | `reports/api/` |
| Failure screenshots (E2E) | `screenshots/` |

---

## Project structure

```
src/test/java/devsu/
├── core/
│   ├── config/          ConfigReader — reads config.properties
│   ├── driver/          DriverFactory — WebDriver management
│   └── utils/           ScreenshotUtils — evidence capture
├── ui/
│   ├── hooks/           Cucumber Hooks (setup/teardown per scenario)
│   ├── pages/           Page Objects (login, inventory, cart, checkout)
│   ├── runners/         UiTestRunner
│   └── stepdefinitions/ Cucumber Step Definitions
└── api/
    └── runners/         ApiTestRunner

src/test/resources/
├── config.properties        Browser and timeout configuration
├── karate-config.js         PetStore API base URL
└── features/
    ├── ui/checkout/         Cucumber feature file — E2E purchase flow
    └── api/petstore/        Karate feature file — user lifecycle
```

---

## Findings and conclusions

### Exercise 1 — E2E

**1. Stability of the system under test**
SauceDemo is designed specifically for automation. Its `data-test` attributes in the HTML enable robust locators resistant to style changes.

**2. Complete purchase flow**
All flow steps were automated with explicit assertions on each page transition, validating URL, element visibility and expected content.

**3. React and Selenium compatibility**
SauceDemo uses React. It was identified that Selenium's native `sendKeys()` method visually updates the checkout form fields but does not trigger React's `onChange` event. As a result, React keeps the fields as empty in its internal state and blocks navigation on submit.

The solution was to use the native `HTMLInputElement` setter via `JavascriptExecutor` to update the value and dispatch the `input` event with `bubbles: true`, allowing React to recognize the change.

**4. Navigation buttons and JavascriptExecutor**
The *Continue* and *Finish* buttons in the checkout flow also do not respond to Selenium's native click in all contexts. This was resolved by executing the click directly with `JavascriptExecutor`.

**5. Automatic evidence capture**
Automatic screenshot capture was implemented for failed scenarios via a Cucumber `@After` Hook, facilitating diagnosis in CI/CD environments.

**E2E Conclusions**
- Page Object Model guarantees maintainability: a UI change only requires modifying the Page Object.
- Cucumber + Gherkin acts as living documentation of the system behavior.
- WebDriverManager eliminates manual driver management.
- In applications with reactive frameworks (React, Angular, Vue) it is necessary to use interaction strategies compatible with their synthetic event system.

---

### Exercise 2 — API

**1. Public API with permissive behavior**
PetStore is a demo environment. It does not apply strict uniqueness validations: it allows creating users with the same ID multiple times. Expected behavior in a demonstration API.

**2. Inconsistent response structure**
`POST`, `PUT` and `DELETE` return a generic envelope `{ code, type, message }`, while `GET /user/{username}` returns the object directly. In a production API this would be reported as technical design debt.

**3. Idempotency guaranteed with dynamic UUID**
The username is generated dynamically with UUID on each run, avoiding conflicts between successive executions and guaranteeing fresh data at all times.

**4. Chained lifecycle in a single scenario**
The 5 test cases were implemented as chained steps in a single Karate scenario, reflecting the sequential nature of the flow where each operation depends on the result of the previous one.

**5. Server availability**
Being a public API without SLA, it may experience variable latency. It is recommended to consider retries or increase timeouts in CI/CD environments.

**API Conclusions**
- Karate DSL allows defining request, response, assertions and variables in the same feature file, without additional Java code.
- Chaining HTTP calls within a scenario is ideal for flows with state dependencies.
- The use of UUID guarantees idempotency and facilitates parallel execution.
- For production APIs it is recommended to complement with JSON schema validation and contract testing.

---

### General conclusion

The challenge integrates two complementary automation approaches: functional UI tests with Selenium + Cucumber for the E2E purchase flow, and REST service tests with Karate for the user lifecycle in PetStore. Both frameworks coexist in a single Maven project, executable independently or together.

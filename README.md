# Impacto de las IAs - Practica Final SpringBoot

## Índice

1. [Documento técnico del proyecto](#1-documento-técnico-del-proyecto)
   - 1.1 [Descripción general](#11-descripción-general)
   - 1.2 [Funcionalidad abierta del grupo](#12-funcionalidad-creativa)
   - 1.3 [Estructura del proyecto](#13-estructura-del-proyecto)
   - 1.4 [Documentación de los endpoints REST](#14-documentación-de-los-endpoints-rest)
   - 1.5 [Esquema de la base de datos](#15-esquema-de-la-base-de-datos)
   - 1.6 [Descripción técnica del cálculo de CO₂ y kWh](#16-descripción-técnica-del-cálculo-de-co2-y-kwh)

2. [Manual de usuario / Manual de integración](#2-manual-de-usuario--manual-de-integración)
   - 2.1 [Requisitos](#21-requisitos)
   - 2.2 [Configuración de MySQL](#22-configuración-de-mysql)
   - 2.3 [Ejemplos con curl](#23-ejemplos-con-curl)
   - 2.4 [Interpretación de resultados](#24-interpretación-de-resultados)

3. [Plan de pruebas (testing)](#3-plan-de-pruebas-testing)
   - 3.1 [Enfoque](#31-enfoque)
   - 3.2 [Master Test Plan](#32-master-test-plan)
   - 3.3 [Pruebas automatizadas en el código](#33-pruebas-automatizadas-en-el-código)
   - 3.4 [Cobertura manual vs automatizada](#34-cobertura-manual-vs-automatizada)

4. [Uso de la inteligencia artificial en el desarrollo](#4-uso-de-la-inteligencia-artificial-en-el-desarrollo)
   - 4.1 [Herramientas utilizadas](#41-herramientas-utilizadas)
   - 4.2 [Dónde se ha aplicado](#42-dónde-se-ha-aplicado)
   - 4.3 [Cómo se ha usado](#43-cómo-se-ha-usado)
   - 4.4 [Valoración crítica](#44-valoración-crítica)

5. [Autores](#5-autores)
6. [Licencia y entrega](#6-licencia-y-entrega)

## 1. Documento técnico del proyecto

### 1.1 Descripción general

Hablamos de una API REST desarrollada con **Spring Boot** que registra sesiones de consultas de diferentes IAs (ChatGPT, Copilot, Stable Diffusion, etc.) y calcula el impacto ambiental aproximado de estas: emisiones de **CO₂** (gramos) y consumo energético (**kWh**) por sesión.

Se quiere hacer "tangible" el coste ambiental de estas consultas, que a menudo se reducen a preguntas cotidianas. Nuestra aplicación centraliza estos datos, los persiste en MySQL y ofrece consultas, informes por usuario y exportación de ellos.

### 1.2 Funcionalidad creativa

Hemos añadido el endpoint **`GET /modelRanking`**, que devuelve todos los modelos de la tabla `ai_model` ordenados por consumo energético por consulta: menos consumo por consulta, más arriba aparece en el ranking.

Nos ayuda a comparar qué herramientas son más eficientes en energía según los datos fijos de los modelos.

### 1.3 Estructura del proyecto

Arquitectura por capas:

```
src/
├── main/
│   ├── java/org/example/co2generatedbyai/
│   │   ├── controller/                      --> Peticiones HTTP (@RestController)
│   │   │   └── AiController.java
│   │   ├── service/                         --> Lógica de negocio
│   │   │   └── AiService.java
│   │   ├── repository/                      --> Acceso a MySQL con JdbcTemplate
│   │   │   ├── AiModelRepository.java
│   │   │   └── AiSesionRepository.java
│   │   └── model/                           --> Clases de dominio
│   │       ├── AiModel.java
│   │       ├── AiSesion.java
│   │       └── AiUserReport.java
│   └── resources/
│       └── application.properties
└── test/java/org/example/co2generatedbyai/
    ├── AiServiceMockitoTest.java
    ├── AiSesionTest.java
    └── ApplicationContextTest.java
```

| Capa | Responsabilidad | Clases principales |
|------|-----------------|-------------------|
| **Controller** | Recibe solicitudes, códigos HTTP, validaciones básicas | `AiController` |
| **Service** | Crear sesiones, informes, exportación CSV, ranking | `AiService` |
| **Repository** | SQL (CRUD, GROUP BY, consultas a `ai_model`) | `AiSesionRepository`, `AiModelRepository` |
| **Model** | Datos de dominio y cálculo de CO₂/kWh al crear sesión | `AiSesion`, `AiModel`, `AiUserReport` |

**NOTAS:** 

- No hemos creado una capa **DTO** separada. El JSON de la API usa directamente `AiSesion` (constructor vacío + setters para el POST). Y hemos creado la clase `AiUserReport`, para las respuestas del informe.

- Anotamos que, aunque el enunciado menciona Spring Data JPA, en este proyecto hemos elegido Spring JDBC (JdbcTemplate) para acceder a MySQL; ya que es lo practicado en clase.

- Estas dos decisiones han sido aceptadas por los profesores en el aula.

#### Relación entre clases

INSERTAR DRAW.IO

### 1.4 Documentación de los endpoints REST

**URL base:** `http://localhost:8080`

| Método | Ruta | Descripción | Cuerpo (JSON) | Respuesta |
|--------|------|-------------|---------------|-----------|
| GET | `/` | Comprobar que la API funciona | — | Texto plano |
| GET | `/sesions` | Listar todas las sesiones | — | 200 + Array de `AiSesion` |
| GET | `/sesions/{id}` | Obtener una sesión por id | — | 200 / 404 |
| POST | `/sesions` | Registrar nueva sesión | `AiSesion` | 201 + sesión creada / 400 |
| DELETE | `/sesions/{id}` | Borrar sesión | — | 204 / 404 |
| GET | `/sesions/report` | CO₂ y kWh totales por usuario | — | 200 + Array de `AiUserReport` |
| GET | `/sesions/export` | Exportar sesiones a CSV y devolver lista | — | 200 + fichero `exports/sesions.csv` |
| GET | `/modelRanking` | Ranking de modelos por eficiencia (consumo) | — | 200 + Array de `AiModel` ordenado |

Nuestras rutas usan `/sesions`, aunque en el enunciado dice `/sessions`. Pero el comportamiento es el mismo.

#### Ejemplo POST `/sesions`

**Request:**
```json
{
  "usuario": "Maria",
  "model": "GPT-4",
  "tipoTask": "texto",
  "numConsultas": 6,
  "fecha": "2026-05-19",
  "descripcion": "Redacción de informe"
}
```

**Response `201`:**
```json
{
  "id": 12,
  "usuario": "Maria",
  "model": "GPT-4",
  "tipoTask": "texto",
  "numConsultas": 6,
  "fecha": "2026-05-19",
  "descripcion": "Redacción de informe",
  "co2Estimado": 39.0,
  "kwhEstimado": 0.03
}
```

- Si falta `usuario` o `model` --> 400
- Si el modelo no existe en BD o `numConsultas <= 0` --> puede devolver 500
- Si no se envía `fecha` o `descripcion` --> se guardan por defecto la fecha de hoy y una cadena vacía como descripción

#### Ejemplo GET `/sesions/report`

**Response `200`:**
```json
[
  {
    "usuario": "Maria",
    "totalCo2": 78.0,
    "totalKwh": 0.06
  }
]
```

#### Ejemplo GET `/modelRanking`

Devuelve modelos ordenados por `consumo` ascendente (de menos a más consumo).

### 1.5 Esquema de la base de datos

**Base de datos:** `co2_by_ai`

INSERTAR DRAW.IO

Cada sesión se vincula a un registro de `ai_model` mediante `modelo` + `tipo_tarea` para obtener `co2_consulta` y `consumo`.

ENLAZE AL SCRIPT EN docs/

### 1.6 Descripción técnica del cálculo de CO2 y kWh

Los valores de referencia se leen de `ai_model` según el nombre del modelo y el tipo de tarea.

Al crear una sesión, el constructor de `AiSesion` con `AiModel` calcula:

- CO₂ estimado (g): `numConsultas × co2_consulta`
- kWh estimado: `numConsultas × consumo`

**Ejemplo:** GPT-4, texto, 6 consultas, `co2_consulta = 6.5`, `consumo = 0.005`:
- CO₂ = 6 × 6.5 = **39.0 g**
- kWh = 6 × 0.005 = **0.03 kWh**

Estos valores se guardan en `sesions` y se agregan en el informe por usuario.

Aunque, en la cabezera de la tabla de datos enegéticos de los respectivos modelos proporcionado por el enunciado pone que las unidades energeticas son Wh, en los datos númericos de la misma tabla vemos que las unidades son kWh.

## 2. Manual de usuario / Manual de integración

### 2.1 Requisitos

- JDK 25
- Maven
- MySQL 8 en ejecución
- Postman o curl

### 2.2 Configuración de MySQL

1. Crear la base `co2_by_ai` y las tablas con el script propircionado en este mismo repositorio.
2. Insertar datos en `ai_model`.
3. Editar `src/main/resources/application.properties`:

```
spring.application.name=CO2generatedByAI
spring.datasource.url=jdbc:mysql://localhost:3306/co2_by_ai
spring.datasource.username=root
spring.datasource.password=root
```

La API estará en **http://localhost:8080**

### 2.3 Ejemplos con curl

```bash
# Índice
curl http://localhost:8080/

# Crear sesión
curl -X POST http://localhost:8080/sesions \
  -H "Content-Type: application/json" \
  -d "{\"usuario\":\"Maria\",\"model\":\"GPT-4\",\"tipoTask\":\"texto\",\"numConsultas\":6}"

# Listar
curl http://localhost:8080/sesions

# Por id
curl http://localhost:8080/sesions/1

# Informe por usuario
curl http://localhost:8080/sesions/report

# Export (JSON + CSV en exports/sesions.csv)
curl http://localhost:8080/sesions/export

# Ranking
curl http://localhost:8080/modelRanking

# Borrar
curl -X DELETE http://localhost:8080/sesions/1
```

### 2.4 Interpretación de resultados

**`/sesions/report`:** Para cada `usuario`, los campos `totalCo2` y `totalKwh` son la suma de todas sus sesiones. Así comparamos quién acumula más consumo energético.

**`/sesions/export`:** Devuelve el JSON de todas las sesiones y genera **`exports/sesions.csv`** en el directorio del proyecto (carpeta ignorada por Git y creada automáticamente). Cada línea del CSV contiene id, usuario, modelo, tipo, consultas, fecha, descripción, CO₂ y kWh.

**`/modelRanking`:** Lista los modelos de más a menos eficientes en consumo por consulta (no en CO₂ directamente). Es útil para elegir herramientas con menor huella energética según la tabla.

## Autores
Julio Arango <br>
Lucía Martínez <br>
Maria Montobbio <br>

## Licencia y entrega
Este proyecto es con fines educativos para el ciclo formativo de La Salle Gràcia | DAM.

**Módulo:** 
- 0485: Programació (RA5, RA8, RA9)
- 0486: Entorns de desenvolupament (RA3)
- 0487: Sostenibilitat aplicada al sistema productiu (tots els RAs)

Release / tag: []

# LRCPact4_t4 — API REST con Spring Security y JWT

**Actividad 4 | Programación Web | Adelina Martínez**

Proyecto educativo con **Spring Boot 4.1.2, Java 21, MySQL, Spring Data JPA,
Spring Security, JWT HS256, BCrypt y Bruno**. Solo expone JSON; no utiliza
Thymeleaf ni React.

## Arquitectura

`Bruno → SecurityFilterChain/JWT → Controllers → Services → JPA Repositories → MySQL`

La autenticación consulta usuarios reales en MySQL y compara hashes BCrypt.
En el registro y el login, se emite un JWT firmado con HS256, issuer y
expiración de 60 minutos. Spring Security valida los JWT por medio del
filtro nativo OAuth2 Resource Server; todas las rutas excepto el registro
y login están protegidas.

## Endpoints

| Verbo | Endpoint | JWT | Respuesta esperada |
|---|---|---|---|
| POST | `/api/auth/register` | No | 201 token |
| POST | `/api/auth/login` | No | 200 token |
| GET | `/api/categorias` | Sí | 200 JSON |
| POST | `/api/categorias` | Sí | 201 JSON |
| GET | `/api/materias?page=0&size=10` | Sí | 200 JSON paginado |
| GET | `/api/materias/{id}` | Sí | 200 / 404 |
| POST | `/api/materias` | Sí | 201 JSON |
| PUT | `/api/materias/{id}` | Sí | 200 / 404 |
| DELETE | `/api/materias/{id}` | Sí | 204 / 404 |

También contempla 400 en validaciones, 401 sin JWT o con JWT inválido y
409 ante duplicados. `passwordHash` **nunca** se incluye en respuestas DTO.

## Cómo ejecutar

Consultar [Guía completa por fases](docs/GUIA_PASO_A_PASO.md).
La BD `lrcp_act4` debe existir; se crea con `sql/01-crear-base.sql`.
Configurar variables privadas `MYSQL_URL`, `MYSQL_USERNAME`,
`MYSQL_PASSWORD`, `JWT_SECRET_BASE64` (Base64 de al menos 32 bytes) y `PORT`.
El Maven Wrapper se copia de la Actividad 3; no se adjunta en este ZIP.

En Windows PowerShell, despues de definir esas variables:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

## Bruno

Abrir la carpeta `bruno` como una colección. Entornos `Local` y `Azure`.
Crear en Bruno el valor **secreto** `demoPassword` para cada entorno.
Las 13 solicitudes cubren registro, login, token inválido, falta de token,
categoría, CRUD, paginación, validación y 404.
El token obtenido del login se guarda solamente como variable de ejecución.

## Despliegue previsto

- VPS: `vm-t3-act8-eq18` (Azure Ubuntu 24.04, Java 21)
- Puerto previsto: `8085/TCP`
- Carpeta: `/home/azureuser/springboot-lrcp-act4/`
- Base: `lrcp_act4` (MySQL local al VPS)
- Archivo de servicio: `deploy/lrcp-act4.service`

**Estado actual:** proyecto preparado como base de trabajo. Aún se deben
ejecutar y comprobar compilación, pruebas locales, Bruno y Azure en los
entornos del estudiante; NO se presentan capturas ni despliegues ficticios.

## Evidencias pendientes de generar en Bruno

Guardar las capturas reales una vez ejecutada la API:

- Registro HTTP 201: `evidencias/registro.png`
- Login HTTP 200: `evidencias/login.png` (ocultar JWT antes de subir)
- Crear HTTP 201: `evidencias/crear.png`
- Listar HTTP 200: `evidencias/listar.png`
- Actualizar HTTP 200: `evidencias/actualizar.png`
- Eliminar HTTP 204: `evidencias/eliminar.png`
- Sin JWT HTTP 401: `evidencias/sin-token.png`

Después de crear las capturas, reemplazar la lista por enlaces como:
`![Registro en Bruno](evidencias/registro.png)`.

## Investigación

- [Spring Security — OAuth2 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Spring Security — BCrypt](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
- [Bruno](https://www.usebruno.com/)

## Cuestionario

Borrador de respuestas en [docs/CLASSROOM.md](docs/CLASSROOM.md).

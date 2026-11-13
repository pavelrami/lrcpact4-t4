# Respuestas para Classroom (revisar despues de probar)

**a) ¿Qué es un JWT y qué información contiene normalmente?**
JSON Web Token es un token compacto firmado para transportar declaraciones sobre un usuario.
Tiene tres partes: header (algoritmo y tipo), payload (claims como sub, iss, iat, exp)
y signature (firma que permite detectar modificaciones). Está codificado, no cifrado:
no se incluyen contraseñas ni otros datos privados en el payload. En esta API incluye
`sub` = email, `uid` = identificador, `iss`, `iat` y `exp`.

**b) ¿Cómo protege Spring Security tus endpoints? ¿Qué pasa sin token?**
`SecurityFilterChain` permite /api/auth/register y /api/auth/login y exige autenticación
para el resto. El mecanismo OAuth2 Resource Server extrae el Bearer JWT, comprueba
firma HS256, emisor y vigencia y llena el contexto de seguridad. Sin token o con un
token inválido se obtiene HTTP 401 y un mensaje JSON. No hay sesiones HTTP.

**c) ¿Por qué DTO en vez de exponer Entity?**
Separamos el contrato público de las tablas. Los DTOs controlan datos de entrada
y salida, usan validaciones con `@Valid` y evitan exponer `passwordHash`,
asociaciones internas y detalles de persistencia.

**d) Enlaces (NO afirmar funcionalidad hasta comprobarlo):**
Repositorio: https://github.com/pavelrami/LRCPact4_t4
Base de la API en Azure: http://65.52.8.72:8085
Endpoint verificable: http://65.52.8.72:8085/api/materias
*Nota: /api/materias responde 401 SIN token, lo cual prueba que esta protegido.*
*Para evidencia de CRUD, usar Bruno con JWT.*

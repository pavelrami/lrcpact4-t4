# Guía paso a paso: LRCPact4_t4

## Fase 1: en Windows 11

1. Descomprimir el ZIP y abrir `C:\Users\carlo\Desktop\lrcpact4-t4` en VS Code.
   La carpeta interna del ZIP puede renombrarse a `lrcpact4-t4`.
2. Si esta carpeta no tiene Maven Wrapper, copiar **de la Actividad 3**:
   `mvnw`, `mvnw.cmd` y la carpeta `.mvn`. No copiar su `.git`, `target` ni credenciales.
3. En MySQL Workbench, ejecutar `sql/01-crear-base.sql`.
   Crear un usuario MySQL local con permisos solo para `lrcp_act4` o usar
   un usuario existente de desarrollo, sin publicar contraseñas.
4. PowerShell (misma terminal en que correra Maven):
   ```powershell
   $env:PORT = "8086"
   $env:MYSQL_URL = "jdbc:mysql://localhost:3306/lrcp_act4?serverTimezone=UTC"
   $env:MYSQL_USERNAME = "TU_USUARIO_MYSQL"
   $env:MYSQL_PASSWORD = "TU_PASSWORD_MYSQL"
   $bytes = New-Object byte[] 32
   [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
   $env:JWT_SECRET_BASE64 = [Convert]::ToBase64String($bytes)
   .\mvnw.cmd -v
   .\mvnw.cmd clean test
   .\mvnw.cmd spring-boot:run
   ```
   Si `-v` no funciona, confirmar que se copio la carpeta .mvn correctamente.
5. La consola debe mostrar que Tomcat escucha en 8086 y que arranco sin errores.
   Un GET a `http://localhost:8086/api/materias` sin token debe dar **401 JSON**.
6. Abrir Bruno (`https://www.usebruno.com/`) y seleccionar **Open Collection**
   en la carpeta `bruno`. Elegir entorno `Local`.
7. En Bruno, configurar `demoPassword` como **Secret** dentro del entorno
   `Local` (usar una clave de prueba de al menos 8 caracteres). No escribir
   esa clave dentro de archivos que se suban a GitHub.
8. Ejecutar solicitudes 01 a 13 en orden; 02-Registro devolvera **409**
   si repites el mismo correo. Cambiar `demoEmail` a otro correo ficticio,
   o saltar el registro y volver a iniciar sesion.
9. Al ejecutar Login, su script guarda el token como variable **temporal de
   ejecucion**, `{{token}}`, no en un archivo versionado. Al reiniciar Bruno,
   hay que hacer login de nuevo. La categoria y materia se guardan en variables
   `{{categoriaId}}` y `{{materiaId}}`.

## Fase 2: VPS Azure

1. Desde Git Bash en Windows:
   ```bash
   ssh -i ~/.ssh/clave-equipo18.pem azureuser@65.52.8.72
   ```
2. Diagnosticar ANTES de ejecutar otro Java:
   ```bash
   free -h
   swapon --show
   df -h /
   sudo ss -ltnp | grep -E ':8081|:8083|:8084|:8085|:3306'
   systemctl is-active mysql
   curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8081/proyecto
   curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8083/mvc/materias
   curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8084/materias
   ```
   Los endpoints protegidos, redirecciones o HEAD pueden devolver 200/302/401;
   verificar y registrar el resultado REAL, sin dar por hecho que todo esta activo.
   Si memoria libre no alcanza, NO detener aplicaciones anteriores.
3. En VPS:
   ```bash
   sudo mysql
   ```
   Dentro de MySQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS lrcp_act4 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER IF NOT EXISTS 'lrcp_act4'@'127.0.0.1' IDENTIFIED BY 'UNA_CLAVE_NUEVA_AQUI';
   GRANT ALL PRIVILEGES ON lrcp_act4.* TO 'lrcp_act4'@'127.0.0.1';
   EXIT;
   ```
   La clave de SQL debe ser distinta a otras bases.
4. Compilar en PowerShell local:
   ```powershell
   .\mvnw.cmd clean package
   ```
5. Desde Git Bash local, subir JAR a carpeta NUEVA:
   ```bash
   ssh -i ~/.ssh/clave-equipo18.pem azureuser@65.52.8.72 'mkdir -p ~/springboot-lrcp-act4'
   scp -i ~/.ssh/clave-equipo18.pem target/lrcpact4-t4-0.0.1-SNAPSHOT.jar azureuser@65.52.8.72:/home/azureuser/springboot-lrcp-act4/
   scp -i ~/.ssh/clave-equipo18.pem deploy/lrcp-act4.service azureuser@65.52.8.72:/home/azureuser/springboot-lrcp-act4/
   scp -i ~/.ssh/clave-equipo18.pem deploy/act4.env.example azureuser@65.52.8.72:/home/azureuser/springboot-lrcp-act4/
   ```
6. Desde SSH (Ubuntu), **nunca subir credenciales a GitHub**:
   ```bash
   sudo cp ~/springboot-lrcp-act4/act4.env.example /etc/lrcp-act4.env
   sudo nano /etc/lrcp-act4.env
   ```
   En `nano`, sustituir usuarios/claves, mantener PORT=8085 y MYSQL_URL privado.
   Generar JWT secret nueva en VPS sin imprimirla en el chat:
   ```bash
   openssl rand -base64 32
   ```
   Copiarla en `JWT_SECRET_BASE64=` dentro del archivo. Luego:
   ```bash
   sudo chmod 600 /etc/lrcp-act4.env
   sudo chown root:root /etc/lrcp-act4.env
   sudo cp ~/springboot-lrcp-act4/lrcp-act4.service /etc/systemd/system/lrcp-act4.service
   sudo systemctl daemon-reload
   sudo systemctl enable --now lrcp-act4
   sudo systemctl status lrcp-act4 --no-pager
   sudo journalctl -u lrcp-act4 -n 60 --no-pager
   curl -i http://127.0.0.1:8085/api/materias
   ```
   Sin token, el ultimo curl debe devolver 401, no 200.
7. En el Network Security Group de Azure, agregar **8085/TCP** (NO UDP).
   Si es posible, limitar la regla a tu IP mientras se usa HTTP sin TLS.
   No abrir puerto 3306. NO usar contraseñas reales en Bruno sobre HTTP.
8. Bruno: seleccionar entorno **Azure**, configurar su demoPassword
   de forma segura, repetir registro, login, categoria y CRUD.
   Tomar capturas de resultados REALES para README.
9. Volver a probar 8081, 8083 y 8084 y comparar con el diagnostico inicial.

## Fase 3: GitHub y entrega

1. Desde PowerShell, dentro del proyecto:
   ```powershell
   git rev-parse --show-toplevel
   ```
   Debe ser `C:/Users/carlo/Desktop/lrcpact4-t4`, no `C:/Users/carlo`.
   Si Git aun no esta inicializado, ejecutar `git init .` dentro de la carpeta.
2. Antes de versionar:
   ```powershell
   git status --short
   git check-ignore .env
   ```
   Verificar manualmente que NO haya tokens, claves SSH, contrasenas ni archivos .env.
3. Crear repositorio vacio `pavelrami/LRCPact4_t4` en GitHub. Luego:
   ```powershell
   git add .
   git commit -m "Actividad 4 REST Security JWT Bruno"
   git branch -M main
   git remote add origin https://github.com/pavelrami/LRCPact4_t4.git
   git push -u origin main
   ```
   Si ya existe `origin`, comprobarlo con `git remote -v` antes de agregar.
4. Guardar evidencias de Bruno como `evidencias/registro.png`, `login.png`,
   `crear.png`, `listar.png`, `actualizar.png`, `eliminar.png` y
   `sin-token.png` (la regla .gitignore los ignora por defecto; usar
   `git add -f evidencias/nombre.png` tras revisar que no exponen secretos).
   Editar `README.md` para enlazar esas capturas. Usar `docs/CLASSROOM.md`
   como base de las respuestas, una vez verificado el VPS.

## Importante
- HTTP transmite las contrasenas de prueba y el Bearer JWT sin cifrar; usar
  solo usuarios ficticios. Para sistemas reales se requiere HTTPS.
- Nunca borrar las bases `lrcp_act1`, `lrcp_act2` o `lrcp_act3`.
- Spring Boot 4.1.2 utiliza `spring-boot-starter-webmvc` y el recurso
  JWT es `spring-boot-starter-security-oauth2-resource-server`.
- Las pruebas `mvnw.cmd clean test` prueban JWT y BCrypt a nivel unitario.
  Los endpoints y el servicio de MySQL se comprueban con Bruno, no por suposicion.

-- Ejecutar en MySQL Workbench local o en sudo mysql dentro de Ubuntu.
-- Independiente de lrcp_act3: NO ejecutar DROP.
CREATE DATABASE IF NOT EXISTS lrcp_act4
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- Crear usuario de aplicacion con clave distinta en cada entorno:
-- CREATE USER 'lrcp_act4'@'localhost' IDENTIFIED BY 'CLAVE_LOCAL_UNICA';
-- GRANT ALL PRIVILEGES ON lrcp_act4.* TO 'lrcp_act4'@'localhost';
-- En VPS, si conecta por 127.0.0.1, usar host '127.0.0.1'.

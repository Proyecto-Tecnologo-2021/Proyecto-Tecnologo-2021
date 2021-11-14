# Appettit

Proyecto Grupo 03 - 2021 Tecnólogo en Informática – C.E.T.P - UdelaR.

[Appettit - Azure](#)

## Contenido
1. [Descripción](#descripción)
2. [Despliegue](#despliegue)
3. [Screenshots](#screenshots)
4. [Tecnologías](#tecnologías)

## Descripción

Appetit es un sistema de gestión de pedidos a restaurantes que permitirá a los restaurantes aceptados ofrecer sus servicios y a los clientes registrados realizar(o cancelar) pedidos, calificar y reportar posibles incidentes. A su vez, la plataforma será gestionada por un usuario Administrador. Estos tres conceptos (Restaurante, Cliente y Administrador) componen los diferentres tipos de usuario que integran el sistema.a


## Datos de prueba
Usuario por defecto en backoffice:

- Usuario: admin / Contraseña: 1234

## Screenshots

![](./Documentacion/screenshots-del-sistema/screenshot.png)

## Tecnologías

- JakartaEE
- PostgreSQL con PostGIS
- Java (para Android)
- React
- JQuery
- MongoDB

## Despliegue

- WildFly 21
- [Azure](https://azure.microsoft.com/)
- Firebase
- MongoDB Atlas

### Configuración servidor WildFly 
Para que no se prensenten problemas con las geometrías en Hibernate se deben agregar las siguientes librerías en WildFly de forma manual.
Origen de la copia:
MAVEN_PATH/repository/org/hibernate/hibernate-spatial/5.0.12.Final/hibernate-spatial-5.0.12.Final.jar
MAVEN_PATH/repository/org/geolatte/geolatte-geom/1.0.6/geolatte-geom-1.0.6.jar
MAVEN_PATH/repository/com/vividsolutions/jts/1.13/jts-1.13.jar

Directorio destino:
WILDFLY_PATH/modules/system/layers/base/org/hibernate/main/


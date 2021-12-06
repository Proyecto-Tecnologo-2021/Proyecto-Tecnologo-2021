/*Modificacion de estructura GIS*/
CREATE EXTENSION IF NOT EXISTS postgis;
-- in PostGIS 3, postgis_raster is a separate extension from postgis
-- so do this in addition if you are experimenting PostGIS 3+
CREATE EXTENSION IF NOT EXISTS postgis_raster;
 
CREATE EXTENSION IF NOT EXISTS postgis_sfcgal;
CREATE EXTENSION IF NOT EXISTS address_standardizer;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder;

/* Categorias */
INSERT INTO categorias (nombre) VALUES ('Celiaco');
INSERT INTO categorias (nombre) VALUES ('Vegetariano');
INSERT INTO categorias (nombre) VALUES ('Parrilla');
INSERT INTO categorias (nombre) VALUES ('Minutas');
INSERT INTO categorias (nombre) VALUES ('Bebidas');

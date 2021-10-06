/*Modificacion de estructura*/
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
INSERT INTO categorias (nombre) VALUES ('Bebidas');

/* Departamentos */
INSERT INTO departamentos (nombre) VALUES ('Montevideo');
INSERT INTO departamentos (nombre) VALUES ('Canelones');
INSERT INTO departamentos (nombre) VALUES ('San Jose');
INSERT INTO departamentos (nombre) VALUES ('Maldonado');
INSERT INTO departamentos (nombre) VALUES ('Lavalleja');
INSERT INTO departamentos (nombre) VALUES ('Florida');
INSERT INTO departamentos (nombre) VALUES ('Colonia');
INSERT INTO departamentos (nombre) VALUES ('Flores');
INSERT INTO departamentos (nombre) VALUES ('Soriano');
INSERT INTO departamentos (nombre) VALUES ('Rio Negro');
INSERT INTO departamentos (nombre) VALUES ('Rocha');
INSERT INTO departamentos (nombre) VALUES ('Durazno');
INSERT INTO departamentos (nombre) VALUES ('Treina y Tres');
INSERT INTO departamentos (nombre) VALUES ('Cerro Largo');
INSERT INTO departamentos (nombre) VALUES ('Rivera');
INSERT INTO departamentos (nombre) VALUES ('Salto');
INSERT INTO departamentos (nombre) VALUES ('Paysandu');
INSERT INTO departamentos (nombre) VALUES ('Tacuarembo');
INSERT INTO departamentos (nombre) VALUES ('Artigas');


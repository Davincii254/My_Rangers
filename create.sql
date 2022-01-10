CREATE DATABASE wildlife_trackers;

\c wildlife_trackers;
CREATE TABLE  sightings(
    id serial PRIMARY KEY,
    rangerName VARCHAR,
    endangered BOOLEAN,
    location VARCHAR,
    sightingTime timestamp
);
CREATE TABLE animals (id serial PRIMARY KEY, animalName varchar, health varchar, animalAge varchar, sightingId int);

CREATE DATABASE wildlife_trackers_test WITH TEMPLATE wildlife_trackers;
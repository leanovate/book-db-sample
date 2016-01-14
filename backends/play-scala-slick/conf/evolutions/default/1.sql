# --- !Ups

create table "GENRE" (
  "NAME" VARCHAR NOT NULL PRIMARY KEY,
  "DESCRIPTION" VARCHAR
);

# --- !Downs

drop table "GENRE";

# --- !Ups

create table "AUTHOR" (
  "ID" VARCHAR NOT NULL PRIMARY KEY,
  "NAME" VARCHAR NOT NULL,
  "DESCRIPTION" VARCHAR
);

# --- !Downs

drop table "AUTHOR";

# --- !Ups

create table "AUTHOR" (
  "ID" VARCHAR NOT NULL PRIMARY KEY,
  "NAME" VARCHAR NOT NULL
);

# --- !Downs

drop table "AUTHOR";

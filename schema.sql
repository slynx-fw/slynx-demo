-- table declarations :
create table "sys" (
    "id" bigint primary key not null,
    "db_version" bigint not null
  );
create sequence "s_sys_id";
create table "account" (
    "account_name" text not null,
    "id" bigint primary key not null
  );
create sequence "s_account_id";
create table "user" (
    "email" text not null,
    "account_id" bigint not null,
    "_salt" text not null,
    "_password" text not null,
    "last_name" text not null,
    "first_name" text not null,
    "id" bigint primary key not null,
    "remember_login" text not null
  );
create sequence "s_user_id";
-- foreign key constraints :
alter table "user" add constraint "userFK1" foreign key ("account_id") references "account"("id");

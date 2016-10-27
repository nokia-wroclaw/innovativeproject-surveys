# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table unactivated_account (
  expired_date                  timestamp,
  activation_link               varchar(255)
);

create table user_account (
  login                         varchar(255) not null,
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  created_time                  timestamp,
  email                         varchar(255),
  constraint pk_user_account primary key (login)
);


# --- !Downs

drop table if exists unactivated_account;

drop table if exists user_account;


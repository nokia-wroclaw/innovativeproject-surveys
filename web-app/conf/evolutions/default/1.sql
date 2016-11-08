# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  id                            integer not null,
  text                          varchar(255),
  survey_id                     integer,
  constraint pk_question primary key (id)
);
create sequence question_seq;

create table response (
  id                            integer not null,
  text                          varchar(255),
  answer                        varchar(255),
  ip                            varchar(255),
  question_id                   integer,
  survey_id                     integer,
  constraint pk_response primary key (id)
);
create sequence response_seq;

create table survey (
  id                            integer not null,
  name                          varchar(255),
  description                   varchar(255),
  email                         varchar(255),
  ip                            varchar(255),
  constraint pk_survey primary key (id)
);
create sequence survey_seq;

create table unactivated_account (
  activation_link               varchar(255) not null,
  expired_date                  timestamp,
  constraint pk_unactivated_account primary key (activation_link)
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

alter table question add constraint fk_question_survey_id foreign key (survey_id) references survey (id) on delete restrict on update restrict;
create index ix_question_survey_id on question (survey_id);

alter table response add constraint fk_response_question_id foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_response_question_id on response (question_id);

alter table response add constraint fk_response_survey_id foreign key (survey_id) references survey (id) on delete restrict on update restrict;
create index ix_response_survey_id on response (survey_id);


# --- !Downs

alter table question drop constraint if exists fk_question_survey_id;
drop index if exists ix_question_survey_id;

alter table response drop constraint if exists fk_response_question_id;
drop index if exists ix_response_question_id;

alter table response drop constraint if exists fk_response_survey_id;
drop index if exists ix_response_survey_id;

drop table if exists question;
drop sequence if exists question_seq;

drop table if exists response;
drop sequence if exists response_seq;

drop table if exists survey;
drop sequence if exists survey_seq;

drop table if exists unactivated_account;

drop table if exists user_account;


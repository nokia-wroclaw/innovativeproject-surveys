# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  id                            integer not null,
  question                      varchar(255),
  survey_id                     integer,
  constraint pk_question primary key (id)
);
create sequence question_seq;

create table response (
  id                            integer not null,
  answer                        varchar(255),
  question_id                   integer,
  survey_id                     integer,
  user_account_login            varchar(255),
  constraint pk_response primary key (id)
);
create sequence response_seq;

create table survey (
  id                            integer not null,
  name                          varchar(255),
  description                   varchar(255),
  email                         varchar(255),
  admin_login                   varchar(255),
  constraint pk_survey primary key (id)
);
create sequence survey_seq;

create table survey_member (
  id                            integer not null,
  survey_id                     integer,
  login                         varchar(255),
  constraint pk_survey_member primary key (id)
);
create sequence survey_member_seq;

create table unactivated_account (
  activation_link               varchar(255) not null,
  expired_date                  timestamp,
  constraint pk_unactivated_account primary key (activation_link)
);

create table user_account (
  login                         varchar(255) not null,
  password                      varchar(100),
  first_name                    varchar(100),
  last_name                     varchar(100),
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

alter table response add constraint fk_response_user_account_login foreign key (user_account_login) references user_account (login) on delete restrict on update restrict;
create index ix_response_user_account_login on response (user_account_login);

alter table survey_member add constraint fk_survey_member_survey_id foreign key (survey_id) references survey (id) on delete restrict on update restrict;
create index ix_survey_member_survey_id on survey_member (survey_id);


# --- !Downs

alter table question drop constraint if exists fk_question_survey_id;
drop index if exists ix_question_survey_id;

alter table response drop constraint if exists fk_response_question_id;
drop index if exists ix_response_question_id;

alter table response drop constraint if exists fk_response_survey_id;
drop index if exists ix_response_survey_id;

alter table response drop constraint if exists fk_response_user_account_login;
drop index if exists ix_response_user_account_login;

alter table survey_member drop constraint if exists fk_survey_member_survey_id;
drop index if exists ix_survey_member_survey_id;

drop table if exists question;
drop sequence if exists question_seq;

drop table if exists response;
drop sequence if exists response_seq;

drop table if exists survey;
drop sequence if exists survey_seq;

drop table if exists survey_member;
drop sequence if exists survey_member_seq;

drop table if exists unactivated_account;

drop table if exists user_account;


create table task (id bigint not null auto_increment, description varchar(500), done bit, task_name varchar(50), user_id bigint, primary key (id))
create table user (id bigint not null auto_increment, admin bit, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))
create table task (id bigint not null auto_increment, description varchar(500), done bit, task_name varchar(50), user_id bigint, primary key (id))
create table user (id bigint not null auto_increment, admin bit, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))
create sequence hibernate_sequence start 1 increment 1
create table task (id int8 not null, description varchar(500), done boolean, task_name varchar(50), user_id int8, primary key (id))
create table user (id int8 not null, admin boolean, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))
create sequence hibernate_sequence start 1 increment 1
create table task (id int8 not null, description varchar(500), done boolean, task_name varchar(50), user_id int8, primary key (id))
create table user (id int8 not null, admin boolean, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))
create sequence hibernate_sequence start 1 increment 1
create table task (id int8 not null, description varchar(500), done boolean, task_name varchar(50), user_id int8, primary key (id))
create table user (id int8 not null, admin boolean, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))

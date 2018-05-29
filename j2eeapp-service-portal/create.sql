create table task (id bigint not null auto_increment, description varchar(500), done bit, task_name varchar(50), user_id bigint, primary key (id))
create table user (id bigint not null auto_increment, admin bit, email varchar(200), name varchar(200), password varchar(50), user_name varchar(50), primary key (id))

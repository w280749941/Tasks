Create Table users (
	user_id serial primary key,
	email varchar(128) not null unique,
	passcode varchar(64) not null,
	first_name varchar(64),
	last_name varchar(64),
	is_deleted boolean default false,
	is_active boolean default true,
	created_time timestamp default current_timestamp not null,
	updated_time timestamp default current_timestamp not null
);

Create Table categories_belong_user (
	c_id serial primary key,
	title varchar(64) not null,
	priority smallint not null default 0,
	created_time timestamp default current_timestamp not null,
	updated_time timestamp default current_timestamp not null,
	is_deleted boolean default false,
	owner_id integer not null,
	foreign key (owner_id) references users(user_id)
);

Create Table tasks_from_user (
	t_id serial primary key,
	title varchar(64) not null,
	description text,
	priority smallint not null default 0,
	created_time timestamp default current_timestamp not null,
	updated_time timestamp default current_timestamp not null,
	due_time timestamp,
	reminder_time timestamp,
	is_completed boolean default false,
	is_deleted boolean default false,
	owner_id integer not null,
	category_id integer not null,
	foreign key (owner_id) references users(user_id),
	foreign key (category_id) references categories_belong_user(c_id)
);


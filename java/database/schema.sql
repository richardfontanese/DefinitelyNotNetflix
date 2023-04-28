BEGIN TRANSACTION;

DROP TABLE IF EXISTS genre_movie;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS directors;
DROP TABLE IF EXISTS user_movie;
DROP TABLE IF EXISTS user_dislike_movie;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS people;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);


create table movies(
	movie_id serial primary key,
	imdb_id varchar not null,
	title varchar not null,
	year int,
	plot varchar,
	poster varchar,
	rated varchar
);

create table genres(
	genre_id serial primary key,
	genre_name varchar not null
);

create table people(
	person_id serial primary key, 
	name varchar
);

create table genre_movie(
	genre_id int not null,
	movie_id int not null,
	
	constraint fk_genre foreign key (genre_id) references genres (genre_id),
	constraint fk_movie foreign key (movie_id) references movies (movie_id)
);

create table directors(
	person_id int,
	movie_id int,
	
	constraint fk_director_movie foreign key (movie_id) references movies (movie_id),
	constraint fk_director_person foreign key (person_id) references people (person_id)
);

create table actors(
	person_id int,
	movie_id int,
	
	constraint fk_actor_person foreign key (person_id) references people (person_id),
	constraint fk_actor_movie foreign key (movie_id) references movies (movie_id)
);

create table user_movie(
	user_id int not null,
	movie_id int not null,
	
	constraint fk_user foreign key (user_id) references users (user_id),
	constraint fk_movie foreign key (movie_id) references movies (movie_id)
);

create table user_dislike_movie(
	user_id int,
	movie_id int,
	constraint fk_user foreign key (user_id) references users(user_id),
	constraint fk_movie foreign key (movie_id) references movies (movie_id)
);

COMMIT TRANSACTION;

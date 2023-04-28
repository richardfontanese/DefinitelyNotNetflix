create table user_dislike_movie(
	user_id int,
	movie_id int,
	constraint fk_user foreign key (user_id) references users(user_id),
	constraint fk_movie foreign key (movie_id) references movies (movie_id)
);
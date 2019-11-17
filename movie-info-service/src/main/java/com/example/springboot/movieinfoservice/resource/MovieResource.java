package com.example.springboot.movieinfoservice.resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.movieinfoservice.models.Movie;

@RestController
@RequestMapping("/movies")
@Service
public class MovieResource {

	@RequestMapping("/{movieId}")
	public Movie getMovieInf(@PathVariable("movieId") String mId ) {
		return(new Movie("111", "Movie"+mId));
	}
}

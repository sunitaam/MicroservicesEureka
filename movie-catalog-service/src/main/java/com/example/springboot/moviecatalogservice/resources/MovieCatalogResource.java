package com.example.springboot.moviecatalogservice.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springboot.moviecatalogservice.models.CatalogItem;
import com.example.springboot.moviecatalogservice.models.Movie;
import com.example.springboot.moviecatalogservice.models.Rating;
import com.example.springboot.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;
	
	
	 
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalogItem(@PathVariable("userId") String userId) {
		//return (List<CatalogItem>) Collections.singletonList(new CatalogItem("kkhh", "Kuch Kuch Hota Hai", 5));
		
		///Instead of thi do using autowire :::::: RestTemplate restTemplate = new RestTemplate();
		
		/*** This is the restTeplate to call the Movie info webservice to get the movie id sam
		 * Use this in the below return stament
		Movie movie = restTemplate.getForObject("http://localhost:8080/movies/sam", Movie.class);***/
		
		
		
		//Based on userid get the movieid & rating 1st from Ratings data service
		List<Rating> listRatings = Arrays.asList(new Rating("111", 4), new Rating("222", 5));
		UserRating ratings = new UserRating();
		ratings.setUserRating(listRatings);
		
		/*This is for harcoded url
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/sam" + userId, UserRating.class);*/
		
		/*For the Eureka server registered url 
		UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/sam" + userId, UserRating.class);*/
		
		/*return ratings.stream().map(rating -> 
			new CatalogItem("kkhh", "Kuch Kuch Hota Hai", 5)
				).collect(Collectors.toList());*/
		
			
		return ratings.getUserRating().stream().map(rating -> {
			//Based on movie id get the movie details from Movie infor service	
			/*This is for harcoded url
			Movie movie = restTemplate.getForObject("http://localhost:8081/movies/"+rating.getMovieId(), Movie.class);*/
			//Eureka server url
			Map<String, String> vars = new HashMap<>();
			vars.put("user", "user");
			vars.put("password", "passw0rd");

//			restTemplate.getForObject("http://example.com/hotels/{hotel}/rooms/{hotel}", 
	//		    String.class, vars);
			Movie movie = restTemplate.getForObject("http://movieInfoService-101/movies/"+rating.getMovieId(), Movie.class, vars);
			//Movie movie = restTemplate.getForObject("https://movieinfo/movies/"+rating.getMovieId(), Movie.class);
			/*
			 * This is passing userid & password-------------
			 * RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
			 
			System.out.println("Before................");
			restTemplate  = restTemplateBuilder.basicAuthentication("user","passw0rd").build();  // build using basic authentication details
			
			URI requestUri = UriComponentsBuilder.fromUriString("http://movieInfo/movies/").build().toUri();
			Movie movie = restTemplate.getForObject(requestUri+rating.getMovieId(), Movie.class);
			*/
			
			
			//new CatalogItem("kkhh", "Kuch Kuch Hota Hai", 5)
			System.out.println("After................");
			return new CatalogItem(movie.getName(), "Movie Desc", rating.getRating());
			
		})
				.collect(Collectors.toList());
		
		//Put them all together
	}
}

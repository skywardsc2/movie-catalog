package com.example.moviecatalog.models;

import com.squareup.moshi.Json;

import java.util.List;

public class MovieDetails {
    public int id;
    @Json(name = "poster_url") public String posterUrl;
    public String title;
    @Json(name = "release_date") public String releaseDate;
    @Json(name = "vote_average") public float voteAverage;
    @Json(name = "vote_count") public int voteCount;
    @Json(name = "homepage") public String website;
    @Json(name = "runtime") public int duration;
    public List<String> genres;
    public String overview;
}

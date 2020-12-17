package com.example.moviecatalog.models;

import com.squareup.moshi.Json;

import java.util.Date;
import java.util.List;

public class MovieOverview {
    public int id;
    @Json(name = "poster_url") public String posterUrl;
    public String title;
    @Json(name = "release_date") public String releaseDate;
    public List<String> genres;
    @Json(name = "vote_average") public float voteAverage;
}

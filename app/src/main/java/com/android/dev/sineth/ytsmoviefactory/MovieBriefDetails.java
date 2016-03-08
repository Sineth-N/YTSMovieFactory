package com.android.dev.sineth.ytsmoviefactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sineth on 2/20/2016.
 */
public class MovieBriefDetails implements Serializable,Comparator<MovieBriefDetails> {

    private String movie_title;
    private String movie_genre;
    private String movie_released_year;
    private float movie_rating;
    private String movie_cover_url_small;
    private String movie_cover_url_medium;
    private String movie_cover_url_large;
    private Boolean isFavourite=false;
    private int id;
    private String summary;
    private List<Torrent> torrentList;
    private String downloads;
    private String youTubeurl;

    public MovieBriefDetails(int id, String movie_title, String movie_genre, String movie_released_year, float movie_rating, String movie_cover_url_small) {

        this.movie_title = movie_title;
        this.movie_genre = movie_genre;
        this.movie_released_year = movie_released_year;
        this.movie_rating = movie_rating;
        this.movie_cover_url_small = movie_cover_url_small;
        this.id = id;
        this.torrentList=new ArrayList<>();
    }
    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }


    public String getMovie_cover_url_small() {
        return movie_cover_url_small;
    }

    public void setMovie_cover_url_small(String movie_cover_url_small) {
        this.movie_cover_url_small = movie_cover_url_small;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_genre() {
        return movie_genre;
    }

    public void setMovie_genre(String movie_genre) {
        this.movie_genre = movie_genre;
    }

    public String getMovie_released_year() {
        return movie_released_year;
    }

    public void setMovie_released_year(String movie_released_year) {
        this.movie_released_year = movie_released_year;
    }

    public float getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(float movie_rating) {
        this.movie_rating = movie_rating;
    }

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Torrent> getTorrentList() {
        return torrentList;
    }

    public void addToTorrentList(Torrent torrent) {
        this.torrentList.add(torrent);
    }

    public String getYouTubeurl() {
        return youTubeurl;
    }

    public void setYouTubeurl(String youTubeurl) {
        this.youTubeurl = youTubeurl;
    }

    public String getMovie_cover_url_medium() {
        return movie_cover_url_medium;
    }

    public void setMovie_cover_url_medium(String movie_cover_url_medium) {
        this.movie_cover_url_medium = movie_cover_url_medium;
    }

    public String getMovie_cover_url_large() {
        return movie_cover_url_large;
    }

    public void setMovie_cover_url_large(String movie_cover_url_large) {
        this.movie_cover_url_large = movie_cover_url_large;
    }


    @Override
    public int compare(MovieBriefDetails lhs, MovieBriefDetails rhs) {
        int lhsId = lhs.getId();
        int rhsId = rhs.getId();
        return rhsId > lhsId ? 1 : -1;

//        return rhsId-lhsId;
    }
}

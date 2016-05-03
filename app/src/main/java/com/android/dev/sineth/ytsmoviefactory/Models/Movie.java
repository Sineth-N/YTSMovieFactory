package com.android.dev.sineth.ytsmoviefactory.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sineth on 2/20/2016.
 */
public class Movie implements Parcelable,Comparator<Movie> {

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
    private String youTubeURL;

    public Movie(int id, String movie_title, String movie_genre, String movie_released_year, float movie_rating, String movie_cover_url_small) {

        this.movie_title = movie_title;
        this.movie_genre = movie_genre;
        this.movie_released_year = movie_released_year;
        this.movie_rating = movie_rating;
        this.movie_cover_url_small = movie_cover_url_small;
        this.id = id;
        this.torrentList=new ArrayList<>();
    }

    protected Movie(Parcel in) {
        movie_title = in.readString();
        movie_genre = in.readString();
        movie_cover_url_large = in.readString();
        movie_cover_url_medium = in.readString();
        movie_cover_url_small = in.readString();
        summary = in.readString();
        movie_released_year = in.readString();
        downloads = in.readString();
        youTubeURL = in.readString();
        movie_rating = in.readFloat();
        id = in.readInt();
        in.readList(torrentList,null);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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
        return youTubeURL;
    }

    public void setYouTubeurl(String youTubeURL) {
        this.youTubeURL = youTubeURL;
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
    public int compare(Movie lhs, Movie rhs) {
        int lhsId = lhs.getId();
        int rhsId = rhs.getId();
        return rhsId > lhsId ? 1 : -1;

//        return rhsId-lhsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_title);
        dest.writeString(movie_genre);
        dest.writeString(movie_cover_url_large);
        dest.writeString(movie_cover_url_medium);
        dest.writeString(movie_cover_url_small);
        dest.writeString(summary);
        dest.writeString(movie_released_year);
        dest.writeString(downloads);
        dest.writeString(youTubeURL);
        dest.writeFloat(movie_rating);
        dest.writeInt(id);
        dest.writeList(torrentList);


    }
}

package com.android.dev.sineth.ytsmoviefactory.Models;

/**
 * Created by Sineth on 2/24/2016.
 */
public class Torrent {
    private String quality;
    private int seeds;
    private int peers;
    private String size;
    private int movieID;
    private String url;

    public Torrent()  {

    }

    public Torrent(String quality, int seeds, int peers, String size, int movieID) {
        this.quality = quality;
        this.seeds = seeds;
        this.peers = peers;
        this.size = size;
        this.movieID = movieID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public int getPeers() {
        return peers;
    }

    public void setPeers(int peers) {
        this.peers = peers;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}

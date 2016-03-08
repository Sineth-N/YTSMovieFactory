package com.android.dev.sineth.ytsmoviefactory;

import java.io.Serializable;

/**
 * Created by Sineth on 2/24/2016.
 */
public class Torrent implements Serializable {
    private String quality;
    private int seeds;
    private int peers;
    private String size;
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

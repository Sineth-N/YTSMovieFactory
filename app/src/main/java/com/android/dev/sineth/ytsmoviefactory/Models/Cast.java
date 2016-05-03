package com.android.dev.sineth.ytsmoviefactory.Models;


/**
 * Created by Sineth on 2/24/2016.
 */
public class Cast {
    private String actorName;
    private String characterName;

    public Cast(String actorName, String characterName) {
        this.actorName = actorName;
        this.characterName = characterName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String character_name) {
        this.characterName = character_name;
    }

    public String getName() {
        return actorName;
    }

    public void setName(String actorName) {
        this.actorName = actorName;
    }
}

package com.example.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayList {

    private Integer id;
    private String icon;
    private String iconCacheId;
    private String playListName;
    private String musicInfoIds;

    public synchronized void setMusicInfoIds(String musicInfoIds) {
        this.musicInfoIds = musicInfoIds;
    }

    public String getIconCacheId() {
        return id +".icon";
    }
}

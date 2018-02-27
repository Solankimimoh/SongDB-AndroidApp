package com.songdb.webmob.songsdb.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by solan on 03-12-17.
 */

public class SongModel {

    private String songId;
    private String songTitle;
    private String songDuration;
    private String songYear;
    private List<String> songHashTagList;
    private List<String> songArtistsList;

    public SongModel(String songId, String songTitle, String songDuration, String songYear, List<String> songHashTagList, List<String> songArtistsList) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songYear = songYear;
        this.songHashTagList = songHashTagList;
        this.songArtistsList = songArtistsList;
    }

    public String getSongId() {
        return songId;
    }

    public SongModel() {
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongYear() {
        return songYear;
    }

    public void setSongYear(String songYear) {
        this.songYear = songYear;
    }

    public List<String> getSongHashTagList() {
        return songHashTagList;
    }

    public void setSongHashTagList(List<String> songHashTagList) {
        this.songHashTagList = songHashTagList;
    }

    public List<String> getSongArtistsList() {
        return songArtistsList;
    }

    public void setSongArtistsList(List<String> songArtistsList) {
        this.songArtistsList = songArtistsList;
    }
}

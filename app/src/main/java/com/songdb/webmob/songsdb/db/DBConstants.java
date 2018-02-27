package com.songdb.webmob.songsdb.db;


public class DBConstants {

    //DB Name
    static final String DATABASE_NAME = "songsdb.db";

    //Song Table & Column
    static final String TABLE_SONG = "song";
    static final String SONG_ID = "song_id";
    static final String SONG_TITLE = "song_title";
    static final String SONG_DURATION = "song_duration";
    static final String SONG_SIZE = "song_size";
    static final String SONG_YEAR = "song_year";

    //Song table create query
    static final String CREATE_TABLE_SONG = "CREATE TABLE " + TABLE_SONG
            + "(" + SONG_ID + " INTEGER PRIMARY KEY,"
            + SONG_TITLE + " TEXT,"
            + SONG_DURATION + " TEXT,"
            + SONG_YEAR + " TEXT" + ")";


    //HashTag Table & Column
    static final String TABLE_HASHTAG = "hashtag";
    static final String HASHTAG_ID = "tag_id";
    static final String HASHTAG_TITLE = "tag_title";

    //hashtag table create query
    static final String CREATE_TABLE_HASHTAG = "CREATE TABLE " + TABLE_HASHTAG
            + "(" + HASHTAG_ID + " INTEGER PRIMARY KEY,"
            + HASHTAG_TITLE + " TEXT,"
            + SONG_ID + " TEXT" + ")";


    //Artist Table & Column
    static final String TABLE_ARTIST = "artist";
    static final String ARTIST_ID = "artist_id";
    static final String ARTIST_TITLE = "artist_name";

    //Artist table create query
    static final String CREATE_TABLE_ARTIST = "CREATE TABLE " + TABLE_ARTIST
            + "(" + ARTIST_ID + " INTEGER PRIMARY KEY,"
            + ARTIST_TITLE + " TEXT,"
            + SONG_ID + " TEXT" + ")";


//    Select Query of Song Details from Song

    static final String SELECT_SONG_DETAILS = "SELECT * FROM " + TABLE_SONG;


//    Select Query for get hashtag by ID

    static final String SELECT_HASHTAG_BY_ID = "SELECT * FROM " + TABLE_HASHTAG + " WHERE " + SONG_ID + " = ?";


//    Select Query for get hashtag by ID

    static final String SELECT_ARTIST_BY_ID= "SELECT * FROM " + TABLE_ARTIST + " WHERE " + SONG_ID + " = ?";


}

package com.songdb.webmob.songsdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.songdb.webmob.songsdb.models.SongModel;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase mdb;

    public DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DATABASE_VERSION);
        mdb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.CREATE_TABLE_SONG);
        db.execSQL(DBConstants.CREATE_TABLE_HASHTAG);
        db.execSQL(DBConstants.CREATE_TABLE_ARTIST);
        mdb = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_HASHTAG);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_ARTIST);
    }


    public void close() {
        if (mdb != null) {
            mdb.close();
        }
    }

    public long insertSongDetails(SongModel songModel) {
        long i = 0;
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.SONG_TITLE, songModel.getSongTitle());
            contentValues.put(DBConstants.SONG_DURATION, songModel.getSongDuration());
            contentValues.put(DBConstants.SONG_YEAR, songModel.getSongYear());
            i = mdb.insert(DBConstants.TABLE_SONG, null, contentValues);

            if (i > 0) {
                Cursor c = mdb.rawQuery("SELECT last_insert_rowid()", null);
                c.moveToFirst();
                int id = c.getInt(0);
                insertHashTags(id, songModel.getSongHashTagList());
                insertArtists(id, songModel.getSongArtistsList());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return i;

    }

    private void insertArtists(int id, List<String> artistList) {

        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            ContentValues contentValues = new ContentValues();

            for (String hashtag : artistList) {
                contentValues.put(DBConstants.ARTIST_TITLE, hashtag);
                contentValues.put(DBConstants.SONG_ID, id);
                mdb.insert(DBConstants.TABLE_ARTIST, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void insertHashTags(int id, List<String> hashTagList) {
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            ContentValues contentValues = new ContentValues();

            for (String hashtag : hashTagList) {
                contentValues.put(DBConstants.HASHTAG_TITLE, hashtag);
                contentValues.put(DBConstants.SONG_ID, id);
                mdb.insert(DBConstants.TABLE_HASHTAG, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int deleteSingleSong(String id) {
        int i = 0;
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            i = mdb.delete(DBConstants.TABLE_SONG, DBConstants.SONG_ID + "=?", new String[]{id});

            deleteHashtagFromId(id);
            deleteArtistFromId(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return i;

    }

    private void deleteHashtagFromId(String id) {

        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            mdb.delete(DBConstants.TABLE_HASHTAG, DBConstants.SONG_ID + "=?", new String[]{id});


        } catch (Exception e) {
            e.printStackTrace();
        }
        close();

    }


    private void deleteArtistFromId(String id) {

        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        try {
            mdb.delete(DBConstants.TABLE_ARTIST, DBConstants.SONG_ID + "=?", new String[]{id});


        } catch (Exception e) {
            e.printStackTrace();
        }
        close();

    }


    public ArrayList<SongModel> getSongDetails() {
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        ArrayList<SongModel> songModelArrayList = null;
        Cursor cursor = null;
        try {
            cursor = mdb.rawQuery(DBConstants.SELECT_SONG_DETAILS, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                songModelArrayList = new ArrayList<>();
                for (int j = 0; j < cursor.getCount(); j++) {
                    SongModel songModel = new SongModel();
                    songModel.setSongId(String.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstants.SONG_ID))));
                    songModel.setSongTitle(cursor.getString(cursor.getColumnIndex(DBConstants.SONG_TITLE)));
                    songModel.setSongDuration(cursor.getString(cursor.getColumnIndex(DBConstants.SONG_DURATION)));
                    songModel.setSongYear(cursor.getString(cursor.getColumnIndex(DBConstants.SONG_YEAR)));

                    songModel.setSongHashTagList(getAllHashTagFromId(songModel.getSongId()));
                    songModel.setSongArtistsList(getAllArtistFromId(songModel.getSongId()));
                    cursor.moveToNext();
                    songModelArrayList.add(songModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();

        }
        return songModelArrayList;

    }


    private ArrayList<String> getAllHashTagFromId(String id) {
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        ArrayList<String> hashTagList = null;
        Cursor cursor = null;
        try {
            String[] args = {id};
            cursor = mdb.rawQuery(DBConstants.SELECT_HASHTAG_BY_ID, args);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                hashTagList = new ArrayList<>();
                for (int j = 0; j < cursor.getCount(); j++) {
                    hashTagList.add(cursor.getString((cursor.getColumnIndex(DBConstants.HASHTAG_TITLE))));
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();

        }
        return hashTagList;

    }

    private ArrayList<String> getAllArtistFromId(String id) {
        if (!mdb.isOpen()) {
            mdb = getWritableDatabase();
        }
        ArrayList<String> artistList = null;
        Cursor cursor = null;
        try {
            String[] args = {id};
            cursor = mdb.rawQuery(DBConstants.SELECT_ARTIST_BY_ID, args);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                artistList = new ArrayList<>();
                for (int j = 0; j < cursor.getCount(); j++) {
                    artistList.add(cursor.getString((cursor.getColumnIndex(DBConstants.ARTIST_TITLE))));
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();

        }
        return artistList;

    }


}

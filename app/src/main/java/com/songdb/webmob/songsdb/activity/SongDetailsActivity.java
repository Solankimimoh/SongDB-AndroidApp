package com.songdb.webmob.songsdb.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.songdb.webmob.songsdb.R;
import com.songdb.webmob.songsdb.app.SqliteApp;
import com.songdb.webmob.songsdb.db.DBHelper;
import com.songdb.webmob.songsdb.models.SongModel;

import me.originqiu.library.EditTag;

public class SongDetailsActivity extends AppCompatActivity implements View.OnClickListener, EditTag.TagAddCallback, EditTag.TagDeletedCallback {


    private EditText songTitleEd;
    private EditText songDurationEd;
    private EditText songYearEd;
    private EditTag songHashTagEd;
    private EditTag songArtistEd;
    private Button songInsertBtn;
    private SqliteApp sqliteApp;
    private DBHelper dbHelper;


    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        sqliteApp = (SqliteApp) getApplicationContext();
        dbHelper = sqliteApp.getDbHelper();

        initview();
    }

    private void initview() {

//        component init
        songTitleEd = (EditText) findViewById(R.id.activity_song_details_songtitle_ed);
        songDurationEd = (EditText) findViewById(R.id.activity_song_details_songduration_ed);
        songYearEd = (EditText) findViewById(R.id.activity_song_details_songyear_ed);
        songHashTagEd = (EditTag) findViewById(R.id.dialog_layout_song_hashtag);
        songArtistEd = (EditTag) findViewById(R.id.dialog_layout_song_artist);
        songInsertBtn = (Button) findViewById(R.id.activity_song_details_songSbmt_btn);
        //lister set

        songInsertBtn.setOnClickListener(this);

        songHashTagEd.setTagAddCallBack(this);
        songArtistEd.setTagAddCallBack(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.song_add_menu_browse:
                browseSongFromDirectory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void browseSongFromDirectory() {
        final Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                //the selected audio.
                uri = data.getData();

                final MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(SongDetailsActivity.this, uri);

                final String songTitle = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                final long songDuration = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                final String songYear = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);

                final String durationValue = getDurationFormate(songDuration);


                songTitleEd.setText(songTitle);
                songDurationEd.setText(durationValue);
                songYearEd.setText(songYear);


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    get formated duration in string (MM:SS)
    private String getDurationFormate(long songDuration) {

        final long duration = songDuration / 1000;
        final long h = duration / 3600;
        final long m = (duration - h * 3600) / 60;
        final long s = duration - (h * 3600 + m * 60);

        String durationValue;
        if (h == 0) {
            durationValue = String.format(getString(R.string.song_duration_formate_mmss), m, s);
        } else {
            durationValue = String.format(getString(R.string.song_duration_formate_hhmmss), h, m, s);
        }

        return durationValue;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_song_details_songSbmt_btn:
                insertDataDB();
                break;
            default:

                break;
        }
    }

    private void insertDataDB() {


        final String songTitleDb = songTitleEd.getText().toString().trim();
        final String songDurationDb = songDurationEd.getText().toString().trim();
        final String songYearDb = songYearEd.getText().toString().trim();


        if (songTitleDb.isEmpty() || songDurationDb.isEmpty() || songYearDb.isEmpty() || songHashTagEd.getTagList().isEmpty() || songArtistEd.getTagList().isEmpty()) {
            Toast.makeText(sqliteApp, "Details not filled ! please fill all details", Toast.LENGTH_SHORT).show();
        } else {

            final SongModel songModel = new SongModel("", songTitleDb, songDurationDb, songYearDb, songHashTagEd.getTagList(), songArtistEd.getTagList());

            long i = dbHelper.insertSongDetails(songModel);

            if (i > 0) {
                Toast.makeText(SongDetailsActivity.this, "Song Details Inserted", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }


    }

    @Override
    public boolean onTagAdd(String s) {
        return true;
    }

    @Override
    public void onTagDelete(String s) {

    }
}

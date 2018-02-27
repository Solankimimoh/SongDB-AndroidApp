package com.songdb.webmob.songsdb.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.songdb.webmob.songsdb.R;
import com.songdb.webmob.songsdb.adapter.SongAdapter;
import com.songdb.webmob.songsdb.app.SqliteApp;
import com.songdb.webmob.songsdb.db.DBHelper;
import com.songdb.webmob.songsdb.listener.RecycleItemClickListener;
import com.songdb.webmob.songsdb.models.SongModel;

import java.util.ArrayList;

import me.originqiu.library.EditTag;

public class HomeActivity extends AppCompatActivity implements RecycleItemClickListener, View.OnClickListener {


    private RecyclerView songRecyclerView;
    private ArrayList<SongModel> songModelArrayList;
    private SongAdapter songAdapter;
    private SqliteApp sqliteDemoApp;
    private DBHelper dbHelper;
    private FloatingActionButton searchFloatingActionButton;
    private SearchView searchView;
    private Dialog songDetailsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initview();


        songModelArrayList = dbHelper.getSongDetails();


        if (songModelArrayList != null && !songModelArrayList.isEmpty()) {
            songAdapter = new SongAdapter(songModelArrayList, this);

        } else {
            Toast.makeText(HomeActivity.this, "No Data Availble", Toast.LENGTH_SHORT).show();
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        songRecyclerView.setLayoutManager(mLayoutManager);
        songRecyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.VERTICAL));
        songRecyclerView.setAdapter(songAdapter);

    }


    private void initview() {
        sqliteDemoApp = (SqliteApp) getApplicationContext();
        dbHelper = sqliteDemoApp.getDbHelper();

        songRecyclerView = (RecyclerView) findViewById(R.id.activity_home_song_recycleview);

        searchFloatingActionButton = (FloatingActionButton) findViewById(R.id.activity_home_search_song_fb);
        searchFloatingActionButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                songAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                songAdapter.getFilter().filter(query);
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home_menu_addsong:
                final Intent intent = new Intent(HomeActivity.this, SongDetailsActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (songModelArrayList != null && !songModelArrayList.isEmpty()) {
                    songModelArrayList.clear();
                    songModelArrayList.addAll(dbHelper.getSongDetails());
                    songAdapter.notifyDataSetChanged();


                } else {
                    songModelArrayList = dbHelper.getSongDetails();

                    songAdapter = new SongAdapter(songModelArrayList, this);
                    songRecyclerView.setAdapter(songAdapter);
                    songModelArrayList.clear();
                    songModelArrayList.addAll(dbHelper.getSongDetails());
                    songAdapter.notifyDataSetChanged();
                }

            }
        }
    }


    @Override
    public void itemClickListener(int position, final SongModel songModel) {


        songDetailsDialog = new AppCompatDialog(HomeActivity.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        songDetailsDialog.setContentView(R.layout.dialog_layout);


        songDetailsDialog.setTitle("Song Details");


        final TextView dialogSongTitleTv = (TextView) songDetailsDialog.findViewById(R.id.dialog_layout_song_title);
        final TextView dialogSongDurationTv = (TextView) songDetailsDialog.findViewById(R.id.dialog_layout_song_duration);
        final TextView dialogSongYearTv = (TextView) songDetailsDialog.findViewById(R.id.dialog_layout_song_year);
        final EditTag dialogSongHashtagEd = (EditTag) songDetailsDialog.findViewById(R.id.dialog_layout_song_hashtag);
        final EditTag dialogSongArtistEd = (EditTag) songDetailsDialog.findViewById(R.id.dialog_layout_song_artist);
        final Button dialogSongDeleteBtn = (Button) songDetailsDialog.findViewById(R.id.dialog_layout_song_delete_btn);


        dialogSongHashtagEd.setEditable(false);
        dialogSongArtistEd.setEditable(false);

        dialogSongTitleTv.setText(songModel.getSongTitle());
        dialogSongDurationTv.setText(songModel.getSongDuration());
        dialogSongYearTv.setText(songModel.getSongYear());
        dialogSongHashtagEd.setTagList(songModel.getSongHashTagList());
        dialogSongArtistEd.setTagList(songModel.getSongArtistsList());


        dialogSongDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSongDetails(songModel);
            }
        });

        songDetailsDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            floating button case
            case R.id.activity_home_search_song_fb:
                serachViewInit();
                break;

        }
    }

    private void deleteSongDetails(SongModel songModel) {
        dbHelper.deleteSingleSong(songModel.getSongId());
        songModelArrayList.remove(songModel);
        songAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Song Details Successfully Deleted", Toast.LENGTH_SHORT).show();
        songDetailsDialog.hide();

    }

    private void serachViewInit() {
//fab button method
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}

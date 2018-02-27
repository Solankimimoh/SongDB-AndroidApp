package com.songdb.webmob.songsdb.listener;

import com.songdb.webmob.songsdb.models.SongModel;

/**
 * Created by solan on 10-12-17.
 */

public interface RecycleItemClickListener {

    void itemClickListener(final int position, final SongModel songModel);

}

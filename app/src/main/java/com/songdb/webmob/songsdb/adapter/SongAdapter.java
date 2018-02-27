package com.songdb.webmob.songsdb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.songdb.webmob.songsdb.R;
import com.songdb.webmob.songsdb.listener.RecycleItemClickListener;
import com.songdb.webmob.songsdb.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {


    private ArrayList<SongModel> songModelArrayList;
    private ArrayList<SongModel> filterSongModelArrayList;
    private RecycleItemClickListener itemClick;

    public SongAdapter(ArrayList<SongModel> songModelArrayList, RecycleItemClickListener itemClick) {
        this.songModelArrayList = songModelArrayList;
        this.itemClick = itemClick;
        filterSongModelArrayList = songModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_song, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SongModel songModel = filterSongModelArrayList.get(position);

        holder.songTitleTv.setText(songModel.getSongTitle());
        final String songArtistName = setTextViewValues(songModel.getSongArtistsList());
        holder.songArtistTv.setText(songArtistName);
        holder.songDurationTv.setText(songModel.getSongDuration());
    }

    private String setTextViewValues(List<String> vals) {
        //Variable to hold all the values
        String output = "";

        for (int i = 0; i < vals.size(); i++) {
            //Append all the values to a string
            output += vals.get(i) + ", ";
        }

        return output.substring(0, output.length() - 2);
    }

    @Override
    public int getItemCount() {
        return filterSongModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filterSongModelArrayList = songModelArrayList;
                } else {
                    ArrayList<SongModel> filteredList = new ArrayList<>();
                    for (SongModel row : songModelArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSongTitle().toLowerCase().contains(charString.toLowerCase()) || row.getSongYear().contains(charSequence)) {
                            filteredList.add(row);
                        } else {
                            for (int i = 0; i < row.getSongArtistsList().size(); i++) {
                                if (row.getSongArtistsList().get(i).toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.remove(row);
                                    filteredList.add(row);
                                }

                            }
                        }

                    }

                    filterSongModelArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterSongModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterSongModelArrayList = (ArrayList<SongModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView songTitleTv;
        private TextView songArtistTv;
        private TextView songDurationTv;

        ViewHolder(View itemView) {
            super(itemView);

            songTitleTv = (TextView) itemView.findViewById(R.id.row_layout_song_title);
            songArtistTv = (TextView) itemView.findViewById(R.id.row_layout_song_artist);
            songDurationTv = (TextView) itemView.findViewById(R.id.row_layout_song_duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClick != null) {
                        itemClick.itemClickListener(getAdapterPosition()
                                , filterSongModelArrayList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }
}

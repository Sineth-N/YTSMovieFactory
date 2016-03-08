package com.android.dev.sineth.ytsmoviefactory.RecycleViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dev.sineth.ytsmoviefactory.MovieBriefDetails;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.ViewMovie;
import com.android.dev.sineth.ytsmoviefactory.VolleySingleton;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;


/**
 * Created by Sineth on 2/20/2016.
 */
public class  RecycleViewAdapterMovie extends RecyclerView.Adapter<RecycleViewAdapterMovie.RecycleViewHolders> {

    private List<MovieBriefDetails> briefDetailsList;
    private Context context;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public RecycleViewAdapterMovie(Context context) {
        this.context = context;
        volleySingleton=VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieList(List<MovieBriefDetails> movieList){
        this.briefDetailsList=movieList;
        notifyItemRangeChanged(0,movieList.size());
    }
    @Override
    public RecycleViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View movie_detail = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout,null);
        RecycleViewHolders movie_detail_holder = new RecycleViewHolders(movie_detail);
        return movie_detail_holder;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolders holder, int position) {
        final MovieBriefDetails currentMovie = briefDetailsList.get(position);
        holder.movie_genre.setText(currentMovie.getMovie_genre());
        holder.movie_name.setText(currentMovie.getMovie_title());
        holder.released_Year.setText(currentMovie.getMovie_released_year());
        holder.movie_rating_bar.setRating(currentMovie.getMovie_rating());
        String movie_cover_url = currentMovie.getMovie_cover_url_small();
        if (movie_cover_url!=null){
            imageLoader.get(movie_cover_url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap cover = response.getBitmap();
                    holder.movie_poster.setImageBitmap(cover);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.movie_poster.setImageResource(R.drawable.ic_poster_not_available);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (briefDetailsList==null){
            return 0;
        }
        return briefDetailsList.size();
    }

    class RecycleViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movie_poster;
        TextView movie_name;
        TextView movie_genre;
        TextView released_Year;
        RatingBar movie_rating_bar;

        public RecycleViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movie_poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name);
            movie_genre = (TextView) itemView.findViewById(R.id.movie_genre);
            released_Year = (TextView) itemView.findViewById(R.id.movie_released_year);
            movie_rating_bar = (RatingBar) itemView.findViewById(R.id.movie_rating_bar);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "selected "+getLayoutPosition(), Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent=new Intent(context, ViewMovie.class);
            intent.putExtra("Movie",briefDetailsList.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }
}
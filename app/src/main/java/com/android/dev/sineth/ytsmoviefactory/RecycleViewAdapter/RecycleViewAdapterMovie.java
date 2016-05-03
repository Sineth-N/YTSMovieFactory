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

import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.View.ViewMovie;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;


/**
 * Created by Sineth on 2/20/2016.
 */
public class  RecycleViewAdapterMovie extends RecyclerView.Adapter<RecycleViewAdapterMovie.RecycleViewHolders> {

    private List<Movie> movieList;
    private Context context;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public RecycleViewAdapterMovie(Context context) {
        this.context = context;
        volleySingleton=VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList=movieList;
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
        final Movie currentMovie = movieList.get(position);
        holder.movieGenre.setText(currentMovie.getMovie_genre());
        holder.movieName.setText(currentMovie.getMovie_title());
        holder.releasedYear.setText(currentMovie.getMovie_released_year());
        holder.movieRating.setRating(currentMovie.getMovie_rating());
        String movieCoverUrlSmall = currentMovie.getMovie_cover_url_small();
        if (movieCoverUrlSmall!=null){
            imageLoader.get(movieCoverUrlSmall, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap cover = response.getBitmap();
                    holder.moviePoster.setImageBitmap(cover);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.moviePoster.setImageResource(R.drawable.ic_poster_not_available);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (movieList==null){
            return 0;
        }
        return movieList.size();
    }

    class RecycleViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;
        TextView movieName;
        TextView movieGenre;
        TextView releasedYear;
        RatingBar movieRating;

        public RecycleViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            movieGenre = (TextView) itemView.findViewById(R.id.movie_genre);
            releasedYear = (TextView) itemView.findViewById(R.id.movie_released_year);
            movieRating = (RatingBar) itemView.findViewById(R.id.movie_rating_bar);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "selected "+getLayoutPosition(), Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent=new Intent(context, ViewMovie.class);
            intent.putExtra("Movie",movieList.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }
}
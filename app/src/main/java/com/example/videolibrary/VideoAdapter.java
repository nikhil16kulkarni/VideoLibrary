package com.example.videolibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> allvideos;
    private Context context;

    // Creating Constructor
    public VideoAdapter(Context ctx, List<Video> videos){
        this.allvideos = videos;
        this.context = ctx;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(allvideos.get(position).getTitle());
        Picasso.get().load(allvideos.get(position).getImageUrl()).into(holder.videoImage);

        holder.vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable("videoData", allvideos.get(position)); // VideoData is key & video is object(Value)

                Intent i = new Intent(context, Player.class);
                i.putExtras(b); // Put all the bundle
                view.getContext().startActivity(i);

                //view.getContext().startActivity(new Intent(context, Player.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return allvideos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView videoImage;
        TextView title;
        View vv;
        public ViewHolder(View itemView){
            super(itemView);

            videoImage = itemView.findViewById(R.id.videoThumbnell);
            title = itemView.findViewById(R.id.videoTitle);
            vv = itemView;
        }
    }

}

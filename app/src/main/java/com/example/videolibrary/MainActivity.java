package com.example.videolibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView videoList;
    VideoAdapter adapter;
    List<Video> all_videos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all_videos = new ArrayList<>();

        videoList = findViewById(R.id.videoList);
        videoList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VideoAdapter(this, all_videos);
        videoList.setAdapter(adapter);

        getJsonData();

    }

    private void getJsonData() {
        String URL = "https://raw.githubusercontent.com/bikashthapa01/myvideos-android-app/master/data.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("Message", "onResponse: " + response); // Everything inside JSON file will be displayed
                try {
                    JSONArray categories = response.getJSONArray("categories"); // JSONArray :
                    JSONObject categoriesData = categories.getJSONObject(0); // category JSONArray has only 1 JSONObject (JSONObject : [])
                    JSONArray videos = categoriesData.getJSONArray("videos"); // categoriesData has 2 JSONArrays : name & videos. But we need only videos

                    //Log.d("Message", "onResponse: " +videos); // Now here everything after videos : [] will be displayed
                    // Currently videos has 13 items so iterating 13 times.

                    for(int i=0; i<videos.length(); i++){
                        JSONObject video = videos.getJSONObject(i);
                        //Log.d("Message", "onResponse: " +video.getString("title")); // Giving title as a key

                        Video v = new Video(); // Object of class Video

                        v.setTitle(video.getString("title"));
                        v.setDescription(video.getString("description"));
                        v.setAuthor(video.getString("subtitle"));
                        v.setImageUrl(video.getString("thumb"));

                        JSONArray videoUrl = video.getJSONArray("sources"); // Because in JSON File, there is again one JSONArray([])
                        v.setVideoUrl(videoUrl.getString(0));

                        //Log.d("Message", "onResponse: " +v.getVideoUrl()); // Just to check whether we are accessing the video urls correctly or not

                        all_videos.add(v); // Add the information in the Array List which will be later used to display in Recycler View

                        adapter.notifyDataSetChanged(); // To let the adapter that we've changed the data

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Message", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(objectRequest);

    }
}
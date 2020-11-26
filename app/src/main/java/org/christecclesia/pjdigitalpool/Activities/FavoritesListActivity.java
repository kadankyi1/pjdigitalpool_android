package org.christecclesia.pjdigitalpool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.ListDataGenerators.FavoritesListDataGenerator;
import org.christecclesia.pjdigitalpool.Models.AudioModel;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritesListActivity extends AppCompatActivity  implements View.OnClickListener {

    private ImageView m_back_imageview, m_reload_imageview;
    private ProgressBar m_loading_progressbar;
    private RecyclerView m_recyclerview;
    private LinearLayoutManager m_linearlayoutmanager;
    int getting = 0, error = 0;
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        m_back_imageview = findViewById(R.id.activity_audioslist_back_imageView);
        m_reload_imageview = findViewById(R.id.activity_audioslist_imageview);
        m_loading_progressbar = findViewById(R.id.activity_audioslist_contentloading_progressbar);
        m_recyclerview = findViewById(R.id.activity_audioslist_recyclerView);


        ArrayList<String> favorites = Util.getSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS);
        if(favorites != null){
            if(favorites.isEmpty()){
                error = 1;
            }
        } else {
            error = 1;
        }


        m_linearlayoutmanager = new LinearLayoutManager(FavoritesListActivity.this);

        m_recyclerview.setItemViewCacheSize(20);
        m_recyclerview.setDrawingCacheEnabled(true);
        m_recyclerview.setHasFixedSize(true);
        m_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        m_recyclerview.setLayoutManager(m_linearlayoutmanager);
        m_recyclerview.setAdapter(new RecyclerViewAdapter());

        if(error == 1){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                    .setTitle("Oops")
                    .setMessage("You have not set any favorites")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    //.setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false);

            AlertDialog dialog = alertDialogBuilder.show();
            dialog.setCanceledOnTouchOutside(false);
        } else {
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api("Bearer " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                }
            });
            network_thread.start();

        }
        m_reload_imageview.setOnClickListener(this);
        m_back_imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        } else if(view.getId() == m_reload_imageview.getId()){
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api("Bearer " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                }
            });
            network_thread.start();
        }
    }



    private void allOnClickHandlers(View v, int position){
        if(v.getId() == R.id.parent_holder
                || v.getId() == R.id.listitemaudio_image_constraintlayout
                || v.getId() == R.id.listitemaudio_image_imageview
                || v.getId() == R.id.listitemaudio_title_textview
                || v.getId() == R.id.listitemaudio_time_textview){
            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID, String.valueOf(FavoritesListDataGenerator.getAllData().get(position).getAudio_id()));
            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL, FavoritesListDataGenerator.getAllData().get(position).getAudio_image());
            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL, FavoritesListDataGenerator.getAllData().get(position).getAudio_mp3());
            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE, FavoritesListDataGenerator.getAllData().get(position).getAudio_name());
            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME, FavoritesListDataGenerator.getAllData().get(position).getCreated_at());
            Intent intent = new Intent(getApplicationContext(), AudioPlayerActivity.class);
            startActivity(intent);
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if(FavoritesListDataGenerator.getAllData().get(position).getRowId() == 0){
                return 0;
            }
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_audio, parent, false);
            vh = new AudioViewHolder(v);

            return vh;
        }


        public class AudioViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout m_parent_holder_constraintlayout, m_image_holder_constraintlayout;
            private RoundedCornerImageView m_audio_image;
            private TextView m_title_textview, m_date_textview;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public AudioViewHolder(View v) {
                super(v);
                m_parent_holder_constraintlayout = v.findViewById(R.id.parent_holder);
                m_image_holder_constraintlayout = v.findViewById(R.id.listitemaudio_image_constraintlayout);
                m_audio_image = v.findViewById(R.id.listitemaudio_image_imageview);
                m_title_textview = v.findViewById(R.id.listitemaudio_title_textview);
                m_date_textview = v.findViewById(R.id.listitemaudio_time_textview);

                m_parent_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_image_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_audio_image.setOnClickListener(innerClickListener);
                m_title_textview.setOnClickListener(innerClickListener);
                m_date_textview.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            if(
                    !FavoritesListActivity.this.isFinishing()
                            && !FavoritesListDataGenerator.getAllData().get(position).getAudio_image().equalsIgnoreCase("")
            ){

                Util.loadImageView(getApplicationContext(), FavoritesListDataGenerator.getAllData().get(position).getAudio_image().trim(), ((AudioViewHolder) holder).m_audio_image, null);

            } else {
                ((AudioViewHolder) holder).m_audio_image.setImageResource(R.drawable.testimg);
            }
            ((AudioViewHolder) holder).m_title_textview.setText(FavoritesListDataGenerator.getAllData().get(position).getAudio_name());
            ((AudioViewHolder) holder).m_date_textview.setText(FavoritesListDataGenerator.getAllData().get(position).getCreated_at());

        }

        @Override
        public int getItemCount() {
            return FavoritesListDataGenerator.getAllData().size();
        }

    }


    private void call_audio_list_api(final String token){

        ArrayList<String> favorites = Util.getSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS);
        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_reload_imageview.setVisibility(View.INVISIBLE);
                    m_recyclerview.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("FavoritesListAct", "\n favorites: " + favorites.toString());
            Util.show_log_in_console("FavoritesListAct", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_FAVORITES_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("FavoritesListAct", "response: " +  response);
                            if(!FavoritesListActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONArray linkupsSuggestionsArray = response_json_object.getJSONObject("data").getJSONArray("data");

                                        Util.show_log_in_console("FavoritesListAct", "linkupsSuggestionsArray: " + linkupsSuggestionsArray.toString());
                                        if (linkupsSuggestionsArray.length() > 0) {
                                            FavoritesListDataGenerator.getAllData().clear();

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_recyclerview.getAdapter().notifyDataSetChanged();
                                                }
                                            });
                                            for (int i = 0; i < linkupsSuggestionsArray.length(); i++) {
                                                AudioModel mine1 = new AudioModel();
                                                final JSONObject k = linkupsSuggestionsArray.getJSONObject(i);
                                                mine1.setAudio_id(k.getInt("audio_id"));
                                                mine1.setAudio_name(k.getString("audio_name"));
                                                mine1.setAudio_description(k.getString("audio_description"));
                                                mine1.setAudio_image(k.getString("audio_image"));
                                                mine1.setAudio_mp3(k.getString("audio_mp3"));
                                                mine1.setCreated_at(k.getString("created_at"));
                                                FavoritesListDataGenerator.addOneData(mine1);

                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!FavoritesListActivity.this.isFinishing() && m_recyclerview != null) {
                                                            m_recyclerview.getAdapter().notifyItemInserted(FavoritesListDataGenerator.getAllData().size());
                                                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                                                            m_reload_imageview.setVisibility(View.INVISIBLE);
                                                            m_recyclerview.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        } else {

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                                    m_recyclerview.setVisibility(View.INVISIBLE);
                                                    m_reload_imageview.setVisibility(View.VISIBLE);
                                                    Toast.makeText(getApplicationContext(), "No Audios found", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    } else {
                                        m_recyclerview.setVisibility(View.INVISIBLE);
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_reload_imageview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                    m_recyclerview.setVisibility(View.INVISIBLE);
                                    m_reload_imageview.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                            m_recyclerview.setVisibility(View.INVISIBLE);
                            m_reload_imageview.setVisibility(View.VISIBLE);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", token);
                    //headers.put("ContentType", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("favorites", favorites.toString());
                    //Util.show_log_in_console("LoginActivity", "Map: " +  map.toString());
                    return map;
                }
            };
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}
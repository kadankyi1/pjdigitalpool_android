package org.christecclesia.pjdigitalpool.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.christecclesia.pjdigitalpool.Activities.AudioPlayerActivity;
import org.christecclesia.pjdigitalpool.Activities.AudiosListActivity;
import org.christecclesia.pjdigitalpool.Activities.FavoritesListActivity;
import org.christecclesia.pjdigitalpool.Activities.VideoPlayerActivity;
import org.christecclesia.pjdigitalpool.Activities.VideosListActivity;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.ListDataGenerators.AudioListDataGenerator;
import org.christecclesia.pjdigitalpool.ListDataGenerators.VideoListDataGenerator;
import org.christecclesia.pjdigitalpool.Models.AudioModel;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LibraryFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BottomNavigationView bottomNavigation;
    private ImageView m_back_imageview, m_reload_imageview;
    private ProgressBar m_loading_progressbar;
    private RecyclerView m_recyclerview;
    private LinearLayoutManager m_linearlayoutmanager;
    int getting = 0,  error = 0;
    private Thread network_thread = null;
    private String mParam1;
    private String mParam2;
    private String current_page = "audios", current_favorites = "";
    private String current_page_url = Util.LINK_AUDIO_LIST;

    public LibraryFragment() {}

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        bottomNavigation = view.findViewById(R.id.bottom_navigation);
        m_reload_imageview = view.findViewById(R.id.activity_audioslist_imageview);
        m_loading_progressbar = view.findViewById(R.id.activity_audioslist_contentloading_progressbar);
        m_recyclerview = view.findViewById(R.id.activity_audioslist_recyclerView);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.navigation_audios);



        m_linearlayoutmanager = new LinearLayoutManager(getActivity());

        m_recyclerview.setItemViewCacheSize(20);
        m_recyclerview.setDrawingCacheEnabled(true);
        m_recyclerview.setHasFixedSize(true);
        m_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        m_recyclerview.setLayoutManager(m_linearlayoutmanager);
        m_recyclerview.setAdapter(new RecyclerViewAdapter());


        m_reload_imageview.setOnClickListener(this);

        /*
        view.findViewById(R.id.fragment_library_audios_roundedcornerimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AudiosListActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.fragment_library_videos_roundedcornerimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), VideosListActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.fragment_library_favorites_constraintlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FavoritesListActivity.class);
                startActivity(intent);
            }
        });
         */
        return view;
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_audios:
                            current_favorites = "";
                            current_page = "audios";
                            current_page_url = Util.LINK_AUDIO_LIST;
                            network_thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    call_audio_list_api("audios", Util.LINK_AUDIO_LIST, current_favorites, "Bearer " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                                }
                            });
                            network_thread.start();
                            return true;
                        case R.id.navigation_videos:
                            current_favorites = "";
                            current_page = "videos";
                            current_page_url = Util.LINK_VIDEOS_LIST;
                            network_thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    call_audio_list_api("videos", Util.LINK_VIDEOS_LIST, current_favorites, "Bearer " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                                }
                            });
                            network_thread.start();
                            return true;
                        case R.id.navigation_favorites:
                            if (!AudioListDataGenerator.getAllData().isEmpty()){
                                AudioListDataGenerator.getAllData().clear();
                                if(!getActivity().isFinishing() && m_recyclerview != null && m_recyclerview.getAdapter() != null){
                                    m_recyclerview.getAdapter().notifyDataSetChanged();
                                }
                            }

                            current_page = "favorites";
                            current_page_url = Util.LINK_FAVORITES_LIST;
                            ArrayList<String> favorites = Util.getSharedPreferenceStringSet(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS);
                            if(favorites != null){
                                if(favorites.isEmpty()){
                                    error = 1;
                                }
                            } else {
                                error = 1;
                            }
                            if(error == 1){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog))
                                        .setTitle("Oops")
                                        .setMessage("You have not set any favorites")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //finish();
                                            }
                                        })
                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        //.setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setCancelable(false);

                                AlertDialog dialog = alertDialogBuilder.show();
                                dialog.setCanceledOnTouchOutside(false);
                            } else {
                                current_favorites = favorites.toString();
                                network_thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        call_audio_list_api("favorites", Util.LINK_FAVORITES_LIST, current_favorites, "Bearer " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                                    }
                                });
                                network_thread.start();
                            }
                            return true;
                    }
                    return false;
                }
            };


    @Override
    public void onClick(View view) {
        if(view.getId() == m_reload_imageview.getId()){
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api(current_page, current_page_url, current_favorites, "Bearer " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                }
            });
            network_thread.start();
        }
    }

    private void allOnClickHandlers(View v, int position){
        if(v.getId() == R.id.listitemaudio_parent_holder
                || v.getId() == R.id.listitemaudio_image_constraintlayout
                || v.getId() == R.id.listitemaudio_image_imageview
                || v.getId() == R.id.listitemaudio_title_textview
                || v.getId() == R.id.listitemaudio_time_textview){
            if(current_page == "videos"){
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL, AudioListDataGenerator.getAllData().get(position).getAudio_mp3());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_IMG_URL, AudioListDataGenerator.getAllData().get(position).getAudio_image());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_TITLE, AudioListDataGenerator.getAllData().get(position).getAudio_name());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME, AudioListDataGenerator.getAllData().get(position).getCreated_at());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_BODY, AudioListDataGenerator.getAllData().get(position).getAudio_description());
                Intent intent = new Intent(getActivity().getApplicationContext(), VideoPlayerActivity.class);
                startActivity(intent);
            } else {
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID, String.valueOf(AudioListDataGenerator.getAllData().get(position).getAudio_id()));
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL, AudioListDataGenerator.getAllData().get(position).getAudio_image());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL, AudioListDataGenerator.getAllData().get(position).getAudio_mp3());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE, AudioListDataGenerator.getAllData().get(position).getAudio_name());
                Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME, AudioListDataGenerator.getAllData().get(position).getCreated_at());
                Intent intent = new Intent(getActivity().getApplicationContext(), AudioPlayerActivity.class);
                startActivity(intent);
            }
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if(AudioListDataGenerator.getAllData().get(position).getRowId() == 0){
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
                m_parent_holder_constraintlayout = v.findViewById(R.id.listitemaudio_parent_holder);
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
                    !getActivity().isFinishing()
                            && !AudioListDataGenerator.getAllData().get(position).getAudio_image().equalsIgnoreCase("")
            ){

                Util.loadImageView(getActivity().getApplicationContext(), AudioListDataGenerator.getAllData().get(position).getAudio_image().trim(), ((AudioViewHolder) holder).m_audio_image, null);

            } else {
                ((AudioViewHolder) holder).m_audio_image.setImageResource(R.drawable.testimg);
            }
            ((AudioViewHolder) holder).m_title_textview.setText(AudioListDataGenerator.getAllData().get(position).getAudio_name());
            ((AudioViewHolder) holder).m_date_textview.setText(AudioListDataGenerator.getAllData().get(position).getCreated_at());

        }

        @Override
        public int getItemCount() {
            return AudioListDataGenerator.getAllData().size();
        }

    }


    private void call_audio_list_api(final String fetch_type, final String url, final String data, final String token){

        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_reload_imageview.setVisibility(View.INVISIBLE);
                    m_recyclerview.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                }
            });


            Util.show_log_in_console("AudiosListAct", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("AudiosListAct", "response: " +  response);
                            if(!getActivity().isFinishing() && current_page.trim().equalsIgnoreCase(fetch_type)){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONArray linkupsSuggestionsArray = response_json_object.getJSONObject("data").getJSONArray("data");

                                        Util.show_log_in_console("AudiosListAct", "linkupsSuggestionsArray: " + linkupsSuggestionsArray.toString());
                                        if (linkupsSuggestionsArray.length() > 0) {
                                            AudioListDataGenerator.getAllData().clear();

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_recyclerview.getAdapter().notifyDataSetChanged();
                                                }
                                            });
                                            for (int i = 0; i < linkupsSuggestionsArray.length(); i++) {
                                                AudioModel mine1 = new AudioModel();
                                                final JSONObject k = linkupsSuggestionsArray.getJSONObject(i);
                                                if(fetch_type == "videos"){
                                                    mine1.setAudio_id(k.getInt("video_id"));
                                                    mine1.setAudio_name(k.getString("video_name"));
                                                    mine1.setAudio_description(k.getString("video_description"));
                                                    mine1.setAudio_image(k.getString("video_image"));
                                                    mine1.setAudio_mp3(k.getString("video_mp4"));
                                                    mine1.setCreated_at(k.getString("created_at"));
                                                } else {
                                                    mine1.setAudio_id(k.getInt("audio_id"));
                                                    mine1.setAudio_name(k.getString("audio_name"));
                                                    mine1.setAudio_description(k.getString("audio_description"));
                                                    mine1.setAudio_image(k.getString("audio_image"));
                                                    mine1.setAudio_mp3(k.getString("audio_mp3"));
                                                    mine1.setCreated_at(k.getString("created_at"));
                                                }
                                                AudioListDataGenerator.addOneData(mine1);

                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!getActivity().isFinishing() && m_recyclerview != null) {
                                                            m_recyclerview.getAdapter().notifyItemInserted(AudioListDataGenerator.getAllData().size());
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
                                                    Toast.makeText(getActivity().getApplicationContext(), "Oops. Nothing to show", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    } else {
                                        m_recyclerview.setVisibility(View.INVISIBLE);
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_reload_imageview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity().getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity().getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
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
                    map.put("favorites", data);
                    //Util.show_log_in_console("LoginActivity", "Map: " +  map.toString());
                    return map;
                }
            };
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


}

package org.christecclesia.pjdigitalpool.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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

import org.christecclesia.pjdigitalpool.Activities.ImageArticleActivity;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.ListDataGenerators.ArticleListDataGenerator;
import org.christecclesia.pjdigitalpool.Models.ArticleModel;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.christecclesia.pjdigitalpool.Inc.Util.*;

public class ReadFragment extends Fragment implements View.OnClickListener {

    private ImageView m_reload_imageview;
    private ProgressBar m_loading_progressbar;
    private RecyclerView m_recyclerview;
    private LinearLayoutManager m_linearlayoutmanager;
    int getting = 0;
    private Thread network_thread = null;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReadFragment() {}

    public static ReadFragment newInstance(String param1, String param2) {
        ReadFragment fragment = new ReadFragment();
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
        View view =  inflater.inflate(R.layout.fragment_read, container, false);

        m_reload_imageview = view.findViewById(R.id.activity_audioslist_imageview);
        m_loading_progressbar = view.findViewById(R.id.activity_audioslist_contentloading_progressbar);
        m_recyclerview = view.findViewById(R.id.activity_audioslist_recyclerView);


        m_linearlayoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());

        m_recyclerview.setItemViewCacheSize(20);
        m_recyclerview.setDrawingCacheEnabled(true);
        m_recyclerview.setHasFixedSize(true);
        m_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        m_recyclerview.setLayoutManager(m_linearlayoutmanager);
        m_recyclerview.setAdapter(new RecyclerViewAdapter());

        network_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                call_api("Bearer " + getSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_USER_TOKEN));
            }
        });
        network_thread.start();

        m_reload_imageview.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == m_reload_imageview.getId()){
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_api("Bearer " + getSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_USER_TOKEN));
                }
            });
            network_thread.start();
        }
    }


    private void allOnClickHandlers(View v, int position){
        if(v.getId() == R.id.article_parent
                || v.getId() == R.id.fragment_today_heraldofglorytitle_textview
                || v.getId() == R.id.fragment_today_heraldofglorybody_textview
                || v.getId() == R.id.tag_holder
                || v.getId() == R.id.fragment_today_heraldofgloryimage_roundedcornerimageview
                || v.getId() == R.id.fragment_today_heraldofglorylabel_textview){

            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL, String.valueOf(ArticleListDataGenerator.getAllData().get(position).getArticle_image()));
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT, ArticleListDataGenerator.getAllData().get(position).getArticle_type());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME, ArticleListDataGenerator.getAllData().get(position).getCreated_at());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE, ArticleListDataGenerator.getAllData().get(position).getArticle_title());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY, ArticleListDataGenerator.getAllData().get(position).getArticle_body());
            Intent intent = new Intent(getActivity().getApplicationContext(), ImageArticleActivity.class);
            startActivity(intent);
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if(ArticleListDataGenerator.getAllData().get(position).getRowId() == 0){
                return 0;
            }
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_read, parent, false);
            vh = new ArticleViewHolder(v);

            return vh;
        }


        public class ArticleViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout m_parent_holder_constraintlayout, m_tag_holder_constraintlayout;
            private ImageView m_audio_image;
            private TextView m_title_textview, m_body_textview, m_tag_textview;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public ArticleViewHolder(View v) {
                super(v);
                m_parent_holder_constraintlayout = v.findViewById(R.id.article_parent);
                m_title_textview = v.findViewById(R.id.fragment_today_heraldofglorytitle_textview);
                m_body_textview = v.findViewById(R.id.fragment_today_heraldofglorybody_textview);
                m_tag_holder_constraintlayout = v.findViewById(R.id.tag_holder);
                m_audio_image = v.findViewById(R.id.fragment_today_heraldofgloryimage_roundedcornerimageview);
                m_tag_textview = v.findViewById(R.id.fragment_today_heraldofglorylabel_textview);

                m_parent_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_title_textview.setOnClickListener(innerClickListener);
                m_body_textview.setOnClickListener(innerClickListener);
                m_tag_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_audio_image.setOnClickListener(innerClickListener);
                m_tag_textview.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            if(
                    !getActivity().isFinishing()
                            && !ArticleListDataGenerator.getAllData().get(position).getArticle_image().equalsIgnoreCase("")
            ){

                loadImageView(getActivity().getApplicationContext(), ArticleListDataGenerator.getAllData().get(position).getArticle_image().trim(), ((ArticleViewHolder) holder).m_audio_image, null);

            } else {
                ((ArticleViewHolder) holder).m_audio_image.setImageResource(R.drawable.testimg);
            }

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_title().length() > 20){
                ((ArticleViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_title().substring(0, 19).trim() + "...");
            } else {
                ((ArticleViewHolder) holder).m_title_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_title());
            }

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_body().length() > 85){
                ((ArticleViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body().substring(0, 84).trim() + "...");
            } else {
                ((ArticleViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body());
            }

            ((ArticleViewHolder) holder).m_tag_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_type());

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("HERALD OF GLORY")){
                ((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_bottom_left_corners_gold, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("SPECIAL ARTICLE")){
                ((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_specialarticle, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("GLORY NEWS")){
                ((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_glorynews, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("BIBLE READING PLAN")){
                ((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_bible_reading, null));
            }

        }

        @Override
        public int getItemCount() {
            return ArticleListDataGenerator.getAllData().size();
        }

    }

    private void call_api(final String token){

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


            StringRequest stringRequest = new StringRequest(Request.Method.GET, Util.LINK_ARTICLES_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("AudiosListAct", "response: " +  response);
                            if(!getActivity().isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONArray linkupsSuggestionsArray = response_json_object.getJSONObject("data").getJSONArray("data");

                                        Util.show_log_in_console("AudiosListAct", "linkupsSuggestionsArray: " + linkupsSuggestionsArray.toString());
                                        if (linkupsSuggestionsArray.length() > 0) {
                                            ArticleListDataGenerator.getAllData().clear();

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_recyclerview.getAdapter().notifyDataSetChanged();
                                                }
                                            });
                                            for (int i = 0; i < linkupsSuggestionsArray.length(); i++) {
                                                ArticleModel mine1 = new ArticleModel();
                                                final JSONObject k = linkupsSuggestionsArray.getJSONObject(i);
                                                mine1.setArticle_id(k.getInt("article_id"));
                                                mine1.setArticle_type(k.getString("article_type"));
                                                mine1.setArticle_title(k.getString("article_title"));
                                                mine1.setArticle_body(k.getString("article_body"));
                                                mine1.setArticle_image(k.getString("article_image"));
                                                mine1.setCreated_at(k.getString("created_at"));
                                                ArticleListDataGenerator.addOneData(mine1);

                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!getActivity().isFinishing() && m_recyclerview != null) {
                                                            m_recyclerview.getAdapter().notifyItemInserted(ArticleListDataGenerator.getAllData().size());
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
                                                    Toast.makeText(getActivity().getApplicationContext(), "No Articles found", Toast.LENGTH_LONG).show();
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


                /*
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("user_phone_number", phone_number);
                    map.put("password", password);
                    Util.show_log_in_console("LoginActivity", "Map: " +  map.toString());
                    return map;
                }
                 */
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

package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AudiosListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView m_back_imageview;
    private ProgressBar m_loading_progressbar;
    private RecyclerView m_recyclerview;
    private Thread network_thread = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios_list);

        m_back_imageview = findViewById(R.id.activity_audioslist_back_imageView);
        m_loading_progressbar = findViewById(R.id.activity_audioslist_contentloading_progressbar);
        m_recyclerview = findViewById(R.id.activity_audioslist_recyclerView);


        network_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                call_audio_list_api("Bearer " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
            }
        });
        network_thread.start();

        m_back_imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        }
    }

    private void call_audio_list_api(final String token){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_recyclerview.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("AudiosListAct", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, Util.LINK_AUDIO_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("AudiosListAct", "response: " +  response);
                            if(!AudiosListActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    //Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONObject audios_list_json_object = response_json_object.getJSONObject("data").getJSONObject("data");

                                        Util.show_log_in_console("AudiosListAct", "response: " +  audios_list_json_object);

                                    } else {
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_recyclerview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                    m_recyclerview.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and swipe down to try again", Toast.LENGTH_LONG).show();
                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                            m_recyclerview.setVisibility(View.INVISIBLE);
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

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}

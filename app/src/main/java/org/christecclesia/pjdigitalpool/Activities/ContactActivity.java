package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_USER_TOKEN;
import static org.christecclesia.pjdigitalpool.Inc.Util.getSharedPreferenceString;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView m_cover_img_imageview, m_back_Imageview;
    private TextView m_title_textview;
    private EditText m_message_editttext;
    private Button m_send_button;
    private ProgressBar m_loading_progressbar;
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        m_back_Imageview =  findViewById(R.id.activity_imagearticle_backimage_imageview);
        m_cover_img_imageview =  findViewById(R.id.activity_imagearticle_image_imageview);
        m_title_textview =  findViewById(R.id.contactactivity_title);
        m_message_editttext =  findViewById(R.id.contactactivity_message_edittext);
        m_loading_progressbar =  findViewById(R.id.contactactivity_contentloading_progressbar);
        m_send_button =  findViewById(R.id.contactactivity_message_button);
        m_title_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE));

        if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE).equalsIgnoreCase("Prayer Request")){
            m_message_editttext.setHint("Write your prayer request");
            m_cover_img_imageview.setImageResource(R.drawable.prayer_request);
        } else if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE).equalsIgnoreCase("Feedback")){
            m_message_editttext.setHint("Write your feedback");
            m_cover_img_imageview.setImageResource(R.drawable.feedback);
        } else if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE).equalsIgnoreCase("Testimonies")){
            m_message_editttext.setHint("Write your testimony");
            m_cover_img_imageview.setImageResource(R.drawable.testimony);
        }

        m_back_Imageview.setOnClickListener(this);
        m_send_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_send_button.getId()){
            String message = m_message_editttext.getText().toString().trim();
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!message.equalsIgnoreCase("")){
                        call_api("Bearer " + getSharedPreferenceString(getApplicationContext(), SHARED_PREF_KEY_USER_TOKEN), message);
                    }
                }
            });
            network_thread.start();
        } else if(view.getId() == m_back_Imageview.getId()){
            onBackPressed();
        }
    }


    private void call_api(final String token, final String the_message){

        if(!ContactActivity.this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_message_editttext.setVisibility(View.INVISIBLE);
                    m_send_button.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("AudiosListAct", "\n token: " + token);
            Util.show_log_in_console("AudiosListAct", "\n the_message: " + the_message);
            Util.show_log_in_console("AudiosListAct", "\n SHARED_PREF_KEY_CONTACT_TYPE: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE));
            Util.show_log_in_console("AudiosListAct", "\n SHARED_PREF_KEY_USER_ID: " + String.valueOf(Util.getSharedPreferenceInt(getApplicationContext(), Util.SHARED_PREF_KEY_USER_ID)));


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_CONTACT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("AudiosListAct", "response: " +  response);
                            if(!ContactActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                m_loading_progressbar.setVisibility(View.INVISIBLE);
                                                m_send_button.setVisibility(View.VISIBLE);
                                                m_message_editttext.setVisibility(View.VISIBLE);
                                                m_message_editttext.getText().clear();
                                                Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else {
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_send_button.setVisibility(View.VISIBLE);
                                        m_message_editttext.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                    m_send_button.setVisibility(View.VISIBLE);
                                    m_message_editttext.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                            m_send_button.setVisibility(View.VISIBLE);
                            m_message_editttext.setVisibility(View.VISIBLE);
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
                    map.put("message_text", the_message);
                    map.put("message_type", Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE));
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

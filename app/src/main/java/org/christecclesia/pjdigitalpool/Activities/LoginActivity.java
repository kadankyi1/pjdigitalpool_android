package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText m_phone_edittext, m_password_edittext;
    private Button m_login_button;
    private TextView m_signup_textview, m_forgotpassword_textview;
    private ContentLoadingProgressBar m_loggingin_contentloadingprogressbar;
    private ScrollView m_formholder_scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_formholder_scrollview = findViewById(R.id.loginactivity_formholder_scrollview);
        m_phone_edittext = findViewById(R.id.loginactivity_textinputedittext_phone);
        m_password_edittext = findViewById(R.id.loginactivity_textinputedittext_password);
        m_login_button = findViewById(R.id.loginactivity_button_login);
        m_loggingin_contentloadingprogressbar = findViewById(R.id.loginactivity_loader_contentloadingprogressbar);
        m_signup_textview = findViewById(R.id.loginactivity_textview_signup);
        m_forgotpassword_textview = findViewById(R.id.loginactivity_textview_forgotpassword);

        m_login_button.setOnClickListener(this);
        m_signup_textview.setOnClickListener(this);
        m_forgotpassword_textview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_login_button.getId()){
            if(!m_phone_edittext.getText().toString().trim().isEmpty()
                    && !m_password_edittext.getText().toString().trim().isEmpty()){
                call_sign_in_api(m_phone_edittext.getText().toString().trim(), m_password_edittext.getText().toString().trim());
            }
        } else if(view.getId() == m_signup_textview.getId()){
            onBackPressed();
        } else if(view.getId() == m_forgotpassword_textview.getId()){
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        }
    }

    private void call_sign_in_api(final String phone_number, final String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_formholder_scrollview.setVisibility(View.INVISIBLE);
                    m_loggingin_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity",
                    "\n phone_number: " + phone_number
                    + "\n password: " + password);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("SignupActivity", "response: " +  response);
                            if(!LoginActivity.this.isFinishing()){
                                //if(MyLifecycleHandler.isApplicationVisible() || MyLifecycleHandler.isApplicationInForeground()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    //Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    if(response_json_object.getInt("status") == 1){
                                        JSONObject user_json_object = response_json_object.getJSONObject("user");
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN, response_json_object.getString("access_token"));
                                        Util.setSharedPreferenceInt(getApplicationContext(), Util.SHARED_PREF_KEY_USER_ID, user_json_object.getInt("user_id"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME, user_json_object.getString("user_firstname"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME, user_json_object.getString("user_surname"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_COUNTRY, user_json_object.getString("user_country"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_PHONE, user_json_object.getString("user_phone_number"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL, user_json_object.getString("user_email"));
                                        Util.setSharedPreferenceInt(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FLAGGED, user_json_object.getInt("user_flagged"));

                                        /******************************************************************************************************
                                         *
                                         * BACKGROUND SYNC ITEM FROM SERVER
                                         *
                                         ******************************************************************************************************/

                                        JSONArray notice = response_json_object.getJSONObject("data").getJSONArray("data");
                                        final JSONObject k = notice.getJSONObject(0);
                                        Util.show_log_in_console("LoginDashboard", "notice_image: " + String.valueOf(k.getString("notice_image")));

                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL, k.getString("notice_image"));

                                        JSONArray audio = response_json_object.getJSONObject("audios").getJSONArray("data");
                                        final JSONObject a = audio.getJSONObject(0);
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID, String.valueOf(a.getInt("audio_id")));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_URL, a.getString("audio_mp3"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL, a.getString("audio_image"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE, a.getString("audio_name"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY, a.getString("audio_description"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_UPLOAD_TIME, a.getString("created_at"));

                                        JSONArray videos = response_json_object.getJSONObject("videos").getJSONArray("data");
                                        final JSONObject v = videos.getJSONObject(0);
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_URL, v.getString("video_mp4"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL, v.getString("video_image"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_TITLE, v.getString("video_name"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH, v.getString("created_at"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_UPLOAD_TIME, v.getString("created_at"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_BODY, v.getString("video_description"));

                                        final JSONObject v2 = videos.getJSONObject(1);
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_URL, v2.getString("video_mp4"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL, v2.getString("video_image"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_TITLE, v2.getString("video_name"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH, v2.getString("created_at"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_UPLOAD_TIME, v2.getString("created_at"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_BODY, v2.getString("video_description"));

                                        /******************************************************************************************************
                                         *
                                         * --- END BACKGROUND SYNC ITEM FROM SERVER
                                         *
                                         ******************************************************************************************************/

                                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_formholder_scrollview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                    m_formholder_scrollview.setVisibility(View.VISIBLE);
                                }

                                //}
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Util.show_log_in_console("SignupActivity", "error: " + error.toString());
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                            m_formholder_scrollview.setVisibility(View.VISIBLE);
                        }
                    }) {

                /*
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    //headers.put("ContentType","multipart/form-data");
                    //headers.put("ContentType", "application/json");
                    return headers;
                }
                 */

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("user_phone_number", phone_number);
                    map.put("password", password);
                    Util.show_log_in_console("LoginActivity", "Map: " +  map.toString());
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

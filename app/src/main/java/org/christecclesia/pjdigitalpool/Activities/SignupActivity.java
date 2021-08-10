package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ConnectTimeoutException;
import org.christecclesia.pjdigitalpool.Inc.MyLifecycleHandler;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText m_first_name_editext, m_last_name_edittext, m_country_edittext, m_phone_edittext, m_email_edittext, m_password_edittext, m_password_confirm_editText;
    private Button m_signup_button;
    private TextView m_login_textview;
    private ContentLoadingProgressBar m_signingup_contentloadingprogressBar;
    private ScrollView m_formholder_scrollview;
    private NumberPicker.OnValueChangeListener mNumberSetListener;
    private int default_country = 0;
    private String country_code = "", country = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        m_formholder_scrollview = findViewById(R.id.signupactivity_formholder_scrollview);
        m_login_textview = findViewById(R.id.signupactivity_textview_login);
        m_first_name_editext = findViewById(R.id.signupactivity_textinpputedittext_firstname);
        m_last_name_edittext = findViewById(R.id.signupactivity_textinputedittext_lastname);
        m_country_edittext = findViewById(R.id.signupactivity_textinputedittext_country);
        m_phone_edittext = findViewById(R.id.signupactivity_textinputedittext_phone);
        m_email_edittext = findViewById(R.id.signupactivity_textinputedittext_email);
        m_password_edittext = findViewById(R.id.signupactivity_textinputedittext_password);
        m_password_confirm_editText = findViewById(R.id.signupactivity_textinputedittext_confirmpassword);
        m_signup_button = findViewById(R.id.signupactivity_button_signup);
        m_signingup_contentloadingprogressBar = findViewById(R.id.signupactivity_loader_contentloadingprogressbar);

        m_signup_button.setOnClickListener(this);
        m_login_textview.setOnClickListener(this);

        final String[] countriesStringArraySet = getResources().getStringArray(R.array.countries_array_starting_with_choose_country);
        final List<String> countriesStringArrayList = Arrays.asList(countriesStringArraySet);
        final List<String> countriesCodesStringArrayList = Arrays.asList(getResources().getStringArray(R.array.countries_array_starting_with_choose_country));

        m_country_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberSetListener = openNumberPickerForCountries(mNumberSetListener, 0, countriesStringArraySet.length-1, true, getResources().getStringArray(R.array.countries_array_starting_with_choose_country), default_country);
            }
        });

        mNumberSetListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                default_country = newVal;
                m_country_edittext.setText(countriesStringArrayList.get(newVal));
                country = countriesStringArrayList.get(newVal);
                country_code = countriesCodesStringArrayList.get(newVal);
            }
        };

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_signup_button.getId()){
            if(!m_first_name_editext.getText().toString().trim().isEmpty()
                    && !m_last_name_edittext.getText().toString().trim().isEmpty()
                    && !country.trim().isEmpty()
                    && !country_code.trim().isEmpty()
                    && !m_phone_edittext.getText().toString().trim().isEmpty()
                    && !m_email_edittext.getText().toString().trim().isEmpty()
                    && !m_country_edittext.getText().toString().trim().isEmpty()
                    && !m_country_edittext.getText().toString().trim().isEmpty()
                    && !m_password_edittext.getText().toString().trim().isEmpty()
                    && !m_password_confirm_editText.getText().toString().trim().isEmpty()
                    && m_password_edittext.getText().toString().equals(m_password_confirm_editText.getText().toString())){
                call_sign_up_api(Util.LINK_SIGNUP, m_first_name_editext.getText().toString().trim(), m_last_name_edittext.getText().toString().trim(),
                        m_phone_edittext.getText().toString().trim(), m_email_edittext.getText().toString().trim(), m_country_edittext.getText().toString().trim(),
                        m_password_edittext.getText().toString().trim());
            } else if(!m_password_edittext.getText().toString().equals(m_password_confirm_editText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Complete the form", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == m_login_textview.getId()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        }
    }

    public NumberPicker.OnValueChangeListener openNumberPickerForCountries(NumberPicker.OnValueChangeListener  mNumberSetListener, int minNumber, int maxNumber, Boolean disNumbersOnUiToUser, String[] displayStringsValues, int defaultCountry) {

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_picker);
        Button b1 = d.findViewById(R.id.dialog_button);
        final NumberPicker np = d.findViewById(R.id.numberpicker);
        np.setMaxValue(maxNumber);
        np.setMinValue(minNumber);
        np.setWrapSelectorWheel(false);
        np.setValue(defaultCountry);
        if(disNumbersOnUiToUser){
            np.setDisplayedValues(displayStringsValues);
        }
        np.setOnValueChangedListener(mNumberSetListener);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

        return mNumberSetListener;
    }

    private void call_sign_up_api(final String url, final String first_name, final String last_name, final String phone_number, final String email, final String country, final String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_formholder_scrollview.setVisibility(View.INVISIBLE);
                    m_signingup_contentloadingprogressBar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity","\n first_name: " + first_name
                    + "\n last_name: " + last_name
                    + "\n phone_number: " + phone_number
                    + "\n email: " + email
                    + "\n country: " + country
                    + "\n password: " + password
                    + "\n LINK_SIGNUP: " + url);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("SignupActivity", "response: " +  response);
                            if(!SignupActivity.this.isFinishing()){
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
                                            final JSONObject o = notice.getJSONObject(1);
                                            final JSONObject n = notice.getJSONObject(2);

                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL, k.getString("notice_image"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG2_URL, o.getString("notice_image"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG3_URL, n.getString("notice_image"));

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

                                            JSONArray latest_audios = response_json_object.getJSONObject("latest_audios").getJSONArray("data");
                                            final JSONObject a2 = latest_audios.getJSONObject(0);
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_TRACK_ID, String.valueOf(a.getInt("audio_id")));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_URL, a2.getString("audio_mp3"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_IMG_URL, a2.getString("audio_image"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_TITLE, a2.getString("audio_name"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_BODY, a2.getString("audio_description"));
                                            Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO2_UPLOAD_TIME, a2.getString("created_at"));
                                            /******************************************************************************************************
                                             *
                                             * --- END BACKGROUND SYNC ITEM FROM SERVER
                                             *
                                             ******************************************************************************************************/


                                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                            m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
                                            m_formholder_scrollview.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                        m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
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

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "We failed to recognize your account. Please re-sign-in and try again", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "Registration failed. Account may already exist or incorrect form information. Try again later", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                            }
                            m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
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
                    map.put("user_firstname", first_name);
                    map.put("user_surname", last_name);
                    map.put("user_country", country);
                    map.put("user_phone_number", phone_number);
                    map.put("user_email", email);
                    map.put("password", password);
                    map.put("password_confirmation", password);
                    Util.show_log_in_console("SignupActivity", "Map: " +  map.toString());
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
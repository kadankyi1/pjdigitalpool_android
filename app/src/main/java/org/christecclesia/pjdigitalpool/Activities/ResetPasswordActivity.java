package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText m_phone_edittext, m_resetcode_edittext, m_password_edittext, m_confirmpassword_edittext;
    private Button m_sendcode_button, m_reset_button;
    private ContentLoadingProgressBar m_loading_contentloadingprogressbar;
    private ScrollView m_formholders_scrollview;
    private ConstraintLayout m_resetform_constraintlayout, m_sendcodeform_constraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        m_loading_contentloadingprogressbar = findViewById(R.id.resetpasswordactivity_contentloadingprogressbar);
        m_formholders_scrollview = findViewById(R.id.resetpasswordactivity_form_scrollview);
        m_sendcodeform_constraintlayout = findViewById(R.id.resetpasswordactivity_sendcode_constraintlayout);
        m_phone_edittext = findViewById(R.id.resetpasswordactivity_textinputedittext_phone);
        m_sendcode_button = findViewById(R.id.resetpasswordactivity_button_sendcode);
        m_resetform_constraintlayout = findViewById(R.id.resetpasswordactivity_resetform_constraintlayout);
        m_resetcode_edittext = findViewById(R.id.resetpasswordactivity_textinpputedittext_resetcode);
        m_password_edittext = findViewById(R.id.resetpasswordactivity_textinputedittext_newpassword);
        m_confirmpassword_edittext = findViewById(R.id.resetpasswordactivity_textinputedittext_newconfirmpassword);
        m_reset_button = findViewById(R.id.resetpasswordactivity_button_reset);

        m_sendcode_button.setOnClickListener(this);
        m_reset_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_sendcode_button.getId()){
            if(!m_phone_edittext.getText().toString().trim().isEmpty()){
                call_send_reset_code_api(m_phone_edittext.getText().toString().trim());
            }
        } else if(view.getId() == m_reset_button.getId()){
            if(!m_resetcode_edittext.getText().toString().trim().isEmpty()
                    && !m_password_edittext.getText().toString().trim().isEmpty()
                    && !m_confirmpassword_edittext.getText().toString().trim().isEmpty()
                    && m_password_edittext.getText().toString().equals(m_confirmpassword_edittext.getText().toString())){
                call_reset_password_api(m_resetcode_edittext.getText().toString().trim(), m_phone_edittext.getText().toString().trim(), m_password_edittext.getText().toString().trim());
            }
        }
    }


    private void call_send_reset_code_api(final String phone_number){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_loading_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("MyNetworkRequest", "\n phone_number: " + phone_number);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_SEND_RESET_CODE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Util.show_log_in_console("MyNetworkRequest", "response: " +  response);
                                if(!ResetPasswordActivity.this.isFinishing()){
                                    //if(MyLifecycleHandler.isApplicationVisible() || MyLifecycleHandler.isApplicationInForeground()){
                                    try {
                                        JSONObject response_json_object = new JSONObject(response);

                                        //Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                        if(response_json_object.getInt("status") == 1){
                                            Toast.makeText(getApplicationContext(), "Reset code sent.", Toast.LENGTH_LONG).show();
                                            m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                            m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                            m_resetform_constraintlayout.setVisibility(View.VISIBLE);

                                        } else {
                                            Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                            m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                            m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                            m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                        m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                        m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
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
                                    Toast.makeText(getApplicationContext(), "Operation failed. Try again later or report if this continues", Toast.LENGTH_LONG).show();
                                } else if (error instanceof NetworkError) {
                                    Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                                } else if (error instanceof ParseError) {
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                }
                                m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
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
                        Util.show_log_in_console("MyNetworkRequest", "Map: " +  map.toString());
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


    private void call_reset_password_api(final String reset_code, final String phone_number, final String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_loading_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("MyNetworkRequest", "\n reset_code: " + reset_code +
                    "\n phone_number: " + phone_number +
                    "\npassword: " + password);


            Util.show_log_in_console("MyNetworkRequest", "\n phone_number: " + phone_number);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_RESET_PASSWORD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("MyNetworkRequest", "response: " +  response);
                            if(!ResetPasswordActivity.this.isFinishing()){
                                //if(MyLifecycleHandler.isApplicationVisible() || MyLifecycleHandler.isApplicationInForeground()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    //Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    if(response_json_object.getInt("status") == 1){
                                        Toast.makeText(getApplicationContext(), "Password changed successfully.", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                        m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                        m_resetform_constraintlayout.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                    m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                    m_resetform_constraintlayout.setVisibility(View.VISIBLE);
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
                                Toast.makeText(getApplicationContext(), "Operation failed. Try again later or report if this continues", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                            }
                            m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                            m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                            m_resetform_constraintlayout.setVisibility(View.VISIBLE);
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
                    map.put("resetcode", reset_code);
                    map.put("password", password);
                    map.put("password_confirmation", password);
                    Util.show_log_in_console("MyNetworkRequest", "Map: " +  map.toString());
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

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    private void call_send_reset_code_api(String phone_number){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_loading_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity", "\n phone_number: " + phone_number);

            AndroidNetworking.post(Util.LINK_SEND_RESET_CODE)
                    .addBodyParameter("phone_number", phone_number)
                    .setTag("send_resetcode_request")
                    .setPriority(Priority.HIGH)
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(final String response) {
                    if(!ResetPasswordActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = jsonObject.getJSONArray("data_returned");

                                    final JSONObject o = array.getJSONObject(0);
                                    int myStatus = o.getInt("1");
                                    final String statusMsg = o.getString("2");

                                    // GENERAL ERROR
                                    if(myStatus != 1){
                                        Toast.makeText(getApplicationContext(), statusMsg, Toast.LENGTH_LONG).show();
                                        m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                        m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
                                        return;
                                    } else {
                                        m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                        m_resetform_constraintlayout.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "An error occurred. Try again later", Toast.LENGTH_LONG).show();
                                            m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                            m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                            m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(!ResetPasswordActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                                m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                                m_sendcodeform_constraintlayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }


    private void call_reset_password_api(String reset_code, String phone_number, String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_resetform_constraintlayout.setVisibility(View.INVISIBLE);
                    m_loading_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity", "\n reset_code: " + reset_code +
                    "\n phone_number: " + phone_number +
                    "\npassword: " + password);

            AndroidNetworking.post(Util.LINK_SEND_RESET_CODE)
                    .addBodyParameter("reset_code", reset_code)
                    .addBodyParameter("phone_number", phone_number)
                    .addBodyParameter("password", password)
                    .setTag("reset_passwor_request")
                    .setPriority(Priority.HIGH)
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(final String response) {
                    if(!ResetPasswordActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = jsonObject.getJSONArray("data_returned");

                                    final JSONObject o = array.getJSONObject(0);
                                    int myStatus = o.getInt("1");
                                    final String statusMsg = o.getString("2");

                                    // GENERAL ERROR
                                    if(myStatus != 1){
                                        Toast.makeText(getApplicationContext(), statusMsg, Toast.LENGTH_LONG).show();
                                        m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                        m_resetform_constraintlayout.setVisibility(View.VISIBLE);
                                        return;
                                    } else {
                                        //STORING THE USER DATA
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN, o.getString("3"));
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "An error occurred. Try again later", Toast.LENGTH_LONG).show();
                                            m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                            m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                            m_resetform_constraintlayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(!ResetPasswordActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                                m_loading_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                m_sendcodeform_constraintlayout.setVisibility(View.INVISIBLE);
                                m_resetform_constraintlayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }

}

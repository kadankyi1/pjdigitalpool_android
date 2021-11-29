package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import gh.com.payswitch.thetellerandroid.thetellerManager;
//import ipay.gh.com.ipayandroidsdk.models.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PartnershipActivity extends AppCompatActivity {

    private ConstraintLayout m_second_parent_holder_constraintlayout;
    private EditText m_amt_edittext, m_reason_edittext;
    private Button m_proceed_button;
    private ProgressBar m_loading_progressbar;
    private Thread network_thread = null;
    private String this_amt = "";
    private String this_reason = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        m_second_parent_holder_constraintlayout = findViewById(R.id.second_parent_holder);
        m_amt_edittext = findViewById(R.id.amt_edittext);
        m_reason_edittext = findViewById(R.id.message_edittext);
        m_loading_progressbar = findViewById(R.id.contactactivity_contentloading_progressbar);
        m_proceed_button = findViewById(R.id.proceed_button);

        m_proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                this_amt = m_amt_edittext.getText().toString().trim();
                this_reason = m_reason_edittext.getText().toString().trim();
                if(this_reason.equalsIgnoreCase("")){
                    this_reason = "Church offertory";
                }

                if(this_amt.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                } else {
                    if(Integer.valueOf(this_amt) < 1){
                        Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.e("thetellerManager", "setAmount: " + String.valueOf(0.10));
                        Log.e("thetellerManager", "setEmail: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL));
                        Log.e("thetellerManager", "setfName: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME));
                        Log.e("thetellerManager", "setlName: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME));
                        Log.e("thetellerManager", "setMerchant_id: " + "TTM-00004771");
                        Log.e("thetellerManager", "setNarration: " + this_reason);
                        Log.e("thetellerManager", "setApiUser: " + "caw5fc4efa195d0c");
                        Log.e("thetellerManager", "setApiKey: " + "NWEyOGYyNWY1N2M5MzU3ZTAyZDk3MTI4ZmNkYzZlMTM=");
                        Log.e("thetellerManager", "setTxRef: " + "123456789123");

                        new thetellerManager(PartnershipActivity.this)
                                .setAmount(0.10)
                                .setEmail(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL))
                                .setfName(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME))
                                .setlName(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME))
                                .setMerchant_id("TTM-00004771")
                                .setNarration(this_reason)
                                .setApiUser("caw5fc4efa195d0c")
                                .setApiKey("NWEyOGYyNWY1N2M5MzU3ZTAyZDk3MTI4ZmNkYzZlMTM=")
                                .setTxRef("123456789123")
                                .set3dUrl(Util.LINK_UPDATE_TRANSACTION_STATUS)
                                .acceptCardPayments(true)
                                .acceptGHMobileMoneyPayments(true)
                                .onStagingEnv(true)
                                .initialize();
                        /*
                        network_thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                call_api("Bearer " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
                            }
                        });
                        network_thread.start();
                        */
                    }
                }


            }
        });
    }


    private void call_api(final String token){

        if(!PartnershipActivity.this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_second_parent_holder_constraintlayout.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("AudiosListAct", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_GENERATE_TRANSACTION_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("PartnershipAct", "response: " +  response);
                            if(!PartnershipActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);
                                    String this_transaction_id = response_json_object.getString("message");
                                    String apiUser = response_json_object.getString("app_user");
                                    String apiKey = response_json_object.getString("app_key");
                                    String merchantId = response_json_object.getString("merchant_id");
                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                m_loading_progressbar.setVisibility(View.INVISIBLE);

                                                /*
                                                Payment p = new Payment();
                                                p.setMerchantKey(merchantId);
                                                p.setInvoiceId(this_transaction_id);
                                                p.setAmount(Double.parseDouble(this_amt));
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("payment", p);
                                                startActivity(new Intent(PartnershipActivity.this, ipay.gh.com.ipayandroidsdk.PaymentActivity.class).putExtras(bundle));
                                                 */




                                                Log.e("thetellerManager", "setAmount: " + String.valueOf(Long.parseLong(this_amt)));
                                                Log.e("thetellerManager", "setEmail: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL));
                                                Log.e("thetellerManager", "setfName: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME));
                                                Log.e("thetellerManager", "setlName: " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME));
                                                Log.e("thetellerManager", "setMerchant_id: " + merchantId);
                                                Log.e("thetellerManager", "setNarration: " + this_reason);
                                                Log.e("thetellerManager", "setApiUser: " + apiUser);
                                                Log.e("thetellerManager", "setApiKey: " + apiKey);
                                                Log.e("thetellerManager", "setTxRef: " + this_transaction_id);

                                                new thetellerManager(PartnershipActivity.this)
                                                        .setAmount(0.10)
                                                        .setEmail(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL))
                                                        .setfName(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME))
                                                        .setlName(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME))
                                                        .setMerchant_id(merchantId)
                                                        .setNarration(this_reason)
                                                        .setApiUser(apiUser)
                                                        .setApiKey(apiKey)
                                                        .setTxRef(this_transaction_id)
                                                        .set3dUrl(Util.LINK_UPDATE_TRANSACTION_STATUS)
                                                        .acceptCardPayments(true)
                                                        .acceptGHMobileMoneyPayments(true)
                                                        .onStagingEnv(true)
                                                        .initialize();
                                            }
                                        });

                                    } else {
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_second_parent_holder_constraintlayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                    m_second_parent_holder_constraintlayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                            m_second_parent_holder_constraintlayout.setVisibility(View.VISIBLE);
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
                    map.put("amount", this_amt);
                    map.put("reason", this_reason);
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

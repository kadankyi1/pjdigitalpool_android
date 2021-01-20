package org.christecclesia.pjdigitalpool.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zatana on 1/9/19.
 */

public class UpdateContent extends Service {

    // constant
    private static final long NOTIFY_INTERVAL = 5 * 60 * 1000; // 5 minutes

    // run on another Thread to avoid crash
    // timer handling
    private Timer mTimer = null;
    private Thread newsFetchThread = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        newsFetchThread = new Thread(new Runnable() {
            @Override
            public void run () {
                // schedule task
                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
            }
        });

        newsFetchThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {

            Util.show_log_in_console("UpdateService", "\n run: ");
            call_api("Bearer " + Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN));
            /*
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    updateUserInfo(getApplicationContext(), instanceIdResult.getToken(), LocaleHelper.getLanguage(getApplicationContext()));
                }
            });
            */

        }

    }


    private void call_api(final String token){

        if(getApplicationContext() != null){

            Util.show_log_in_console("UpdateService", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, Util.LINK_UPDATE_INFO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.show_log_in_console("UpdateService", "response: " +  response);
                            if(getApplicationContext() != null){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        /******************************************************************************************************
                                         *
                                         * BACKGROUND SYNC ITEM FROM SERVER
                                         *
                                         ******************************************************************************************************/

                                        JSONArray notice = response_json_object.getJSONObject("data").getJSONArray("data");
                                        final JSONObject k = notice.getJSONObject(0);
                                        final JSONObject o = notice.getJSONObject(1);

                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL, k.getString("notice_image"));
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG2_URL, o.getString("notice_image"));

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

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Util.show_log_in_console("UpdateService", "JSONException error");
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Util.show_log_in_console("UpdateService", "onErrorResponse error");
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
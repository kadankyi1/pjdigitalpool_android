package org.christecclesia.pjdigitalpool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.christecclesia.pjdigitalpool.Activities.ContactActivity;
import org.christecclesia.pjdigitalpool.Activities.PartnershipActivity;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

public class WitnessFragment extends Fragment implements View.OnClickListener {

    private TextView m_member_name_textview;
    private ConstraintLayout m_notifications_holder_constraintlayout, m_prayer_requests_holder_constraintlayout,
            m_feedback_holder_constraintlayout, m_testimonies_holder_constraintlayout, m_support_caw_holder_constraintlayout,
            m_christwitness_holder_constraintlayout, m_holygen_holder_constraintlayout, m_impacttrain_holder_constraintlayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WitnessFragment() {}

    public static WitnessFragment newInstance(String param1, String param2) {
        WitnessFragment fragment = new WitnessFragment();
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
        View view = inflater.inflate(R.layout.fragment_witness, container, false);

        m_member_name_textview = view.findViewById(R.id.fragment_witness_membername_textview);
        m_notifications_holder_constraintlayout = view.findViewById(R.id.fragment_witness_mynotificationsmenuholder_constraintLayout);
        m_prayer_requests_holder_constraintlayout = view.findViewById(R.id.fragment_witness_prayerrequestmenuholder_constraintLayout);
        m_feedback_holder_constraintlayout = view.findViewById(R.id.fragment_feedbackmenuholder_constraintLayout);
        m_testimonies_holder_constraintlayout = view.findViewById(R.id.fragment_testimoniesmenuholder_constraintLayout);
        m_support_caw_holder_constraintlayout = view.findViewById(R.id.fragment_supportcawmenuholder_constraintLayout);
        m_christwitness_holder_constraintlayout = view.findViewById(R.id.fragment_christwitness_constraintLayout);
        m_holygen_holder_constraintlayout = view.findViewById(R.id.fragment_holygen_constraintLayout);
        m_impacttrain_holder_constraintlayout = view.findViewById(R.id.fragment_impacttrain_constraintLayout);

        m_member_name_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME) + " "+ Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME));

        m_notifications_holder_constraintlayout.setOnClickListener(this);
        m_prayer_requests_holder_constraintlayout.setOnClickListener(this);
        m_feedback_holder_constraintlayout.setOnClickListener(this);
        m_testimonies_holder_constraintlayout.setOnClickListener(this);
        m_support_caw_holder_constraintlayout.setOnClickListener(this);
        m_christwitness_holder_constraintlayout.setOnClickListener(this);
        m_holygen_holder_constraintlayout.setOnClickListener(this);
        m_impacttrain_holder_constraintlayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_notifications_holder_constraintlayout.getId()){
            Intent intent = new Intent(getActivity().getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        } else if(view.getId() == m_prayer_requests_holder_constraintlayout.getId()){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE, "Prayer Request");
            Intent intent = new Intent(getActivity().getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        } else if(view.getId() == m_feedback_holder_constraintlayout.getId()){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE, "Feedback");
            Intent intent = new Intent(getActivity().getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        } else if(view.getId() == m_testimonies_holder_constraintlayout.getId()){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_CONTACT_TYPE, "Testimonies");
            Intent intent = new Intent(getActivity().getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        } else if(view.getId() == m_support_caw_holder_constraintlayout.getId()){
            Intent intent = new Intent(getActivity().getApplicationContext(), PartnershipActivity.class);
            startActivity(intent);
        } else if(view.getId() == m_christwitness_holder_constraintlayout.getId()){
            startActivity(getOpenFacebookIntent(getActivity().getPackageManager(), "https://www.facebook.com/thegloriouschurch11"));
        } else if(view.getId() == m_holygen_holder_constraintlayout.getId()){
            startActivity(getOpenFacebookIntent(getActivity().getPackageManager(), "https://www.facebook.com/theHoly.Generation20"));
        } else if(view.getId() == m_impacttrain_holder_constraintlayout.getId()){
            startActivity(getOpenFacebookIntent(getActivity().getPackageManager(), "https://www.facebook.com/thegloriouschurch11"));
        }
    }

    /**
     * <p>Intent to open the official Facebook app. If the Facebook app is not installed then the
     * default web browser will be used.</p>
     *
     * <p>Example usage:</p>
     *
     * {@code newFacebookIntent(ctx.getPackageManager(), "https://www.facebook.com/JRummyApps");}
     *
     * @param pm
     *     The {@link PackageManager}. You can find this class through {@link
     *     Context#getPackageManager()}.
     * @param url
     *     The full URL to the Facebook page or profile.
     * @return An intent that will open the Facebook page/profile.
     */
    public static Intent getOpenFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
            return new Intent(Intent.ACTION_VIEW, uri);
        } catch (PackageManager.NameNotFoundException ignored) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
    }

}

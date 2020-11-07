package org.christecclesia.pjdigitalpool.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.christecclesia.pjdigitalpool.Activities.AudiosListActivity;
import org.christecclesia.pjdigitalpool.Activities.FavoritesListActivity;
import org.christecclesia.pjdigitalpool.Activities.VideosListActivity;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

public class LibraryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LibraryFragment() {}

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        view.findViewById(R.id.fragment_library_audios_roundedcornerimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AudiosListActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.fragment_library_videos_roundedcornerimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), VideosListActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.fragment_library_favorites_constraintlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FavoritesListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}

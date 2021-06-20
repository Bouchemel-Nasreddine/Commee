package com.exemple.commee.fragments.profilefragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exemple.commee.R;
import com.exemple.commee.activities.Splash;

public class ProfileHistoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Splash.changeStatusBarColor(getActivity(), R.color.white);
        View view = inflater.inflate(R.layout.profile_history_fragment, container, false);

        return view;
    }

}

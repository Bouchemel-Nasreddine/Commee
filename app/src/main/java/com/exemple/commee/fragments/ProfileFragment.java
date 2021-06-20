package com.exemple.commee.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.exemple.commee.FirebaseUtils;
import com.exemple.commee.activities.LoginActivity;
import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.fragments.profilefragments.ProfileHelpFragment;
import com.exemple.commee.fragments.profilefragments.ProfileHistoryFragment;
import com.exemple.commee.fragments.profilefragments.ProfileSettingsFragment;
import com.exemple.commee.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {

    private ConstraintLayout settings, history, help, invite, logout;
    private FragmentManager manager;
    private View view = null;
    private ShapeableImageView picture;
    private TextView user, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!FirebaseUtils.isUserConnected()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else {

            if (view == null) {

                view = inflater.inflate(R.layout.profile_fragment, container, false);
                view.setBackgroundColor(getResources().getColor(R.color.white));
                manager = getFragmentManager();

                settings = view.findViewById(R.id.setting);
                help = view.findViewById(R.id.help);
                history = view.findViewById(R.id.history);
                invite = view.findViewById(R.id.invite);
                logout = view.findViewById(R.id.logout);
                user = view.findViewById(R.id.external_user_name);
                email = view.findViewById(R.id.external_email);
                picture = view.findViewById(R.id.external_profile_picture);

                email.setText(FirebaseUtils.getUserInfo().get("email"));
                user.setText(FirebaseUtils.getUserInfo().get("user"));
                try {
                    Glide.with(view.getContext())
                            .load(Uri.parse(FirebaseUtils.getUserInfo().get("picture")))
                            .signature(new ObjectKey(2000))
                            .into(picture);
                } catch (Exception e) {
                    Log.d("photo", "onCreateView: " +e.getMessage());
                }


                settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSubFragment(0);
                    }
                });

                history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSubFragment(1);
                    }
                });

                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSubFragment(2);
                    }
                });

                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "hey Zaki is shopping on Commee, come and join him \nhttps://stackoverflow.com");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FirebaseUtils.logout()) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        };
                    }
                });

            }

        }

        return view;
    }

    private void openSubFragment(int nbr) {

        Fragment selectedFragment = null;
        switch (nbr) {
            case 0:
                selectedFragment = new ProfileSettingsFragment();
                break;
            case 1:
                selectedFragment = new ProfileHistoryFragment();
                break;
            case 2:
                selectedFragment = new ProfileHelpFragment();
                break;
        }
        Fragment finalSelectedFragment = selectedFragment;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frame_container, finalSelectedFragment).commit();
            }
        }, 80);
    }

}

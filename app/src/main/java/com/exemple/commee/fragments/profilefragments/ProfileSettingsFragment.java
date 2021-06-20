package com.exemple.commee.fragments.profilefragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.exemple.commee.FirebaseUtils;
import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.R;
import com.exemple.commee.activities.Splash;
import com.exemple.commee.fragments.ProfileFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileSettingsFragment extends Fragment {

    private MaterialButton editPicture, editPassword;
    private ShapeableImageView profilePicture;
    private EditText email, user, birth, phone, password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Splash.changeStatusBarColor(getActivity(), R.color.white);
        View view =  inflater.inflate(R.layout.profile_setting_fragment, container, false);

        editPicture = view.findViewById(R.id.profile_settings_edit_picture);
        profilePicture = view.findViewById(R.id.profile_settings_picture);
        email = view.findViewById(R.id.profile_settings_email);
        user = view.findViewById(R.id.profile_settings_user_name);
        birth = view.findViewById(R.id.profile_settings_birth_date);
        phone = view.findViewById(R.id.profile_settings_phone_number);
        editPassword = view.findViewById(R.id.profile_settings_edit_password);

        setUi();

        editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1000);
            }
        });
        MainActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.slide_out_right).replace(R.id.frame_container, new ProfileFragment()).commit();
                MainActivity.back.setVisibility(View.GONE);
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("change password");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                EditText newPassword = new EditText(getContext());
                newPassword.setHint("new password");
                newPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                EditText currentPassword = new EditText(getContext());
                currentPassword.setHint("current password");
                currentPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                layout.addView(newPassword);
                layout.addView(currentPassword);
                builder.setView(layout);
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUtils.changePassword(getContext(), newPassword.getText().toString(), currentPassword.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();


            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profilePicture.setImageURI(imageUri);
                FirebaseUtils.uploadPictureToFirebase(imageUri);
            }
        }
    }

    private void setUi() {
        MainActivity.back.setVisibility(View.VISIBLE);
        email.setText(FirebaseUtils.getUserInfo().get("email"));
        user.setText(FirebaseUtils.getUserInfo().get("user"));
        birth.setText(FirebaseUtils.getUserInfo().get("birth"));
        phone.setText(FirebaseUtils.getUserInfo().get("phone"));
        Glide.with(getContext())
                .load(FirebaseUtils.getUserInfo().get("picture"))
                .signature(new ObjectKey(2000))
                .into(profilePicture);
    }

}

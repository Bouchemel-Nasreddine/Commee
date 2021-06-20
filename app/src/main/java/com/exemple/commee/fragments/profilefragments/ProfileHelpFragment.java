package com.exemple.commee.fragments.profilefragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exemple.commee.FirebaseUtils;
import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.R;
import com.exemple.commee.activities.Splash;
import com.exemple.commee.fragments.ProfileFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ProfileHelpFragment extends Fragment {

    MaterialButton send;
    EditText subject, core;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Splash.changeStatusBarColor(getActivity(), R.color.white);

        View view = inflater.inflate(R.layout.profile_help_fragment, container, false);

        subject = view.findViewById(R.id.profile_help_subject);
        core = view.findViewById(R.id.profile_help_message);
        send = view.findViewById(R.id.help_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }

        });

        MainActivity.back.setVisibility(View.VISIBLE);
        MainActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.slide_out_right).replace(R.id.frame_container, new ProfileFragment()).commit();
                MainActivity.back.setVisibility(View.GONE);
            }
        });


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return view;

    }

    private void sendEmail() {
        final String username = "n.bouchemel@esi-sba.dz";
        final String password = "bouchemel1cpi";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("bouchemelnasreddine@gmail.com"));
            msg.setSubject(subject.getText().toString().trim());
            msg.setText(core.getText().toString().trim());
            Transport.send(msg);
            subject.setText("");
            core.setText("");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
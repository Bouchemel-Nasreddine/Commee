package com.exemple.commee.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.commee.FirebaseUtils;
import com.exemple.commee.R;
import com.exemple.commee.activities.LoginActivity;
import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.activities.ProductPageActivity;
import com.exemple.commee.adapters.CartItemAdapter;
import com.exemple.commee.database.DataBaseUtils;
import com.exemple.commee.product.ProductsUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CartFragment extends Fragment implements CartItemAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private static CartItemAdapter adapter;
    private ExtendedFloatingActionButton checkOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        checkOut = view.findViewById(R.id.check_out);

        adapter = new CartItemAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FirebaseUtils.isUserConnected()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    try {
                        sendBill(getContext());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        notifyAdapter();

        checkOut.setBackgroundDrawable(getResources().getDrawable(R.drawable.check_out_back));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProductPageActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public static void notifyAdapter() {
        if (adapter == null) return;
        adapter.notifyDataSetChanged();
    }

    private static void sendBill(Context context) throws MessagingException {
        if (ProductsUtils.getCartList().size() == 0) return;
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

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(FirebaseUtils.getUserInfo().get("email")));
        msg.setSubject("Bill");
        msg.setText("your purchase was registered, it will be shipped in few days \nTotal: " + ProductsUtils.getTotal() + "$");
        BillTask task = new BillTask(msg);
        task.execute();
        Log.d("Bill", "sendBill: success");
        ProductsUtils.clearCartList(context);
        Toast.makeText(context, "Bill sent, check your email", Toast.LENGTH_LONG).show();

    }

    private static class BillTask extends AsyncTask {

        private Message msg;

        public BillTask(Message msg) {
            this.msg = msg;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Transport.send(this.msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static void refreshFragment() {
        MainActivity.navBar.setItemSelected(R.id.home, true);
    }

}
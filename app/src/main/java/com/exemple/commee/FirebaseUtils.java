package com.exemple.commee;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseUtils {

    private static final String GETTING_INFO = "SignIn getting info";
    private static final String LOGOUT = "SignIn logout";
    private static final String SIGNIN = "SignIn";
    private static final String UPLOADING_INFO = "SignIn uploading info";
    private static final String LOGIN = "Login";
    private static final String PHOTO_UPLOAD = "Uploading photo";
    private static final String PHOTO_DOWNLOAD = "Downloading photo";
    private static final String PASSWORD_CHANGE = "Password change";
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static HashMap<String, String> info = new HashMap<>();

    public static boolean isUserConnected() {
        return fAuth.getCurrentUser() != null;
    }

    public static boolean login(Context context, String email, String password) throws InterruptedException {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOGIN, "onComplete: user logged in successfully");
                } else {
                    Log.d(LOGIN, "onComplete: failure in login" + task.getException().getMessage());
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        while (fAuth.getCurrentUser() == null);
        retrieveUserInfo();
        return fAuth.getCurrentUser() != null;
    }

    public static boolean signIn(Context context, String user, String email, String birth, String phone, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(SIGNIN, "onComplete: user created successfully");
                } else {
                    Log.d(SIGNIN, "onComplete: failure in signing in" + task.getException().getMessage());
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        while (fAuth.getCurrentUser() == null) ;
        HashMap<String, String> info = new HashMap<>();
        info.put("email", email);
        info.put("user", user);
        info.put("birth", birth);
        info.put("phone", phone);
        DocumentReference reference = db.collection("users").document(fAuth.getUid());
        reference.set(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(UPLOADING_INFO, "onComplete: user information uploaded successfully");
                } else {
                    Log.d(UPLOADING_INFO, "onComplete: uploading failed" + task.getException().getMessage());
                }
            }
        });
        Log.d(SIGNIN, "signIn: signIn operation finished");
        retrieveUserInfo();
        return fAuth.getCurrentUser() != null;
    }

    public static boolean logout() {
        fAuth.signOut();
        Log.d(LOGOUT, "logout: ... ");
        Log.d(LOGOUT, String.valueOf(fAuth.getCurrentUser() == null));

        return !isUserConnected();
    }

    public static void retrieveUserInfo() {
        FirebaseUser user = fAuth.getCurrentUser();
        info.put("email", user.getEmail());

        DocumentReference reference = db.collection("users").document(fAuth.getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null) return;
                info.put("user", value.getString("user"));
                info.put("birth", value.getString("birth"));
                info.put("phone", value.getString("phone"));

            }
        });

        StorageReference storageReference = storage.getReference().child("pictures/"+fAuth.getUid()+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                info.put("picture", uri.toString());
                Log.d(PHOTO_DOWNLOAD, "onSuccess: picture downloaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                info.put("picture", null);
                Log.d(PHOTO_DOWNLOAD, "onFailure: error in downloading picture" + e.getMessage());
            }
        });

    }

    public static void uploadPictureToFirebase(Uri uri) {
        StorageReference reference = storage.getReference().child("pictures/"+fAuth.getUid()+".jpg");
        reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(PHOTO_UPLOAD, "onComplete: photo uploaded successfully");
                } else {
                    Log.d(PHOTO_UPLOAD, "onComplete: error in uploading photo" + task.getException().getMessage());
                }
            }
        });
    }

    public static HashMap<String, String> getUserInfo() {
        return info;
    }

    public static void changePassword(Context context, String newPassword, String currentPassword) {
        FirebaseUser user = fAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fAuth.getCurrentUser().updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(PASSWORD_CHANGE, "onSuccess: password changed successfully");
                        Toast.makeText(context, "password changed successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(PASSWORD_CHANGE, "onFailure: " + e.getMessage());
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "wrong password", Toast.LENGTH_LONG).show();
                Log.d(PASSWORD_CHANGE, "onFailure: " + e.getMessage());
            }
        });


    }

    public static void saveCartToFirebase(ArrayList<Pair<Long, Integer>> cartList) {

    }

}
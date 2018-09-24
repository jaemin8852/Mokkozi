package mokkozi.com.mokkozi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    ImageView ivUser;
    StorageReference mStorageRef;
    Bitmap bitmap;
    String stUid;
    String stEmail;
    Button btnLogout;
    ProgressBar pbLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("uid", Context.MODE_PRIVATE);
        stUid = sharedPreferences.getString("uid", "");
        stEmail = sharedPreferences.getString("email", "");
        if(stUid.equals("") || stEmail.equals("")) Log.i("my information : " , "asdfasdf");
        pbLogin = v.findViewById(R.id.pbLogin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue().toString();
                    String stPhoto = dataSnapshot.child("photo").getValue().toString();

                    if (TextUtils.isEmpty(stPhoto)) {

                    } else {

                        pbLogin.setVisibility(View.VISIBLE);

                        Picasso.get().load(stPhoto).fit().centerInside().into(ivUser, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                                // Index 0 is the image view.
                                Log.d(TAG, "success");
                                pbLogin.setVisibility(View.GONE);
                            }
                        });
                    }

                    Log.d(TAG, "Value is: " + value);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        ivUser = v.findViewById(R.id.ivUser);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        return v;
    }

    public void uploadImage(){
        StorageReference mountainsRef = mStorageRef.child("users").child(stUid+".jpg");



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Task<Uri> downurl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                Uri downloadUrl = downurl.getResult();
                String photoUri = String.valueOf(downloadUrl);
                Log.d("url", photoUri);

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");

                Hashtable<String, String> profile = new Hashtable<String, String>();
                profile.put("email", stUid);
                profile.put("photo", photoUri);

                myRef.child(stUid).setValue(profile);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String s = dataSnapshot.getValue().toString();
                        Log.d("Profile", s);
                        if(dataSnapshot != null){
                            Toast.makeText(getActivity(), "사진 업로드가 잘 됐습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
            ivUser.setImageBitmap(bitmap);
            uploadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

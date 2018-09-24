package mokkozi.com.mokkozi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static mokkozi.com.mokkozi.ApplicationController.TAG;


public class FriendsFragment extends Fragment {

    RecyclerView mRecyclerView;
    FriendAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    FirebaseDatabase database;

    List<Friend> mFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_friends, container, false);
        mRecyclerView = v.findViewById(R.id.rvFriend);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFriend = new ArrayList<>();

        mAdapter = new FriendAdapter(mFriend, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Log.d(TAG, "Value is: " + value);

                for(DataSnapshot dataSnapshot2 :  dataSnapshot.getChildren()) {
                    String value2 = dataSnapshot2.getValue().toString();
                    Log.d(TAG, "Value is: "+ value2);
                    Friend friend = dataSnapshot2.getValue(Friend.class);

                    mFriend.add(friend);
                    mAdapter.notifyItemChanged(mFriend.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return v;
    }
}

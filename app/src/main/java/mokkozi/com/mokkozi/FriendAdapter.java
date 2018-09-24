package mokkozi.com.mokkozi;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
        List<Friend> mFriend;
        String stEmail;
        Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvEmail;
        public ImageView ivUser;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivUser = itemView.findViewById(R.id.ivUser);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendAdapter(List<Friend> mChat, Context context) {
        this.mFriend = mChat;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    public FriendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_friend, parent, false);



        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @NonNull
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d("AAA", mFriend.get(position).getEmail());
        holder.tvEmail.setText(mFriend.get(position).getEmail());
        //TODO: 사용자 사진 가져오기
        Picasso.get().load(mFriend.get(position).getEmail()).fit().centerInside().into(holder.ivUser);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFriend.size();
    }
}
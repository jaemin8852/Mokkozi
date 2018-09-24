package mokkozi.com.mokkozi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] mDataset;
        List<Chat> mChat;
        String stEmail;
        final int right = 1;
        Context context;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView timeTxt;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.mTextView);
            timeTxt = itemView.findViewById(R.id.timeTxt);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Chat> mChat, String email, Context context) {
        this.mChat = mChat;
        this.stEmail = email;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getEmail().equals(stEmail)){
            return 1;
        }
        else{
            return 2;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v;
        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        } else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
        }


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @NonNull
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mChat.get(position).getText());
        String timekor = "";
        int h = Integer.parseInt(mChat.get(position).getTime().substring(0,2));
        String m = mChat.get(position).getTime().substring(3,5);
        if(h > 12){
            holder.timeTxt.setText("오후 "+(h-12)+":"+m);
        }
        else if(h == 12){
            holder.timeTxt.setText("오후 "+h+":"+m);
        }
        else if(h == 0){
            holder.timeTxt.setText("오전 "+12+":"+m);
        }
        else{
            holder.timeTxt.setText("오전 "+h+":"+m);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}
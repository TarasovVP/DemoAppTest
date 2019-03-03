package com.sideonyou.tommee.demoapptest.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sideonyou.tommee.demoapptest.R;
import com.sideonyou.tommee.demoapptest.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements RealmChangeListener {

    private static final String AVATARS = "https://avatars.io/twitter/";

    private static List<User> usersList;

    private LayoutInflater mInflater;

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.users_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        String name = usersList.get(position).getName();
        holder.userName.setText(name);
        String email = usersList.get(position).getEmail();
        holder.email.setText(email);
        Picasso.with(context).load(getAvatars(position)).into(holder.avatar);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    private String getAvatars(Integer id) {
        return AVATARS + id;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.avatar)
        ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                }
            });
        }
    }

    RecycleViewAdapter(Context context, List<User> usersList) {
        this.mInflater = LayoutInflater.from(context);
        RecycleViewAdapter.usersList = usersList;
    }


}


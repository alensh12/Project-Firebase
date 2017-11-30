package com.example.amprime.firebaseauth.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amprime.firebaseauth.Helper.FlipAnimator;
import com.example.amprime.firebaseauth.R;
import com.example.amprime.firebaseauth.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amprime on 10/26/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> getAllUser;
    private MessageAdapterlistner adapterlistner;
    private SparseBooleanArray selectItemIndex;
    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;

    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex =-1;



    public UserAdapter(Context context, ArrayList<User> userArrayList, MessageAdapterlistner messageAdapterlistner) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.adapterlistner = messageAdapterlistner;
        selectItemIndex = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }




    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v =LayoutInflater.from(context).inflate(R.layout.layout_list_user,null);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
    User user = userArrayList.get(position);

        holder.usernamelist.setText(user.Fullname);
        holder.usernameEmailID.setText(user.Address);
        holder.userDesignation.setText(user.designation);
        holder.registeredDate.setText(String.valueOf(user.date));
        holder.registeredTime.setText(String.valueOf(user.time));
        // displaying first letter from the Text icon
        holder.iconText.setText(user.getFullname().substring(0,1));

        if(selectItemIndex.get(position,false))
        {

        }

        holder.itemView.setActivated(selectItemIndex.get(position,false));
        applyIconAnimation(holder,position);

        //apply icon Animation


        applyClickEvents(holder,position);

        Log.d("itemchagen", String.valueOf(position));
    }

    private void applyIconAnimation(UserViewHolder holder, int position) {
       Log.d("visiblity " + position, String.valueOf(holder.iconFront.getVisibility()));

       boolean selected = selectItemIndex.get(position,false);

        if(selected){
            boolean flip = (holder.iconFront.getVisibility() == View.VISIBLE) ? true : false;

            holder.iconFront.setVisibility(View.GONE);
            resetYaxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);

            if(flip) {
            //if(currentSelectedIndex==position){
                FlipAnimator.flipView(context,holder.iconBack,holder.iconFront,true);
                resetIndex();
            }
        }else{
            boolean flip = (holder.iconBack.getVisibility()==View.VISIBLE)?true:false;

            holder.iconBack.setVisibility(View.GONE);
            resetYaxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if(flip){
           // if(reverseAllAnimations&&animationItemsIndex.get(position,false)||currentSelectedIndex==position){
                FlipAnimator.flipView(context,holder.iconBack,holder.iconFront,false);
                resetIndex();
            }
        }
    }
    public void selectedAll(){
        userArrayList.addAll(getAllUser);
        notifyDataSetChanged();
    }

    private void resetYaxis(View view) {
        if(view.getRotationY()!=0){
            view.setRotationY(0);
        }
    }

    private void applyClickEvents(UserViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterlistner.iconClick(position);
                Log.d("TAG","iconContainer()");
            }
        });
        holder.cardView_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapterlistner.MessageRowClicked(position);

            }
        });
        holder.cardView_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
             adapterlistner.onRowLongClicked(position);
                   v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
       return Long.parseLong(userArrayList.get(position).Fullname);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();

    }

    public int getSelectedItemCount(){
        return selectItemIndex.size();
    }

    public List<Integer> getSelectedItem(){
        List<Integer> item = new ArrayList<>(selectItemIndex.size());
        for(int i =0;i<selectItemIndex.size();i++){
            item.add(selectItemIndex.keyAt(i));
        }
        return item;
    }

    public void toggleSelection(int position) {
            currentSelectedIndex = position;
            if(selectItemIndex.get(position,false)){
                selectItemIndex.delete(position);
                Log.d("Tag","deleted");
            }
            else{ selectItemIndex.put(position,true);

                Log.d("Tag","");

        }
        notifyItemChanged(position);
    }

    public void resetIndex() {
    currentSelectedIndex=0;
    }

    public void clearSelection() {
        reverseAllAnimations =true;
        selectItemIndex.clear();
        animationItemsIndex.clear();
        notifyDataSetChanged();
    }

   public void removeUser(int position){
        userArrayList.remove(position);

        resetIndex();
   }
   public void selectUser(int position){
       userArrayList.get(position);
   }

    public interface MessageAdapterlistner {
        void MessageRowClicked(int position);
        void iconClick(int position);
        void onRowLongClicked(int position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView usernamelist, usernameEmailID,registeredDate,registeredTime,iconText,userDesignation;
        LinearLayout cardView_container;
        RelativeLayout iconContainer,iconFront,iconBack;
        public UserViewHolder(View itemView) {
            super(itemView);
            usernamelist = itemView.findViewById(R.id.RV_username);
            usernameEmailID = itemView.findViewById(R.id.RV_userEmail);
            registeredDate = itemView.findViewById(R.id.RV_date);
            registeredTime = itemView.findViewById(R.id.RV_time);
            userDesignation = itemView.findViewById(R.id.RV_designation);
            cardView_container = itemView.findViewById(R.id.container);
            iconText = itemView.findViewById(R.id.icon_text);
            iconFront = itemView.findViewById(R.id.icon_front);
            iconBack = itemView.findViewById(R.id.icon_back);
            iconContainer = itemView.findViewById(R.id.icon_container);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public boolean onLongClick(View v) {
            adapterlistner.onRowLongClicked(getAdapterPosition());
            //The user has performed a long press on an object that is resulting in an action being performed.
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return false;
        }
    }
}

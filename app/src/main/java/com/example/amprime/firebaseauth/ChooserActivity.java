package com.example.amprime.firebaseauth;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amprime.firebaseauth.helper.SimpleDividerItemDecoration;

public class ChooserActivity extends AppCompatActivity {
    private static final Class[] classes = {
            EmailAndPasswordActivity.class,
            GoogleSignActivity.class,



    };

    //    private static final String[] Description_id = new String[]{
//            String.valueOf(R.string.email_and_password_sign_in),
//            String.valueOf(R.string.desc_google_sign_in)
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        MyAdaptor myAdaptor = new MyAdaptor(this, classes);
//      myAdaptor.setDescriptionID(Description_id);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(myAdaptor);
}


    class MyAdaptor extends RecyclerView.Adapter<MyViewHolder>  {
        private Context mcontext;
        private Class[] mClasses;

//    private String[] mDescription;

        public MyAdaptor() {
        }


        public MyAdaptor(Context mcontext, Class[] mClasses) {
            this.mcontext = mcontext;
            this.mClasses = mClasses;


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mcontext).inflate(android.R.layout.simple_list_item_1, parent, false);
            final MyViewHolder vh =new MyViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.view1.setText(mClasses[position].getSimpleName());
            holder.setItemClickListen(new ItemClicklisten() {
                @Override
                public void onClick(View view, int position) {

                    Class clicked = classes[position];
                    Log.d("TAG","item clicked:"+clicked );
                    Intent intent = new Intent(getApplicationContext(),clicked);
                    startActivity(intent);
                    Log.d("Intent",""+ intent);
                }
            });
//     holder.view2.setText(mDescription[position]);


        }

        @Override
        public int getItemCount() {
            //      return mDescription.length;
            return mClasses.length;
        }

//    public void setDescriptionID(String[] description_id) {
//        mDescription = description_id;
//    }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView view1, view2;
        ItemClicklisten clicklisten;



        public MyViewHolder(View itemView) {
            super(itemView);
            view1 = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
//        view2 = itemView.findViewById(android.R.id.text2);
        }
        public void setItemClickListen(ItemClicklisten itemClickListen){
            this.clicklisten = itemClickListen;
        }


        @Override
        public void onClick(View v) {
            this.clicklisten.onClick(v,getLayoutPosition());
        }
    }
}
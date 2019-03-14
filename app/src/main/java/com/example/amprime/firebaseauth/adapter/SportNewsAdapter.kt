package com.example.amprime.firebaseauth.adapter

    import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.model.News
import com.squareup.picasso.Picasso


class SportNewsAdapter(private var context: Context, private var sportsArrayList: ArrayList<News>) : androidx.recyclerview.widget.RecyclerView.Adapter<SportNewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_sport_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sportsArrayList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val bbcSportsNewModel = sportsArrayList[position]

        holder?.titleText?.text = bbcSportsNewModel.titleTxt
        holder?.contentTextView?.text = bbcSportsNewModel.contentTxt
        Picasso.with(context).load(bbcSportsNewModel.imageString).into(holder?.imageView)
    }


    inner class NewsViewHolder(itemView: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        var imageView = itemView!!.findViewById<ImageView>(R.id.sport_news_image_view)!!
        var titleText = itemView!!.findViewById<TextView>(R.id.sports_news_text_title)!!
        var contentTextView = itemView!!.findViewById<TextView>(R.id.sport_news_content_text)!!


    }
}

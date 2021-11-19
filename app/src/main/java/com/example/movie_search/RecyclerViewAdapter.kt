package com.example.movie_search

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movie_search.data.mItem

class RecyclerViewAdapter(val homefeed: MainActivity.Homefeed):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){


    override fun getItemCount(): Int {
        return homefeed.items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_raw, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(homefeed.items.get(position))
    }


    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : mItem){
            val textView_title : TextView = itemView.findViewById(R.id.textView_title)
            val textView_actor : TextView = itemView.findViewById(R.id.textView_actor)
            val textView_director : TextView = itemView.findViewById(R.id.textView_director)
            val imageView : ImageView = itemView.findViewById(R.id.imageView)

            //
            Glide.with(view.context).load(data.image)
                .apply(RequestOptions().override(300, 450))
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)

            textView_title.text = data.title
            textView_actor.text = "출연 ${data.actor}"
            textView_director.text = "감독 ${data.director}"

            //클릭시 웹사이트 연결
            itemView.setOnClickListener({
                val webpage = Uri.parse("${data.link}")
                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
                view.getContext().startActivity(webIntent);
            })
        }
    }
}
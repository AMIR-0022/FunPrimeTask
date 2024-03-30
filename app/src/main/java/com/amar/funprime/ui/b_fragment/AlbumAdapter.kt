package com.amar.funprime.ui.b_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.amar.funprime.R
import com.bumptech.glide.Glide

class AlbumAdapter(

    private var context: Context,
    val listener: AlbumItemListener,

    ) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var albumList: List<Album> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.list_item_album, parent, false);
        return ViewHolder(rootView);
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        val album: Album = albumList[position]
        holder.itemTitle.text = album.title
        Glide.with(context)
            .load(album.thumbnailUrl)
            .placeholder(R.drawable.icon_no_image)
            .into(holder.itemImage)

        holder.itemView.setOnClickListener {
            listener.onItemClick(album, position)
        }
    }

    fun updateAlbumList(dataList: List<Album>) {
        albumList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemTitle: AppCompatTextView = itemView.findViewById(R.id.itemTitle)
        val itemImage: AppCompatImageView = itemView.findViewById(R.id.itemImage)
    }

    interface AlbumItemListener {
        fun onItemClick(album: Album, position: Int)
    }

}
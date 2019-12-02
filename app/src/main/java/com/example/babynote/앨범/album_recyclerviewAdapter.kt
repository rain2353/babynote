package com.example.babynote.앨범

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso


class album_recyclerviewAdapter(internal val context: Context, internal val album_list: List<album_list>) :
    RecyclerView.Adapter<album_recyclerviewAdapter.Holder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): album_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.album_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return album_list.size
    }

    override fun onBindViewHolder(p0: album_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(album_list[p1].album_image).resize(250,250).into(p0.album_image)


        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {
                if (album_list[p1].album_writer == Common.selected_baby?.parents_id) {
                    Common.selected_album = album_list[Position]
                    val intent = Intent(view.context, album_modify::class.java)
                    view.context.startActivity(intent)
                }
            }

            override fun onClick(view: View, position: Int) {
                Common.selected_album = album_list[position]

                val intent = Intent(view.context, Album_photoview::class.java)
                view.context.startActivity(intent)

            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            iRecyclerOnClick.onLongClick(v!!,adapterPosition)
            return true
        }

        internal var album_image: ImageView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            album_image = itemView.findViewById(R.id.album_image_list) as ImageView
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }
}
package com.example.babynote.공지사항

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso

class notice_recyclerviewAdapter(internal val context: Context, internal val notice_list: List<notice_list>) :
    RecyclerView.Adapter<notice_recyclerviewAdapter.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): notice_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return notice_list.size
    }

    override fun onBindViewHolder(p0: notice_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(notice_list[p1].notice_image).resize(100,100).into(p0.notice_image)
        p0.notice_title.text = notice_list[p1].notice_title
        p0.notice_content.text = notice_list[p1].notice_content
        p0.notice_time.text = notice_list[p1].notice_time

        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {

            }

            override fun onClick(view: View, position: Int) {
                Common.selected_notice = notice_list[position]
//                Toast.makeText(view.context,notice_list[position].notice_nickname + "클릭"+"("+position+")",Toast.LENGTH_SHORT).show()
                val intent = Intent(view.context, Notice_Text::class.java)
//                intent.putExtra("num",position)

                view.context.startActivity(intent)

            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var notice_image: ImageView
        internal var notice_title: TextView
        internal var notice_content: TextView
        internal var notice_time: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            notice_image = itemView.findViewById(R.id.imageView3) as ImageView
            notice_title = itemView.findViewById(R.id.write_title) as TextView
            notice_content = itemView.findViewById(R.id.write_content) as TextView
            notice_time = itemView.findViewById(R.id.wrtie_time) as TextView
            itemView.setOnClickListener(this)
        }

    }
}
package com.example.babynote.식단표

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


class carte_recyclerviewAdapter(internal val context: Context, internal val carte_list: List<carte_list>) :
    RecyclerView.Adapter<carte_recyclerviewAdapter.Holder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): carte_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.carte_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return carte_list.size
    }

    override fun onBindViewHolder(p0: carte_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(carte_list[p1].file1).resize(250,250).into(p0.carte_image1)
        Picasso.get().load(carte_list[p1].file2).resize(250,250).into(p0.carte_image2)
        Picasso.get().load(carte_list[p1].file3).resize(250,250).into(p0.carte_image3)
        p0.textView_day.text = carte_list[p1].carte_time
        p0.menu1.text = "오전 간식 : " + carte_list[p1].menu1
        p0.menu2.text = "점심 식사 : " + carte_list[p1].menu2
        p0.menu3.text = "오후 간식 : " + carte_list[p1].menu3


        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {
                if (carte_list[p1].writer_id == Common.selected_baby?.parents_id) {
                    Common.selected_carte = carte_list[Position]
                    val intent = Intent(view.context, carte_modify::class.java)
                    view.context.startActivity(intent)
                }
            }

            override fun onClick(view: View, position: Int) {
                Common.selected_carte = carte_list[position]
                val intent = Intent(view.context, carte_read::class.java)
//                intent.putExtra("num",position)

                view.context.startActivity(intent)

            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            iRecyclerOnClick.onLongClick(v!!, adapterPosition)
            return true
        }

        internal var textView_day: TextView
        internal var carte_image1: ImageView
        internal var carte_image2: ImageView
        internal var carte_image3: ImageView
        internal var menu1: TextView
        internal var menu2: TextView
        internal var menu3: TextView

        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            textView_day = itemView.findViewById(R.id.textView_day) as TextView
            carte_image1 = itemView.findViewById(R.id.imageView4) as ImageView
            carte_image2 = itemView.findViewById(R.id.imageView5) as ImageView
            carte_image3 = itemView.findViewById(R.id.imageView6) as ImageView
            menu1 = itemView.findViewById(R.id.textView26) as TextView
            menu2 = itemView.findViewById(R.id.textView27) as TextView
            menu3 = itemView.findViewById(R.id.textView28) as TextView

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }
}
package com.example.babynote.귀가동의서

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

class consent_recyclerviewAdapter(internal val context: Context, internal val consent_list: List<consent_list>) :
    RecyclerView.Adapter<consent_recyclerviewAdapter.Holder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): consent_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.administration_request_form_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return consent_list.size
    }

    override fun onBindViewHolder(p0: consent_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(consent_list[p1].baby_image).resize(250,250).into(p0.item_babyimage)
        p0.item_day.text = consent_list[p1].consent_day
        p0.item_babyname.text = consent_list[p1].babyname
        p0.item_classname.text = consent_list[p1].classname

        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {

            }

            override fun onClick(view: View, position: Int) {
                Common.selected_consent_form = consent_list[position]
                val intent = Intent(view.context, consent_text::class.java)
                view.context.startActivity(intent)

            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var item_babyimage: ImageView
        internal var item_day: TextView
        internal var item_babyname: TextView
        internal var item_classname: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            item_babyimage = itemView.findViewById(R.id.item_babyimage) as ImageView
            item_day = itemView.findViewById(R.id.item_day) as TextView
            item_babyname = itemView.findViewById(R.id.item_babyname) as TextView
            item_classname = itemView.findViewById(R.id.item_classname) as TextView
            itemView.setOnClickListener(this)
        }

    }
}
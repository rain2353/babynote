package com.example.babynote.Main

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.babynote.Add_baby.modify_baby
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Common.Common
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.squareup.picasso.Picasso

class main_drawerAdapter(internal val context: Context, internal val kizList: List<Kiz>) :
    RecyclerView.Adapter<main_drawerAdapter.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_items, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return kizList.size
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        Picasso.get().load(kizList[p1].baby_imagepath).resize(250, 250).into(p0.kiz_image)
        p0.kizname.text = kizList[p1].baby_name
        p0.kizAge.text = kizList[p1].baby_birth
        p0.kizgender.text = kizList[p1].baby_gender
        p0.kiz_kindergarten.text = kizList[p1].baby_kindergarten
        p0.kiz_class.text = kizList[p1].baby_class
        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {
                Common.selected_baby = kizList[Position]
                val intent = Intent(view.context, modify_baby::class.java)
                view.context.startActivity(intent)
            }

            override fun onClick(view: View, position: Int) {
                Common.selected_baby = kizList[position]
//                Toast.makeText(view.context,kizList[position].baby_name + "클릭"+"("+position+")",Toast.LENGTH_SHORT).show()
                val intent = Intent(view.context, MainActivity::class.java)
                intent.putExtra("num", position)
                view.context.startActivity(intent)

            }

        })
//        p0?.bind(kizList[p1], context)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        internal var kiz_image: ImageView
        internal var kizname: TextView
        internal var kizAge: TextView
        internal var kizgender: TextView
        internal var kiz_kindergarten: TextView
        internal var kiz_class: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick


        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!, adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick
        }

        init {
            kiz_image = itemView.findViewById(R.id.kizPhotoImg) as ImageView
            kizname = itemView.findViewById(R.id.kizName) as TextView
            kizAge = itemView.findViewById(R.id.kizbirth) as TextView
            kizgender = itemView.findViewById(R.id.dogGenderTv) as TextView
            kiz_kindergarten = itemView.findViewById(R.id.kiz_kindergarten) as TextView
            kiz_class = itemView.findViewById(R.id.kiz_class) as TextView
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            iRecyclerOnClick.onLongClick(v!!, adapterPosition)
            return true
        }


    }


}



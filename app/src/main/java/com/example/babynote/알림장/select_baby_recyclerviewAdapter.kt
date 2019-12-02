package com.example.babynote.알림장

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso

class select_baby_recyclerviewAdapter(internal val context: Context, internal val Kiz: List<select_baby_list>) :
    RecyclerView.Adapter<select_baby_recyclerviewAdapter.Holder>()  {
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): select_baby_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.select_baby_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return Kiz.size
    }

    override fun onBindViewHolder(p0: select_baby_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(Kiz[p1].baby_imagepath).resize(100,100).into(p0.imageView13)
        p0.textView100.text = Kiz[p1].baby_name + " ( " + Kiz[p1].baby_class + " )"


        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {

            }

            override fun onClick(view: View, position: Int) {
                Common.selected_baby_list = Kiz[position]
                Toast.makeText(view.context,Kiz[position].baby_name + "클릭"+"("+position+")",Toast.LENGTH_SHORT).show()
                val intent = Intent(view.context, advice_write::class.java)
                view.context.startActivity(intent)
            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var imageView13: ImageView
        internal var textView100: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            imageView13 = itemView.findViewById(R.id.imageView13) as ImageView
            textView100 = itemView.findViewById(R.id.textView100) as TextView

            itemView.setOnClickListener(this)
        }

    }
}
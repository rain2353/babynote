package com.example.babynote.알림장

import android.content.Context
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

class advice_recyclerviewAdapter(internal val context: Context, internal val advice_list: List<advice_list>) :
    RecyclerView.Adapter<advice_recyclerviewAdapter.Holder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): advice_recyclerviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.advice_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return advice_list.size
    }

    override fun onBindViewHolder(p0: advice_recyclerviewAdapter.Holder, p1: Int) {
        Picasso.get().load(advice_list[p1].file).resize(100,100).into(p0.file)
        p0.advice_time.text = advice_list[p1].advice_time
        p0.advice_content.text = advice_list[p1].advice_content


        p0.setClick(object : IRecyclerOnClick {
            override fun onLongClick(view: View, Position: Int) {

            }

            override fun onClick(view: View, position: Int) {
                Common.selected_advice = advice_list[position]
                Toast.makeText(view.context,advice_list[position].advice_content + "클릭"+"("+position+")",Toast.LENGTH_SHORT).show()
//                val intent = Intent(view.context, Notice_Text::class.java)
//                intent.putExtra("num",position)

//                view.context.startActivity(intent)

            }

        })
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var file: ImageView
        internal var advice_time: TextView
        internal var advice_content: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            file = itemView.findViewById(R.id.advice_file) as ImageView
            advice_time = itemView.findViewById(R.id.advice_time) as TextView
            advice_content = itemView.findViewById(R.id.advice_content) as TextView
            itemView.setOnClickListener(this)
        }

    }
}
package com.example.babynote.Add_baby

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.babynote.R
import kotlinx.android.synthetic.main.item_raw.view.*

class search_RecyclerViewAdapter(val context: Context, val search_result: search_result) :
    RecyclerView.Adapter<search_RecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return search_result.items.count()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): search_RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_raw, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: search_RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(search_result.items.get(position))

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: Item) {


            var title = data.title.replace("<b>", " ")
            var title1 = title.replace("</b>", "")

            itemView.select_kindergarten.text = "${title1}"
            itemView.kindergarten_adress.text = "${data.roadAddress}"
            itemView.kindergarten_callnumber.text = "${data.telephone}"

            //클릭시
            itemView.setOnClickListener {
                Log.d("선택한 아이템", title1)
                Toast.makeText(itemView.context, "'${title1}'을/를 클릭했습니다.", Toast.LENGTH_LONG).show()

                val intent = Intent(itemView.context, Add_baby_info::class.java)
                intent.putExtra("유치원이름", title1)
                itemView.context.startActivity(intent)


            }
        }

    }
}
package com.example.babynote.알림장

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import io.reactivex.disposables.CompositeDisposable

class advice_comment_Adapter(
    internal val context: Context,
    internal val advice_comment_list: List<advice_comment_list>
) :
    RecyclerView.Adapter<advice_comment_Adapter.Holder>()  {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): advice_comment_Adapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_comment_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return advice_comment_list.size
    }

    override fun onBindViewHolder(p0: advice_comment_Adapter.Holder, p1: Int) {
        p0.comment_nickname.text = advice_comment_list[p1].comment_nickname
        p0.comment_time.text = advice_comment_list[p1].comment_time
        p0.comment_content.text = advice_comment_list[p1].comment_content

        p0.setClick(object : IRecyclerOnClick {

            override fun onClick(view: View, position: Int) {


            }

            override fun onLongClick(view: View, Position: Int) {
                Common.selected_advice_comment = advice_comment_list[Position]
                //Init API
                val retrofit = RetrofitClient.instance
                myAPI = retrofit.create(INodeJS::class.java)

                if (advice_comment_list[p1].comment_writer == Common.selected_baby?.parents_id) {
                    val intent = Intent(view.context, advice_comment_modify::class.java)
                    intent.putExtra("댓글",advice_comment_list[p1].comment_content)
                    view.context.startActivity(intent)
                }


            }

        })

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        override fun onLongClick(v: View?): Boolean {
            iRecyclerOnClick.onLongClick(v!!, adapterPosition)
            return true
        }


        internal var comment_nickname: TextView
        internal var comment_time: TextView
        internal var comment_content: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!, adapterPosition)

        }


        fun setClick(iRecyclerOnClick: IRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick
        }


        init {
            comment_time = itemView.findViewById(R.id.textView19) as TextView
            comment_content = itemView.findViewById(R.id.textView18) as TextView
            comment_nickname = itemView.findViewById(R.id.textView17) as TextView
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }
}
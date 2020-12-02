package com.epsi.app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

class Adapter(private val context: Context, private val title: Array<String>, private val imageId: Array<String>, private val editor: Array<String>, private val releaseDate: Array<String>) : BaseAdapter() {

  companion object {
    private var inflater: LayoutInflater? = null
  }

  init {
    inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  }

  override fun getCount(): Int {
    return title.size
  }

    override fun getItem(position: Int): Any {
        return position
    }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }


  class Holder {
    lateinit var title: TextView
    lateinit var editor: TextView
    lateinit var date: TextView
    lateinit var cover: ImageView
  }


  private fun initHolder(view: View): Holder {
    val holder = Holder()
    holder.date = view.findViewById(R.id.release_date)
    holder.title = view.findViewById(R.id.title)
    holder.cover = view.findViewById(R.id.cover)
    holder.editor = view.findViewById(R.id.editor)
    holder.title.maxLines = 1
    holder.title.isSelected = true
    holder.title.isSingleLine = true
    holder.title.isFocusable = true
    holder.title.isFocusableInTouchMode = true
    holder.editor.maxLines = 1
    holder.editor.isSelected = true
    holder.editor.isSingleLine = true
    holder.editor.isFocusable = true
    holder.editor.isFocusableInTouchMode = true
    return holder
  }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var cv = convertView
      if (cv == null) {
        cv = inflater!!.inflate(R.layout.cardview_slide_panel, parent, false)
      }
      val holder = initHolder(cv!!)
      holder.editor.text = editor[position]
      holder.title.text = title[position]
      holder.date.text = releaseDate[position]
      Picasso.get().load(imageId[position]).resize(90, 120).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.cover)




      cv.setOnClickListener{
        //TODO
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("user_id", imageId[position])
        intent.putExtra("findFor", editor[position])
        context.startActivity(intent)
      }
      cv.setOnLongClickListener{
        //delConversation(imageId[position])
        return@setOnLongClickListener true
      }
      return cv
    }


    /*private fun delConversation(ref: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getContext().resources.getString(R.string.confirm))
        builder.setMessage(getContext().resources.getString(R.string.delete_conversation))
        builder.setPositiveButton(getContext().resources.getString(R.string.yes)) { dialog: DialogInterface, _: Int ->
            val docRef = getContext().getConversationList()
            docRef.update(ref, FieldValue.delete())
            docRef.update("time_$ref", FieldValue.delete())
            docRef.collection(ref).get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        docRef.collection(ref).document(doc.id).delete()
                    }
                }
            dialog.dismiss()
        }
        builder.setNegativeButton(getContext().resources.getString(R.string.no)) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        builder.setCancelable(true)
        builder.show()
    }*/

}

package hu.ait.tanzina.discswap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.tanzina.discswap.data.Game
import kotlinx.android.synthetic.main.game_row.view.*

class GamesAdapter(private val context: Context, private val uid: String) : RecyclerView.Adapter<GamesAdapter.ViewHolder>()
{

    private var postsList = mutableListOf<Game>()
    private var postKeys = mutableListOf<String>()

    private var lastIndex = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.game_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = postsList[position]

        holder.tvAuthor.setText(post.author)
        holder.tvTitle.setText(post.title)
        holder.tvBody.setText(post.description)


//        if (post.imageUrl.isEmpty()) {
//            holder.ivPhoto.visibility = View.INVISIBLE
//        } else {
//            holder.ivPhoto.visibility = View.VISIBLE
//            Glide.with(context).load(post.imageUrl).into(holder.ivPhoto)
//        }


        // if this is my post message
        if (post.uid == uid) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener {
                removePost(holder.adapterPosition)
            }
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        holder.itemView.rowContainer.setOnClickListener{
            (context as GameActivity).showSingleGameView(post, holder.adapterPosition)
        }

        setAnimation(holder.itemView, position)
    }

    fun addPost(post: Game, key: String) {
        postsList.add(post)
        postKeys.add(key)

        notifyDataSetChanged()
    }


    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection("games").document(
            postKeys[index]
        ).delete()

        postsList.removeAt(index)
        postKeys.removeAt(index)
        notifyItemRemoved(index)
    }


    fun removePostByKey(key: String) {
        val index = postKeys.indexOf(key)
        if (index != -1) {
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastIndex) {
            val animation = AnimationUtils.loadAnimation(context,
                android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastIndex = position
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor = itemView.tvAuthor
        val tvTitle = itemView.tvTitle
        val tvBody = itemView.tvBody
        val btnDelete = itemView.btnDelete
        val ivPhoto = itemView.ivPhoto
    }
}
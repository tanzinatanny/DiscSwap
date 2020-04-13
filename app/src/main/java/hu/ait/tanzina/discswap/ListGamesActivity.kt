package hu.ait.tanzina.discswap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.tanzina.discswap.data.Game
import kotlinx.android.synthetic.main.activity_main.*

class ListGamesActivity : AppCompatActivity() {

    private lateinit var postsAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postsAdapter = GamesAdapter(this,
            FirebaseAuth.getInstance().currentUser!!.uid)

        var linLayoutManger = LinearLayoutManager(this)
        linLayoutManger.reverseLayout = true
        linLayoutManger.stackFromEnd = true

        recyclerPosts.layoutManager = linLayoutManger

        recyclerPosts.adapter = postsAdapter

        queryPosts()
    }

    private fun queryPosts() {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("games")

        var allPostsListener = query.addSnapshotListener(
            object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {

                    if (e != null) {
                        Toast.makeText(this@ListGamesActivity, "listen error: ${e.message}", Toast.LENGTH_LONG).show()
                        return
                    }

                    for (dc in querySnapshot!!.getDocumentChanges()) {
                        when (dc.getType()) {
                            DocumentChange.Type.ADDED -> {
                                val post = dc.document.toObject(Game::class.java)
                                postsAdapter.addPost(post, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(this@ListGamesActivity, "update: ${dc.document.id}", Toast.LENGTH_LONG).show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                postsAdapter.removePostByKey(dc.document.id)
                            }
                        }
                    }
                }
            })
    }
}

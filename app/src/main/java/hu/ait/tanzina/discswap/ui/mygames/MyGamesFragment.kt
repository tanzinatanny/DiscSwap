package hu.ait.tanzina.discswap.ui.mygames

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.tanzina.discswap.AddGameActivity
import hu.ait.tanzina.discswap.GameActivity
import hu.ait.tanzina.discswap.GamesAdapter
import hu.ait.tanzina.discswap.R
import hu.ait.tanzina.discswap.data.Game
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerPosts
import kotlinx.android.synthetic.main.fragment_mygames.*

class MyGamesFragment : Fragment() {

    private lateinit var myGamesViewModel: MyGamesViewModel

    private lateinit var postsAdapter: GamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myGamesViewModel =
            ViewModelProviders.of(this).get(MyGamesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mygames, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fab.setOnClickListener { view ->
            addGame()
        }

        postsAdapter = GamesAdapter(requireContext(),
            FirebaseAuth.getInstance().currentUser!!.uid)

        var linLayoutManger = LinearLayoutManager(requireContext())
        linLayoutManger.reverseLayout = true
        linLayoutManger.stackFromEnd = true
//
        recyclerPosts.layoutManager = linLayoutManger

        recyclerPosts.adapter = postsAdapter

        queryPosts()

        showNotice()
    }

    private fun queryPosts() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
//        val query = db.collection("games")
        val query = db.collection("games").whereEqualTo("uid",uid)

        var allPostsListener = query.addSnapshotListener(
            object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {

                    if (e != null) {
                        Toast.makeText(requireContext(), "listen error: ${e.message}", Toast.LENGTH_LONG).show()
                        return
                    }

                    for (dc in querySnapshot!!.getDocumentChanges()) {
                        when (dc.getType()) {
                            DocumentChange.Type.ADDED -> {
                                val post = dc.document.toObject(Game::class.java)
                                postsAdapter.addPost(post, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(requireContext(), "update: ${dc.document.id}", Toast.LENGTH_LONG).show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                postsAdapter.removePostByKey(dc.document.id)
                            }
                        }
                    }
                }
            })
    }

    private fun showNotice(){
        if( postsAdapter.itemCount == 0 ){
            text_my_games.setText(R.string.no_games)
            text_my_games.visibility = View.VISIBLE
        }
    }

    private fun addGame(){
        val intent = Intent (activity, AddGameActivity::class.java)
        activity?.startActivity(intent)
    }
}
package hu.ait.tanzina.discswap

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.tanzina.discswap.data.Game

object Common {
    var activeGame: Game = Game()

    var activeId : Int = 0

    fun getDb(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("games")
    }

    fun currentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}
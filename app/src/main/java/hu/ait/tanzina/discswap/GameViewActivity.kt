package hu.ait.tanzina.discswap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import hu.ait.tanzina.discswap.data.Game
import kotlinx.android.synthetic.main.activity_game_view.*
import kotlinx.android.synthetic.main.nav_header_game.*

class GameViewActivity : AppCompatActivity() {

    lateinit var game : Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)

        game = Common.activeGame

        var idx = Common.activeId

//        if (!game.imageUrl.isNullOrEmpty()) {
//            Glide.with(this)
//                .load(game.imageUrl)
//                .into(imageIcon)
//        }

        gameTitle.text = game.title
        gameBody.text = game.description
    }
}

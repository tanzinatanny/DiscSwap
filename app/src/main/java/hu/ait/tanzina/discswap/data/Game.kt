package hu.ait.tanzina.discswap.data

import java.io.Serializable

data class Game(
    var uid : String = "",
    var author: String = "",
    var title: String = "",
    var description: String = "",
    var imageUrl: String = ""
) : Serializable
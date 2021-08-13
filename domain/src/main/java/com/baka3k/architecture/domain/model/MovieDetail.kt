package com.baka3k.architecture.domain.model

data class MovieDetail(
    var cast: String = "",
    var title: String = "",
    var content: String = "",
    var time: String = "",
    var languge: String = "",
    var rating: String = "",
    var category: String = "",
    var cover: String = "",
    var actors: MutableList<Actor> = mutableListOf()
)

data class Actor(
    val name: String = "",
    val image: String = ""
)

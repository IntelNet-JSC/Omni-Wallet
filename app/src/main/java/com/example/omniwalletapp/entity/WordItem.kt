package com.example.omniwalletapp.entity

import java.util.regex.Pattern

data class WordItem(
    val id: Int,
    var name: String,
    var isChecked:Boolean=false
) {
    companion object {
        fun generateListWord(): List<WordItem> {
            val seedCode =
                "yard impulse luxury drive today throw farm pepper survey wreck glass federal"
            return Pattern.compile(" ").split(seedCode).mapIndexed { i, name ->
                WordItem(i + 1, name)
            }.shuffled()
        }

        fun generateListBlank(): List<WordItem> {
            val seedCode =
                "yard impulse luxury drive today throw farm pepper survey wreck glass federal"
            return Pattern.compile(" ").split(seedCode).mapIndexed { i, name ->
                WordItem(i + 1, "")
            }
        }
    }
}
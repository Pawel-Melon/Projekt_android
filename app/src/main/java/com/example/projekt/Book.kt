package com.example.projekt

data class Book(
    val id: Int,
    val tytul: String,
    val autor: String,
    val kategoria: String,
    val rok_wydania: Int,
    val liczba_dostepnych_kopii: Int
)
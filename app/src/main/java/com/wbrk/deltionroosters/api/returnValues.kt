package com.wbrk.deltionroosters.api

data class Week(
    val group: String,
    val data: List<Day>
)

data class Day(
    val date_f: String,
    val weeknum: String,
    val items: List<LesItem>
)

data class LesItem(
    val t: String,
    val v: String,
    val r: String
)

data class Groups(
    val data: List<String>
)
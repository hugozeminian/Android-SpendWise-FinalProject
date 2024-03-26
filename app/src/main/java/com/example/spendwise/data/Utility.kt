package com.example.spendwise.data

fun containsOnlyNumbers(input: String): Boolean {
    return input.all { it.isDigit() || it == '.' }
}

fun isValidDateFormat(date: String): Boolean {
    val regex = Regex("^\\w{3} \\d{2}, \\d{4}$")
    return regex.matches(date)
}

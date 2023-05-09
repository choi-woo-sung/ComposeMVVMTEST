package com.woosung.compose.common.ext // ktlint-disable filename

fun Long.addCommasToThousands(): String {
    return String.format("%,d", this)
}

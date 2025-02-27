package com.paandaaa.homey

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
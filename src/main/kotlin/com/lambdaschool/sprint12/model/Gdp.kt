package com.lambdaschool.sprint12.model

import java.util.concurrent.atomic.AtomicLong

data class Gdp(var countryName: String, var gdp: Long)
{
    var id: Long = counter.incrementAndGet()

    companion object
    {
        private val counter = AtomicLong()
    }
}
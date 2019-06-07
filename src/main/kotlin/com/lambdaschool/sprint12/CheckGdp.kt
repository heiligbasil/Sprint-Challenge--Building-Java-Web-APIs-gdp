package com.lambdaschool.sprint12

import com.lambdaschool.sprint12.model.Gdp

interface CheckGdp
{
    fun test(gdp: Gdp): Boolean

    companion object
    {
        inline operator fun invoke(crossinline op: (gdp: Gdp) -> Boolean) =
                object : CheckGdp
                {
                    override fun test(gdp: Gdp): Boolean = op(gdp)
                }
    }
}
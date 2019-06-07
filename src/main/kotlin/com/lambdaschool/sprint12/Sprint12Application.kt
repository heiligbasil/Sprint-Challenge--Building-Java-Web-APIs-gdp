package com.lambdaschool.sprint12

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
open class Sprint12Application
{
    companion object
    {
        private lateinit var ourGdpList: GdpList

        @JvmStatic
        fun main(args: Array<String>)
        {
            ourGdpList = GdpList()
            val ctx: ConfigurableApplicationContext = runApplication<Sprint12Application>(*args)

            val dispatcherServlet: DispatcherServlet = ctx.getBean("dispatcherServlet") as DispatcherServlet
            dispatcherServlet.setThrowExceptionIfNoHandlerFound(true)
        }

        fun getOurGdpList(): GdpList = ourGdpList
    }
}

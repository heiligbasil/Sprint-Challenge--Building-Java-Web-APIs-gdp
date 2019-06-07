package com.lambdaschool.sprint12.controller

import com.lambdaschool.sprint12.CheckGdp
import com.lambdaschool.sprint12.Sprint12Application
import com.lambdaschool.sprint12.exception.ResourceNotFoundException
import com.lambdaschool.sprint12.model.Gdp
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/gdp")
class GdpController
{
    companion object
    {
        private val logger = LoggerFactory.getLogger(GdpController::class.java)
    }

    // localhost:2020/gdp/names
    @GetMapping(value = ["/names"], produces = ["application/json"])
    fun getAllCountries(request: HttpServletRequest): ResponseEntity<*>
    {
        val messageLog: String = "${request.requestURI} accessed on ${LocalDateTime.now()}"
        logger.info(messageLog)

        val rtnGdps: MutableList<Gdp> = Sprint12Application.getOurGdpList().gdpList

        if (rtnGdps.isEmpty())
        {
            throw ResourceNotFoundException("No results to display")
        }

        rtnGdps.sortBy { it.countryName }

        return ResponseEntity(rtnGdps, HttpStatus.OK)
    }

    // localhost:2020/gdp/economy
    @GetMapping(value = ["/economy"], produces = ["application/json"])
    fun getAllGdps(request: HttpServletRequest): ResponseEntity<*>
    {
        val messageLog: String = "${request.requestURI} accessed on ${LocalDateTime.now()}"
        logger.info(messageLog)

        val rtnGdps: MutableList<Gdp> = Sprint12Application.getOurGdpList().gdpList

        if (rtnGdps.isEmpty())
        {
            throw ResourceNotFoundException("No results to display")
        }

        rtnGdps.sortByDescending { it.gdp }

        return ResponseEntity(rtnGdps, HttpStatus.OK)
    }

    // localhost:2020/gdp/country/{id}
    @GetMapping(value = ["/country/{id}"], produces = ["application/json"])
    fun getGdpDetail(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*>
    {
        val messageLog: String = "${request.requestURI} accessed with id $id on ${LocalDateTime.now()}"
        logger.info(messageLog)

        val rtnGdp: Gdp? = Sprint12Application.getOurGdpList().findGdp(CheckGdp { gdp -> gdp.id == id })

        if (rtnGdp == null)
        {
            throw ResourceNotFoundException("No country exists with id $id")
        }

        return ResponseEntity<Gdp>(rtnGdp, HttpStatus.OK)
    }

    // localhost:2020/gdp/country/stats/median
    @GetMapping(value = ["/country/stats/median"], produces = ["application/json"])
    fun getMedianGdp(request: HttpServletRequest): ResponseEntity<*>
    {
        val messageLog: String = "${request.requestURI} accessed on ${LocalDateTime.now()}"
        logger.info(messageLog)

        val sortedList: List<Gdp> = Sprint12Application.getOurGdpList().gdpList.sortedWith(compareBy { it.gdp })

        if (sortedList.isEmpty())
        {
            throw ResourceNotFoundException("No median GDP to display")
        }

        val gdp: Gdp;
        val medianIndex: Int = sortedList.size.div(2)

        if (sortedList.size % 2 == 0)
        {
            gdp = sortedList.get(medianIndex)
        }
        else
        {
            gdp = sortedList.get(medianIndex - 1)
        }

        return ResponseEntity(gdp, HttpStatus.OK)
    }

    // localhost:2020/gdp/economy/table
    @GetMapping(value = ["/economy/table"], produces = ["application/json"])
    fun displayGdpTable(request: HttpServletRequest): ModelAndView
    {
        val messageLog: String = "${request.requestURI} accessed on ${LocalDateTime.now()}"
        logger.info(messageLog)

        val gdpList: MutableList<Gdp> = Sprint12Application.getOurGdpList().gdpList
        gdpList.sortByDescending { it.gdp }

        val mav = ModelAndView()
        mav.viewName = "gdp"
        mav.addObject("gdpList", gdpList)

        return mav
    }
}
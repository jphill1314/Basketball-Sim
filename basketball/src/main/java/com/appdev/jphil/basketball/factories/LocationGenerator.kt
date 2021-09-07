package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

object LocationGenerator {

    fun getLocation(): Location {
        val country = Random.nextDouble()
        return when {
            country < 0.1 -> getCanadianLocation()
            else -> getAmericanLocation()
        }
    }

    private fun getCanadianLocation(): Location {
        val location = Random.nextDouble(100.0)
        return when {
            location <= .1 -> Location.NU
            location <= .21 -> Location.YT
            location <= .33 -> Location.NT
            location <= .75 ->  Location.PE
            location <= 2.12 -> Location.NL
            location <= 4.18 ->  Location.NB
            location <= 6.75 -> Location.NS
            location <= 9.85 ->  Location.SK
            location <= 13.48 -> Location.MB
            location <= 25.14 -> Location.AB
            location <= 38.68 -> Location.BC
            location <= 61.22 -> Location.QC
            else -> Location.ON
        }
    }

    private fun getAmericanLocation(): Location {
        val location = Random.nextDouble(100.0)
        return when {
            location <= 0.17 -> Location.WY
            location <= 0.36 -> Location.VT
            location <= 0.57 -> Location.DC
            location <= 0.79 -> Location.AK
            location <= 1.02 -> Location.ND
            location <= 1.29 -> Location.SD
            location <= 1.58 -> Location.DE
            location <= 1.90 -> Location.MT
            location <= 2.22 -> Location.RI
            location <= 2.62 -> Location.ME
            location <= 3.03 -> Location.NH
            location <= 3.46 -> Location.HI
            location <= 3.99 -> Location.ID
            location <= 4.54 -> Location.WV
            location <= 5.12 -> Location.NE
            location <= 5.75 -> Location.NM
            location <= 6.63 -> Location.KS
            location <= 7.53 -> Location.MS
            location <= 8.44 -> Location.AR
            location <= 9.36 -> Location.NV
            location <= 10.31 -> Location.IA
            location <= 11.27 -> Location.UT
            location <= 12.24 -> Location.PR
            location <= 13.32 -> Location.CT
            location <= 14.51 -> Location.OK
            location <= 15.78 -> Location.OR
            location <= 17.13 -> Location.KY
            location <= 18.54 -> Location.LA
            location <= 20.02 -> Location.AL
            location <= 21.56 -> Location.SC
            location <= 23.26 -> Location.MN
            location <= 24.98 -> Location.CO
            location <= 26.74 -> Location.WI
            location <= 28.57 -> Location.MD
            location <= 30.42 -> Location.MO
            location <= 32.44 -> Location.IN
            location <= 34.49 -> Location.TN
            location <= 36.58 -> Location.MA
            location <= 38.75 -> Location.AZ
            location <= 41.03 -> Location.WA
            location <= 43.61 -> Location.VA
            location <= 46.30 -> Location.NJ
            location <= 49.32 -> Location.MI
            location <= 52.58 -> Location.NC
            location <= 55.76 -> Location.GA
            location <= 59.29 -> Location.OH
            location <= 63.14 -> Location.IL
            location <= 67.01 -> Location.PA
            location <= 72.92 -> Location.NY
            location <= 79.36 -> Location.FL
            location <= 88.04 -> Location.TX
            else -> Location.CA
        }
    }
}

package com.appdev.jphil.basketball.location

enum class Region(val string: String) {
    NEW_ENGLAND("NE"),
    MIDEAST("ME"),
    SOUTHEAST("SE"),
    GREAT_LAKES("GL"),
    PLAINS("PL"),
    SOUTHWEST("SW"),
    ROCKY_MOUNTAINS("RM"),
    WEST_COAST("WC"),
    HAWAII("HI"),
    ALASKA("AK"),
    CANADA("CD")
}

fun Location.getRegion() = when (this) {
    Location.AL -> Region.SOUTHEAST
    Location.AK -> Region.ALASKA
    Location.AZ -> Region.SOUTHWEST
    Location.AR -> Region.SOUTHEAST
    Location.CA -> Region.WEST_COAST
    Location.CO -> Region.ROCKY_MOUNTAINS
    Location.CT -> Region.NEW_ENGLAND
    Location.DE -> Region.MIDEAST
    Location.DC -> Region.MIDEAST
    Location.FL -> Region.SOUTHEAST
    Location.GA -> Region.SOUTHEAST
    Location.HI -> Region.HAWAII
    Location.ID -> Region.ROCKY_MOUNTAINS
    Location.IL -> Region.GREAT_LAKES
    Location.IN -> Region.GREAT_LAKES
    Location.IA -> Region.PLAINS
    Location.KS -> Region.PLAINS
    Location.KY -> Region.SOUTHEAST
    Location.LA -> Region.SOUTHEAST
    Location.ME -> Region.NEW_ENGLAND
    Location.MD -> Region.MIDEAST
    Location.MA -> Region.NEW_ENGLAND
    Location.MI -> Region.GREAT_LAKES
    Location.MN -> Region.PLAINS
    Location.MS -> Region.SOUTHEAST
    Location.MO -> Region.PLAINS
    Location.MT -> Region.ROCKY_MOUNTAINS
    Location.NE -> Region.PLAINS
    Location.NV -> Region.WEST_COAST
    Location.NH -> Region.NEW_ENGLAND
    Location.NJ -> Region.MIDEAST
    Location.NM -> Region.SOUTHWEST
    Location.NY -> Region.MIDEAST
    Location.NC -> Region.SOUTHEAST
    Location.ND -> Region.PLAINS
    Location.OH -> Region.GREAT_LAKES
    Location.OK -> Region.SOUTHWEST
    Location.OR -> Region.WEST_COAST
    Location.PA -> Region.MIDEAST
    Location.RI -> Region.NEW_ENGLAND
    Location.SC -> Region.SOUTHEAST
    Location.SD -> Region.PLAINS
    Location.TN -> Region.SOUTHEAST
    Location.TX -> Region.SOUTHWEST
    Location.UT -> Region.ROCKY_MOUNTAINS
    Location.VT -> Region.NEW_ENGLAND
    Location.VA -> Region.SOUTHEAST
    Location.WA -> Region.WEST_COAST
    Location.WV -> Region.SOUTHEAST
    Location.WI -> Region.GREAT_LAKES
    Location.WY -> Region.ROCKY_MOUNTAINS
    Location.AB -> Region.CANADA
    Location.BC -> Region.CANADA
    Location.MB -> Region.CANADA
    Location.NB -> Region.CANADA
    Location.NL -> Region.CANADA
    Location.NS -> Region.CANADA
    Location.ON -> Region.CANADA
    Location.PE -> Region.CANADA
    Location.QC -> Region.CANADA
    Location.SK -> Region.CANADA
    Location.NT -> Region.CANADA
    Location.NU -> Region.CANADA
    Location.YT -> Region.CANADA
}

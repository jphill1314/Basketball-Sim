package com.appdev.jphil.basketball.recruits

enum class RecruitingEvent(val type: Int) {
    SCOUT(0),
    OFFER_SCHOLARSHIP(1);


    companion object {
        fun getEventByType(type: Int): RecruitingEvent {
            return when (type) {
                RecruitingEvent.OFFER_SCHOLARSHIP.type -> OFFER_SCHOLARSHIP
                else -> SCOUT
            }
        }
    }
}
package com.appdev.jphil.basketball.recruits

enum class RecruitingEvent(val type: Int) {
    SCOUT(0),
    COACH_CONTACT(1),
    OFFER_SCHOLARSHIP(2),
    OFFICIAL_VISIT(3);


    companion object {
        fun getEventByType(type: Int): RecruitingEvent {
            return when (type) {
                RecruitingEvent.COACH_CONTACT.type -> RecruitingEvent.COACH_CONTACT
                RecruitingEvent.OFFER_SCHOLARSHIP.type -> RecruitingEvent.OFFER_SCHOLARSHIP
                RecruitingEvent.OFFICIAL_VISIT.type -> RecruitingEvent.OFFICIAL_VISIT
                else -> RecruitingEvent.SCOUT
            }
        }
    }
}
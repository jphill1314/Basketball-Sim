package com.appdev.jphil.basketball.recruits

enum class RecruitDesire(val type: Int) {
    STAR(0),
    DEVELOP(1),
    COMPETE(2),
    GOOD_FIT(3);

    companion object {
        fun fromType(type: Int): RecruitDesire {
            return when (type) {
                STAR.type -> STAR
                DEVELOP.type -> DEVELOP
                GOOD_FIT.type -> GOOD_FIT
                else -> COMPETE
            }
        }
    }
}
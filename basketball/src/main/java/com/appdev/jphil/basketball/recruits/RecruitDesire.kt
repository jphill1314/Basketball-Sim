package com.appdev.jphil.basketball.recruits

enum class RecruitDesire(val type: Int) {
    STAR(0),
    DEVELOP(1),
    COMPETE(2);

    companion object {
        fun fromType(type: Int): RecruitDesire {
            return when (type) {
                STAR.type -> STAR
                DEVELOP.type -> DEVELOP
                else -> COMPETE
            }
        }
    }
}
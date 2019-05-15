package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.PostMoveTextContract

class PostMoveText : PostMoveTextContract {

    override fun madeShot(shooter: Player, fouled: Boolean): String {
        return if (fouled) {
            "${shooter.fullName} goes to work in the post, is fouled, and makes the bucket anyways!"
        } else {
            "${shooter.fullName} goes to work in the post, and gets an easy bucket!"
        }
    }

    override fun missedShot(shooter: Player, fouled: Boolean): String {
        return if (fouled) {
            "${shooter.fullName} goes to work in the post, is fouled, but cannot get the shot to fall!"
        } else {
            "${shooter.fullName} goes to work in the post, but cannot get the shot to fall!"
        }
    }
}
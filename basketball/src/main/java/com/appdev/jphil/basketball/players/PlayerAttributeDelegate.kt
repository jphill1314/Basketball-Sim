package com.appdev.jphil.basketball.players

import kotlin.math.max
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PlayerAttributeDelegate(private var value: Int) : ReadWriteProperty<Player, Int> {

    override fun getValue(thisRef: Player, property: KProperty<*>): Int {
        return when (thisRef.inGame) {
            true -> max(((value + thisRef.offensiveStatMod) * getFatigueFactor(thisRef)).toInt(), 20)
            false -> value
        }
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Int) {
        this.value = value
    }

    private fun getFatigueFactor(player: Player): Double {
        return if (player.fatigue > 0.1) {
            Math.max(1 - (Math.exp(player.fatigue / 25.0) / 100.0), .5)
        } else {
            1.0
        }
    }
}
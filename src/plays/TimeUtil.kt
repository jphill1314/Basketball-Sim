package plays

class TimeUtil {

    fun  smartTimeChange(timeChange: Int, shotClock: Int): Int{
        return if(timeChange > shotClock){
            shotClock
        }
        else{
            timeChange
        }
    }
}
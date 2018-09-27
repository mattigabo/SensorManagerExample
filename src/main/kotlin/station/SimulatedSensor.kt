package station

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by Matteo Gabellini on 27/09/2018.
 */
class SimulatedSensor(override val name: String, refreshRate: Long) : Sensor{
    @Volatile
    private var value: Int = 0
    private val executor = Executors.newScheduledThreadPool(1)
    private val changeLogic: Runnable

    init {
        changeLogic = Runnable { this.setCurrentValue(Random().nextInt((100 + 1) - 0) +  0) }
        this.executor.scheduleAtFixedRate(changeLogic, 0L, refreshRate, TimeUnit.MILLISECONDS)
    }

    //thread safe read/write-access to the simulated current value
    @Synchronized
    override fun getCurrentValue() = value

    @Synchronized
    private fun setCurrentValue(v: Int) {
        value = v
    }

    fun stopGeneration() {
        this.executor.shutdown()
    }

}
package station

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * A decoration for a Sensor in order to adds the logic of observable data flow creation
 * according to the RxKotlin (ReactiveX API)
 * */
class ObservableSensor(private val observedSensor: Sensor) : Observable<Int>, Sensor {

    override val name: String = observedSensor.name

    @Volatile
    private var continueObservation = true

    override fun getCurrentValue() = observedSensor.getCurrentValue()

    override fun createObservable(refreshPeriod: Long): Flowable<Int> = Flowable.create<Int>({ emitter ->
        continueObservation = true
        Thread{
            var curVal: Int
            while (continueObservation) {
                try {
                    curVal = observedSensor.getCurrentValue()
                    emitter.onNext(curVal)
                    Thread.sleep(refreshPeriod)
                } catch (ex: Exception) {
                    println(ex.stackTrace)
                }
            }
        }.start()
    }, BackpressureStrategy.BUFFER)

    override fun stopObservation() {
        continueObservation = false
    }
}
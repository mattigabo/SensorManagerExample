package manager

import java.util.*
import kotlin.collections.ArrayList

class SensorValuesStorage(val limit: Int) {
    val queue: Queue<SensorValueRecord> = LinkedList()

    fun store(record: SensorValueRecord){
        if(queue.size == limit){
            queue.poll()
        }
        queue.add(record)
    }

    fun peekAll(): List<SensorValueRecord>{
        var result = ArrayList<SensorValueRecord>()
        queue.forEach{ result.add(it) }
        return result
    }
}
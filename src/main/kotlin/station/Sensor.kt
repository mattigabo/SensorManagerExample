package station

interface Sensor{
    val name: String;
    fun getCurrentValue(): Int;
}

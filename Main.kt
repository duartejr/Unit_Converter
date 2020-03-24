package converter
import java.io.IOException
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val unitDistance = "meter kilometer millimeter centimeter mile yard foot"
    val unitMass = "gram kilogram milligram pound ounce"
    val unitTemperature = "degree Celsius degree Fahrenheit Kelvin"

    while (true) {
        var control = 0
        println("Enter what you want to convert (or exit):")
        val input = scanner.nextLine()
        var newValue = 0.0

        if (input.split(" ")[0] == "exit") {
            break
        }

        var value = 0.0

        try {
            value = input.split(" ")[0].toDouble()
        }
        catch (e: NumberFormatException) {
            println("Parse error")
            continue
        }

        var toUnit = input.split(" to ", " in ").last()
        var unit = input.split(" to ", " in ")[0].split(" ")
        val len = unit.size
        var unit2 = unit.slice(1 until len).joinToString(separator=" ")
        var oldUnit = Units.findByName(unit2)
        var newUnit = Units.findByName(toUnit)

        if (oldUnit == Units.NULL || newUnit == Units.NULL) {
            val unt = oldUnit.n3
            val toUnt = newUnit.n3
            println("Conversion from $unt to $toUnt is impossible")
        } else {
            when {
                unitDistance.contains(oldUnit.n2, ignoreCase = true) -> {
                    if (value < 0) {
                        println("Length shouldn't be negative")
                        control = -1
                    }
                    newValue = distances(value, oldUnit.n1, newUnit.n1)
                }
                unitMass.contains(oldUnit.n2, ignoreCase = true) -> {
                    if (value < 0) {
                        println("Weight shouldn't be negative")
                        control = -1
                    }
                    newValue = mass(value, oldUnit.n1, newUnit.n1)
                }
                unitTemperature.contains(oldUnit.n2, ignoreCase = true) -> {
                    newValue = temperature(value, oldUnit.n1, newUnit.n1)
                }
            }

            if (control == 0) {
                if (newValue != -999999.9999) {
                    printResult(value, oldUnit.n1, newValue, newUnit.n1)
                } else {
                    println("Conversion from ${oldUnit.n3} to ${newUnit.n3} is " +
                            "impossible")
                }
            }
        }
    }
}

enum class Units(val n1: String, val n2: String, val n3: String, val
n4:String, val n5: String) {
    M("m", "meter", "meters", "", ""),
    KM("km", "kilometer", "kilometers", "", ""),
    CM("cm", "centimeter", "centimeters", "", ""),
    MM("mm", "millimeter", "millimeters", "", ""),
    MI("mi", "mile", "miles", "", ""),
    YD("yd", "yard", "yards", "", ""),
    FT("ft", "foot", "feet", "", ""),
    GR("gr", "gram", "grams", "g", ""),
    KG("kg", "kilogram", "kilograms'", "", ""),
    MG("mg", "milligram", "milligrams", "", ""),
    LB("lb", "pound", "pounds", "", ""),
    OZ("oz", "ounce", "ounces", "", ""),
    DC("dc", "degree Celsius", "degrees Celsius", "c", "Celsius"),
    DF("df", "degree Fahrenheit", "degrees Fahrenheit", "f", "Fahrenheit"),
    K("K", "Kelvin", "Kelvins","", ""),
    NULL("???", "???", "???", "", "");

    companion object {
        fun findByName(name: String): Units {
            for (enum in Units.values()) {
                when(name.toLowerCase()) {
                    enum.n1.toLowerCase() -> return enum
                    enum.n2.toLowerCase() -> return enum
                    enum.n3.toLowerCase() -> return enum
                    enum.n4.toLowerCase() -> return enum
                    enum.n5.toLowerCase() -> return enum
                }
            }
            return NULL
        }
    }
}

fun distances(length: Double, unit: String, toUnit: String): Double {
    var convert: Double = length

    when(unit) {
        "m" -> convert = convert
        "km" -> convert *= 1000
        "cm" -> convert *= 0.01
        "mi" -> convert *= 1609.35
        "mm" -> convert *= 0.001
        "yd" -> convert *= 0.9144
        "ft" -> convert *= 0.3048
        "in" -> convert *= 0.0254
    }

    when(toUnit) {
        "m" -> convert = convert
        "km" -> convert /= 1000
        "cm" -> convert /= 0.01
        "mi" -> convert /= 1609.35
        "mm" -> convert /= 0.001
        "yd" -> convert /= 0.9144
        "ft" -> convert /= 0.3048
        "in" -> convert /= 0.0254
        else -> convert = -999999.9999
        }

    return convert
}

fun mass(value: Double, unit: String, toUnit: String): Double {
    var convert = value

    when(unit) {
        "gr" -> convert = convert
        "kg" -> convert *= 1000
        "mg" -> convert *= 0.001
        "lb" -> convert *= 453.592
        "oz" -> convert *= 28.3495
    }

    when(toUnit) {
        "gr" -> convert = convert
        "kg" -> convert /= 1000
        "mg" -> convert /= 0.001
        "lb" -> convert /= 453.592
        "oz" -> convert /= 28.3495
        else -> convert = -999999.9999
    }

    return convert
}

fun temperature(value: Double, unit: String, toUnit: String): Double {
    var convert = 0.0

    convert = when{
        unit == "dc" && toUnit == "K" -> value + 273.15
        unit == "K" && toUnit == "dc" -> value - 273.15
        unit == "dc" && toUnit == "df" -> (value * 9 / 5) + 32
        unit == "df" && toUnit == "dc" -> (value - 32) * 5 / 9
        unit == "K" && toUnit == "df" -> (value * 9 / 5) - 459.67
        unit == "df" && toUnit == "K" -> (value + 459.67) * 5 / 9
        unit == toUnit -> value
        else -> -999999.9999
    }

    return convert
}

fun printResult(old: Double, oldUnit: String, new: Double, newUnit: String) {
    var unit = ""
    var toUnit = ""

    unit = when(old){
        1.0 -> Units.findByName(oldUnit).n2
        else -> Units.findByName(oldUnit).n3
    }

    toUnit = when(new){
        1.0 -> Units.findByName(newUnit).n2
        else -> Units.findByName(newUnit).n3
    }

    println("$old $unit is $new $toUnit")

}

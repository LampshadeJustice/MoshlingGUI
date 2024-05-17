import java.io.*
const val fileName = "res/Moshling Seed Combos - Sheet1.csv"

val recipes = File(fileName).bufferedReader().lineSequence()
    .filter { it.isNotBlank() }
    .filter { !it.contains("-") }
    .map(::processCSVLine)
    .toList()

fun testRecipe(recipe: MoshlingRecipe): String {
    var outStr = ""

    val newRecipes = recipes.map { Pair(it, it.match(recipe)) }
        .toMutableList()
    newRecipes.sortByDescending { it.second }

    var top: Int = 400
    for (r in newRecipes) {
        if (r.second < top) {
            outStr += "\n--- MATCH BY ${r.second} ---\n"
            top = r.second
        }
        outStr += "  ${r.first}\n"
    }

    return outStr
}

fun processCSVLine(s: String): MoshlingRecipe {

    val (name, p1, p2, p3) = s.split(",")
    return MoshlingRecipe(name, p1, p2, p3)

}

class MoshlingRecipe(val name: String, p1: Plant, p2: Plant, p3: Plant) {

    val plants: Array<Plant> = arrayOf(p1, p2, p3)

    constructor(name: String, p1: String, p2: String, p3: String) :
            this(name, Plant.processPlant(p1), Plant.processPlant(p2), Plant.processPlant(p3))



    fun match(recipe: MoshlingRecipe): Int {
        var count = 0
        val matches = arrayOf(false, false, false)

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (matches[j]) continue

                val m = plants[i].match(recipe.plants[j])
                count += m

                if (m > 0) {
                    matches[j] = true
                    break
                }
            }
        }

        return count
    }

    override fun toString(): String { return "$name: ${plants[0]}, ${plants[1]}, ${plants[2]}" }

}



class Plant(var c: Color, var p: PlantType) {

    fun match(plant: Plant): Int = when {
        (p == plant.p && plant.c == Color.Any) -> 1
        (p == plant.p && c == plant.c) -> 5
        (p == plant.p && c == Color.Any) -> 1
        else -> 0
    }

    override fun toString(): String { return "${c.toString()} ${p.toString()}" }

    override fun equals(other: Any?): Boolean {
        if (other is Plant) {
            return other.p == p && other.c == c
        }
        return false
    }

    companion object

}

private fun Plant.Companion.processPlant(s: String): Plant {
    val (color, type) = s.split(" ")
    return Plant(enumValueOf<Color>(color), enumValueOf<PlantType>(type))
}


enum class Color {Any, Pink, Red, Orange, Yellow, Green, Blue, Purple, Black}
enum class PlantType {None, Apple, Beans, Berries, Blossom, Daisy, Fruit, Orchid, Peppers}
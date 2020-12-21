package `2020`.`21`

import commonObjects
import util.printDay
import util.readFileLineByLineToText
import java.util.stream.Stream
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Ingrediënts without allergens are used in # dishes: ")
    println(IngredientsList(readFileLineByLineToText("2020_21.txt")).getIngredientsWithoutAllergensUsageCount())

    printDay(2)
    print("The canonical dangerous ingredient list is: ")
    println(IngredientsList(readFileLineByLineToText("2020_21.txt")).getCanonicalDangerousIngredientList())
}

private class IngredientsList(list: Stream<String>) {
    val recipes = list.map { Recipe(it) }.toList()
    val allergenIngredientsMap: Map<String, List<String>>

    init {
        val allergenIngredients = mutableMapOf<String, MutableList<String>>()

        for (allergen in getAllAllergens()) {
            allergenIngredients[allergen] = getPossibleIngredientsForAllergen(allergen).toMutableList()
        }
        while (allergenIngredients.values.any { it.size > 1 }) {
            for (allergenInfo in allergenIngredients.filter { it.value.size > 1 }) {
                allergenInfo.value.removeIf { allergenIngredients.filter { it.value.size == 1 }.values.flatMap { it }.contains(it) }
            }
        }

        allergenIngredientsMap = allergenIngredients
    }

    fun getCanonicalDangerousIngredientList() : String {
        return allergenIngredientsMap.toSortedMap().map { it.value.first() }.joinToString(",")
    }

    fun getIngredientsWithoutAllergensUsageCount() : Int {
        return recipes.flatMap { it.ingredients }.count { getIngredientsWithoutAllergens().contains(it) }
    }

    private fun getIngredientsWithoutAllergens() : List<String> {
        val list = getAllIngredients().toMutableList()
        list.removeIf { allergenIngredientsMap.values.flatMap { it }.contains(it) }
        return list
    }

    private fun getAllIngredients(): List<String> {
        return recipes.flatMap { it.ingredients }.distinct()
    }

    private fun getAllAllergens(): List<String> {
        return recipes.flatMap { it.allergens }.distinct()
    }

    private fun getPossibleIngredientsForAllergen(allergen: String) : List<String> {
        return recipes.filter { it.allergens.contains(allergen) }.map { it.ingredients }.commonObjects() as List<String>
    }
}

private class Recipe(recipe: String) {
    val allergens: List<String> = recipe.substringAfter("(contains ").substringBefore(")").split(", ")
    val ingredients: List<String> = recipe.substringBefore(" (contains ").split(" ")
}
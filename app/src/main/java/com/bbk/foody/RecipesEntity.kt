package com.bbk.foody

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bbk.foody.models.FoodRecipe
import com.bbk.foody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
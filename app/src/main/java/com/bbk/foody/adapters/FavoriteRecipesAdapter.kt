package com.bbk.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bbk.foody.R
import com.bbk.foody.data.database.entities.FavoritesEntity
import com.bbk.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.bbk.foody.ui.fragments.favorites.FavoriteRecipesFragment
import com.bbk.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.bbk.foody.util.RecipesDiffUtil


class FavoriteRecipesAdapter(
    private val fragment: FavoriteRecipesFragment
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {

    private val requireActivity: FragmentActivity = fragment.requireActivity()

    var multiSelection = false

    private lateinit var rootView: View

    var selectedRecipes = arrayListOf<FavoritesEntity>()
    var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single Click Listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {

            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(fragment)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder, currentRecipe)
                true
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        }
        fragment.applyActionModeTitle()
    }

    fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }



    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }



    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }


}
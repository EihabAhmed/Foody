package com.bbk.foody.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbk.foody.R
import com.bbk.foody.adapters.FavoriteRecipesAdapter
import com.bbk.foody.databinding.FragmentFavoriteRecipesBinding
import com.bbk.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(), ActionMode.Callback {

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(this) }

    private lateinit var mActionMode: ActionMode

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setHasOptionsMenu(true)

        setupRecyclerView(binding.favoriteRecipesRecyclerView)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All recipes removed.")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clearContextualActionMode()
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {

        if (menu?.itemId == R.id.delete_favorite_recipe_menu) {
            mAdapter.selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }

            showSnackBar("${mAdapter.selectedRecipes.size} Recipe/s removed.")
            mAdapter.multiSelection = false
            mAdapter.selectedRecipes.clear()
            actionMode?.finish()
        }

        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {

        mAdapter.myViewHolders.forEach { holder ->
            mAdapter.changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }

        mAdapter.multiSelection = false
        mAdapter.selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), color)
    }

    fun applyActionModeTitle() {
        when (mAdapter.selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
                mAdapter.multiSelection = false
            }
            1 -> {
                mActionMode.title = "1 item selected"
            }
            else -> {
                mActionMode.title = "${mAdapter.selectedRecipes.size} items selected"
            }
        }
    }

    private fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}
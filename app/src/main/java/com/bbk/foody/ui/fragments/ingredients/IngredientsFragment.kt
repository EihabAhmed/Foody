package com.bbk.foody.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbk.foody.R
import com.bbk.foody.adapters.IngredientsAdapter
import com.bbk.foody.models.Result
import com.bbk.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecycletView(view)
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return view
    }

    private fun setupRecycletView(view: View) {
        view.ingredients_recyclerview.adapter = mAdapter
        view.ingredients_recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}
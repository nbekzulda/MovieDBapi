package com.example.moviedbapi.main.cinema.list
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedbapi.R
import com.example.moviedbapi.base.ParentFragment
import com.example.moviedbapi.data.roomCinema.Cinema
import com.example.moviedbapi.main.cinema.adapters.CinemaListAdapter
import com.example.moviedbapi.main.cinema.CinemaViewModel
import com.example.moviedbapi.utilities.AppConstants
import org.koin.android.ext.android.inject

class CinemaListFragment : ParentFragment() {

    private lateinit var navController: NavController
    private val listViewModel: CinemaViewModel by inject()
    private lateinit var rvCinemas: RecyclerView

    companion object {
        fun newInstance() : CinemaListFragment =
            CinemaListFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.kinoteatr_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setAdapter()
        setData()
    }

    private val onClickListener = object:
        CinemaListAdapter.ItemClickListener {
        override fun onItemClick(item: Cinema) {
            val bundle = Bundle()
            item.id?.let{
                bundle.putInt(AppConstants.CINEMA_ID, item.id)
            }

            navController.navigate(
                R.id.action_cinemaFragment_to_cinemaDetailsFragment,
                bundle
            )
        }
    }
    private val cinemaListAdapter by lazy {
        CinemaListAdapter (
            context = context,
            itemClickListener = onClickListener
        )
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        rvCinemas = findViewById(R.id.recyclerView)
        rvCinemas.layoutManager = LinearLayoutManager(context)
    }

    override fun setData() {
        listViewModel.liveData.observe(viewLifecycleOwner, Observer { cinemaList ->
            cinemaListAdapter.setCinemas(cinemaList)
        })
    }

    private fun setAdapter() {
        rvCinemas.adapter = cinemaListAdapter
    }
}
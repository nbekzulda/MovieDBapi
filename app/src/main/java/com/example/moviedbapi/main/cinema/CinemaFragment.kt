package com.example.moviedbapi.main.cinema


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.moviedb.presentation.movie.cinema.adapters.ViewPagerAdapter
import com.example.moviedbapi.R
import com.example.moviedbapi.main.cinema.list.CinemaListFragment
import com.example.moviedbapi.main.cinema.map.CinemaMapFragment
import com.google.android.material.tabs.TabLayout

class CinemaFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.kinoteatr_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupViewPager()
    }

    private fun initViews(view: View) = with(view) {
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
    }

    private fun setupViewPager() {
        val adapter = childFragmentManager.let { fragmentManager ->
            ViewPagerAdapter(
                fragmentManager
            )
        }
        val cinemaListFragment: CinemaListFragment = CinemaListFragment.newInstance()
        val cinemaMapFragment: CinemaMapFragment = CinemaMapFragment.newInstance()
        adapter.addFragment(cinemaListFragment, "Cinemas")
        adapter.addFragment(cinemaMapFragment, "Map")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

}

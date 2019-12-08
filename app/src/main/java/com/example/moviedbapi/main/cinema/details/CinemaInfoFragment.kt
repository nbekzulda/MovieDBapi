package com.example.moviedbapi.main.cinema.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviedbapi.R
import com.example.moviedbapi.base.ParentFragment
import com.example.moviedbapi.utilities.AppConstants

class CinemaInfoFragment : ParentFragment() {

    private lateinit var viewModel: CinemaInfoViewModel
    private lateinit var ivBackdrop: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvParking: TextView
    private lateinit var tvContacts: TextView
    private lateinit var tvEntry: TextView
    private lateinit var tvBuffet: TextView
    private var cinemaId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.kinoteatr_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CinemaInfoViewModel::class.java)
        bindViews(view)
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        ivBackdrop = view.findViewById(R.id.ivBackdrop)
        tvName = view.findViewById(R.id.tvName)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvParking = view.findViewById(R.id.tvParking)
        tvContacts = view.findViewById(R.id.tvContacts)
        tvEntry = view.findViewById(R.id.tvEntry)
        tvBuffet = view.findViewById(R.id.tvBuffet)
        cinemaId = arguments?.getInt(AppConstants.CINEMA_ID)
    }

    override fun setData() {
        cinemaId?.let { id ->
            viewModel.getCinema(id)
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer {result ->
            val imageUrl = "${AppConstants.POSTER_CINEMA_BASE_URL}${result.poster}"
            Glide.with(this)
                .load(imageUrl)
                .into(ivBackdrop)
            tvName.text = result.name
            tvAddress.text = result.address
            tvParking.text = result.parking
            tvContacts.text = result.phoneNumber
            tvEntry.text = result.entry
            tvBuffet.text = result.buffet
        })
    }
}

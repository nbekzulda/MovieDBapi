package com.example.moviedbapi.main.user


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.moviedbapi.R
import com.example.moviedbapi.base.ParentFragment
import com.example.moviedbapi.utilities.AppPreferences
import org.koin.android.ext.android.inject

class UserFragment : ParentFragment() {
    private val viewModel: UserViewModel by inject()
    private lateinit var progressBar: ProgressBar
    private lateinit var tvName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvAdult: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        Log.d("profile","Here is an error")
        setData()
    }

    override fun bindViews(view: View) = with(view){
        progressBar = view.findViewById(R.id.progressBarProfile)
        tvName = view.findViewById(R.id.NameValue)
        tvUsername = view.findViewById(R.id.UsernameValue)
        tvAdult = view.findViewById(R.id.AdultValue)

    }

    override fun setData() {
        val sessionId = AppPreferences.getSessionId(activity?.applicationContext!!)
        sessionId?.let {
            viewModel.getAccountDetails(sessionId)
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is UserViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is UserViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is UserViewModel.State.Result -> {
                    tvName.text = result.account?.name
                    tvUsername.text = result.account?.username
                    tvAdult.text = result.account?.adult.toString()
                }
                is UserViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is UserViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}

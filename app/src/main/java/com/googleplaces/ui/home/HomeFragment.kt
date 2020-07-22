package com.googleplaces.ui.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.googleplaces.R
import com.googleplaces.data.model.Response
import com.googleplaces.di.vm.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

import javax.inject.Inject


class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory<HomeViewModel>

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var alertDialog: AlertDialog
    lateinit var list: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var progress: ProgressBar
    private lateinit var textView: TextView

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, mViewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val locationAdapter = LocationRecyclerViewAdapter()
        list = root.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = locationAdapter
        }

        fab = root.findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                alertDialog.show()
            }
        }

        progress = root.findViewById(R.id.progress)
        textView = root.findViewById(R.id.error_message)

        createAlertDialog()



        homeViewModel.locationResults.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Response.ResultSuccess -> {
                    locationAdapter.updateList(response.results)
                    progress.visibility = View.GONE
                    list.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                }
                is Response.ResultFailure -> {
                    progress.visibility = View.GONE
                    list.visibility = View.GONE
                    fab.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                    textView.text = response.error
                }

                Response.Loading -> {
                    progress.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    fab.visibility = View.GONE
                    textView.visibility = View.GONE
                }
            }
        })

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocation()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0]
                            == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {
                    Toast.makeText(activity, "Location services is required",
                        Toast.LENGTH_LONG).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getLocation() {
        activity?.let {

            if (ContextCompat.checkSelfPermission(
                    it,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        homeViewModel.getNearByLocation("${location?.latitude}, ${location?.longitude}")
                    }
            } else {
                requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
        }
    }

    private fun createAlertDialog() {
        val builder =  AlertDialog.Builder(activity as AppCompatActivity)
        builder.setTitle("Choose Radius")

        val radius = arrayOf("5 Miles", "10 miles", "25 miles", "100 miles")
        builder.apply {
            var selection = homeViewModel.getRadius()
            builder.setTitle("Choose Radius")
            setSingleChoiceItems(radius, selection) { dialog, which ->
                selection = which
            }
            setPositiveButton("Submit") { _, _ ->
                homeViewModel.saveRadius(selection)
                getLocation()
            }
            setNegativeButton("Cancel", null)
        }

        alertDialog = builder.create()
    }

    companion object {
        const val REQUEST_CODE = 111
    }
}
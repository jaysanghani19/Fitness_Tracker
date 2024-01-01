package com.jay.fitnesstracker.dashboardactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jay.fitnesstracker.databinding.DashboardFragmentBinding
import com.jay.fitnesstracker.R
import com.jay.fitnesstracker.stepcounter.StepsCountActivity

class DashboardFragment : Fragment() {
    private lateinit var binding: DashboardFragmentBinding

    private var isRecentActive = true
    private var isAllActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Dashboard Fragment", "onCreate: Fragment created")
    }
    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false)

        binding.recentButton.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.circular_50_counter_white_blue))
        binding.recentButton.setTextColor(requireActivity().getColor(R.color.white))

        binding.allButton.setTextColor(requireActivity().getColor(R.color.black))
        binding.allButton.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.circular_background))

        binding.settingButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSettingFragment())
            
        }

        binding.runningButton.setOnClickListener {
            val intent = Intent(requireContext(), StepsCountActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(){


//        binding.recentButton.setOnClickListener {
//            clickListenerOfRecent()
//        }
//
//        binding.allButton.setOnClickListener {
//            clickListenerOfAll()
//        }
    }

    private fun clickListenerOfRecent(){

    }

    private fun clickListenerOfAll(){

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Dashboard Fragment", "onCreate: Fragment destroyed")

    }

}
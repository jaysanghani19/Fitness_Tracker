package com.jay.fitnesstracker.dashboardactivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jay.fitnesstracker.databinding.SettingFragmentBinding
import com.jay.fitnesstracker.R

class SettingFragment : Fragment() {

    private lateinit var binding: SettingFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Setting Fragment", "onCreate: Fragment created")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.setting_fragment,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Setting Fragment", "onCreate: Fragment destroyed")

    }
}
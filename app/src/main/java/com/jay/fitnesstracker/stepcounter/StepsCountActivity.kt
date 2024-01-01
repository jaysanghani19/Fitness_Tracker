package com.jay.fitnesstracker.stepcounter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jay.fitnesstracker.DashboardActivity
import com.jay.fitnesstracker.databinding.StepCountLayoutBinding
import com.jay.fitnesstracker.R

class StepsCountActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: StepCountLayoutBinding

    private lateinit var sensorManager: SensorManager

    private lateinit var stepCounterSensor: Sensor

    private lateinit var viewModel: StepCounterActivityViewModel

    private var initialStep = 0f

    private var stepsByUser = 0f

    private var distancePerStep = 0.762f

    private val REQUEST_CODE_ACTIVITY_RECOGNITION = 1

    private var isTimeRunning = true

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.step_count_layout)

        viewModel = ViewModelProvider(this).get(StepCounterActivityViewModel::class.java)

        viewModel.startTimer()

        binding.stepsTextview.text = getString(R.string.steps, "0")
        binding.timeTextview.text = getString(R.string.time, "0")
        binding.distanceTextview.text = getString(R.string.distance, "0")

        binding.pauseButton.setOnClickListener {
            clickListenerOfPauseButton()
        }

        viewModel.stepsByUser.observe(this) {it ->
            binding.stepsTextview.text = getString(R.string.steps, it)
        }

        viewModel.distance.observe(this) {it->
            binding.distanceTextview.text = getString(R.string.distance, it)
        }

        viewModel.elapsedTime.observe(this){it->
            binding.timeTextview.text= getString(R.string.time,setTime(it))
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        if (stepCounterSensor == null) {
            Toast.makeText(
                this,
                "There's No Sensor /n Please use Diffrent device to use service",
                Toast.LENGTH_SHORT
            ).show()
            Log.i("Dashboard", "Sensor Null: Dashboard Called From Sensor is Null")
           startDashboardActivity()
        }

        if (!isPhysicalActivityPermissionGiven()){
            requestPhysicalActivityPermission()
        }
    }

    private fun clickListenerOfPauseButton(){
        if (isTimeRunning) {
            viewModel.stopTimer()
            isTimeRunning = false
            binding.pauseButton.text = getString(R.string.resume)
        }
        else {
            viewModel.startTimer()
            isTimeRunning = true
            binding.pauseButton.text=getString(R.string.pause)
        }
    }
//    This function will start DashboardActivity and remove this activity from stack
    private fun startDashboardActivity(){
    Log.i("Dashboard", "startDashboardActivity: Starting Dashboard")
        val intent = Intent(this,DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

//    This function will check the permission is given or not
//    Using ContextCompat.checkSelfPermission
    private fun isPhysicalActivityPermissionGiven():Boolean{
        val isPermissionGiven = ContextCompat.checkSelfPermission(this,Manifest.permission.ACTIVITY_RECOGNITION)
        return isPermissionGiven == PackageManager.PERMISSION_GRANTED
    }

//    This function will ask for permission from user
//    Using ActivityCompat.requestPermissions
    private fun requestPhysicalActivityPermission(){
        val permission = Manifest.permission.ACTIVITY_RECOGNITION
        ActivityCompat.requestPermissions(this, arrayOf(permission),REQUEST_CODE_ACTIVITY_RECOGNITION)
    }

//    This Function will return String for the Seconds in Hour,Minute,Second
    fun setTime(t: Int): String {
        val intTime = t
        val hours: Int = intTime % 86400 / 3600
        val minutes: Int = intTime % 86400 % 3600 / 60
        val seconds: Int = intTime % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Physical Activity Permission is Required")
        builder.setMessage("This app requires the Physical Activity permission to function properly. Please enable the permission in the app settings.")

        builder.setPositiveButton("Go to Settings") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // Handle cancellation if needed
        }

        builder.show()
    }
//   This function will return the result of permission.
//   Has user allowed or not allowed
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_DENIED){
                Log.i("Dashboard", "onRequestPermissionsResult: Dashboard Called From Request Permission")
                showSettingsDialog()
//                startDashboardActivity()
            }
        }
    }
//    onResume and onStop function for register and deregister the sensors
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
//        viewModel.startTimer()
//        isTimeRunning = true
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
        viewModel.stopTimer()
        isTimeRunning = false
        binding.pauseButton.text = getString(R.string.resume)
    }

//    When the sensor listen to the activity
//    It'll Notify through this method
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent != null) {
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if (initialStep == 0f) {
                    viewModel.setInitialStep(sensorEvent.values[0])
                    initialStep = sensorEvent.values[0]
                }

                viewModel.initialStep.observe(this) {
                    viewModel.setStepsAndDistance(sensorEvent.values[0])
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}
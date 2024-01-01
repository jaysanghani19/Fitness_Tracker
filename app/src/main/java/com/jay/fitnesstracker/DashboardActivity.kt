    package com.jay.fitnesstracker

    import android.os.Bundle
    import android.util.Log
    import android.view.MenuItem
    import androidx.appcompat.app.AppCompatActivity
    import androidx.databinding.DataBindingUtil
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentManager
    import androidx.fragment.app.FragmentTransaction
    import androidx.lifecycle.ReportFragment.Companion.reportFragment
    import com.google.android.material.bottomnavigation.BottomNavigationItemView
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import com.jay.fitnesstracker.R
    import com.jay.fitnesstracker.dashboardactivity.DashboardFragment
    import com.jay.fitnesstracker.dashboardactivity.SettingFragment
    import com.jay.fitnesstracker.databinding.DashboardActivityBinding

    @Suppress("DEPRECATION")
    class DashboardActivity : AppCompatActivity() {
        private lateinit var binding: DashboardActivityBinding

        private val dashboardFragment = DashboardFragment()
        private val settingFragment = SettingFragment()

        @Suppress("DEPRECATION")
        private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> {
                    Log.i("Fragment Change", "Home Fragment: ")
                    replaceFragment(dashboardFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.setting -> {
                    Log.i("Fragment Change", "Setting Fragment: ")

                    replaceFragment(settingFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> return@OnNavigationItemSelectedListener false
            }

        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this,R.layout.dashboard_activity)
            replaceFragment(dashboardFragment)
            binding.bottomNavigation.setOnNavigationItemSelectedListener(
                onNavigationItemSelectedListener
            )
            binding.bottomNavigation.menu.findItem(R.id.home).isCheckable = true
    //        binding.bottomNavigation.menu.findItem(R.id.setting).isCheckable = true

        }

        private fun replaceFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frame_layout,fragment,fragment.javaClass.simpleName)
                .commit()
        }
    }
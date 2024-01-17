package com.demo.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.demo.app.databinding.ActivityMainBinding
import com.demo.app.ui.HomeFragment
import com.demo.app.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ParentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        onNewIntent(intent)

        addFragment(true, 1, HomeFragment())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val bundle: Bundle? = intent?.extras
        if (bundle != null) {
            Log.d("xxyyzz", "onNewIntent: 1" + Gson().toJson(bundle))
        } else
            Log.d("xxyyzz", "onNewIntent: 2")
    }

    override fun onBackPressed() {
        if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() != 1) {
            clearBackStack(2)
            clearBackStack(3)
            clearBackStack(4)
            clearBackStack(5)
        } else if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() == 1) {
            clearBackStack(0)
            finish()
        } else {
            removeCurrentFragment()

        }
    }

    override fun onClick(p0: View?) {
    }

}
package com.baka3k.architecture.clean.movie

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

import com.baka3k.architecture.clean.R
import com.baka3k.architecture.clean.base.ui.BaseActivity

class MovieActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
    }
}
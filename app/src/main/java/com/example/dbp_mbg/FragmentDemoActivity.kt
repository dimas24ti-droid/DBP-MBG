package com.example.dbp_mbg

import android.os.Bundle

class FragmentDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(Fragment1())
    }
}

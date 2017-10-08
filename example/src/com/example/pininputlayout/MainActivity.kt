package com.example.pininputlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hendraanggrian.app.PinDialog
import kota.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        pinInputLayout.setOnStateChangedListener { _, isComplete -> textView.text = isComplete.toString() }
    }

    fun buttonOnClick(view: View) = PinDialog.Builder(this)
            .setTitle("Enter pin")
            .setNegativeButton(getText(android.R.string.cancel))
            .setPositiveButton(getText(android.R.string.ok), { _, _, text -> toast(text) })
            .build()
            .show()
}
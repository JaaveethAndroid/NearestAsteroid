package dev.filipebezerra.android.nearearthasteroids

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import dev.filipebezerra.android.nearearthasteroids.databinding.NeaActivityBinding

class NeaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<NeaActivityBinding>(this, R.layout.nea_activity)
    }
}
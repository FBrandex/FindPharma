package com.felipebrand.findpharma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.felipebrand.findpharma.mapas.MapsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_FindPharma)
        setContentView(R.layout.activity_main)
        Thread.sleep(1000);
        tela1();
    }

    private fun tela1() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        Toast.makeText(applicationContext, "Teste", Toast.LENGTH_SHORT).show()
    }



}


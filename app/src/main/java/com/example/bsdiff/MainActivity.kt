package com.example.bsdiff

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.EnvironmentCompat
import com.example.bspatch.NativeLib
import java.io.File

class MainActivity : AppCompatActivity() {

    init {
        System.loadLibrary("bspatch")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES
            ), 101
        )

        findViewById<TextView>(R.id.main_tv).text = "当前版本: "+BuildConfig.VERSION_NAME
        findViewById<Button>(R.id.main_btn).text = "升级"
        findViewById<Button>(R.id.main_btn).setOnClickListener {
//            Toast.makeText(this,"已是最新版本",Toast.LENGTH_SHORT).show();
            update()
        }



    }

    private fun update() {
        PatchUtil.getInstance().patch(this);
    }


}
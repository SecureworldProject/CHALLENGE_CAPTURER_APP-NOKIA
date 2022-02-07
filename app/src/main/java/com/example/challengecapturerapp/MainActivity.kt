package com.example.challengecapturerapp

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

const val TAG: String = "secureworld"   // tag to use in the log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the ids from the view
        val btnOpenQRScanner: Button = findViewById(R.id.btnOpenQRScanner)
        val btnCaptureImage: Button = findViewById(R.id.btnCaptureImage)
        val btnCaptureAudio: Button = findViewById(R.id.btnCaptureAudio)
        val btnCaptureVideo: Button = findViewById(R.id.btnCaptureVideo)
        val tvDeviceLinkedUrlData: TextView = findViewById(R.id.tvDeviceLinkedUrlData)
        val tvDeviceLinkedInfo: TextView = findViewById(R.id.tvDeviceLinkedInfo)

        var driveLink: String = ""

        // Assign the action from the intent and compare with the different cases
        when (val action: String? = intent?.action) {
            "android.intent.action.MAIN" -> {
                Toast.makeText(this, "App manually opened", Toast.LENGTH_SHORT).show()
            }
            "android.intent.action.VIEW" -> {
                val data: Uri? = intent?.data
                Toast.makeText(this, "App opened from secureworld link (QR code scanned)", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, "Action is: $action", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, "Data is: ${data.toString()}", Toast.LENGTH_SHORT).show()

                // TODO process the link string and remove "secureworld://" from it
                // Set the scanned link and fill the TextViews
                driveLink = data.toString()
                tvDeviceLinkedUrlData.text = driveLink
                if (driveLink.isEmpty()){
                    tvDeviceLinkedInfo.text = resources.getString(R.string.device_linked_info_false)
                    tvDeviceLinkedInfo.setTextColor(resources.getColor(R.color.red_light))
                } else {
                    tvDeviceLinkedInfo.text = resources.getString(R.string.device_linked_info_true)
                    tvDeviceLinkedInfo.setTextColor(resources.getColor(R.color.green_dark))
                }
            }
            else -> {
                // Unrecognized action
                Toast.makeText(this, "Unrecognized action", Toast.LENGTH_SHORT).show()
                return
            }
        }


        btnOpenQRScanner.setOnClickListener(){

            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
                intentQRScannerLauncher.launch(intent)
            } catch (e: Exception) {
                // TODO
                Toast.makeText(this, "On development...", Toast.LENGTH_SHORT).show()

                //Toast.makeText(this, "No QR Code Scanner is installed", Toast.LENGTH_SHORT).show()

                // Opens app store to download ZXing app (which is a QR scanner)
                //val marketUri: Uri = Uri.parse("market://details?id=com.google.zxing.client.android")
                //val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                //startActivity(marketIntent)

                // Creates a list with the apps installed (not tested)
                //val mainIntent = Intent(Intent.ACTION_MAIN, null)
                //mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                //val pkgAppsList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

                // Prints in debug log the apps installed in the device
                /*
                val pm = packageManager
                //get a list of installed apps.
                val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

                for (packageInfo in packages) {
                    Log.d(TAG, "Installed package :" + packageInfo.packageName)
                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir)
                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName)
                    )
                }*/
                // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
            }
        }

        btnCaptureImage.setOnClickListener() {
            // TODO
            Toast.makeText(this, "Pressed capture image", Toast.LENGTH_SHORT).show()
        }

        btnCaptureAudio.setOnClickListener() {
            // TODO
            Toast.makeText(this, "Pressed capture audio", Toast.LENGTH_SHORT).show()
        }

        btnCaptureVideo.setOnClickListener() {
            // TODO
            Toast.makeText(this, "Pressed capture video", Toast.LENGTH_SHORT).show()
        }


    }

    // Function for the intent associated to the btnOpenQRScanner
    var intentQRScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val contents = data?.getStringExtra("SCAN_RESULT")
            Toast.makeText(this, "Scanned $contents", Toast.LENGTH_SHORT).show()
        }
        if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Error scanning", Toast.LENGTH_SHORT).show()
            //handle cancel
        }
    }





}
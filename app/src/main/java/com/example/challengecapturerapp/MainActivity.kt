package com.example.challengecapturerapp

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager


const val TAG: String = "secureworld"   // tag to use in the log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the ids from the view
        val btnClearLink: Button = findViewById(R.id.btnClearLink)
        val btnOpenQRScanner: Button = findViewById(R.id.btnOpenQRScanner)
        val linearLayoutCaptureMethods: LinearLayout = findViewById(R.id.linearLayoutCaptureMethods)
        val btnCaptureImage: Button = findViewById(R.id.btnCaptureImage)
        val btnCaptureAudio: Button = findViewById(R.id.btnCaptureAudio)
        val btnCaptureVideo: Button = findViewById(R.id.btnCaptureVideo)
        val tvDeviceLinkedUrlData: TextView = findViewById(R.id.tvDeviceLinkedUrlData)
        val tvDeviceLinkedInfo: TextView = findViewById(R.id.tvDeviceLinkedInfo)

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        fun getDriveLink(): String {
            // Variable should not be null, but it is checked for safety
            prefs.getString("driveLink", "")?.let {
                return it
            } ?: run {
                return ""
            }
        }

        fun setDriveLink(newDriveLink: String): Unit {
            val edit = prefs.edit()
            edit.putString("driveLink", newDriveLink)
            edit.apply()
        }

        fun updateDriveLinkView(){
            val driveLink: String = getDriveLink()
            tvDeviceLinkedUrlData.text = driveLink
            if (driveLink.isEmpty()){
                tvDeviceLinkedInfo.text = resources.getString(R.string.device_linked_info_false)
                tvDeviceLinkedInfo.setTextColor(resources.getColor(R.color.red_light))
                linearLayoutCaptureMethods.visibility = View.GONE
            } else {
                tvDeviceLinkedInfo.text = resources.getString(R.string.device_linked_info_true)
                tvDeviceLinkedInfo.setTextColor(resources.getColor(R.color.green_dark))
                linearLayoutCaptureMethods.visibility = View.VISIBLE
            }
        }

        updateDriveLinkView()


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

                // Remove "secureworld://" from the input link string and set it as new value of driveLink
                // Set the scanned link and fill the TextViews
                if (getDriveLink().isNotEmpty()) {
                    // Ask user if he wants to update the link with the new one or keep the old one
                    val okCancelBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                    okCancelBuilder.setMessage("The application is already linked with secureworld. Do you want to use the new link or keep the old one?")
                    okCancelBuilder.setCancelable(true)
                    okCancelBuilder.setPositiveButton("Update",
                        DialogInterface.OnClickListener { dialog, id ->
                            // Code executed when "update" button is clicked
                            val inputLink = data.toString()
                            setDriveLink(inputLink.subSequence("secureworld://".length, inputLink.length) as String)
                            updateDriveLinkView()
                            dialog.dismiss()
                        })
                    okCancelBuilder.setNegativeButton("Keep old",
                        DialogInterface.OnClickListener { dialog, id ->
                            // Code executed when "Keep old" button is clicked
                            dialog.dismiss()
                        })

                    val okCancelDialog: AlertDialog = okCancelBuilder.create()
                    okCancelDialog.show()
                } else {
                    val inputLink = data.toString()
                    setDriveLink(inputLink.subSequence("secureworld://".length, inputLink.length) as String)
                    updateDriveLinkView()
                }
            }
            else -> {
                // Unrecognized action
                Toast.makeText(this, "Unrecognized action", Toast.LENGTH_SHORT).show()
                return
            }
        }

        btnClearLink.setOnClickListener(){
            if (getDriveLink().isNotEmpty()) {
                // Ask user if he really wants to remove the link
                val okCancelBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                okCancelBuilder.setMessage("Are you sure you want to remove the link?")
                okCancelBuilder.setCancelable(true)
                okCancelBuilder.setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Code executed when "Confirm" button is clicked
                        setDriveLink("")
                        updateDriveLinkView()
                        dialog.dismiss()
                    })
                okCancelBuilder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Code executed when "Cancel" button is clicked
                        dialog.dismiss()
                    })

                val okCancelDialog: AlertDialog = okCancelBuilder.create()
                okCancelDialog.show()
            } else {
                Toast.makeText(this, "Link is already clear.", Toast.LENGTH_SHORT).show()
            }
        }

        btnOpenQRScanner.setOnClickListener(){
            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
                intentQRScannerLauncher.launch(intent)
            } catch (e: Exception) {
                // TODO launch an app or app selector of apps that can read QR codes
                Toast.makeText(this, "On development...", Toast.LENGTH_SHORT).show()
                // Fake the drivelink for testing
                println("Change drivelink for testing purposes... xxx")
                setDriveLink("xxx")
                updateDriveLinkView()


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
            if (getDriveLink().isNotEmpty()){
                Toast.makeText(this, "Pressed capture image.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CapturePhotoActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Ended capture image.", Toast.LENGTH_SHORT).show()
            } else {
                // This should never happen (the button is hidden)
                Toast.makeText(this, "Device is not linked yet.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCaptureAudio.setOnClickListener() {
            // TODO
            if (getDriveLink().isNotEmpty()){
                Toast.makeText(this, "Pressed capture audio.", Toast.LENGTH_SHORT).show()
            } else {
                // This should never happen (the button is hidden)
                Toast.makeText(this, "Device is not linked yet.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCaptureVideo.setOnClickListener() {
            // TODO
            if (getDriveLink().isNotEmpty()){
                Toast.makeText(this, "Pressed capture video.", Toast.LENGTH_SHORT).show()
            } else {
                // This should never happen (the button is hidden)
                Toast.makeText(this, "Device is not linked yet.", Toast.LENGTH_SHORT).show()
            }
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
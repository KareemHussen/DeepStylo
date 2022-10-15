package com.example.deepstylo

import FilterAdapter
import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.deepstylo.Utils.InternetUtil.isOnline
import com.example.deepstylo.Utils.RealPathUtil
import com.example.deepstylo.model.FilterItem
import com.example.deepstylo.viewmodel.ViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.snatik.storage.Storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.slider_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val adapter : FilterAdapter by lazy {
        FilterAdapter()
    }
    private lateinit var imageUri : Uri
    var pickedImage = false

    val viewModel : ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        imageUri = intent.extras!!.get("image") as Uri

        mySlider.before_image_view_id.scaleType = ImageView.ScaleType.FIT_XY
        mySlider.after_image_view_id.scaleType = ImageView.ScaleType.FIT_XY

        mySlider.before_image_view_id.setImageURI(imageUri)


        imageView.setImageURI(imageUri)

        loadBannerAd()

        setupRecyclerView()


        val filtersList = ArrayList<FilterItem>()
        filtersList.add(FilterItem(R.drawable.qq , "19"))
        filtersList.add(FilterItem(R.drawable.ff , "6"))
        filtersList.add(FilterItem(R.drawable.gg , "7"))
        filtersList.add(FilterItem(R.drawable.aa , "1"))
        filtersList.add(FilterItem(R.drawable.bb , "2"))
        filtersList.add(FilterItem(R.drawable.cc , "3"))
        filtersList.add(FilterItem(R.drawable.dd , "4"))
        filtersList.add(FilterItem(R.drawable.ee , "5"))
        filtersList.add(FilterItem(R.drawable.hh , "8"))
        filtersList.add(FilterItem(R.drawable.ii , "9"))
        filtersList.add(FilterItem(R.drawable.jj , "10"))
        filtersList.add(FilterItem(R.drawable.kk , "11"))
        filtersList.add(FilterItem(R.drawable.ll , "12"))
        filtersList.add(FilterItem(R.drawable.mm , "13"))
        filtersList.add(FilterItem(R.drawable.nn , "14"))
        filtersList.add(FilterItem(R.drawable.oo , "15"))
        filtersList.add(FilterItem(R.drawable.pp , "16"))
        filtersList.add(FilterItem(R.drawable.rr , "17"))
        filtersList.add(FilterItem(R.drawable.ss , "18"))
        filtersList.add(FilterItem(R.drawable.tt , "20"))
        filtersList.add(FilterItem(R.drawable.u , "21"))
        filtersList.add(FilterItem(R.drawable.v , "22"))
        filtersList.add(FilterItem(R.drawable.w , "23"))
        filtersList.add(FilterItem(R.drawable.x , "24"))
        filtersList.add(FilterItem(R.drawable.y , "25"))
        filtersList.add(FilterItem(R.drawable.uu , "26"))
        filtersList.add(FilterItem(R.drawable.vv , "27"))
        filtersList.add(FilterItem(R.drawable.ww , "28"))
        filtersList.add(FilterItem(R.drawable.xx , "29"))

        adapter.submitList(filtersList)

        adapter.setOnItemClickListner {
            if (it.styleNum.toInt() >= 21 ){
                Toast.makeText(this , "Will Be Available Soon" , Toast.LENGTH_SHORT).show()

            } else {

                onLoadingFilter()
                lifecycleScope.launch(Dispatchers.IO) {
                    if (isOnline()){
                        val file = File(RealPathUtil.getRealPath(this@MainActivity , imageUri))
                        val imagePart = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val imageFile = MultipartBody.Part.createFormData("myfile" , file.name , imagePart)
                        val style: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull() , it.styleNum)

                        withContext(Dispatchers.Main){
                            try {
                                viewModel.postImage(imageFile , style)

                            }catch (e : Exception){
                                Toast.makeText(this@MainActivity , "No Internet Connection" , Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {
                        withContext(Dispatchers.Main){
                            afterLoadingFilter()
                            Toast.makeText(this@MainActivity , "No Internet Connection" , Toast.LENGTH_SHORT).show()

                        }

                    }
                }

            }
        }



        viewModel.mutableResultImage.observe(this, Observer {
            if (it.result == "Error"){
                afterLoadingFilter()
                Toast.makeText(this , "No Internet Connection" , Toast.LENGTH_SHORT).show()
            }else {

                lifecycleScope.launch(Dispatchers.Main) {
                    launch {
                        Glide.with(this@MainActivity).load("http://144.217.15.17:8000${it.result}").into(imageView)
                    }

                    withContext(Dispatchers.IO){
                        mySlider.setAfterImage("http://144.217.15.17:8000${it.result}")
                        pickedImage = true
                    }

                        launch {
                            Handler().postDelayed(
                                Runnable {
                                    afterLoadingFilter()
                                }, 1000
                            )
                        }
                    }
                }
            })


            pickImg.setOnClickListener {
                ImagePicker.Companion.with(this)
                    .crop()
                    .start()
            }

            backBtn.setOnClickListener {
                val intent = Intent(this , HomeActivity::class.java)
                startActivity(intent)
            }

            shareImg.setOnClickListener {
                if (mySlider.after_image_view_id.drawable == null ){
                    Toast.makeText(this@MainActivity , "Pick Filter First" , Toast.LENGTH_SHORT).show()
                } else {
                    shareImage(imageView.drawable.toBitmap())
                }
            }

            downloadBtn.setOnClickListener {
                if (mySlider.after_image_view_id.drawable == null || imageView.drawable == null){
                    Toast.makeText(this@MainActivity , "Pick Filter First" , Toast.LENGTH_SHORT).show()
                } else {
                    saveToDevice(imageView.drawable.toBitmap())
                }

            }

        }


        private fun saveToDevice(bitmap : Bitmap){

                if (EasyPermissions.hasPermissions(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    val storage = Storage(this@MainActivity)

                    val path =
                        storage.getExternalStorageDirectory(Environment.DIRECTORY_DCIM) + "/Camera/"
                    storage.createDirectory(path);

                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    file.setReadable(true)
                    val outStream = FileOutputStream(file)
                    lifecycleScope.launch(Dispatchers.Default) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                    }
                    Toast.makeText(this@MainActivity, "Saved To Device", Toast.LENGTH_LONG).show()
                    outStream.flush()
                    outStream.close()
            } else {
                EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this,1 , Manifest.permission.WRITE_EXTERNAL_STORAGE )
                        .setRationale("We Need It") // TODO("Write Your Message")
                        .setPositiveButtonText("Okay")
                        .setNegativeButtonText("Cancel")
                        .build()
                )
            }

    }



    private fun shareImage(bitmap: Bitmap) {

        lifecycleScope.launch(Dispatchers.Default) {

            if (EasyPermissions.hasPermissions(this@MainActivity , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                try {
                    val cachePath = File(this@MainActivity.cacheDir, "images")
                    cachePath.mkdirs() // don't forget to make the directory
                    val stream = FileOutputStream("$cachePath/DeepStyle.png") // overwrites this image every time
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val imagePath = File(this@MainActivity.cacheDir, "images")
                    val newFile = File(imagePath, "DeepStyle.png")
                    val contentUri = FileProvider.getUriForFile(this@MainActivity, BuildConfig.APPLICATION_ID + ".provider", newFile)

                    if (contentUri != null) {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        shareIntent.setDataAndType(
                            contentUri,
                            this@MainActivity.contentResolver.getType(contentUri)
                        )
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                        shareIntent.type = "image/*"
                        startActivity(Intent.createChooser(shareIntent, "Deep Style"))
                    }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri = data!!.data
            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("image", uri)
            startActivity(intent)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }


    }


    fun setupRecyclerView(){
        rvFilters.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL,false)
        rvFilters.adapter = adapter
    }



    fun onLoadingFilter(){
        shareImg.isEnabled = false
        downloadBtn.isEnabled = false
        pickImg.isEnabled = false
        rvFilters.isEnabled = false
        backBtn.isEnabled = false
        imageView.visibility = View.GONE
        parentConstraint.alpha = 0.5f
        progressBar.visibility = View.VISIBLE
    }

    fun afterLoadingFilter(){
        shareImg.isEnabled = true
        downloadBtn.isEnabled = true
        pickImg.isEnabled = true
        rvFilters.isEnabled = true
        backBtn.isEnabled = true
        imageView.visibility = View.VISIBLE
        parentConstraint.alpha = 1f
        progressBar.visibility = View.GONE
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    fun loadBannerAd(){
        MobileAds.initialize(this)
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }

    override fun onBackPressed() {
        val intent = Intent(this , HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            (this.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData() // note: it has a return value!
        } else {
            // use old hacky way, which can be removed
            // once minSdkVersion goes above 19 in a few years.
            val packageName = applicationContext.packageName
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear $packageName")
        }
    }




}





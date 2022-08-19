package com.alfrosoft.firebaseimageupload

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alfrosoft.firebaseimageupload.databinding.ActivityStorageBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class StorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorageBinding

    private val pickImage = 100
    private var imageUri: Uri? = null

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent())
    {
        imageUri = it
        binding.imageView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.selectImageBtn.setOnClickListener {

            selectImage()
        }

        binding.uploadImageBtn.setOnClickListener {

            uploadImage()
//            uploadImageToStorage("Bikini")
        }
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading file.........")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        val storageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("Images/KotlinUploads/$fileName.jpg")

            storageRef.putFile(imageUri!!).addOnSuccessListener {

                binding.imageView.setImageURI(null)
                Toast.makeText(this@StorageActivity, "Successfully Uploaded", Toast.LENGTH_SHORT)
                    .show()

                if (progressDialog.isShowing)
                    progressDialog.dismiss()

            }.addOnFailureListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()


                Toast.makeText(this@StorageActivity, "Upload Failed", Toast.LENGTH_SHORT).show()
            }

    }
    private fun selectImage() {

        activityResultLauncher.launch("image/*")

//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//
//        startActivityForResult(intent, pickImage)

    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == pickImage && resultCode == RESULT_OK) {
//
//            imageUri = data?.data!!
//
//            binding.imageView.setImageURI(imageUri)
//        }
//    }
    private fun uploadImageToStorage(filename: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            imageUri?.let {
                FirebaseStorage.getInstance().reference.child("Images/KotlinUploads/$filename.jpg").putFile(it)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StorageActivity, "Successfully uploaded image",
                        Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@StorageActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


}
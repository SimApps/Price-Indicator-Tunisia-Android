package com.amirami.simapp.priceindicatortunisia.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.amirami.simapp.priceindicatortunisia.utils.Functions.commentaire
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getuserid
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentTiketBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*



@AndroidEntryPoint
class TiketFragment : Fragment(R.layout.fragment_tiket) {
    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
 private lateinit   var   myBitmap: Bitmap
 

    lateinit var _binding: FragmentTiketBinding
   

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding  = FragmentTiketBinding.bind(view)


//take photo begin
          //  val sendBtn: TextView = requireView().findViewById<Button>(R.id.send_btn)

                if(commentaire !="") _binding.addcommentaireEdittext.setText(commentaire)


        if(this::myBitmap.isInitialized ) _binding.takeSendPictureBtn.text= getString(R.string.Envoyer)
        else  _binding.takeSendPictureBtn.text= getString(R.string.prendrephotos)

        _binding.takeSendPictureBtn.setSafeOnClickListener {
                //   val bitmap = (previewtakenimg_imvw.drawable as BitmapDrawable).bitmap



            }

        _binding.takeSendPictureBtn.setSafeOnClickListener {
            if( this::myBitmap.isInitialized ){
                    if(commentaire =="" && _binding.addcommentaireEdittext.text.toString()!=""){
                        commentaire = _binding.addcommentaireEdittext.text.toString()
                    }
                    shareImageandText(myBitmap, getuserid(), commentaire)
            }
            else {
                commentaire =_binding.addcommentaireEdittext.text.toString()
                captureImage()
                //   previewtakenimg_imvw.setImageBitmap(myBitmap)
            }

            }
    }




    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)

            if( this::myBitmap.isInitialized ){
                //  myBitmap  = BitmapFactory.decodeResource(requireContext().resources, R.drawable.ic_take_a_picture)


                _binding.takeSendPictureBtn.text= getString(R.string.Envoyer)
                _binding.previewtakenimgImvw.load(myBitmap) {
                    // crossfade(true)
                    // crossfade(500)
                    transformations(RoundedCornersTransformation(16f))
                    error(R.drawable.ic_take_a_picture)
                    diskCachePolicy(CachePolicy.ENABLED)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    placeholder(R.drawable.ic_take_a_picture) //image shown when loading image
                    scale(Scale.FILL)
                    //   transformations(CircleCropTransformation())
                    // transformations(GrayscaleTransformation())
                    //   transformations(BlurTransformation(applicationContext))
                    //  transformations(BlurTransformation(applicationContext, 5f))
                }



                //    previewtakenimg_imvw.setImageBitmap(myBitmap)
            }

            // val previewtakenimg_imvw=findViewById<ImageView>(R.id.previewtakenimg_imvw)

            // photodialogue(this@MainFragment,"yes")
//            previewtakenimg_imvw.setImageBitmap(myBitmap)
        }

        else errorToast(requireContext(), getString(R.string.erreur_permission))
    }

    private fun openSomeActivityForResult(intent: Intent) {
    //    val intent = Intent(requireContext(), requireContext()::class.java)
        resultLauncher.launch(intent)
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach {
            //Log.e("DEBUG", "${it.key} = ${it.value}")
            if (  it.value == true) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile()
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            val photoURI = FileProvider.getUriForFile(
                                requireContext(),
                                getString(R.string.pakege_fileprovider),
                                photoFile!!
                            )
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)


                            openSomeActivityForResult(takePictureIntent)
                            // startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                        }
                    } catch (ex: Exception) {
                        // Error occurred while creating the File
                        DynamicToast.makeError(requireContext(), ex.message.toString(), 9).show()

                    }

                }

            }
            else {
                _binding.takeSendPictureBtn.text= getString(R.string.prendrephotos)
                errorToast(requireContext(), getString(R.string.erreur_permission))
            }
        }
    }


    private fun captureImage() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          //  Functions.PERMISSION_REQUEST_CODE =1

          //  Functions.requestPermission(requireContext())

            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.CAMERA ,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )

        }
        else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            requireContext(),
                            getString(R.string.pakege_fileprovider),
                            photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)


                        openSomeActivityForResult(takePictureIntent)
                        // startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                        errorToast(requireContext(), ex.message.toString())
                }

            }
            else errorToast(requireContext(),getString(R.string.null_intent))
        }

    }


    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }



    private fun shareImageandText(bitmap: Bitmap, user_id: String, message: String) {
        val uri = getmageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share
        intent.putExtra( /* Intent.EXTRA_EMAIL,*/ Intent.EXTRA_TEXT, getString(R.string.propose_liste_produits,message,user_id))

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email) ))


        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Partager_image))

        // setting type to image
        intent.type = "image/png"//"message/rfc822"//"text/plain"

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, getString(R.string.choisir_email)))
    }

    // Retrieving the url to share
    private fun getmageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(requireContext().cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()

            val outputStream = FileOutputStream(photoFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(requireContext(), getString(R.string.pakege_fileprovider), photoFile!!)
        }
        catch (e: java.lang.Exception) {
            e.message?.let { errorToast(requireContext(), it) }
        }
        return uri
    }


    // take photo end
}
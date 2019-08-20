package com.rohitrj.dmme.ui.home.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation

import com.rohitrj.dmme.R
import com.rohitrj.dmme.ui.authentication.IsLoggedIn
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.ByteArrayOutputStream

const val RC_SELECT_IMAGE = 1

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel

        buttonSignOut.setOnClickListener {
            signOut(it)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.onBackPressedDispatcher.addCallback(
            this@ProfileFragment,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(view).navigate(
                        ProfileFragmentDirections.chat()
                    )
                }

            })

        view.apply {
            textViewChangeProfile.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
        }

    }

    private fun signOut(it: View) {
        IsLoggedIn().logOut(context!!)
        Navigation.findNavController(it).navigate(ProfileFragmentDirections.signOut())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
//            selectedImageBytes = outputStream.toByteArray()
//
//
//            GlideApp.with(this)
//                .load(selectedImageBytes)
//                .into(imageView_profile_picture)
//
//            pictureJustChanged = true
        }
    }

}

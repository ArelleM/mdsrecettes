package com.example.mdsrecipe;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;



import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mdsrecipe.Helper.ImageHelper;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;



public class AddRecipeFragment extends Fragment {

    ImageHelper imageHelper = new ImageHelper();
    InputMethodManager imm;
    TextView recipeName;
    TextView author;
    Spinner country;
    EditText description;
    ImageButton camera;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
   // private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;

    public AddRecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        setRetainInstance(true);

        //set userid
        SharedPreferences pref = view.getContext().getSharedPreferences("Login", Activity.MODE_PRIVATE);
        String userID = pref.getString("userID","");
        author = view.findViewById(R.id.txt_Add_Author);
        author.setText(userID);

        //hidding keybord
        imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
        description = view.findViewById(R.id.txt_Add_Description);
        recipeName = (EditText)view.findViewById(R.id.txt_Add_NewName);
        RelativeLayout root = view.findViewById(R.id.root_Add_Recipe);
        root.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(recipeName.getWindowToken(), 0);
            }
        });

        camera = view.findViewById(R.id.btn_Add_Camera);
        mPhotoImageView = view.findViewById(R.id.imgv_Add_Image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        doTakePhotoAction();

                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        doTakeAlbumAction();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(getContext())
                        .setTitle("Sélectionner une image")
                        .setPositiveButton("Prendre une photo", cameraListener)
                        .setNeutralButton("Choisir depuis la gallerie", albumListener)
                        .setNegativeButton("Annuler", cancelListener)
                        .show();
            }
        });
        return view;
    }

    private void doTakePhotoAction()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction()
    {
        // Call to photo gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            //from camera
            if (requestCode == PICK_FROM_CAMERA)
            {
                Bundle extra = data.getExtras();
                if (extra != null) {
                    //Toast.makeText(this.getContext(), "ok", Toast.LENGTH_SHORT).show();
                    Bitmap photo = (Bitmap) extra.getParcelable("data");
                    //Bitmap rphoto = Bitmap.createScaledBitmap(photo, 350, 200, true);
                    //Toast.makeText(getContext(), rphoto.getHeight(),Toast.LENGTH_SHORT).show();
                    mPhotoImageView.setImageBitmap(imageHelper.resizeImage(photo));

                }
            }
            else //from photo gallery
            {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                    mPhotoImageView.setImageBitmap(imageHelper.resizeImage(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        //mPhotoImageView.setImageURI(data.getData());
    }

    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this.getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    1052);

        }
    }
}


package com.elmahousingfinanceug_test.main_Pages;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends BaseAct {
    CircleImageView user_profile_picture;
    TextView userName,userPhone,userEmail;
    AlertDialog aD;
    String profile_Pic_String,mCurrentPhotoPath;
    static final int PHOTO = 971,REQUEST_CAMERA = 333,SELECT_FILE = 222,P_FILE = 62;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gToolBar(getString(R.string.myProfile));

        user_profile_picture = findViewById(R.id.user_profile_picture);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);

        userName.setText(am.getUserName());
        userPhone.setText(am.getUserPhone());
        userEmail.setText(am.getUserEmail());

        profile_Pic_String = am.getUserPic();

        if (!profile_Pic_String.equals("")) {
            byte[] decodedString = android.util.Base64.decode(profile_Pic_String, android.util.Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            user_profile_picture.setImageBitmap(decodedByte);
        }

        user_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {getString(R.string.takePhoto),getString(R.string.chooseFrmLib),getString(R.string.removePic),getString(R.string.cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setCancelable(true)
                        .setTitle(R.string.addProfilepic)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if(items[item].equals(getString(R.string.takePhoto))) {
                                    if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                            && ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            && ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                        ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PHOTO);
                                    } else {
                                        dispatchTakePictureIntent();
                                    }
                                   dialog.dismiss();
                                } else if(items[item].equals(getString(R.string.chooseFrmLib))) {
                                    if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                        ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},P_FILE);
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectFile)),SELECT_FILE);
                                    }
                                    dialog.dismiss();
                                } else if(items[item].equals(getString(R.string.removePic))) {
                                    user_profile_picture.setImageResource(R.drawable.roundaccount);
                                    profile_Pic_String = "";
                                    am.saveUserPic("");
                                    dialog.dismiss();
                                } else if(items[item].equals(getString(R.string.cancel))) {
                                    dialog.dismiss();
                                }
                            }});
                aD = builder.show();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                am.LogThis("File Error " + ex.getMessage());
                am.ToastMessageLong(getApplicationContext(),getString(R.string.errorImage));
            }

            if (photoFile != null) {
                Uri photoURI;
                if (Build.VERSION_CODES.N <= android.os.Build.VERSION.SDK_INT) {
                    photoURI = FileProvider.getUriForFile(this,
                            "com.elmahousingfinanceug.fileprovider",
                            photoFile);
                } else {
                    photoURI = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            } else {
                am.ToastMessageLong(getApplicationContext(),getString(R.string.errorImage));
            }
        } else {
            am.ToastMessageLong(getApplicationContext(),getString(R.string.errorImage));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH).format(new Date());
        String imageFileName = "HFB_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap returnCompressedBitmap (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp = createScaledBitmapKeepingAspectRatio(bmp);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        profile_Pic_String = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
        am.LogThis("encodedImage ► " + profile_Pic_String);
        am.saveUserPic(profile_Pic_String);
        return bmp;
    }

    private Bitmap createScaledBitmapKeepingAspectRatio(Bitmap bitmap) {
        int orgHeight = bitmap.getHeight();
        int orgWidth = bitmap.getWidth();
        // scale to no longer any either side than 75px
        int scaledWidth = (orgWidth >= orgHeight) ? 300 : (int) ((float) 300 * ((float) orgWidth / (float) orgHeight));
        int scaledHeight = (orgHeight >= orgWidth) ? 300 : (int) ((float) 300 * ((float) orgHeight / (float) orgWidth));
        // create the scaled bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        return bitmap;
    }

    public String getPath(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PHOTO:
                    dispatchTakePictureIntent();
                    break;
                case P_FILE:
                    Intent intentFile = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentFile.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentFile,getString(R.string.selectFile)),SELECT_FILE);
                    break;
            }
        } else {
            am.myDialog(this,getString(R.string.alert),getString(R.string.noPermission));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_CAMERA:
                    try {
                        Bitmap bm;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bm = BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);
                        user_profile_picture.setImageBitmap(returnCompressedBitmap(bm));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SELECT_FILE:
                    Uri selectedImageUri = data.getData();
                    String tempPath = getPath(selectedImageUri);
                    Bitmap bm;
                    BitmapFactory.Options bitmapFOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(tempPath, bitmapFOptions);
                    user_profile_picture.setImageBitmap(returnCompressedBitmap(bm));
                    break;
            }
        }  else {
            am.ToastMessageLong(getApplicationContext(),getString(R.string.errorImage));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause(){
        if(aD!=null) aD.dismiss();
        super.onPause();
    }
}

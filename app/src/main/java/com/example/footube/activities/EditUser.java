package com.example.footube.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.footube.BasicClasses.User;
import com.example.footube.R;
import com.example.footube.ViewModel.UserViewModel;
import com.example.footube.localDB.LoggedInUser;

import java.io.ByteArrayOutputStream;

public class EditUser extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int CAMERA_REQUEST_CODE = 22;
    private EditText userName;
    private EditText displayName;
    private EditText password;
    private EditText confirmPassword;
    private ImageView userImage;
    private Button btnUpdate;
    private ImageView ivSelectedImage;
    private User newUser;
    private Uri imageUri;
    private String imagePath;
    private UserViewModel userviewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);

        userName = findViewById(R.id.etUsername);
        displayName = findViewById(R.id.etDisplayName);
        userImage = findViewById(R.id.ivSelectedImage);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etVerifyPassword);
        btnUpdate = findViewById(R.id.btnUpdate);


        userName.setText(LoggedInUser.getInstance().getUser().getUsername());
        displayName.setText(LoggedInUser.getInstance().getUser().getDisplayName());
        userImage.setImageBitmap(MoviesList.base64ToBitmap(LoggedInUser.getInstance().getUser().getImage()));

        // Define user view model
        userviewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Button btnUploadPicture = findViewById(R.id.btnUploadPicture);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(v);
                showPictureDialog();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user1 = LoggedInUser.getInstance().getUser();
                String userName1 = !userName.getText().toString().isEmpty() ? userName.getText().toString() : user1.getUsername();
                String displayName1 = !displayName.getText().toString().isEmpty() ? displayName.getText().toString() : user1.getDisplayName();
                String password1 = !password.getText().toString().isEmpty() ? password.getText().toString() : user1.getPassword();
                String confirmPassword1 = !confirmPassword.getText().toString().isEmpty() ? confirmPassword.getText().toString() : user1.getPassword();
                String image = imagePath != null ? imagePath : user1.getImage();

                newUser = new User(userName1, displayName1, password1, image, confirmPassword1);

                userviewModel.updateUser(newUser);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap userphoto = (Bitmap) data.getExtras().get("data");
                ivSelectedImage.setImageBitmap(userphoto);
                imagePath = bitmapToBase64(userphoto);
                // Save the bitmap to a Uri if needed
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                imageUri = data.getData();
                ivSelectedImage.setImageURI(imageUri);
                Bitmap userphoto = getBitmapFromImageView(ivSelectedImage);
                imagePath = bitmapToBase64(userphoto);
            }
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



    private Bitmap getBitmapFromImageView(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void closeKeyboard(View view) {
        // Check if no view has focus
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        } else {
            Toast.makeText(this, "No view has focus", Toast.LENGTH_SHORT).show();
        }
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    private void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditUser.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }
}
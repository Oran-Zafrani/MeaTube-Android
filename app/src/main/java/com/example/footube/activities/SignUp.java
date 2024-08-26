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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.footube.BasicClasses.User;
import com.example.footube.R;
import com.example.footube.Repository.UserRepository;
import com.example.footube.ViewModel.UserViewModel;
import com.example.footube.managers.UserManager;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int CAMERA_REQUEST_CODE = 22;
    private Uri imageUri;
    private String imagePath;
    private ImageView ivSelectedImage;

    UserManager userManager = UserManager.getInstance(); // Get the singleton instance
    private UserViewModel userviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userviewModel = new ViewModelProvider(this).get(UserViewModel.class);
        //userviewModel = new UserViewModel();

        Button btnUploadPicture = findViewById(R.id.btnUploadPicture);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(v);
                showPictureDialog();
            }
        });

        Intent SignInIntent = new Intent(this, SignIn.class);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.etUsername);
                EditText displayName = findViewById(R.id.etDisplayName);
                EditText password = findViewById(R.id.etPassword);
                EditText verifypassword = findViewById(R.id.etVerifyPassword);

                String user = username.getText().toString();
                String display = displayName.getText().toString();
                String pass = password.getText().toString();
                String pass2 = verifypassword.getText().toString();
                String image = imagePath != null ? imagePath : "default_profile.jpg";

                explaintouser(user,display,pass,imagePath,pass2);

                if (userconfirm(user,display,pass,imagePath, pass2)){
                    //userManager.addUser(user, display, pass, imagePath);
                    User newUser = new User(user, display, pass, imagePath, pass2);
                    userviewModel.addUser(newUser);
                    startActivity(SignInIntent);
                }
            }
        });
    }

    private void explaintouser(String user,String diaplay, String pass, String image, String pass2){
        TextView errorTextView = findViewById(R.id.errorTextView);
        String error = "";
        if (!allempty(user, diaplay,pass,image)){
            error += "It is necessary to fill all the fields.\n";
        }
        if (!passlen(pass)){
            error += "The password must contain at least 8 characters.\n";
        }
        if (!rightletters(pass)){
            error += "Your password must contain at least one letter (a-z or A-Z).\n"
                    + "Your password must contain at least one numeric digit (0-9).\n"
            + " Your password must contain at least one special character among these: !@#$%^&*(),.?\":{}|<>.\n";
        }
        if (!samepass(pass,pass2)){
            error += "Passwords are not the same.\n";
        }
        if (userManager.userExists(user)){
            error += "This user is already exist.\n";
        }
        errorTextView.setText(error);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private boolean samepass (String pass1, String pass2){
        return pass1.equals(pass2);
    }

    private boolean allempty(String user,String diaplay, String pass, String image){
        return !user.isEmpty() && !diaplay.isEmpty() && !pass.isEmpty() && (image != null);
    }

    private boolean rightletters(String password) {
        // Regular expression to check if the password contains both letters and special characters
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\\$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(password).matches();
    }

    private boolean passlen(String pass){
        // Password must be at least 8 characters long
        return pass.length() >= 8;
    }

    private boolean userconfirm(String user,String diaplay, String pass, String image, String pass2){
        boolean notempty = allempty(user,diaplay,pass,image);
        return !userManager.userExists(user) && notempty && passlen(pass) && rightletters(pass) && samepass(pass, pass2);
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

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    private void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SignUp.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
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

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
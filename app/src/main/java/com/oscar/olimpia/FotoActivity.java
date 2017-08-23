package com.oscar.olimpia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.oscar.olimpia.Modelo.Usuario;
import com.oscar.olimpia.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FotoActivity extends AppCompatActivity {

    @Bind(R.id.btnCamara)
    ImageButton btnCamara;
    @Bind(R.id.btnGaleria)
    ImageButton btnGaleria;
    @Bind(R.id.btnSiguiente)
    Button btnSiguiente;
    @Bind(R.id.imgFoto)
    ImageView imgFoto;

    private File output = null;
    private Usuario usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra(Constants.extraActivity.pojoUsuario);
        //Log.e("foto", usuario.toString());

    }

    @OnClick({R.id.btnCamara, R.id.btnGaleria, R.id.btnSiguiente})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCamara:
                permissionCameraGallery();
                break;
            case R.id.btnGaleria:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, Constants.codePhoto.GALLERY_REQUEST);
                break;
            case R.id.btnSiguiente:
                usuario.toString();
                Intent intent = new Intent(this, MapLocationActivity.class);
                intent.putExtra(Constants.extraActivity.pojoUsuario, usuario);
                startActivity(intent);
                break;
        }
    }

    private void permissionCameraGallery() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED))  {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }else if((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }else if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
        else {
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //Creamos una carpeta en la memeria del terminal
            File imagesFolder = new File(
                    Environment.getExternalStorageDirectory(), "Olimpia");
            imagesFolder.mkdirs();
            //añadimos el nombre de la imagen
            File image = new File(imagesFolder, "usuario.jpg");
            Uri uriSavedImage = Uri.fromFile(image);
            //Le decimos al Intent que queremos grabar la imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            //Lanzamos la aplicacion de la camara con retorno (forResult)
            startActivityForResult(cameraIntent, Constants.codePhoto.CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.codePhoto.CAMERA_REQUEST && resultCode == RESULT_OK) {

            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()+
                            "/Olimpia/"+"usuario.jpg");


            int nh = (int) ( bMap.getHeight() * (512.0 / bMap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bMap, 512, nh, true);
            usuario.setFoto("url foto");
            imgFoto.setImageBitmap(scaled);

        }else if(requestCode == Constants.codePhoto.GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            Log.e("uri2", selectedImage.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                usuario.setFoto("url foto");
                Log.e("bit", usuario.getFoto());
                imgFoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

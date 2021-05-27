package com.example.image_encrypt_decrypt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button btn, bt, bt3;
    private static final int PICK_IMAGE = 100;
    private static final int RESULT_LOAD_IMAGE = 1;
    Uri imageUri;
    TextView t1;
    String s, path;
    File f1, infile;
    Bitmap b;
    int status = 0;
    ProgressBar pb;
    ProgressDialog pd;
    public static Cipher cipher;
    public static KeyGenerator keyGen;
    public static Key key;
    FileOutputStream fos, fileop;
    String pass="imageencryptdecrypt";
    byte[] keyy=pass.getBytes("UTF-8");
    MessageDigest sha=MessageDigest.getInstance("SHA-1");
    Key k=new SecretKeySpec(Arrays.copyOf(sha.digest(pass.getBytes("UTF-8")),16),"AES");       //javax.crypto.spec.SecretKeySpec@fffe83f8

        static {
        try {
            cipher = Cipher.getInstance("AES");
           // keyGen = KeyGenerator.getInstance("AES");
           // key = keyGen.generateKey();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    SecureRandom rnd = new SecureRandom();

    IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));

    public MainActivity() throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.i1);
        btn = (Button) findViewById(R.id.b1);
        bt = (Button) findViewById(R.id.b2);
        bt3 = (Button) findViewById(R.id.b3);
        pb = (ProgressBar) findViewById(R.id.loading);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pb.setVisibility(v.VISIBLE);
                System.out.println(k);
                openGallery();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AsyncTaskExample asyncTask;
                asyncTask = new AsyncTaskExample();
                //asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                asyncTask.execute("");
                // if(asyncTask!=null && asyncTask.getStatus() !=AsyncTaskExample.Status.FINISHED)
                // asyncTask.cancel(true);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AsyncTaskExample1 asyncTask;
                asyncTask = new AsyncTaskExample1();
                //asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                asyncTask.execute("");
                // if(asyncTask!=null && asyncTask.getStatus() !=AsyncTaskExample.Status.FINISHED)
                // asyncTask.cancel(true);
            }
        });

    }

    class AsyncTaskExample extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            System.out.println(this.getStatus());
            //if(this.getStatus() ==AsyncTaskExample.Status.FINISHED)
            // this.cancel(true);
            System.out.println(s+" "+(s==null));
            if(s!=null) {  // pb.setVisibility(View.VISIBLE);
                pd = new ProgressDialog(MainActivity.this);
                 pd.setMessage("Please wait...Encrypting");
                 pd.setIndeterminate(false);
                 pd.setCancelable(false);
                 pd.show();
            }
            super.onPreExecute();

            //pd = new ProgressDialog(MainActivity.this);
           // pd.setMessage("Please wait...It is Encrypting");
           // pd.setIndeterminate(false);
           // pd.setCancelable(true);
           // pd.show();
        }

        @Override
        protected String doInBackground(String... str) {
            if(s==null)
                return "no";
           try {
               status = 1;
               File f = new File(s);
               //System.out.println(key + "    " + key.getClass().getName());
               cipher.init(Cipher.ENCRYPT_MODE, k);
               CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(f), cipher);
               Random ran = new Random();
               int j = ran.nextInt(1000);
               String p = Environment.getExternalStorageDirectory() + "/Pictures/encrypt.jpg";
               f1 = new File(p);
               System.out.println(f1.getName());
               //f1.createNewFile();
               fos = new FileOutputStream(f1);
               int i;

               while ((i = cipherIn.read()) != -1) {
                   System.out.println("encrypt inprocess");
                   fos.write(i);
               }

               //Bitmap b =  ((BitmapDrawable) img.getDrawable()).getBitmap();


               // fos.flush();
               //fos.close();
               //cipherIn.close();
           }catch(Exception e){}
            return "Encrypted";
        }

        @Override
        protected void onPostExecute(String sg) {
            System.out.println(sg);
            if(sg=="no")
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("File not found");
                ab.setMessage("Upload File");
                ab.setPositiveButton("OK", null);
                ab.show();
                //onCancelled();
            }
            else {
                if (b.compress(CompressFormat.JPEG, 100, fos)) {
                    System.out.println("saved");
                    //img.setImageBitmap(b);
                    try {
                        MediaStore.Images.Media.insertImage(getContentResolver(), f1.getAbsolutePath(), f1.getName(), f1.getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //String sv= MediaStore.Images.Media.insertImage(getContentResolver(), f1.getAbsolutePath(), UUID.randomUUID().toString()+".jpg", f1.getName());
                    //System.out.println(sv);
                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                    ab.setTitle("Image Encrypted");
                    ab.setMessage("saved in gallery as : " + f1.getName());
                    ab.setPositiveButton("OK", null);
                    ab.show();
                }
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("success");
                System.out.println(this.getStatus());

                this.cancel(true);
                System.out.println(this.getStatus());
               /* if(status==1)
                {
                    this.cancel(true);
                    onCreate(new Bundle());
                }*/
                onCancelled();
            }
            }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //pb.setVisibility(GONE);
            pd.dismiss();
        }


    }

    class AsyncTaskExample1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            System.out.println(this.getStatus());
            //if(this.getStatus() ==AsyncTaskExample.Status.FINISHED)
            // this.cancel(true);
            if(s!=null)
            {
              //  pb.setVisibility(View.VISIBLE);
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Please wait...Decrypting");
                pd.setIndeterminate(false);
                pd.setCancelable(false);
                pd.show();
            }
            super.onPreExecute();
            /*
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait...It is Decrypting");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();*/
        }

        @Override
        protected String doInBackground(String... str) {
            if(s==null)
                return "no";
            try {

                File fi = new File(s);
                // String v=fi.getName().split("\\.")[0];
                //System.out.println(v);
                cipher.init(Cipher.DECRYPT_MODE, k);
                CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(fi), cipher);
                Random ran = new Random();
                int i = ran.nextInt(1000);
                String p = Environment.getExternalStorageDirectory() + "/Pictures/decrypt.jpg";
                infile = new File(p);
                System.out.println(infile.getAbsolutePath());
                fileop = new FileOutputStream(infile);

                int j;
                try {
                    while ((j = cipherIn.read()) != -1) {
                        System.out.println("decrypt inprocess");
                        fileop.write(j);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                //Bitmap b =  ((BitmapDrawable) img.getDrawable()).getBitmap();


                // fos.flush();
                //fos.close();
                //cipherIn.close();
            } catch (Exception e) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("File not found");
                ab.setMessage("Upload File");
                ab.setPositiveButton("OK", null);
                ab.show();
            }
            return "Encrypted";
        }

        @Override
        protected void onPostExecute(String sg) {
            System.out.println(sg);
            if (sg == "no") {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("File not found");
                ab.setMessage("Upload File");
                ab.setPositiveButton("OK", null);
                ab.show();
                //onCancelled();
            } else {
                if (b.compress(CompressFormat.JPEG, 0, fileop)) {
                    System.out.println("saved");
                    try {
                        MediaStore.Images.Media.insertImage(getContentResolver(), infile.getAbsolutePath(), infile.getName(), infile.getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                    ab.setTitle("Image decrypted");
                    ab.setMessage("saved in gallery as : " + infile.getName());
                    ab.setPositiveButton("OK", null);
                    ab.show();
                }

                try {
                    fileop.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileop.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onCancelled();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            //pb.setVisibility(GONE);
            pd.dismiss();
        }


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); //MediaStore.Images.Media.INTERNAL_CONTENT_URI
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);//PICK_IMAGE
        //Intent gallery = new Intent(Intent.ACTION_GET_CONTENT,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        String[] path = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentURI, path, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(path[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            try {
                InputStream imgstrm = getContentResolver().openInputStream(imageUri);
                b = BitmapFactory.decodeStream(imgstrm);
                img.setImageBitmap(b);
                img.setDrawingCacheEnabled(true);
                //img.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                //       View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                // img.layout(0,0,img.getMeasuredWidth(),img.getMeasuredHeight());
                //img.buildDrawingCache(true);
                // b=((BitmapDrawable)img.getDrawable()).getBitmap();
                b = Bitmap.createBitmap(img.getDrawingCache());
                img.setDrawingCacheEnabled(false);
                System.out.println(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(e);
            }


            System.out.println(imageUri);
            //img.setImageURI(imageUri);
            s = getRealPathFromURI(imageUri);
            System.out.println("path  :" + s);
            System.out.println(Environment.getExternalStorageDirectory());
            if (s != null || imageUri != null) {
                Toast tm = Toast.makeText(this, "File Uploaded Successfully", Toast.LENGTH_LONG);
                tm.show();
            } else {
                Toast tm = Toast.makeText(this, "Error in Uploading", Toast.LENGTH_LONG);
                tm.show();
            }
            File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                System.out.println("folder created");
            } else
                System.out.println("folder not created");

            /* File f=new File(s);
             if(f.exists())
                 System.out.println("true");
             else
                 System.out.println("false");*/
            // s="/Internal storage"+s.substring(19);
            //System.out.println(s);
            //Bitmap b= BitmapFactory.decodeFile(s);
            // img.setImageBitmap(b);

        }
    }
}

/*

    public void encrypt(View view) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException
    {

        pb.setVisibility(view.VISIBLE);
        //ProgressDialog pd;
        //pd=new ProgressDialog(this);
        //pd.setMessage("Wait...");
        //pd.show();
            try{

            File f = new File(s);
            System.out.println(key + "    " + key.getClass().getName());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(f), cipher);
            Random ran = new Random();
            int j = ran.nextInt(1000);
            String p = Environment.getExternalStorageDirectory() + "/AES/encrypt.jpg";
            f1 = new File(p);
            System.out.println(f1.getName());
            //f1.createNewFile();
            FileOutputStream fos = new FileOutputStream(f1);
            int i;

            while ((i = cipherIn.read()) != -1) {
                System.out.println("encrypt inprocess");
                fos.write(i);
            }
            pb.setVisibility(GONE);

            //Bitmap b =  ((BitmapDrawable) img.getDrawable()).getBitmap();
            if (b.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                System.out.println("saved");
                //img.setImageBitmap(b);
                MediaStore.Images.Media.insertImage(getContentResolver(), f1.getAbsolutePath(), f1.getName(), f1.getName());
                //String sv= MediaStore.Images.Media.insertImage(getContentResolver(), f1.getAbsolutePath(), UUID.randomUUID().toString()+".jpg", f1.getName());
                //System.out.println(sv);
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("Image Encrypted");
                ab.setMessage("saved in gallery as : " + f1.getName());
                ab.setPositiveButton("OK", null);
                ab.show();
            }

            fos.flush();
            fos.close();
            cipherIn.close();
            System.out.println("success");
        } catch (Exception e) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("File not found");
            ab.setMessage("Upload File");
            ab.setPositiveButton("OK", null);
            ab.show();
        }
    }


    public void decrypt(View view) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        //EditText et=(EditText)findViewById(R.id.t2);
        //String str=Environment.getExternalStorageDirectory()+String.valueOf(et.getText());
        //System.out.println(str);
        //File df=new File(str);
        //System.out.println(key);
        try{

        File fi = new File(s);
        // String v=fi.getName().split("\\.")[0];
        //System.out.println(v);
        cipher.init(Cipher.DECRYPT_MODE, key);
        CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(fi), cipher);
        Random ran = new Random();
        int i = ran.nextInt(1000);
        String p = Environment.getExternalStorageDirectory() + "/AES/decrypt.jpg";
        File infile = new File(p);
        System.out.println(infile.getAbsolutePath());
        FileOutputStream fileop = new FileOutputStream(infile);

        int j;
        try {
            while ((j = cipherIn.read()) != -1) {
                System.out.println("decrypt inprocess");
                fileop.write(j);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
            pb.setVisibility(GONE);

        System.out.println("success");
        if (img.getDrawable() == null) {
            System.out.println("null");
        } else {
            System.out.println(" not null");
        }
        if (fileop == null) {
            System.out.println("null");
        } else {
            System.out.println(" not null");
        }
        //Bitmap bi= BitmapFactory.decodeFile(path);
        if (b == null) {
            System.out.println("null");
        } else {
            System.out.println(" not null");
        }
        //BitmapFactory.decodeResource(getResources(), R.id.i1);//((BitmapDrawable) img.getDrawable()).getBitmap();


        if (b.compress(Bitmap.CompressFormat.JPEG, 0, fileop)) {
            System.out.println("saved");
            MediaStore.Images.Media.insertImage(getContentResolver(), infile.getAbsolutePath(), infile.getName(), infile.getName());
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("Image decrypted");
            ab.setMessage("saved in gallery as : " + infile.getName());
            ab.setPositiveButton("OK", null);
            ab.show();
        }

        fileop.flush();
        fileop.close();
        cipherIn.close();
    }
    catch (Exception e){
        AlertDialog.Builder ab= new AlertDialog.Builder(this);
        ab.setTitle("File not found");
        ab.setMessage("Upload File");
        ab.setPositiveButton("OK",null);
        ab.show();
    }
    }
}*/
package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.async_tasks.PostRequestSenderAsyncTask;


import org.apache.http.entity.mime.HttpMultipartMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by ZahARin on 01.05.2016.
 */
public class EditCardsActivity extends AppCompatActivity {

    private EditText etQuestion;
    private EditText etAnswer;
    private Deck deck;
    AlertDialog alert;

    ImageView ivFace;
    ImageView ivBack;

    private String imagePath1 = "";
    private String imagePath2 = "";
    private File fileToSend1;
    private File fileToSend2;
    public static int SELECT_PICTURE_ONE = 1;
    public static int TAKE_PHOTO_ONE = 11;
    public static int SELECT_PICTURE_TWO = 2;
    public static int TAKE_PHOTO_TWO = 22;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);

        Button btnSave = (Button) findViewById(R.id.CardEditActivity_btnSave);
        btnSave.setOnClickListener(saveListener);
        etQuestion = (EditText) findViewById(R.id.CardEditActivity_etFace);
        etAnswer = (EditText) findViewById(R.id.CardEditActivity_etBack);

        initToolbar();
        initImageButtons();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.CardEditActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initImageButtons() {
        ivFace = (ImageView) findViewById(R.id.CardEditActivity_ivFace);
        ivFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditCardsActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                view.findViewById(R.id.DialogLayout_btnTakePhoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoIntent, TAKE_PHOTO_ONE);
                    }
                });
                view.findViewById(R.id.DialogLayout_btnChoosePicture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent imageIntent = new Intent();
                        imageIntent.setType("image/*");
                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(imageIntent, getString(R.string.select_image)), SELECT_PICTURE_ONE);
                    }
                });
                dialog.setView(view);
                dialog.setTitle(R.string.image);
                alert = dialog.create();
                alert.show();
            }
        });
        ivBack = (ImageView) findViewById(R.id.CardEditActivity_ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditCardsActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                view.findViewById(R.id.DialogLayout_btnTakePhoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoIntent, TAKE_PHOTO_TWO);
                    }
                });
                view.findViewById(R.id.DialogLayout_btnChoosePicture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent imageIntent = new Intent();
                        imageIntent.setType("image/*");
                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(imageIntent, getString(R.string.select_image)), SELECT_PICTURE_TWO);
                    }
                });
                dialog.setView(view);
                dialog.setTitle(R.string.image);
                alert = dialog.create();
                alert.show();
            }
        });
    }

    Button.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String question = etQuestion.getText().toString();
            String answer = etAnswer.getText().toString();
            AlertDialog.Builder alDialog = new AlertDialog.Builder(EditCardsActivity.this);
            alDialog.setPositiveButton(R.string.ok, null);
            alDialog.setTitle(R.string.form_validation);
            if (question.equals("")) {
                alDialog.setMessage(R.string.dm_err_msg_face_cant_be_empty);
                alDialog.show();
            } else if (answer.equals("")) {
                alDialog.setMessage(R.string.dm_err_msg_back_cant_be_empty);
                alDialog.show();
            } else {
                Formatter urlCreator = new Formatter();
                urlCreator.format(getString(R.string.url_add_deck_card),
                        Tools.createUID(),
                        deck.getUid(),
                        DataManager.getUserToken(),
                        etQuestion.getText().toString(),
                        etAnswer.getText().toString());
                String url = urlCreator.toString();


//                HttpPost uploadFile = new HttpPost(url);
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.addBinaryBody(getString(R.string.image_parameter),
//                        new File(fileToSend1.getPath()), ContentType.APPLICATION_OCTET_STREAM, fileToSend1.getName());
//                HttpEntity multipart = builder.build();
//                uploadFile.setEntity(multipart);


                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Log.w("MyWarning", "url = " + params[0]);
                            Log.w("MyWarning", "file name = " + fileToSend1.getName());
                            Tools.uploadMultipart(params[0],fileToSend1,fileToSend2);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(url);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bm = BitmapFactory.decodeFile(fileToSend1.getPath(), options);
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] imageBytes = baos.toByteArray();
//                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//                Log.d("MyLogs", "encoded Image = " + encodedImage);


                PostRequestSenderAsyncTask prs = new PostRequestSenderAsyncTask();
                prs.execute(url);
//                String res = null;
//                try {
//                    res = prs.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    JSONObject result = new JSONObject(res).getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonStatus);
//                    int code = result.getInt(Tools.jsonStatusCode);
//                    if (code == Tools.errOk) {
//                        Toast.makeText(EditCardsActivity.this, R.string.new_card_created, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(EditCardsActivity.this, R.string.something_wrong_try_one, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                urlCreator.close();
//                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            alert.cancel();
            if (requestCode == SELECT_PICTURE_ONE) {
                Uri uri = data.getData();
                imagePath1 = uri.getPath();
                fileToSend1 = new File(String.valueOf(uri));
            }
            if (requestCode == SELECT_PICTURE_TWO) {
                Uri uri = data.getData();
                imagePath2 = uri.getPath();
                fileToSend2 = new File(String.valueOf(uri));
            }
            if (requestCode == TAKE_PHOTO_ONE) {
                try {
                    File file = createImageFile();
                    FileOutputStream os = new FileOutputStream(file);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivFace.setImageBitmap(bitmap);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                    imagePath1 = file.getPath();
                    fileToSend1 = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == TAKE_PHOTO_TWO) {
                try {
                    File file = createImageFile();
                    FileOutputStream os = new FileOutputStream(file);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivBack.setImageBitmap(bitmap);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                    imagePath2 = file.getPath();
                    fileToSend2 = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File createImageFile() throws IOException {
        //SD Card path
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath() + "/MyFcImages/");
        if (!dir.exists()) {
            if (dir.mkdirs())
                Toast.makeText(EditCardsActivity.this, String.format(getString(R.string.dir_created), dir.getAbsolutePath()), Toast.LENGTH_LONG).show();
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        return File.createTempFile("My_FC_" + timeStamp, ".jpg", dir);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

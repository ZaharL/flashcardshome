package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.DownloadImageTask;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by ZahARin on 01.05.2016.
 */
public class EditCardsActivity extends AppCompatActivity {

    private EditText etQuestion;
    private EditText etAnswer;
    private Deck deck;
    private Card card;
    private String userToken;
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
    public static int NECESSARY_WIDTH = 512;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);
        card = getIntent().getParcelableExtra(Tools.cardsExtraTag);

        userToken = DataManager.getUserToken();

        Button btnSave = (Button) findViewById(R.id.CardEditActivity_btnSave);
        btnSave.setOnClickListener(saveListener);
        etQuestion = (EditText) findViewById(R.id.CardEditActivity_etFace);
        etAnswer = (EditText) findViewById(R.id.CardEditActivity_etBack);
        ivFace = (ImageView) findViewById(R.id.CardEditActivity_ivFace);
        ivBack = (ImageView) findViewById(R.id.CardEditActivity_ivBack);

        if (card != null) initCard();

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

    private void initCard() {
        new DownloadImageTask(ivFace).execute(String.format(getString(R.string.url_host), card.getImagePath() + card.getImage1URL()));
        new DownloadImageTask(ivBack).execute(String.format(getString(R.string.url_host), card.getImagePath() + card.getImage2URL()));
        etQuestion.setText(card.getQuestion());
        etAnswer.setText(card.getAnswer());
    }

    private void initImageButtons() {

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
                if (card != null) {
                    try {
                        urlCreator.format(getString(R.string.url_update_deck_card),
                                card.getUid(),
                                deck.getUid(),
                                userToken,
                                URLEncoder.encode(etQuestion.getText().toString(), "UTF-8"),
                                URLEncoder.encode((etAnswer.getText().toString()), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        urlCreator.format(getString(R.string.url_add_deck_card),
                                Tools.createUID(),
                                deck.getUid(),
                                userToken,
                                URLEncoder.encode(etQuestion.getText().toString(), "UTF-8"),
                                URLEncoder.encode(etAnswer.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                String url = urlCreator.toString();

                new AsyncTask<String, Void, String>() {
                    Context c;
                    @Override
                    protected void onPreExecute() {
                         c = EditCardsActivity.this;
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Tools.uploadMultipart(params[0], fileToSend1, fileToSend2);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(c, R.string.card_added, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }.execute(url);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (alert != null)
                alert.cancel();
            if (requestCode == SELECT_PICTURE_ONE) {
                File file;
                try {
                    file = createImageFile();
                    FileOutputStream os = new FileOutputStream(file);

                    Uri uri = data.getData();
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    try {
                        int nh = (int) (bitmap.getHeight() * ((float) NECESSARY_WIDTH / bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, NECESSARY_WIDTH, nh, true);
                        ivFace.setDrawingCacheEnabled(false);
                        ivFace.setImageBitmap(scaled);
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    } catch (NullPointerException e) {
                        Toast.makeText(this, R.string.image_violated, Toast.LENGTH_LONG).show();
                    }
                    imagePath1 = file.getPath();
                    fileToSend1 = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == SELECT_PICTURE_TWO) {
                try {
                    File file = createImageFile();
                    FileOutputStream os = new FileOutputStream(file);

                    Uri uri = data.getData();
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    try {
                        int nh = (int) (bitmap.getHeight() * ((float) NECESSARY_WIDTH / bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, NECESSARY_WIDTH, nh, true);
                        ivBack.setDrawingCacheEnabled(false);
                        ivBack.setImageBitmap(scaled);
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    } catch (NullPointerException e) {
                        Toast.makeText(this, R.string.image_violated, Toast.LENGTH_LONG).show();
                    }
                    imagePath2 = file.getPath();
                    fileToSend2 = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == TAKE_PHOTO_ONE) {
                try {
                    File file = createImageFile();
                    FileOutputStream os = new FileOutputStream(file);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivFace.setImageBitmap(bitmap);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
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

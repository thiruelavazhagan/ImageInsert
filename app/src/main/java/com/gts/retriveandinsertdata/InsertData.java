package com.gts.retriveandinsertdata;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class InsertData extends AppCompatActivity {

    Button SelectImageGallery, UploadImageServer,viewData;
    ImageView imageView;
    EditText imageName;
    RequestQueue requestQueue;
    String FirstNameHolder;
    ProgressDialog progressDialog;
    String HttpUrl = "http://192.168.106.2/GTS/InsertImage/img_upload_to_server.php";
    Bitmap bitmap;
    public Boolean imageChanged = false;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageName = (EditText)findViewById(R.id.editTextImageName);
        SelectImageGallery = (Button)findViewById(R.id.buttonSelect);
        UploadImageServer = (Button)findViewById(R.id.buttonUpload);
        viewData = (Button)findViewById(R.id.button);
        // Creating Volley newRequestQueue .

        requestQueue = Volley.newRequestQueue(InsertData.this);
        progressDialog = new ProgressDialog(InsertData.this);

    /*   final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        imageName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InsertData.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

       imageName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(InsertData.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        imageName.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        // Adding click listener to button.
        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageName.getText().toString().trim().equals("")) {
                    Toast.makeText(InsertData.this, "set name", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (!imageChanged) {
                    Toast.makeText(InsertData.this, "set image", Toast.LENGTH_SHORT).show();

                    }


                else {

                    // Showing progress dialog at user registration time.
                    progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                    progressDialog.show();

                    // Calling method to get value from EditText.
                    GetValueFromEditText();

                    ByteArrayOutputStream byteArrayOutputStreamObject;
                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InsertData.this, ServerResponse, Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InsertData.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("image_name", FirstNameHolder);
                            params.put("image", ConvertImage);

                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(InsertData.this);

                    requestQueue.add(stringRequest);
                    imageName.setText("");
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                    imageChanged = false;

                }
            }
        }); */

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(InsertData.this,RetrieveData.class);
               startActivity(i);
            }
        });

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        imageName.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        super.onActivityResult(RC, RQC, I);
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
            Uri uri = I.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                imageChanged = true;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetValueFromEditText(){
        FirstNameHolder = imageName.getText().toString().trim();
    }

}
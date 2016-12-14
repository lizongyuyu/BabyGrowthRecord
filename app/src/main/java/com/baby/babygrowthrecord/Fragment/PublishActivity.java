package com.baby.babygrowthrecord.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.baby.babygrowthrecord.MainActivity.BabyMainActivity;
import com.baby.babygrowthrecord.R;
import com.baby.babygrowthrecord.user.UserAlbum;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.utils.HttpUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PublishActivity extends AppCompatActivity {


    private Context context;
    private Button button1;
    private Button button2;
    private ImageView imageView_camera;
    private ImageView image_view;
    private EditText editText;
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private static final String TAG = "MyActivity";

    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        imageView_camera = (ImageView)findViewById(R.id.income_camera);
        image_view = (ImageView)findViewById(R.id.image_view);
        editText = (EditText)findViewById(R.id.edit_text);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
                builder.setTitle("你确定要取消发布并保存为草稿？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                 //这里添加点击确定后的逻辑
                                Toast.makeText(PublishActivity.this, "已保存到草稿", Toast.LENGTH_SHORT).show();
                                finish();
                                //Intent intent = new Intent(PublishActivity.this, BabyMainActivity.class);
                                //startActivity(intent);
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }
                        });
                builder.create().show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
               /* String text = editText.getText().toString();
                String image = image_view.toString();
                Map<String,String> params = new HashMap<String, String>();
                params.put("context",text);
                params.put("image",image);
                editText.setText(HttpUtils.submitPostData(params, "utf-8"));
                //使用HttpPost发送请求
                HttpPost httpPost = new HttpPost("http://169.254.76.180:8080/circle/uploading");
                //使用NameValuePaira保存请求中所需要传入的参数
                List<NameValuePair> paramas = new ArrayList<NameValuePair>();
                paramas.add(new BasicNameValuePair("friend_content", "friend_photo"));
                try{
                    HttpResponse httpResponse;
                    //将NameValuePair放入HttpPost请求体中
                    httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
                    //执行HttpPost请求
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200)
                    {
                        String s = EntityUtils.toString(httpResponse.getEntity());
                    }
                } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch bloc
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch bloc
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch bloc
                    e.printStackTrace();
                }*/
            }
        });
        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(PublishActivity.this)
                        .setPositiveButton(R.string.dialog_p_btn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i(TAG, "相册");
                                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                                        startActivityForResult(intent, ALBUM_REQUEST_CODE);
                                    }
                                })
                        .setNegativeButton(R.string.dialog_n_btn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i(TAG, "相机");
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                    }
                                }).create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i(TAG, "相册，开始裁剪");
                Log.i(TAG, "相册 [ " + data + " ]");
                if (data == null) {
                    return;
                }
                doCrop(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                Log.i(TAG, "相机, 开始裁剪");
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                doCrop(Uri.fromFile(picture));
                break;
            case CROP_REQUEST_CODE:
                Log.i(TAG, "相册裁剪成功");
                Log.i(TAG, "裁剪以后 [ " + data + " ]");
                if (data == null) {
                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    image_view.setImageBitmap(photo); //把图片显示在ImageView控件上
                }
                break;
            default:
                break;
        }
    }
    private void doCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }
    /**
     * 判断sdcard卡是否可用
     *
     * @return 布尔类型 true 可用 false 不可用
     */
    private boolean isSDCardCanUser() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /*public static String submitPostData(Map<String, String> params, String encode){
        byte[] data = getRequestData(params, encode).toString().getBytes();   //获得请求体
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);        //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
                 StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
                try {
                         for(Map.Entry<String, String> entry : params.entrySet()) {
                                 stringBuffer.append(entry.getKey())
                                             .append("=")
                                             .append(URLEncoder.encode(entry.getValue(), encode))
                                             .append("&");
                             }
                         stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 return stringBuffer;
             }
    public static String dealResponseResult(InputStream inputStream) {
                 String resultData = null;      //存储处理结果
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 byte[] data = new byte[1024];
                 int len = 0;
                 try {
                         while((len = inputStream.read(data)) != -1) {
                                 byteArrayOutputStream.write(data, 0, len);
                             }
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 resultData = new String(byteArrayOutputStream.toByteArray());
                 return resultData;
             }*/

}

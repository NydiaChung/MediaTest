package com.example.mediatest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class VideoPlay extends AppCompatActivity implements View.OnClickListener{
    private VideoView videoView;
    private static final int FILE_SELECT_CODE=1;
    private static final String TAG="VideoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoView=findViewById(R.id.video_view);
        Button play=findViewById(R.id.play);
        Button pause=findViewById(R.id.pause);
        Button replay=findViewById(R.id.replay);
        Button choice=findViewById(R.id.choice) ;//按钮的初始化
        choice.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);//给按钮加监听
        if(ContextCompat.checkSelfPermission(VideoPlay.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VideoPlay.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);//判断你是否授权
        }
        else {
            initVideoPath();
        }
    }
    private void initVideoPath(){
        File file=new File(Environment.getExternalStorageDirectory(),"movie.mp4");//打开软件直接播放的视频名字是movie.mp4
        videoView.setVideoPath(file.getPath());//指定视频文件的路径
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }
                else {
                    Toast.makeText(this,"拒绝权限将无法访问程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public void onClick(View v){//各按钮的功能
        switch (v.getId()){
            case R.id.play:
                if(!videoView.isPlaying()){//播放
                    videoView.start();
                }
                Toast.makeText(this,"video is playing",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause:
                if(videoView.isPlaying()){//暂停
                    videoView.pause();
                }
                Toast.makeText(this,"video is paused",Toast.LENGTH_SHORT).show();
                break;
            case R.id.replay:
                if(videoView.isPlaying()){
                    videoView.resume();//重新播放
                }
                Toast.makeText(this,"video is replaying",Toast.LENGTH_SHORT).show();
                break;
            case R.id.choice://选择文件
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，这是任意类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
        }
    }
    public void onDestroy(){//释放资源
        super.onDestroy();
        if(videoView!=null){
            videoView.suspend();
        }
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            videoView.setVideoURI(uri);//将选择的文件路径给播放器
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            Log.i(TAG, "------->" + uri.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //public void choseFile(){
    ///    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    //    intent.setType("*/*");
    //    intent.addCategory(Intent.CATEGORY_OPENABLE);
    //    try {
    //        startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
    //    } catch (android.content.ActivityNotFoundException ex) {
    //        Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
    //    }

    // }

}
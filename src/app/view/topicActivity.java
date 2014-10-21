package app.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.iflytek.speech.*;
import app.speech.util.ApkInstaller;
import app.speech.util.JsonParser;
import app.speech.Iat;
import app.speech.Tts;

public class topicActivity extends Activity implements OnClickListener{
    private Toast mToast;
    private Handler mHandler;
    private Dialog mLoadDialog;
    private String TAG = "Speech";
    //语音识别对象
    private Iat iat ;
    //语音合成对象
    private Tts tts;
    //手势识别对象
    private GestureDetector mDetector;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 设置标题栏（无标题）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.topic);
        findViewById(R.id.start).setOnClickListener(topicActivity.this);
        findViewById(R.id.stop).setOnClickListener(topicActivity.this);
        findViewById(R.id.read).setOnClickListener(topicActivity.this);

        mToast = Toast.makeText(topicActivity.this,"",Toast.LENGTH_LONG);
        //语音识别对象初始化
        iat = new Iat();
        iat.Init(topicActivity.this,mInitListener);
        //语音合成对象初始化
        tts = new Tts();
        tts.Init(topicActivity.this,mTtsInitListener);
        //手势识别初始化
        mDetector= new GestureDetector(topicActivity.this,new DefaultGestureListener());
    }
    @Override
    public void onClick(View view) {
        if(view.getTag() == null)
        {
            showTip("未知错误");
            return;
        }


        int tag = Integer.parseInt(view.getTag().toString());

        if(tag == 0)
        {
            // 开始识别
            iat.startListening(mRecognizerListener);
        }else if(tag == 1)
        {
            // 停止识别
            iat.stopListening(mRecognizerListener);
        }else if(tag == 2)
        {
            String text = ((EditText) findViewById(R.id.text)).getText().toString();
            // 开始读
            tts.startReading(mTtsListener,text);
        }
    }

    //修改onTouch 使手势识别可用
    public boolean onTouchEvent(MotionEvent event) {
            if (mDetector.onTouchEvent(event))
                 return true;
            else
                 return false;
      }

    private void showTip(final String str)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    /**
     * 如果服务组件没有安装，有两种安装方式。
     * 1.直接打开语音服务组件下载页面，进行下载后安装。
     * 2.把服务组件apk安装包放在assets中，为了避免被编译压缩，修改后缀名为mp3，然后copy到SDcard中进行安装。
     */
    private boolean processInstall(Context context ,String url,String assetsApk){
        // 直接下载方式
//		ApkInstaller.openDownloadWeb(context, url);
        // 本地安装方式
        if(!ApkInstaller.installFromAssets(context, assetsApk)){
            Toast.makeText(topicActivity.this, "安装失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    // 判断手机中是否安装了讯飞语音+
    private boolean checkSpeechServiceInstall(){
        String packageName = "com.iflytek.speechcloud";
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for(int i = 0; i < packages.size(); i++){
            PackageInfo packageInfo = packages.get(i);
            if(packageInfo.packageName.equals(packageName)){
                return true;
            }else{
                continue;
            }
        }
        return false;
    }


    private RecognizerListener mRecognizerListener = new RecognizerListener.Stub() {

        @Override
        public void onVolumeChanged(int v) throws RemoteException {
            showTip("onVolumeChanged："	+ v);
        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast)
                throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != result) {
                        // 显示
                        Log.d(TAG, "recognizer result：" + result.getResultString());
                        String iattext = JsonParser.parseIatResult(result.getResultString());
                        showTip(iattext);
                        ((EditText)findViewById(R.id.text)).setText(iattext);
                    } else {
                        Log.d(TAG, "recognizer result : null");
                        showTip("无识别结果");
                    }
                }
            });

        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            showTip("onError Code："	+ errorCode);
        }

        @Override
        public void onEndOfSpeech() throws RemoteException {
            showTip("onEndOfSpeech");
        }

        @Override
        public void onBeginOfSpeech() throws RemoteException {
            showTip("onBeginOfSpeech");
        }
    };

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(ISpeechModule module, int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code == ErrorCode.SUCCESS) {
                findViewById(R.id.start).setEnabled(true);
                findViewById(R.id.stop).setEnabled(true);
            }
        }
    };

    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(ISpeechModule arg0, int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code == ErrorCode.SUCCESS) {
                ((Button)findViewById(R.id.read)).setEnabled(true);
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
            Log.d(TAG, "onBufferProgress :" + progress);
//        	 showTip("onBufferProgress :" + progress);
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
            Log.d(TAG, "onCompleted code =" + code);
            showTip("onCompleted code =" + code);
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            Log.d(TAG, "onSpeakBegin");
            showTip("onSpeakBegin");
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
            Log.d(TAG, "onSpeakPaused.");
            showTip("onSpeakPaused.");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
            Log.d(TAG, "onSpeakProgress :" + progress);
            showTip("onSpeakProgress :" + progress);
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
            Log.d(TAG, "onSpeakResumed.");
            showTip("onSpeakResumed");
        }
    };

    private class DefaultGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        //长按

        public void onLongPress(MotionEvent e) {
            Log.d(DEBUG_TAG,"onLongPress: "+e.toString());
            tts.startReading(mTtsListener,"长按");
        }

        @Override
        //暂停 双击
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(DEBUG_TAG,"onLongPress: "+e.toString());
            tts.startReading(mTtsListener,"双击");
            return true;
        }

        //滑动最短距离
        private int verticalMinDistance = 100;
        //最小的滑动速度
        private int minVelocity         = 0;
        @Override
        //滑动
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + e1.toString() + e2.toString());
            if (e1.getX() - e2.getX() > Math.abs(e1.getY()-e2.getY()) && Math.abs(velocityX) > minVelocity) {


                tts.startReading(mTtsListener,"向左");
            } else if (e2.getX() - e1.getX() >Math.abs(e1.getY()-e2.getY()) && Math.abs(velocityX) > minVelocity) {

                tts.startReading(mTtsListener,"向右");
            }else if(e1.getY() - e2.getY() > Math.abs(e1.getX()-e2.getX()) && Math.abs(velocityY) > minVelocity){
                tts.startReading(mTtsListener,"向上");
            }
            else if(e2.getY() - e1.getY() > Math.abs(e1.getX()-e2.getX()) && Math.abs(velocityY) > minVelocity){
                tts.startReading(mTtsListener,"向下");
            }
            return true;
        }
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        iat.destory(mRecognizerListener);
        tts.destory(mTtsListener);
    }
}

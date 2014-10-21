package app.speech;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.iflytek.speech.*;

/**
 * Created by Administrator on 2014/10/14.
 */
public class Iat {
    private static String TAG = "IatDemo";
    // 语音识别对象。
    private SpeechRecognizer mIat;
    private Toast mToast;
    private Context mma;
    private static final String ACTION_INPUT = "com.iflytek.speech.action.voiceinput";
    private static final int REQUEST_CODE_SEARCH = 1099;



    public void Init(Context context,InitListener initListener){
        mma=  context;
        mIat = new SpeechRecognizer(mma, initListener);
        mToast = Toast.makeText(mma, "", Toast.LENGTH_LONG);

    }

    public void startListening(RecognizerListener recognizerListener){

        setParam();
        mIat.startListening(recognizerListener);
    }

    public void stopListening(RecognizerListener recognizerListener){
        mIat.stopListening(recognizerListener);
    }

    private void setParam(){

        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.VAD_BOS,  "4000");
//		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");


        mIat.setParameter(SpeechConstant.PARAMS, "asr_ptt=0,asr_audio_path=/sdcard/iflytek/wavaudio.pcm");

    }

    public void destory(RecognizerListener recognizerListener){
        mIat.cancel(recognizerListener);
        mIat.destory();
    }





}

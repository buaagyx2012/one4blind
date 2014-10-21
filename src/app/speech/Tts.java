package app.speech;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

/**
 * Created by Administrator on 2014/10/18.
 */
public class Tts {
    private static String TAG = "TtsDemo";
    // 语音合成对象
    private SpeechSynthesizer mTts;
    private Toast mToast;
    public static String SPEAKER = "speaker";
    private Context mma;
    public void Init(Context context,InitListener TtsInitListener){
        mma = context;
        mTts = new SpeechSynthesizer(mma, TtsInitListener);
        mToast = Toast.makeText(mma,"",Toast.LENGTH_LONG);

    }

    public void startReading(SynthesizerListener TtsLister,String text){
        setParam();
        // 设置参数
        int code = mTts.startSpeaking(text, TtsLister);
        if (code != 0) {
            showTip("start speak error : " + code);
        } else {
            showTip("start speak success.");
        }

    }

    public void stopReading(SynthesizerListener TtsLister){
        mTts.stopSpeaking(TtsLister);
    }


    public void destory(SynthesizerListener TtsLister){
        mTts.stopSpeaking(TtsLister);
        // 退出时释放连接
        mTts.destory();
    }

    private void setParam(){
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, "local");


        mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");

        mTts.setParameter(SpeechSynthesizer.SPEED, "50");

        mTts.setParameter(SpeechSynthesizer.PITCH, "50");

        mTts.setParameter(SpeechSynthesizer.VOLUME, "50");
    }
    private void showTip(final String str){
                mToast.setText(str);
                mToast.show();
    }



}

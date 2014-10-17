package app.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class welcomeActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
    }

    public void subData(View view){
        AVObject testobject =new AVObject("testData");
        testobject.put("username","lewisKit");
        testobject.put("password","todd");

        testobject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                String text = "";
                if (e == null){
                    text = "保存成功";

                }else{
                    text = "保存失败";
                }
                Toast.makeText(welcomeActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }


}

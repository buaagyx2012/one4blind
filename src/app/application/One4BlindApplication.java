package app.application;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
/**
 * Created by lewiskit on 14-10-16.
 */
public class One4BlindApplication extends Application {

    @Override
    public void onCreate() {
        //initialize the key to AVOSCloud

        AVOSCloud.initialize(this,"49lf3qpi8eqa3j5u9s1vesnxswfqf8hrtb2yed3nba5kiuzb",
                "lui3o62tm4gyhsqo0r16ljgopl7f7gv4mmkig98ih34tzp94");


        super.onCreate();
    }
}

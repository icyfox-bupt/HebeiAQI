package icyfox.hebeiair;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by FOX on 2015/11/8.
 */
public class BaseActivity extends Activity{

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

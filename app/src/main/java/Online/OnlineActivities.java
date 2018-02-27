package Online;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by otto on 2018-02-21.
 */

public class OnlineActivities extends AppCompatActivity {
    protected boolean yourTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourTurn = false;
    }
}

package android.example.com.providersample;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewResult = (TextView) findViewById(R.id.tv_result);
        String uri = "content://com.sungwoo.boostcamp.sungwooalarmapp/findall";
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(uri), null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();
        if (cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            stringBuffer.append(cursor.getString(cursor.getColumnIndex("hour")) + " : ");
            stringBuffer.append(cursor.getString(cursor.getColumnIndex("minute")) + "  memo : ");
            stringBuffer.append(cursor.getString(cursor.getColumnIndex("memoStr")) + "\n");
        }

        mTextViewResult.setText(stringBuffer.toString());

    }
}

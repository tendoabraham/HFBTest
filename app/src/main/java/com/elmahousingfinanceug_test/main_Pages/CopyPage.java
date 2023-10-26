package com.elmahousingfinanceug_test.main_Pages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;

public class CopyPage extends BaseAct {
    private TextView textView;
    private Button copyButton;
    private AllMethods am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        am = new AllMethods(this);

        textView = findViewById(R.id.textView);
        copyButton = findViewById(R.id.copyButton);

        textView.setText(am.getSavedData("RAO"));

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipboard();
            }
        });
    }

    private void copyTextToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", textView.getText());
        clipboard.setPrimaryClip(clip);
    }
}

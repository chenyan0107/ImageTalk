package com.chen.cy.talkimage.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chen.cy.talkimage.R;

/**
 * Created by C-y on 2015/10/14.
 */
public class EditDialog extends Dialog{
    private Context context;
    private String msg;

    private EditText editText;
    private Button yesButton;
    private Button cancelButton;
    private TextView titleText;

    private DialogClick dialogClick;

    public interface DialogClick{
        public void yesClick(String name);
        public void cancelClick();
    }

    public EditDialog(Context context, String msg) {
        super(context);
        this.context = context;
        this.msg = msg;
    }

    public EditDialog(Context context, int theme, String msg){
        super(context, theme);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit);
        editText = (EditText)findViewById(R.id.dialog_edit_editText);
        yesButton = (Button)findViewById(R.id.dialog_edit_yesBtn);
        cancelButton = (Button)findViewById(R.id.dialog_edit_cancelBtn);
        titleText = (TextView)findViewById(R.id.dialog_edit_title);
        editText.setHint(msg);
        titleText.setText(msg);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.yesClick(editText.getText().toString());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.cancelClick();
            }
        });
    }

    public void setDialogClick(DialogClick dialogClick){
        this.dialogClick = dialogClick;
    }
}

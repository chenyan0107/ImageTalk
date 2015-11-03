package com.chen.cy.talkimage.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.chen.cy.talkimage.R;

/**
 * Created by C-y on 2015/10/14.
 */
public class SelectDialog extends Dialog{
    private Context context;
    private int layout;

    private Button yesButton;
    private Button cancelButton;
    private RadioGroup radioGroup;

    private SelectDialogClick selectDialogClick;
    public SelectDialog(Context context, int theme, int layout) {
        super(context, theme);
        this.context = context;
        this.layout = layout;
    }

    public interface SelectDialogClick{
        public void yesClick(int checkedID);
        public void cancelClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        yesButton = (Button)findViewById(R.id.dialog_select_yesBtn);
        cancelButton = (Button)findViewById(R.id.dialog_select_cancelBtn);
        radioGroup = (RadioGroup)findViewById(R.id.dialog_select_radioGroup);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialogClick.yesClick(radioGroup.getCheckedRadioButtonId());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialogClick.cancelClick();
            }
        });
    }

    public void setSelectDialogClick(SelectDialogClick selectDialogClick){
        this.selectDialogClick = selectDialogClick;
    }
}

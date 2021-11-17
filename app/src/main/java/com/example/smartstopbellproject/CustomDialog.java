package com.example.smartstopbellproject;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {
    private static CustomDialog customDialog;

    private CustomDialog(@NonNull Context context) {
        super(context);
    }

    public static CustomDialog getInstance(Context context) {
        customDialog = new CustomDialog(context);

        return customDialog;
    }

    public void showDefaultDialog() {
        customDialog.setContentView(R.layout.custom_dialog);
        customDialog.show();
    }

    /*
    public void cancelDefaultDialog() {
        customDialog.dismiss();
    }
     */
}

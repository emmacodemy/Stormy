package com.longbridge.learning.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by EMMA PC on 23/08/2018.
 */

public class UserAlertDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("oops an error occured").setMessage("There was an error, please try again!")
                .setPositiveButton("OK",null);
        return builder.create();

    }





}

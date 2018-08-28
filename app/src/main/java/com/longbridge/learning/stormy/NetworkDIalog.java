package com.longbridge.learning.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by EMMA PC on 27/08/2018.
 */

public class NetworkDIalog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Failure").setMessage("pls try again network is unavailable").setPositiveButton("OK",null);
        return builder.create();
    }
}

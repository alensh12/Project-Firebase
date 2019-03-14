package com.example.amprime.firebaseauth;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.amprime.firebaseauth.retrofit.APIService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class NotificationDialog extends AppCompatDialogFragment {

    private EditText titleText;
    private EditText messageText;
    APIService mService;

    DialogClick _listener;


    public static NotificationDialog newInstance(DialogClick listener) {
        NotificationDialog fragment = new NotificationDialog();

        fragment._listener = listener;

        return  fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.notification_dialog, null);
        titleText = view.findViewById(R.id.title_text);
        messageText = view.findViewById(R.id.message_text);
        mService = Token.getFCMClient();

        builder.setView(view).setTitle("Send Notification")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textTitle = titleText.getText().toString().trim();
                String textBody = messageText.getText().toString().trim();

                _listener.click(textTitle, textBody);
                dialog.dismiss();


            }
                })

                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   _listener.clickCancel();
                   dialog.dismiss();

            }
        });
            return  builder.create();

    }
}

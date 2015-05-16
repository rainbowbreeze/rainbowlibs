package it.rainbowbreeze.libs.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by alfredomorresi on 16/05/15.
 */
public class RainbowAskForQuestionDialog extends DialogFragment {
    private static final String ARG_QUESTION_RESOURCEID = "question_resourceId";

    /**
     * Returns a new instance of this dialog
     */
    public static RainbowAskForQuestionDialog newInstance() {
        return newInstance(0);
    }

    /**
     * Returns a new instance of this dialog
     *
     * @param questionResourceId: resource id of the question to ask
     */
    public static RainbowAskForQuestionDialog newInstance(int questionResourceId) {
        RainbowAskForQuestionDialog fragment = new RainbowAskForQuestionDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_RESOURCEID, questionResourceId);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int questionResourceId = getArguments().getInt(ARG_QUESTION_RESOURCEID, R.string.rainbow_lblAreYouSure);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(questionResourceId)
                .setPositiveButton(R.string.rainbow_btnYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        // Wondering why?
                        //  http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment
                        //  http://stackoverflow.com/questions/13733304/callback-to-a-fragment-from-a-dialogfragment
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton(R.string.rainbow_btnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    */
}

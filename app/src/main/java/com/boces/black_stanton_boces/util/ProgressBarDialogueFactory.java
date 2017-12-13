package com.boces.black_stanton_boces.util;

import android.app.Dialog;
import android.content.Context;

import com.boces.black_stanton_boces.R;

/**
 * Basic Dialogue Factory
 * Produces A Dialogue With A Indeterminate Spinner And A Message
 */
public class ProgressBarDialogueFactory {

    /**
     * Produces A Basic Progress Dialogue
     *
     * @param context
     * The Current Context
     *
     * @return
     * A Hidden Progress Dialogue
     */
    public static Dialog make(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_progress);
        dialog.setTitle(R.string.title_dialogue_progress);
        return dialog;
    }
}

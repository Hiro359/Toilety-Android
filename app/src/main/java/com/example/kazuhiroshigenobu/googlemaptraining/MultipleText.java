package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by KazuhiroShigenobu on 1/3/17.
 *
 */

public class MultipleText extends EditText {


    public MultipleText(Context context) {
        super(context);
        init();
    }

    public MultipleText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultipleText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // set something

    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        // InputConnection connection = super.onCreateInput(outAttrs);
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
        if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
            // clear the existing action
            outAttrs.imeOptions ^= imeActions;
            // set the DONE action
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
        }
        if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return connection;
    }

}

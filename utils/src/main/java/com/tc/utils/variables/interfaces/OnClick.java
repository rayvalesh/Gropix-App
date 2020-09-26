package com.tc.utils.variables.interfaces;

import android.view.View;

import com.tc.utils.variables.enums.ActionType;
import com.tc.utils.variables.enums.ModuleType;

public interface OnClick {

    interface Listener {
        void getEventData(Object object);

        void getEventData(Object object, View view);
    }

    interface OnDataListener {
        void onErrorResponse(Object object, String errorMessage);

        void getEventData(Object object, int comeFor);

        void getEventData(Object object, ActionType actionType);

        void getEventData(Object object, ActionType actionType, int adapterPosition);

        void getEventData(Object object, ActionType actionType, Object variable);
    }
}

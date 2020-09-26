package com.tc.utils.variables.abstracts;


import android.view.View;

import com.tc.utils.variables.enums.ActionType;
import com.tc.utils.variables.enums.ModuleType;
import com.tc.utils.variables.interfaces.OnClick;

public abstract class OnEventOccurListener implements OnClick.OnDataListener, OnClick.Listener {

    @Override
    public void getEventData(Object object, ActionType actionType) {

    }

    @Override
    public void getEventData(Object object, View view) {

    }

    @Override
    public void getEventData(Object object, int comeFor) {

    }

    @Override
    public void onErrorResponse(Object object, String errorMessage) {

    }

    @Override
    public void getEventData(Object object) {

    }

    @Override
    public void getEventData(Object object, ActionType actionType, int adapterPosition) {

    }

    @Override
    public void getEventData(Object object, ActionType actionType, Object variable) {

    }
}

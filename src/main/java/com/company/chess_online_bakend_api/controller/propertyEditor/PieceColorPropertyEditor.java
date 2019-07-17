package com.company.chess_online_bakend_api.controller.propertyEditor;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
public class PieceColorPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(PieceColor.fromValue(text));
    }
}

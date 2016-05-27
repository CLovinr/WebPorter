package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
public enum ParseType
{
    INT(Integer.class.getSimpleName(), IntChecker.class), INT2("Integer2", Int2Checker.class), INT8("Integer8",
        Int8Checker.class), INT16("Integer16",
        Int16Checker.class), LONG(Long.class.getSimpleName(), LongChecker.class), BOOLEAN(Boolean.class.getSimpleName(),
        BooleanChecker.class), BYTE(Byte.class.getSimpleName(),
        ByteChecker.class), SHORT(Short.class.getSimpleName(), ShortChecker.class), FLOAT(Float.class.getSimpleName(),
        FloatCheck.class), DOUBLE(
        Double.class.getSimpleName(),
        DoubleChecker.class), STRING(String.class.getSimpleName(), StringChecker.class), STRING_ARR(
        String.class.getSimpleName() + "[]",
        StringArrChecker.class), ARRAY_LIST_STR(
        ArrayList.class.getSimpleName() + "<" + String.class.getSimpleName() + ">", ArrayListArrChecker.class), JSON(
        JSONObject.class.getSimpleName(), JSONObjectChecker.class), JSON_ARR(JSONArray.class.getSimpleName(),
        JSONArrayChecker.class);
    private String type;
    private Class<? extends InCheckParser.Checker> checkerClass;

    ParseType(String type, Class<? extends InCheckParser.Checker> checkerClass)
    {
        this.type = type;
        this.checkerClass = checkerClass;
    }

    public Class<? extends InCheckParser.Checker> getCheckerClass()
    {
        return checkerClass;
    }

    public String getType()
    {
        return type;
    }
}

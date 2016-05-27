package com.chenyg.wporter;

import com.chenyg.wporter.annotation.NotNULL;
import com.chenyg.wporter.incheck.ParseType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数检测与转换管理对象.
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class InCheckParser implements Serializable
{
    /**
     * 不合法的参数异常类.
     */
    public class IllegalParamException extends Exception
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        String needType, name;
        Object value;
        String udesc;

        /**
         * @param needType 需要的类型
         * @param name     参数名
         * @param value    实际值
         * @param udesc    描述（面向用户的）
         */
        private IllegalParamException(String needType, String name, Object value, String udesc)
        {
            this.needType = needType;
            this.name = name;
            this.value = value;
            this.udesc = udesc;
        }

        /**
         * 得到需要的类型
         *
         * @return
         */
        public String getNeedType()
        {
            return needType;
        }

        /**
         * 得到参数名
         *
         * @return
         */
        public String getName()
        {
            return name;
        }

        public Object getValue()
        {
            return value;
        }

        /**
         * 描述（面向用户的）
         *
         * @return
         */
        public String getUDesc()
        {
            return udesc;
        }

    }


    public class CommonBinder
    {
        private String checkerType;

        CommonBinder(String checkerType)
        {
            this.checkerType = checkerType;
        }

        CommonBinder(ParseType parseType)
        {
            this.checkerType = parseType.getType();
        }

        public CommonBinder bind(String name, String udesc)
        {
            bindChecker(name, udesc, checkerType);
            return this;
        }

    }


    public static class CheckResult
    {

        public static final CheckResult ILLEGAL = new CheckResult();

        private boolean isLegal;
        private Object value;

        /**
         * 转换不合法
         */
        public CheckResult()
        {
            this.isLegal = false;
        }

        /**
         * 转换合法
         *
         * @param value 结果值,可以为{@link com.chenyg.wporter.InCheckParser.DecodeParams}类型。
         */
        public CheckResult(Object value)
        {
            this.isLegal = true;
            this.value = value;
        }

        /**
         * 设置值
         *
         * @param value
         */
        public void setValue(Object value)
        {
            this.value = value;
        }

        /**
         * 设置结果是否合法
         *
         * @param isLegal
         */
        public void setIsLegal(boolean isLegal)
        {
            this.isLegal = isLegal;
        }

        public boolean isLegal()
        {
            return isLegal;
        }

        /**
         * 得到转换后的值
         *
         * @return
         */
        public Object getValue()
        {
            return value;
        }
    }

    /**
     * 用于分解参数。
     * <pre>
     *     假如：dec=obj
     *     obj为分解类型：1）若obj中含有名为dec的值val，则最终dec=val；2）若没有且{@linkplain #isAdd2Param()}，则最终dec=obj。
     * </pre>
     * 返回的值会被统一放到一个临时Map中，以后在获取参数值时，优先从该map中获取,并且从它中获取的参数无法再被转换。
     */
    public static class DecodeParams
    {

        private Map<String, Object> params = new HashMap<>();

        private boolean add2Param = false;

        public DecodeParams()
        {

        }

        public void setAdd2Param(boolean add2Param)
        {
            this.add2Param = add2Param;
        }

        /**
         * 默认为false。
         *
         * @return
         */
        public boolean isAdd2Param()
        {
            return add2Param;
        }

        public Map<String, Object> getParams()
        {
            return params;
        }

        public DecodeParams put(String name, Object value)
        {
            params.put(name, value);
            return this;
        }
    }

    /**
     * 参数处理函数
     */
    public static abstract class Checker implements Serializable
    {
        /**
         * 处理函数
         *
         * @param value 要处理的值，不为null。
         * @return 处理结果
         */
        public abstract CheckResult parse(@NotNULL Object value);
    }

    private static class Temp
    {
        String udesc, type;
        Checker checker;


        public Temp(String udesc, String type, Checker checker)
        {
            this.udesc = udesc;
            this.type = type;
            this.checker = checker;
        }
    }

    public static final HashMap<Class<?>, String> SUPPORTED_TYPES = getSupportedTypes();
    private HashMap<String, Checker> checkerHashMap = new HashMap<String, Checker>();
    private HashMap<String, Temp> bindsMap = new HashMap<String, Temp>();

    public InCheckParser()
    {

        try
        {
            ParseType[] parseTypes = ParseType.values();
            for (ParseType parseType : parseTypes)
            {
                putChecker(parseType.getType(), parseType.getCheckerClass().newInstance());
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e.toString());
        }

    }

    /**
     * 添加
     *
     * @param type    类型
     * @param checker
     * @return
     */
    public InCheckParser putChecker(String type, Checker checker)
    {
        if (checker == null) throw new NullPointerException();
        checkerHashMap.put(type, checker);
        return this;
    }

    public InCheckParser putChecker(ParseType parseType, Checker checker)
    {
        return this.putChecker(parseType.getType(), checker);
    }

    /**
     * 把参数name与checkerType类型的Checker进行绑定.
     *
     * @param name
     * @param udesc       该参数的描述（面向用户）
     * @param checkerType
     * @return
     */
    public InCheckParser bindChecker(String name, String udesc, String checkerType)
    {
        Checker checker = checkerHashMap.get(checkerType);
        if (checker == null)
        {
            throw new NullPointerException("there is no Checker of type'" + checkerType + "'");
        }
        bindsMap.put(name, new Temp(udesc, checkerType, checker));
        return this;
    }

    /**
     * 把参数name与checkerType类型的Checker进行绑定.
     *
     * @param name
     * @param udesc     该参数的描述（面向用户）
     * @param parseType
     * @return
     */
    public InCheckParser bindChecker(String name, String udesc, ParseType parseType)
    {
        return this.bindChecker(name, udesc, parseType.getType());
    }

    public CommonBinder commonBinder(ParseType parseType)
    {
        return new CommonBinder(parseType);
    }

    public CommonBinder commonBinder(String checkerType)
    {
        return new CommonBinder(checkerType);
    }

    /**
     * 注意：若value为String类型且绑定的type为String（{@linkplain ParseType#STRING}），则直接返回该值。
     *
     * @param name
     * @param value
     * @return
     * @throws IllegalParamException
     */
    public Object parse(String name, @NotNULL Object value) throws IllegalParamException
    {
        Temp temp = bindsMap.get(name);
        if (temp == null || temp.checker == null || (temp.type
                .equals(ParseType.STRING.getType()) && value instanceof String))
        {
            return value;
        }
        CheckResult checkResult = temp.checker.parse(value);
        if (checkResult.isLegal())
        {
            return checkResult.getValue();
        } else
        {
            throw new IllegalParamException(temp.type, name, value, temp.udesc);
        }
    }

    /**
     * 得到该field的类型，若不在支持的类型{@linkplain ParseType}中，则抛出RuntimeException异常。
     *
     * @param field
     * @return
     */
    public static String getType(Field field)
    {
        Class<?> c = field.getType();
        if (c.isPrimitive())
        {
            return c.getSimpleName();
        } else
        {
            String type = SUPPORTED_TYPES.get(c);
            if (type == null)
            {
                throw new RuntimeException("unsupported type '" + c + "'");
            }
            return type;
        }
    }

    /**
     * 得到支持的类型
     *
     * @return
     */
    private static HashMap<Class<?>, String> getSupportedTypes()
    {
        HashMap<Class<?>, String> hashMap = new HashMap<Class<?>, String>();
        hashMap.put(Boolean.class, ParseType.BOOLEAN.getType());
        hashMap.put(Byte.class, ParseType.BYTE.getType());
        hashMap.put(Short.class, ParseType.SHORT.getType());
        hashMap.put(Integer.class, ParseType.INT.getType());
        hashMap.put(Long.class, ParseType.LONG.getType());
        hashMap.put(Float.class, ParseType.FLOAT.getType());
        hashMap.put(Double.class, ParseType.DOUBLE.getType());
        hashMap.put(String.class, ParseType.STRING.getType());
        hashMap.put(String[].class, ParseType.STRING_ARR.getType());
        hashMap.put(ArrayList.class, ParseType.ARRAY_LIST_STR.getType());
        hashMap.put(JSONObject.class, ParseType.JSON.getType());
        hashMap.put(JSONArray.class, ParseType.JSON_ARR.getType());
        return hashMap;
    }
}

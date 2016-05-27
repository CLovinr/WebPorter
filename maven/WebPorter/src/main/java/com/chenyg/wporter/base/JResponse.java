package com.chenyg.wporter.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 响应结果的封装.
 * <pre>
 * 1.结果为一个json对象，含有的属性为下面的某几个:
 *   {@linkplain #CODE_FILED},{@linkplain #RESULT_FILED},{@linkplain #DESCRIPTION_FILED},{@linkplain
 *   #REQUEST_URI_FILED},{@linkplain #NEED_ARGS_FIELD},{@linkplain #ILLEGAL_ARGS_FIELD}
 * 2.结果码(code)为0表示成功，其他表示不成功（不同值对应不同意思）
 * 3.desc属性表示描述；rs表示返回的结果数据；uri表示请求的uri，当发生异常，会自动设置该值。
 * 4.needArgs用于提示必须参数缺乏，且只有缺乏必须参数和设置的响应缺乏参数为true时，才有该属性：
 *     格式为needArgs:[{"name":"paramName","desc":"描述"}...]
 *     其中描述不一定存在。
 * 5.illegalArgs:[{"name":"keyname","value":value,"needType":"type","udesc":"描述"}...]
 * </pre>
 *
 * @author Administrator
 */
public class JResponse
{

    public static class JResponseFormatException extends Exception
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public JResponseFormatException()
        {

        }

        public JResponseFormatException(String info)
        {
            super(info);
        }

        public JResponseFormatException(Throwable throwable)
        {
            super(throwable);
        }

    }

    public static final String CODE_FILED = "code";
    public static final String RESULT_FILED = "rs";
    public static final String DESCRIPTION_FILED = "desc";
    public static final String REQUEST_URI_FILED = "uri";


    public static final String NEED_ARGS_FIELD = "needArgs";
    public static final String NEED_ARGS_NAME_FIELD = "name",
            NEED_ARGS_DESC_FIELD = "desc";
    public static final String ILLEGAL_ARGS_FIELD = "illegalArgs";
    public static final String ILLEGAL_ARGS_NAME_FIELD = "name",
            ILLEGAL_ARGS_UDESC_FIELD = "udesc", ILLEGAL_ARGS_VALUE_FIELD = "value", ILLEGAL_ARGS_NEEDTYPE_FIELD =
            "needType";


    public static final JResponse SUCCESS_RESPONSE = new JResponse(ResultCode.SUCCESS);

    private ResultCode code = ResultCode.Other;
    private String description;
    private Object result;
    private String uri;
    private JSONArray needArgs, illegalArgs;
    private Throwable exCause;

    public JResponse(ResultCode code)
    {
        setCode(code);
    }

    public JResponse(int code)
    {
        setCode(code);
    }

    public JResponse()
    {

    }

    private static String getString(JSONObject jsonObject, String name) throws JSONException
    {
        if (jsonObject.has(name))
        {
            return jsonObject.getString(name);
        } else
        {
            return null;
        }
    }

    /**
     * 设置异常原因
     * @param exCause
     */
    public void setExCause(Throwable exCause)
    {
        Throwable cause = exCause.getCause();
        if(cause==null){
            cause=exCause;
        }
        this.exCause = cause;
    }

    /**
     * 得到异常原因
     */
    public Throwable getExCause()
    {
        return exCause;
    }

    /**
     * 得到不合法的参数列表
     *
     * @return
     */
    public JSONArray getIllegalArgs()
    {
        return illegalArgs;
    }

    /**
     * 元素:
     * {"needType":"String，需要的类型","key":"参数名称","value":"当前参数值，可为空"}
     *
     * @param illegalArgs
     */
    public void setIllegalArgs(JSONArray illegalArgs)
    {
        this.illegalArgs = illegalArgs;
    }


    private static JSONArray getNeedArgs(JSONObject jsonObject) throws JSONException
    {
        JSONArray array = null;
        if (jsonObject.has(NEED_ARGS_FIELD))
        {
            array = jsonObject.getJSONArray(NEED_ARGS_FIELD);
        }
        return array;
    }

    private static JSONArray getIllegalArgs(JSONObject jsonObject) throws JSONException
    {
        JSONArray array = null;
        if (jsonObject.has(ILLEGAL_ARGS_FIELD))
        {
            array = jsonObject.getJSONArray(ILLEGAL_ARGS_FIELD);
        }
        return array;
    }

    private static Object getResult(JSONObject jsonObject) throws JSONException
    {
        Object resutl = null;

        if (jsonObject.has(RESULT_FILED))
        {
            resutl = jsonObject.get(RESULT_FILED);
        }

        return resutl;
    }

    /**
     * 从对应的json字符转换成JResponse
     *
     * @param json
     * @return
     * @throws JResponseFormatException
     */
    public static JResponse fromJSON(String json) throws JResponseFormatException
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt(CODE_FILED);
            String desc = getString(jsonObject, DESCRIPTION_FILED);
            String uri = getString(jsonObject, REQUEST_URI_FILED);
            JSONArray needArgs = getNeedArgs(jsonObject);
            JSONArray illegalArgs = getIllegalArgs(jsonObject);
            Object result = getResult(jsonObject);

            JResponse jsonResponse = new JResponse();
            jsonResponse.setCode(ResultCode.toResponseCode(code));
            jsonResponse.setDescription(desc);
            jsonResponse.setUri(uri);
            jsonResponse.setNeedArgs(needArgs);
            jsonResponse.setIllegalArgs(illegalArgs);
            jsonResponse.setResult(result);

            return jsonResponse;
        } catch (Exception e)
        {
            throw new JResponseFormatException(e);
        }
    }

    /**
     * 得到缺少的必须参数的描述。
     *
     * @return
     */
    public JSONArray getNeedArgs()
    {
        return needArgs;
    }

    /**
     * 元素:
     * {"$NEED_ARGS_NAME_FIELD":"参数名称","$NEED_ARGS_DESC_FIELD":"描述（可为空）"}
     *
     * @param needArgs
     */
    public void setNeedArgs(JSONArray needArgs)
    {
        this.needArgs = needArgs;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /**
     * 得到请求的uri
     *
     * @return
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * 设置结果码,默认为{@linkplain ResultCode#OK_BUT_FAILED OK_BUT_FAILED}.
     *
     * @param code
     */
    public void setCode(ResultCode code)
    {
        this.code = code;
    }

    public void setCode(int code)
    {
        this.code = ResultCode.toResponseCode(code);
    }

    /**
     * 设置描述信息
     *
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * 设置结果对象
     *
     * @param result
     */
    public void setResult(Object result)
    {
        this.result = result;
    }

    public ResultCode getCode()
    {
        return code;
    }

    public Object getResult()
    {
        return result;
    }

    /**
     * 转换为字符串
     */
    @Override
    public String toString()
    {
        JSONObject jobj = new JSONObject();
        try
        {
            jobj.put(CODE_FILED, code != null ? code.toCode()
                    : ResultCode.Other.toCode());
            jobj.put(DESCRIPTION_FILED, description);
            jobj.put(RESULT_FILED, result);
            jobj.put(REQUEST_URI_FILED, uri);
            jobj.put(NEED_ARGS_FIELD, needArgs);
            jobj.put(ILLEGAL_ARGS_FIELD, illegalArgs);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jobj.toString();
    }

}

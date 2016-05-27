package com.chenyg.wporter;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.chenyg.wporter.base.*;
import com.chenyg.wporter.log.WPLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chenyg.wporter.annotation.AnnotationSearch;

import com.chenyg.wporter.annotation.FieldOut;
import com.chenyg.wporter.annotation.SimpleResponse;
import com.chenyg.wporter.annotation.ThinkType;
import com.chenyg.wporter.InCheckParser.IllegalParamException;
import com.chenyg.wporter.util.MyJsonTool;
import com.chenyg.wporter.util.WPTool;

/**
 * 处理请求
 *
 * @author ZhuiFeng
 */
class WPorterExecuter implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private RequestExListener requestExListener;
    private WPLog<?> wpLog;

    private WPorterSearcher wPorterSearcher;
    private CheckPassable globleCheck;// 全局凭证检测对象
    private String tiedPrefix;// 绑定名字前缀
    private String encode = "utf-8";
    private boolean responseLosedArgs;
    private ParamDealt paramDealt;
    private CheckType defaultCheckType = CheckType.GLOBAL;
    private ThinkType notFoundThinkType = ThinkType.DEFAULT;
    private ResponseDealt responseDealt;

    private ParamSourceHandleManager paramSourceHandleManager;

    public WPorterExecuter(WPMainInit wpMainInit)
    {
        this.paramDealt = wpMainInit.paramDealt;
        this.globleCheck = wpMainInit.globalCheck;
        this.notFoundThinkType = ThinkType.valueOf(wpMainInit.notFoundThinkType);
        this.defaultCheckType = CheckType.valueOf(wpMainInit.defaultCheckType);
        this.tiedPrefix = wpMainInit.tiedPrefix;
        this.requestExListener = wpMainInit.requestExListener;
        this.wpLog = wpMainInit.wpLog;
        this.responseLosedArgs = wpMainInit.responseLosedArgs;
        this.encode = wpMainInit.encoding;
        this.wPorterSearcher = wpMainInit.wPorterSearcher;
        this.paramSourceHandleManager = wpMainInit.paramManager;
        ResponseDealtWithREST responseDealt = new ResponseDealtWithREST(wpLog, wpMainInit.responseLosedArgs);
        this.responseDealt = responseDealt;
    }


    public String getTiedPrefix()
    {
        return tiedPrefix;
    }

    public WPorterSearcher getwPorterSearcher()
    {
        return wPorterSearcher;
    }

    /**
     * 得到编码方式
     *
     * @return
     */
    public String getEncode()
    {
        return encode;
    }

    /**
     * 处理请求
     * /**
     *
     * @param request
     * @param response
     * @param method
     * @param porterType
     * @throws IOException
     */
    public void doRequest(WPRequest request, WPResponse response, RequestMethod method,
            WPorterType porterType) throws IOException
    {
        request.setCharacterEncoding(encode);
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        int preLength = contextPath.length() + tiedPrefix.length();
        WPorterAndMethod wpfr = null;


        if (uri != null && preLength <= uri.length())
        {
            String preUri = uri.substring(preLength);

            wpfr = wPorterSearcher.getWPorterAndMethod(method, preUri,encode);
        }

        if (wpfr != null)
        {
            WPorter._ChildIn childIn = wpfr.wPorter.getChildIn();

            CheckType checkType = childIn.checkType();

            Object pass = checkPassable(method, childIn.checkPassTiedName(), checkType, wpfr.wPorter, request,
                    response);

            if (pass == null)
            {
                response.setCharacterEncoding(encode);// 设置编码
                ResponseTemp responseTemp = new ResponseTemp();
                try
                {
                    executePorter(responseTemp, request, response, wpfr, method, porterType);
                } catch (WPCallException e)
                {
                    wpLog.error(e.getMessage(), e);
                    JResponse jResponse;
                    if (e.getJResponse() != null)
                    {
                        jResponse = e.getJResponse();
                    } else
                    {
                        jResponse = new JResponse();
                        jResponse.setCode(ResultCode.INVOKE_METHOD_EXCEPTION);
                        jResponse.setDescription(e.toString());
                    }
                    writeEx(request, response, responseTemp.wpObject, jResponse, ResponseType.DirectResult, porterType,
                            wpfr.thinkType);
                    return;
                } catch (IllegalParamException e)
                {

                    JResponse jResponse = new JResponse();
                    jResponse.setCode(ResultCode.ILLEGAL_PARAM);
                    jResponse.setDescription("illegal param!");
                    if (responseLosedArgs)
                    {

                        try
                        {
                            JSONArray illegalArgs = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(JResponse.ILLEGAL_ARGS_NEEDTYPE_FIELD, e.getNeedType());
                            jsonObject.put(JResponse.ILLEGAL_ARGS_NAME_FIELD, e.getName());
                            jsonObject.put(JResponse.ILLEGAL_ARGS_UDESC_FIELD, e.getUDesc());
                            jsonObject.put(JResponse.ILLEGAL_ARGS_VALUE_FIELD, e.getValue());
                            illegalArgs.put(jsonObject);
                            jResponse.setIllegalArgs(illegalArgs);
                            //onExListener(request, jResponse);
                        } catch (JSONException e1)
                        {
                            wpLog.error(e1.getMessage(), e1);
                        }
                    }
                    writeEx(request, response, responseTemp.wpObject, jResponse, ResponseType.DirectResult, porterType,
                            wpfr.thinkType);
                    return;
                } catch (LackParamsException e)
                {
                    JResponse jResponse = new JResponse();
                    jResponse.setCode(ResultCode.LACK_NECE_PARAMS);
                    jResponse.setDescription("Lack necessary params!");
                    if (responseLosedArgs && e.getLackParamNames() != null)
                    {
                        try
                        {
                            JSONArray needArgs = new JSONArray();
                            String[] names = e.getLackParamNames();
                            for (String string : names)
                            {
                                needArgs.put(newNeceDesc(e.getNParamsDescMap(), string));
                            }
                            jResponse.setNeedArgs(needArgs);
                            //onExListener(request, jResponse);
                        } catch (JSONException e1)
                        {
                            wpLog.error(e1.getMessage(), e1);
                        }
                    }
                    writeEx(request, response, responseTemp.wpObject, jResponse, ResponseType.DirectResult, porterType,
                            wpfr.thinkType);
                    return;
                } catch (Exception e)
                {
                    wpLog.error(e.getMessage(), e);
                    JResponse jResponse = new JResponse();
                    jResponse.setCode(ResultCode.EXCEPTION);
                    jResponse.setDescription(e.toString());
                    writeEx(request, response, responseTemp.wpObject, jResponse, ResponseType.DirectResult, porterType,
                            wpfr.thinkType);
                    return;
                } finally
                {
                    clearTempData(wpfr);
                }

                if (responseTemp.wpObject != null)
                {
                    WPTool.close(responseTemp.wpObject.getWPForm());
                }
                try
                {
                    // if (responseTemp.result != null)
                    doResponse(responseTemp.result, wpfr, request, response, responseTemp.wpObject, porterType);
                } catch (Exception e)
                {
                    wpLog.error(e.getMessage(), e);

                    JResponse jResponse = new JResponse();
                    jResponse.setCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
                    jResponse.setDescription(e.toString());
                    writeEx(request, response, responseTemp.wpObject, jResponse, ResponseType.DirectResult, porterType,
                            wpfr.thinkType);

                }

            } else
            {
                JResponse jResponse;
                if (pass instanceof JResponse)
                {
                    jResponse = (JResponse) pass;
                } else
                {
                    jResponse = new JResponse(HttpStatusCode.UNAUTHORIZED);
                    jResponse.setDescription("Access denied!");
                    jResponse.setResult(pass);
                }

                writeEx(request, response, null, jResponse, ResponseType.DirectResult, porterType, wpfr.thinkType);

            }

        } else
        {
            JResponse jResponse = new JResponse();
            jResponse.setCode(ResultCode.NOT_AVAILABLE);
            jResponse.setDescription("The requested resource(" + method
                    + ":"
                    + request.getRequestURI()
                    + ") is not available!");
            jResponse.setResult("context:" + contextPath);
            //onExListener(request, jResponse);
            writeEx(request, response, null, jResponse, ResponseType.DirectResult, porterType, notFoundThinkType);
        }
    }

    /**
     * 必须参数描述
     *
     * @param map
     * @param name
     * @return
     * @throws JSONException
     */
    private JSONObject newNeceDesc(Map<String, String> map, String name) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JResponse.NEED_ARGS_NAME_FIELD, name);
        jsonObject.put(JResponse.NEED_ARGS_DESC_FIELD, map == null ? null
                : map.get(name));
        return jsonObject;
    }

    /**
     * 清楚临时数据.
     *
     * @param wpfr
     */
    private void clearTempData(WPorterAndMethod wpfr)
    {
        // wpfr.pathValueTemp = null;
        // wpfr.suffixUrlTemp = null;
    }

    /**
     * 处理响应
     *
     * @param object
     * @param wpfr
     * @param response
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    private void doResponse(Object object, WPorterAndMethod wpfr, WPRequest request,
            WPResponse response, WPObject wpObject,
            WPorterType wPorterType) throws IOException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, JSONException
    {
        response.setCharacterEncoding(encode);// 设置编码
        WPorter._ChildOut childOut = wpfr.wPorter.getChildOut();
        ResponseType responseType;
        if (childOut != null)
        {
            responseType = childOut.value();
        } else
        {
            responseType = ResponseType.DirectResult;
        }

        switch (responseType)
        {
            case ObjectField:
            {
                JSONObject jsonObject;
                jsonObject = responseFromFieldOut(object);
                JResponse jsonResponse = new JResponse();
                jsonResponse.setCode(ResultCode.SUCCESS);
                jsonResponse.setResult(jsonObject);
                writeResponse(request, response, wpObject, jsonResponse, responseType, wPorterType, wpfr.thinkType);
            }
            break;
            case Outers:
            {
                Class<?> c = object.getClass();
                JSONObject jsonObject = null;
                String[] fieldNames = childOut.outers();
                jsonObject = new JSONObject();
                for (int i = 0; i < fieldNames.length; i++)
                {
                    Field field = c.getDeclaredField(fieldNames[i]);
                    field.setAccessible(true);
                    Object obj = field.get(object);
                    jsonObject.put(fieldNames[i], obj);
                }
                WPorter._FatherOut fatherOut = wpfr.wPorter.getFatherOut();
                if (fatherOut != null)
                {
                    fieldNames = fatherOut.outers();
                    for (int i = 0; i < fieldNames.length; i++)
                    {
                        Field field = c.getDeclaredField(fieldNames[i]);
                        field.setAccessible(true);
                        Object obj = field.get(object);
                        jsonObject.put(fieldNames[i], obj);
                    }
                }
                JResponse jsonResponse = new JResponse();
                jsonResponse.setCode(ResultCode.SUCCESS);
                jsonResponse.setResult(jsonObject);
                writeResponse(request, response, wpObject, jsonResponse, responseType, wPorterType, wpfr.thinkType);
            }
            break;
            case SelfResponse:
                return;
            case Simple:
            {
                JResponse jsonResponse = simpleResponse(object);
                if (jsonResponse != null)
                {
                    writeResponse(request, response, wpObject, jsonResponse, responseType, wPorterType, wpfr.thinkType);
                    return;
                }
            }
            break;
            case DirectResult:
                writeResponse(request, response, wpObject, object , responseType, wPorterType, wpfr.thinkType);
                break;
            default:
                break;

        }

    }

    private JResponse simpleResponse(Object object)
    {
        JResponse jsonResponse = null;
        try
        {
            String des = (String) AnnotationSearch.getOneFieldValue(object, SimpleResponse.Description.class);
            ResultCode responseCode = (ResultCode) AnnotationSearch.getOneFieldValue(object, SimpleResponse.Code.class);
            if (des != null && responseCode != null)
            {
                String resultString = (String) AnnotationSearch.getOneFieldValue(object, SimpleResponse.Result.class);
                jsonResponse = new JResponse();
                jsonResponse.setCode(responseCode);
                jsonResponse.setResult(resultString);
                jsonResponse.setDescription(des);
            }
        } catch (Exception e)
        {
            wpLog.error(e.getMessage(), e);
        }

        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    private JSONObject responseFromFieldOut(Object object)
    {
        JSONObject jsonObject = null;

        try
        {
            jsonObject = MyJsonTool.toJsonObject(object, null, FieldOut.class);
        } catch (Exception e)
        {
            wpLog.error(e.getMessage(), e);
        }

        return jsonObject;
    }

    private Object executeOthers(WPorterAndMethod wpfr,
            WPObject wpObject) throws WPCallException
    {
        return wpfr.wPorter.getWebPorterFun().call(wpObject);
    }


    /**
     * @param responseTemp
     * @param request
     * @param response
     * @param wpfr
     * @param method
     * @param porterType
     * @throws LackParamsException
     * @throws IllegalParamException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private void executePorter(ResponseTemp responseTemp, WPRequest request, WPResponse response, WPorterAndMethod wpfr,
            RequestMethod method,
            WPorterType porterType) throws LackParamsException, IllegalParamException, WPCallException
    {
        WPObject wpObject;
        wpObject = new WPObject(method, wpLog, porterType);
        wpObject.request = request;
        wpObject.response = response;
        wpObject.id = wpfr.childBind;
        responseTemp.wpObject = wpObject;

        ParamSource paramSource = paramSourceHandleManager.getParamSource(wpObject, method, request, wpfr, wpLog);
        boolean neces = paramDealt.dealParams(wpfr, wpObject, paramSource, responseLosedArgs);
        if (neces)
        {
            Object result = executeOthers(wpfr, wpObject);
            responseTemp.result = result;
        } else
        {
            LackParamsException lackParamsException = new LackParamsException(null, null);
            throw lackParamsException;
        }


    }


    /**
     * @param method
     * @param passableTiedName
     * @param checkType
     * @param wPorter
     * @param request
     * @param response
     * @return
     */
    private Object checkPassable(RequestMethod method, String passableTiedName, CheckType checkType,
            WPorter wPorter, WPRequest request, WPResponse response)
    {
        Object pass = "";// 默认不通过
        if (checkType == CheckType.DEFAULT)
        {
            WPorter._FatherIn fatherIn = wPorter.getFatherIn();
            if (fatherIn.defaultCheck().equals(""))
            {
                checkType = defaultCheckType;
            } else
            {
                CheckPassable checkPassable = wPorter.getCheckPassable(fatherIn.defaultCheck());
                if (checkPassable == null)
                {
                    return "CheckPassable '" + fatherIn.defaultCheck()
                            + "' is null!";
                }
                return checkPassable.willPass(method, request, response);
            }
        }
        switch (checkType)
        {

            case GLOBAL:
                // 使用全局安全检测对象来检测
                if (globleCheck != null)
                {
                    pass = globleCheck.willPass(method, request, response);
                }
                break;

            case SELF:
                // 调用自己的安全检测对象
                if (wPorter.getCheckPassable(passableTiedName) != null)
                {
                    pass = wPorter.getCheckPassable(passableTiedName).willPass(method, request, response);
                }
                break;

            case SELF_GLOBAL:
                if (wPorter.getCheckPassable(passableTiedName) != null)
                {
                    pass = wPorter.getCheckPassable(passableTiedName).willPass(method, request, response);
                }

                if (pass == null && globleCheck != null)
                {
                    pass = globleCheck.willPass(method, request, response);
                }

                break;

            case GLOBAL_SELF:
                if (globleCheck != null)
                {
                    pass = globleCheck.willPass(method, request, response);
                }
                if (pass == null && wPorter.getCheckPassable(passableTiedName) != null)
                {
                    pass = wPorter.getCheckPassable(passableTiedName).willPass(method, request, response);
                }

                break;

            case NONE:
                // 直接通过
                pass = null;
                break;

            default:
                break;
        }

        return pass;
    }

    private void writeEx(WPRequest request, WPResponse response, WPObject wpObject, JResponse jResponse,
            ResponseType responseType,
            WPorterType wPorterType,
            ThinkType thinkType)
    {

        if (thinkType == ThinkType.REST)
        {
            if(responseLosedArgs){
                jResponse.setUri(request.getRequestURI());
            }
            _onExListener(request, jResponse);
            WPPrinter wpPrinter = null;
            try
            {
                int code = jResponse.getCode().toCode();
                if (HttpStatusCode.isHttpStatusCode(code))
                {
                    response.sendError(code);
                }
                // else
                // {
                // response.sendError(HttpStatusCode.BAD_REQUEST);
                // }

                wpPrinter = response.getPrinter();
                wpPrinter.print(jResponse);
                wpPrinter.flush();
            } catch (IOException e)
            {
                wpLog.error(e.getMessage(), e);
            } finally
            {
                WPTool.close(wpPrinter);
            }
        } else
        {
            writeResponse(request, response, wpObject, jResponse, responseType, wPorterType, ThinkType.DEFAULT);
        }
    }

    private void writeResponse(WPRequest request, WPResponse response, WPObject wpObject, Object object,
            ResponseType responseType,
            WPorterType wPorterType,
            ThinkType thinkType)
    {
        if (object != null && requestExListener != null && (object instanceof JResponse) && ((JResponse) object)
                .getCode() != ResultCode.SUCCESS)
        {
            if(responseLosedArgs){
                ((JResponse) object).setUri(request.getRequestURI());
            }
            _onExListener(request, (JResponse) object);
        }
        responseDealt.writeResponse(request, response, wpObject, object, responseType, wPorterType, thinkType);
    }

    private void _onExListener(WPRequest request, JResponse jResponse)
    {
        if (requestExListener != null)
        {
            requestExListener.onEx(jResponse, wpLog);
        }
    }

    public WPLog<?> getWPLog()
    {
        return wpLog;
    }
}

package com.chenyg.wporter;

import java.io.Serializable;

import com.chenyg.wporter.annotation.ThinkType;
import com.chenyg.wporter.base.*;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 响应处理
 *
 * @author ZhuiFeng
 */
@SuppressWarnings("serial")
abstract class ResponseDealt implements Serializable
{

    protected boolean isDebug;

    public ResponseDealt(boolean isDebug){
        this.isDebug=isDebug;
    }

    /**
     * @param request
     * @param response
     * @param object
     * @param responseType
     * @param thinkType
     */
    public abstract void writeResponse(WPRequest request, WPResponse response,WPObject wpObject, Object object, ResponseType responseType,
            WPorterType wPorterType, ThinkType thinkType);

    public void setContentType(WPRequest request, WPResponse response, Object object, ResponseType responseType,
            WPorterType wPorterType)
    {

        if (wPorterType != WPorterType.APP)
        {
            String ctype = response.getContentType();
            if ((ctype == null || ctype.length() == 0) && responseType != ResponseType.SelfResponse)
            {

                if (object != null && ((object instanceof JResponse) || (object instanceof JSONObject) ||
                        (object instanceof JSONArray) || (object instanceof JSONHeader)))
                {

                    response.setContentType(ContentType.APP_JSON.getType());
                } else
                {
                    response.setContentType(ContentType.TEXT_PLAIN.getType());
                }

                if (object != null && object instanceof JResponse)
                {
                    JResponse jResponse = (JResponse) object;
                    if (jResponse.getCode() != ResultCode.SUCCESS&&isDebug)
                    {
                        jResponse.setUri(request.getRequestURI());
                    }
                }

            }
        }

    }

}

package com.chenyg.wporter;

import java.io.IOException;

import com.chenyg.wporter.base.*;
import com.chenyg.wporter.log.WPLog;

import com.chenyg.wporter.annotation.ThinkType;
import com.chenyg.wporter.util.WPTool;

/**
 * 带有rest处理的
 */
class ResponseDealtWithREST extends ResponseDealt
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ResponseDealt restDealt;
    private WPLog<?> wpLog;

    public ResponseDealtWithREST(WPLog<?> wpLog, boolean isDebug)
    {
        super(isDebug);
        this.wpLog = wpLog;
        this.restDealt = new SimpleRestResponseDealt(isDebug);
    }


    @Override
    public void writeResponse(WPRequest request, WPResponse response, WPObject wpObject, Object object,
            ResponseType responseType,
            WPorterType wPorterType,
            ThinkType thinkType)
    {
        WPPrinter wpPrinter = null;
        boolean needCloseCall = false, success = false;
        try
        {

            if (thinkType == ThinkType.REST)
            {
                restDealt.writeResponse(request, response, wpObject, object, responseType, wPorterType, thinkType);
            } else
            {
                setContentType(request, response, object, responseType, wPorterType);
                wpPrinter = response.getPrinter();
                wpPrinter.print(object);
                wpPrinter.flush();
                success = true;
                needCloseCall = true;
            }

        } catch (IOException e)
        {
            wpLog.error(e.getMessage(), e);
        } catch (Exception e)
        {
            wpLog.error(e.getMessage(), e);

            try
            {
                response.sendError(HttpStatusCode.INTERNAL_SERVER_ERROR);
            } catch (IOException e1)
            {
                wpLog.error(e1.getMessage(), e1);
            }
        } finally
        {

            if (wpPrinter != null)
            {
                try
                {
                    wpPrinter.close();
                } catch (IOException e)
                {
                    success = false;
                }
            }

            if (needCloseCall && wpObject != null)
            {
                wpObject.closeListenerCall(success);
            }
        }
    }


    private static class SimpleRestResponseDealt extends ResponseDealt
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public SimpleRestResponseDealt(boolean isDebug)
        {
            super(isDebug);
        }

        @Override
        public void writeResponse(WPRequest request, WPResponse response, WPObject wpObject, Object object,
                ResponseType responseType,
                WPorterType wPorterType,
                ThinkType thinkType)
        {

            WPPrinter wpPrinter = null;
            boolean needCloseCall = false, success = false;
            try
            {

                if (willResponse(request, response, object, responseType))
                {
                    setContentType(request, response, object, responseType, wPorterType);
                    wpPrinter = response.getPrinter();
                    wpPrinter.print(object);
                    wpPrinter.flush();
                    needCloseCall = true;
                    success = true;
                }

            } catch (IOException e)
            {

            } catch (Exception e)
            {
                try
                {
                    response.sendError(HttpStatusCode.INTERNAL_SERVER_ERROR);
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }

            } finally
            {
                if (wpPrinter != null)
                {
                    try
                    {
                        wpPrinter.close();
                    } catch (IOException e)
                    {
                        success = false;
                    }
                }

                if (needCloseCall && wpObject != null)
                {
                    wpObject.closeListenerCall(success);
                }
            }
        }

        private boolean willResponse(WPRequest request, WPResponse response, Object object, ResponseType responseType)
        {
            boolean willR = true;

            if (object != null && (object instanceof JResponse))
            {
                willR = false;
                JResponse jResponse = (JResponse) object;
                int code = jResponse.getCode().toCode();
                switch (code)
                {
                    case HttpStatusCode.NO_CONTENT:
                    case HttpStatusCode.NOT_FOUND:
                        response.setStatus(code);
                        break;

                    default:
                        if (HttpStatusCode.isHttpStatusCode(code))
                        {
                            response.setStatus(code);
                        } else
                        {
                            response.setStatus(HttpStatusCode.OK);
                        }
                        willR = true;
                        break;
                }

            }

            return willR;
        }

    }

}

package com.chenyg.wporter.a.app;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.chenyg.wporter.UnsupportedException;
import com.chenyg.wporter.WPPrinter;
import com.chenyg.wporter.WPResponse;
import com.chenyg.wporter.log.LogUtil;

class AppResponse implements WPResponse
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AppReciever appReciever = new AppReciever();

    public AppResponse()
    {

    }

    AppReciever getAppReciever()
    {
        return appReciever;
    }

    @Override
    public String getCharacterEncoding()
    {
        return "";
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException
    {

    }

    @Override
    public String getContentType()
    {
        return null;
    }

    @Override
    public void setContentType(String type)
    {

    }

    @Override
    public void setContentLength(int len)
    {
        //throw new UnsupportedException("not support!");
    }

    @Override
    public int getContentLength()
    {
        throw new UnsupportedException("not support!"+ LogUtil.getCodePos());
    }

    @Override
    public void sendError(int errorCode) throws IOException
    {
        appReciever.errorCode = errorCode;
    }

    @Override
    public void addHeader(String name, String value)
    {
        appReciever.headers.put(name, value);
    }

    @Override
    public void setHeader(String name, String value)
    {
        appReciever.headers.put(name, value);
    }

    @Override
    public void setStatus(int sc)
    {
        appReciever.statusCode = sc;
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        throw new UnsupportedException("not support!");
    }

    private WPPrinter wpPrinter = new WPPrinter()
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void print(Object object) throws IOException
        {
            appReciever.list.add(object);
        }

        @Override
        public void flush()
        {

        }

        @Override
        public void close() throws IOException
        {

        }
    };

    @Override
    public WPPrinter getPrinter() throws IOException
    {
        return wpPrinter;
    }


}

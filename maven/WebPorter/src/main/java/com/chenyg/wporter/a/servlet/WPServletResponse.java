package com.chenyg.wporter.a.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import com.chenyg.wporter.WPPrinter;
import com.chenyg.wporter.WPResponse;

public class WPServletResponse implements WPResponse
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;
    private PrintWriter printWriter;

    WPServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }


    @Override
    public String getCharacterEncoding()
    {
        return response.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException
    {
        response.setCharacterEncoding(encoding);
    }

    @Override
    public String getContentType()
    {
        return response.getContentType();
    }

    @Override
    public void setContentType(String type)
    {
        response.setContentType(type);
    }

    private int contentLength;

    @Override
    public void setContentLength(int len)
    {
        this.contentLength = len;
        response.setContentLength(len);
    }

    @Override
    public int getContentLength()
    {
        return this.contentLength;
    }

    @Override
    public void sendError(int code) throws IOException
    {
        response.sendError(code);
    }

    @Override
    public void addHeader(String name, String value)
    {
        response.addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value)
    {
        response.setHeader(name, value);
    }

    @Override
    public void setStatus(int status)
    {
        response.setStatus(status);
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return response.getOutputStream();
    }

    private WPPrinter wPPrinter = new WPPrinter()
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void print(Object object) throws IOException
        {

            printWriter.print(object);

        }

        @Override
        public void flush()
        {
            printWriter.flush();
        }

        @Override
        public void close() throws IOException
        {
            printWriter.close();
        }
    };

    public HttpServletResponse getServletResponse()
    {
        return response;
    }

    @Override
    public WPPrinter getPrinter() throws IOException
    {
        if (printWriter == null)
        {
            printWriter = response.getWriter();
        }
        return wPPrinter;
    }

    public void sendRedirect(String redirect) throws IOException
    {
        response.sendRedirect(redirect);
    }

}

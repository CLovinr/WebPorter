package com.chenyg.wporter.simple;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import com.chenyg.wporter.WPFormFile;

class WPFormInputStream extends InputStream
{
    private WPorterFormUtil wPorterFormUtil;
    private long totalSize;
    private long currentSize;
    private boolean isClose;

    private WPFormFile wpFormFile;

    public WPFormInputStream(WPorterFormUtil wPorterFormUtil, String encoding)
            throws IOException
    {
        this.wPorterFormUtil = wPorterFormUtil;
        currentSize = 0;

        int val = wPorterFormUtil.nextUnShort();
        String json = wPorterFormUtil.nextParamValue(val, encoding);
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            SimpleWPFormFile wpFormFile = new SimpleWPFormFile();
            if (jsonObject.has("name"))
            {
                wpFormFile.name = jsonObject.getString("name");
            }
            if (jsonObject.has("size"))
            {
                wpFormFile.size = jsonObject.getLong("size");
            }
            if (jsonObject.has("type"))
            {
                wpFormFile.type = jsonObject.getString("type");
            }
            wpFormFile.in = this;
            this.totalSize = wpFormFile.size;
            this.wpFormFile = wpFormFile;
        } catch (JSONException e)
        {
            throw new IOException("format exception when read file info:" + e);
        }
    }

    WPFormFile getWPFormFile()
    {
        return wpFormFile;
    }

    @Override
    public int read() throws IOException
    {

        int x;
        if (currentSize >= totalSize)
        {
            x = -1;
        } else
        {
            ByteBuffer buffer = wPorterFormUtil.nextFileBytes(1);

            x = 0xff & buffer.array()[buffer.position()+buffer.arrayOffset()];
            currentSize++;
        }

        return x;

    }

    public long getTotalSize()
    {
        return totalSize;
    }

    /**
     * 当前文件是否已经读取完
     *
     * @return
     */
    boolean isFinished()
    {
        return currentSize >= totalSize;
    }

    /**
     * 是否已经关闭
     *
     * @return
     */
    public boolean isClosed()
    {
        return isClose;
    }

    @Override
    public void close() throws IOException
    {
        while (!isFinished())
        {
            read();
        }
        isClose = true;
    }

}

package com.chenyg.wporter.simple;

import java.io.*;
import java.nio.ByteBuffer;

import com.chenyg.wporter.WPFormFile;

public class SimpleWPFormFile extends WPFormFile
{
    String name;
    String type;
    long size;
    InputStream in;

    SimpleWPFormFile()
    {

    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public long size()
    {
        return size;
    }

    @Override
    public String getFileType()
    {
        return type;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return in;
    }


    /**
     * 通过字节数组构建。
     *
     * @param byteBuffer 数据
     * @param name  名称
     * @param type  类型
     * @return
     */
    public static WPFormFile toWPFormFile(final ByteBuffer byteBuffer, String name, String type)
    {
        SimpleWPFormFile simpleWPFormFile = new SimpleWPFormFile()
        {
            @Override
            public InputStream getInputStream() throws IOException
            {
                return new ByteArrayInputStream(byteBuffer.array(),byteBuffer.arrayOffset()+byteBuffer.position(),byteBuffer.remaining());
            }
        };
        simpleWPFormFile.name = name;
        simpleWPFormFile.size = byteBuffer.remaining();
        simpleWPFormFile.type = type;
        return simpleWPFormFile;
    }

    /**
     * 通过文件构建。
     *
     * @param file 文件
     * @param type 文件类型
     */
    public static WPFormFile toWPFormFile(final File file, String type)
    {
        SimpleWPFormFile simpleWPFormFile = new SimpleWPFormFile()
        {
            @Override
            public InputStream getInputStream() throws IOException
            {
                return new FileInputStream(file);
            }
        };
        simpleWPFormFile.name = file.getName();
        simpleWPFormFile.size = file.length();
        simpleWPFormFile.type = type;
        return simpleWPFormFile;
    }

}

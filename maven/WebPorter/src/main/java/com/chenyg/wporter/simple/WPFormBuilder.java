package com.chenyg.wporter.simple;

import com.chenyg.wporter.WPForm;
import com.chenyg.wporter.WPFormFile;
import com.chenyg.wporter.WPObject;
import com.chenyg.wporter.WPResponse;
import com.chenyg.wporter.base.AppValues;
import com.chenyg.wporter.base.ContentType;
import com.chenyg.wporter.util.BytesTool;
import com.chenyg.wporter.util.WPTool;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by ZhuiFeng on 2015/9/8.
 */
public class WPFormBuilder
{

    private String encoding = "";
    private Map<String, Object> params = new HashMap<String, Object>();
    private List<WPFormFile> wpFormFiles = new ArrayList<>(2);

    public static final int MAX_PARAM_COUNT = 255;


    /**
     * 设置参数编码方式，默认为"utf-8
     *
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    public void setEncoding(String encoding) throws UnsupportedEncodingException
    {
        this.encoding = encoding;
        "".getBytes(encoding);
    }


    private void checkParamCount(int n)
    {
        if (params.size() + n > MAX_PARAM_COUNT)
        {
            throw new RuntimeException("max param count is " + MAX_PARAM_COUNT);
        }
    }

    public WPFormBuilder param(AppValues appValues)
    {
        String[] names = appValues.getNames();
        checkParamCount(names.length);
        Object[] values = appValues.getValues();
        for (int i = 0; i < names.length; i++)
        {
            params.put(names[i], values[i]);
        }
        return this;
    }

    /**
     * 添加参数,最多为255个.
     *
     * @param name
     * @param value 可以是byte[],ByteBuffer,String，或其他Object(会调用toString转换成字符串).
     * @return
     */
    public WPFormBuilder param(String name, Object value)
    {
        checkParamCount(1);
        params.put(name, value);
        return this;
    }

    /**
     * 清除参数
     */
    public void clearParams()
    {
        params.clear();
    }


    /**
     * 添加formfile
     *
     * @param files
     * @return
     */
    public WPFormBuilder formFiles(WPFormFile... files)
    {
        for (int i = 0; i < files.length; i++)
        {
            wpFormFiles.add(files[i]);
        }
        return this;
    }

    public WPForm build()
    {
        return new WPForm(params, encoding)
        {

            int fileCount = wpFormFiles.size();
            int fileIndex = 0;

            @Override
            public int remainFileCount()
            {
                return fileCount - fileIndex;
            }

            @Override
            public boolean hasFile()
            {
                return fileIndex < fileCount;
            }

            @Override
            public WPFormFile nextFile() throws IOException
            {
                if (!hasFile())
                {
                    throw new IOException("there is no WPFormFile!");
                }

                WPFormFile wpFormFile = wpFormFiles.get(fileIndex++);
                return wpFormFile;
            }

            @Override
            public void close() throws IOException
            {

            }
        };
    }

    /**
     * 会写入头信息。
     *
     * @param wpForm
     * @param wpObject
     * @throws IOException
     */
    public static void write(WPForm wpForm, WPObject wpObject) throws IOException
    {
        WPResponse response = wpObject.getResponse();
        response.addHeader(ContentType.HEADER_NAME, "application/octet-stream");
        response.addHeader(WPForm.HEADER_NAME, ContentType.WPORTER_FORM.getType());
        write(wpForm, response.getOutputStream());
    }

    public void write(OutputStream os) throws IOException
    {
        write(build(), os);
    }

    /**
     * 往输出流输出WPForm
     *
     * @param wpForm
     * @param os
     * @throws IOException
     */
    public static void write(WPForm wpForm, OutputStream os) throws IOException
    {
        OutputStream bos = null;
        try
        {
            if (os instanceof BufferedOutputStream || os instanceof ByteArrayOutputStream)
            {
                bos = os;
            } else
            {
                bos = new BufferedOutputStream(os);
            }
            byte[] buf = new byte[2048];
            //参数个数
            writeShortValue(bos, wpForm.getParamCount() + 1, buf);
            String _encoding = wpForm.getParamEncoding();
            //第一个特殊参数
            byte[] bs = _encoding.getBytes();
            bos.write(bs.length);
            bos.write(bs);
            String encoding = WPTool.isNullOrEmpty(_encoding) ? "utf-8" : _encoding;
            writeShortValue(bos, 0, buf);

            Iterator<String> names = wpForm.getParamNames();
            int offset = 0, length = 0;
            while (names.hasNext())
            {
                String name = names.next();
                Object _value = wpForm.getParameter(name);

                if (_value == null)
                {
                    continue;
                }
                bs = name.getBytes(encoding);
                bos.write(bs.length);
                bos.write(bs);

                if (_value == null)
                {
                    bs = new byte[0];
                    length = 0;
                } else if (_value instanceof byte[])
                {
                    bs = (byte[]) _value;
                    length = bs.length;
                    offset = 0;
                } else if (_value instanceof ByteBuffer)
                {
                    ByteBuffer byteBuffer = (ByteBuffer) _value;
                    length = byteBuffer.remaining();
                    offset = byteBuffer.arrayOffset() + byteBuffer.position();
                    bs = byteBuffer.array();
                } else
                {
                    bs = _value.toString().getBytes(encoding);
                    offset = 0;
                    length = bs.length;
                }

                writeShortValue(bos, length, buf);
                if (length > 0)
                {
                    bos.write(bs, offset, length);
                }

            }

            int formfileCount = wpForm.remainFileCount();
            writeShortValue(bos, formfileCount, buf);
            while (wpForm.hasFile())
            {
                WPFormFile file = wpForm.nextFile();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", file.getName());
                jsonObject.put("size", file.size());
                jsonObject.put("type", file.getFileType());
                bs = jsonObject.toString().getBytes(encoding);
                writeShortLenData(bos, bs, buf);
                write(bos, file.getInputStream(), buf);
            }
            bos.flush();
        } catch (IOException e)
        {
            throw e;
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            WPTool.close(bos);
        }
    }


    private static void write(OutputStream os, InputStream in, byte[] buf) throws IOException
    {
        int n;
        while ((n = in.read(buf)) != -1)
        {
            os.write(buf, 0, n);
        }
    }

    private static void writeShortValue(OutputStream os, int n, byte[] buf) throws IOException
    {
        BytesTool.writeShort(buf, 0, n);
        os.write(buf, 0, 2);
    }

    private static void writeShortLenData(OutputStream os, byte[] bs, byte[] buf) throws IOException
    {
        writeShortValue(os, bs.length, buf);
        os.write(bs);
    }


    /**
     * 解析数据
     *
     * @param in
     * @param bufSize
     * @param defaultEncoding
     * @param paramValue2Str  是否把参数值转换为字符串。为否，则参数值都为byte[].
     * @return
     * @throws IOException
     */
    public static WPForm decode(InputStream in, int bufSize, String defaultEncoding,
            boolean paramValue2Str) throws IOException
    {
        try
        {

            final WPorterFormUtil porterFormUtil = new WPorterFormUtil(in, bufSize);

            int paramsCount = porterFormUtil.nextUnShort() - 1;
            if (paramsCount < 0)
            {
                paramsCount = 0;
            }

            Map<String, Object> params = new HashMap<String, Object>(paramsCount);

            // 解析第一个特殊的参数键值对
            int intVal = porterFormUtil.nextUnByte();
            //得到编码方式
            String encoding = porterFormUtil.nextParamName(intVal, "utf-8");
            if (WPTool.isNullOrEmpty(encoding))
            {
                encoding = WPTool.isNullOrEmpty(defaultEncoding) ? "utf-8" : defaultEncoding;
            }


            //特殊值未使用
            intVal = porterFormUtil.nextUnShort();
            porterFormUtil.nextParamValue(intVal, encoding);


            // 解析参数
            for (int i = 0; i < paramsCount; i++)
            {
                intVal = porterFormUtil.nextUnByte();
                String name = porterFormUtil.nextParamName(intVal, encoding);
                intVal = porterFormUtil.nextUnShort();
                Object value;
                if (paramValue2Str)
                {
                    value = porterFormUtil.nextParamValue(intVal, encoding);
                } else
                {
                    value = porterFormUtil.nextBytes(intVal);
                }
                params.put(name, value);
            }

            final String finalEncoding = encoding;
            final WPForm wpForm = new WPForm(params, finalEncoding)
            {
                int fileCount = porterFormUtil.nextUnShort();
                WPFormInputStream formInputStream = null;

                @Override
                public void close() throws IOException
                {
                    porterFormUtil.close();

                }

                @Override
                public WPFormFile nextFile() throws IOException
                {
                    if (!hasFile())
                    {
                        throw new IOException("there is no file!");
                    }
                    if (formInputStream != null && !formInputStream.isClosed())
                    {
                        throw new IOException("last file not closed!!");
                    }
                    fileCount--;
                    formInputStream = new WPFormInputStream(porterFormUtil, finalEncoding);
                    return formInputStream.getWPFormFile();
                }

                @Override
                public boolean hasFile()
                {
                    return fileCount > 0;
                }

                @Override
                public int remainFileCount()
                {
                    return fileCount;
                }
            };

            return wpForm;

        } catch (IOException e)
        {
            throw e;

        } finally
        {
            WPTool.close(in);
        }
    }
}

package com.chenyg.wporter.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WPTool
{


    public static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
     * 判断是否为null或"".
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj)
    {
        return obj == null || "".equals(obj);
    }

    /**
     * 返回c1是c2的第几代子类。
     * -1表示不是子类，0表示是本身，1表示是第一代子类...
     *
     * @param c1 若为null,则会返回-1
     * @param c2
     * @return
     */
    public static int subclassOf(Class<?> c1, Class<?> c2)
    {
        if (c1 == null)
        {
            return -1;
        } else if (c1.getName().equals(c2.getName()))
        {
            return 0;
        } else
        {
            int n = subclassOf(c1.getSuperclass(), c2);
            return n == -1 ? -1 : n + 1;
        }
    }

    /**
     * 判断c1是否是c2的直接或间接子类.
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean isSubclassOf(Class<?> c1, Class<?> c2)
    {
        return subclassOf(c1, c2) > 0;
    }


    /**
     * obj1和obj2都为null或者obj1不为null且obj1.equals(obj2)返回true时，结果为true；否则返回false。
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isEqual(Object obj1, Object obj2)
    {
        if (obj1 == null)
        {
            return obj2 == null;
        } else
        {
            return obj1.equals(obj2);
        }
    }

    /**
     * 判断obj是否是c的实例。
     *
     * @param obj
     * @param c
     * @return 若obj为null，返回false；否则返回判断结果。
     */
    public static boolean instanceOf(Object obj, Class<?> c)
    {
        if (obj != null)
        {
            return isSubclassOf(obj.getClass(), c);
        } else
        {
            return false;
        }
    }

    public static void close(PreparedStatement ps)
    {
        if (ps != null)
        {
            try
            {
                ps.close();
            } catch (SQLException e)
            {
            }
        }
    }

    /**
     * 若不为null则调用关闭closeable.close().
     *
     * @param closeable
     */
    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否‘不为null且不为""’.
     *
     * @param str
     * @return
     */
    public static boolean notNullAndEmpty(String str)
    {
        return str != null && !str.equals("");
    }

    /**
     * 是否为null或"".
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str)
    {
        return str == null || str.equals("");
    }

    /**
     * @param array
     * @param object
     * @return 返回索引
     */
    public static int contains(JSONArray array, Object object)
    {
        int index = -1;
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                if (array.get(i).equals(object))
                {
                    index = i;
                    break;
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * 在json对象里是否含有指定的键值
     *
     * @param array  存放的是json对象
     * @param key
     * @param object
     * @return 返回索引
     */
    public static int containsJsonValue(JSONArray array, String key, Object object)
    {
        int index = -1;
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.has(key) && jsonObject.get(key).equals(object))
                {
                    index = i;
                    break;
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * 得到接口数据
     *
     * @param porterUrl
     * @param params
     * @param connectTimeout
     * @param encode
     * @return
     * @throws IOException
     */
    public static String getResponseFromGET(String porterUrl, Map<String, String> params, int connectTimeout,
            String encode) throws IOException
    {
        StringBuilder sBuilder = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader bReader = null;
        try
        {
            if (params != null && params.size() > 0)
            {
                StringBuilder paramsBuilder = new StringBuilder();
                Iterator<String> keys = params.keySet().iterator();
                while (keys.hasNext())
                {
                    String key = keys.next();
                    String value = params.get(key);
                    paramsBuilder.append(URLEncoder.encode(key, encode)).append("=")
                            .append(URLEncoder.encode(value, encode)).append("&");
                }

                porterUrl += (porterUrl.indexOf("?") == -1 ? "?" : "&") + paramsBuilder
                        .substring(0, paramsBuilder.length() - 1);
            }
            URL url = new URL(porterUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true);

            InputStream in = conn.getInputStream();
            String _encode = conn.getContentEncoding();
            if (_encode == null)
            {
                _encode = encode;
            }

            bReader = new BufferedReader(new InputStreamReader(in, _encode));
            String line;
            while ((line = bReader.readLine()) != null)
            {
                sBuilder.append(line);
            }
            bReader.close();
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            close(bReader);
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return sBuilder.toString();
    }

    /**
     * 得到接口数据,Content-Type:application/x-www-form-urlencoded.
     *
     * @param porterUrl
     * @param params
     * @param connectTimeout
     * @param encode
     * @return
     * @throws IOException
     */
    public static String getResponseFromPOST(String porterUrl, Map<String, String> params, int connectTimeout, String
            encode) throws IOException
    {
        StringBuilder sBuilder = new StringBuilder();
        HttpURLConnection conn = null;
        OutputStream os = null;
        BufferedReader bReader = null;
        try
        {
            URL url = new URL(porterUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true);
            if (params != null && params.size() > 0)
            {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encode);
                StringBuilder paramsBuilder = new StringBuilder();
                Iterator<String> keys = params.keySet().iterator();
                while (keys.hasNext())
                {
                    String key = keys.next();
                    String value = params.get(key);
                    paramsBuilder.append(URLEncoder.encode(key, encode)).append("=")
                            .append(URLEncoder.encode(value, encode)).append("&");
                }
                os = conn.getOutputStream();
                os.write(paramsBuilder.substring(0, paramsBuilder.length() - 1).getBytes());
                os.flush();
            }

            InputStream in = conn.getInputStream();
            String _encode = conn.getContentEncoding();
            if (_encode == null)
            {
                _encode = encode;
            }

            bReader = new BufferedReader(new InputStreamReader(in, _encode));

            String line;
            while ((line = bReader.readLine()) != null)
            {
                sBuilder.append(line);
            }
            bReader.close();
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            close(os);
            close(bReader);
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return sBuilder.toString();
    }

    /**
     * 得到接口数据
     *
     * @param porterUrl
     * @param bs             要输出的数据。
     * @param connectTimeout
     * @param defaultEncode  若返回头中没有字符编码说明则使用该编码.
     * @return
     * @throws IOException
     */
    public static String getResponseByPOSTBinaryData(String porterUrl, byte[] bs, int connectTimeout, int
            writeBufSize, String defaultEncode) throws IOException
    {
        return getResponseByPOSTBinaryData(porterUrl, new ByteArrayInputStream(bs), connectTimeout, writeBufSize,
                defaultEncode);
    }

    /**
     * 得到接口数据
     *
     * @param porterUrl
     * @param in             要输出的数据流。
     * @param connectTimeout
     * @param defaultEncode  若返回头中没有字符编码说明则使用该编码.
     * @return
     * @throws IOException
     */
    public static String getResponseByPOSTBinaryData(String porterUrl, InputStream in, int connectTimeout, int
            writeBufSize, String defaultEncode) throws IOException
    {
        StringBuilder sBuilder = new StringBuilder();
        HttpURLConnection conn = null;
        OutputStream os = null;
        BufferedReader bReader = null;
        try
        {
            URL url = new URL(porterUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            byte[] buf = new byte[writeBufSize];
            int n;
            while ((n = in.read(buf)) != -1)
            {
                os.write(buf, 0, n);
            }
            os.flush();
            String encode = conn.getContentEncoding();
            if (encode == null)
            {
                encode = defaultEncode;
            }
            bReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
            String line;
            while ((line = bReader.readLine()) != null)
            {
                sBuilder.append(line);
            }
            bReader.close();
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            close(os);
            close(in);
            close(bReader);
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return sBuilder.toString();
    }

    /**
     * 支持基本数据类型，json，String类型.
     *
     * @param values 字符串的值
     * @param types  类型，与values对应
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ParseException
     */
    public static Object[] parse(String[] values, Class<?>[] types) throws NoSuchMethodException, SecurityException,
            ParseException
    {
        Object[] objects = new Object[values.length];
        for (int i = 0; i < objects.length; i++)
        {
            Constructor<?> constructor = types[i].getConstructor(String.class);
            try
            {
                Object object = constructor.newInstance(values[i]);
                objects[i] = object;
            } catch (Exception e)
            {
                ParseException parseException = new ParseException(types[i], values[i], e.getMessage());
                throw parseException;
            }
        }
        return objects;
    }

    public static class ParseException extends Exception
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private String value;
        private Class<?> type;
        private String info;

        public ParseException(Class<?> type, String value, String info)
        {
            this.value = value;
            this.type = type;
            this.info = info;
        }

        @Override
        public String toString()
        {
            String string = "can't parse '" + value
                    + "' to "
                    + getType().getName()
                    + " \n"
                    + info;
            return string;
        }

        /**
         * 得到转换类型.
         *
         * @return
         */
        public Class<?> getType()
        {
            return type;
        }

        /**
         * 得到参数值
         *
         * @return
         */
        public String getValue()
        {
            return value;
        }

    }

}

package com.chenyg.wporter.util;

import com.chenyg.wporter.log.LogUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * WPTool Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>ÎåÔÂ 7, 2016</pre>
 */
public class WPToolTest
{

    @Before
    public void before() throws Exception
    {
    }

    @After
    public void after() throws Exception
    {
    }

    /**
     * Method: isEmpty(Object obj)
     */
    @Test
    public void testIsEmpty() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: subclassOf(Class<?> c1, Class<?> c2)
     */
    @Test
    public void testSubclassOf() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isSubclassOf(Class<?> c1, Class<?> c2)
     */
    @Test
    public void testIsSubclassOf() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isEqual(Object obj1, Object obj2)
     */
    @Test
    public void testIsEqual() throws Exception
    {
//TODO: Test goes here...
        String x = "123";
        String y = new String("123");
        LogUtil.printErrPosLn(x==y);
        LogUtil.printErrPosLn(WPTool.isEqual(x,y));
        LogUtil.printErrPosLn(WPTool.isEqual(null,y));
        LogUtil.printErrPosLn(WPTool.isEqual(x,null));
    }

    /**
     * Method: instanceOf(Object obj, Class<?> c)
     */
    @Test
    public void testInstanceOf() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: close(PreparedStatement ps)
     */
    @Test
    public void testClosePs() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: close(Closeable closeable)
     */
    @Test
    public void testCloseCloseable() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: notNullAndEmpty(String str)
     */
    @Test
    public void testNotNullAndEmpty() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isNullOrEmpty(String str)
     */
    @Test
    public void testIsNullOrEmpty() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: contains(JSONArray array, Object object)
     */
    @Test
    public void testContains() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: containsJsonValue(JSONArray array, String key, Object object)
     */
    @Test
    public void testContainsJsonValue() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResponseFromGET(String porterUrl, Map<String, String> params, int connectTimeout, String encode)
     */
    @Test
    public void testGetResponseFromGET() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResponseFromPOST(String porterUrl, Map<String, String> params, int connectTimeout, String
     * encode)
     */
    @Test
    public void testGetResponseFromPOST() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResponseByPOSTBinaryData(String porterUrl, byte[] bs, int connectTimeout, int
     * writeBufSize, String defaultEncode)
     */
    @Test
    public void testGetResponseByPOSTBinaryDataForPorterUrlBsConnectTimeoutWriteBufSizeDefaultEncode() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResponseByPOSTBinaryData(String porterUrl, InputStream in, int connectTimeout, int
     * writeBufSize, String defaultEncode)
     */
    @Test
    public void testGetResponseByPOSTBinaryDataForPorterUrlInConnectTimeoutWriteBufSizeDefaultEncode() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: parse(String[] values, Class<?>[] types)
     */
    @Test
    public void testParse() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getType()
     */
    @Test
    public void testGetType() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getValue()
     */
    @Test
    public void testGetValue() throws Exception
    {
//TODO: Test goes here... 
    }


} 

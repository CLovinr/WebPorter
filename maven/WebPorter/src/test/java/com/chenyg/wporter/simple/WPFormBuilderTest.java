package com.chenyg.wporter.simple;

import com.chenyg.wporter.WPForm;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

/**
 * WPFormBuilder Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 2, 2016</pre>
 */
public class WPFormBuilderTest
{

    private byte[] bs;

    @Before
    public void before() throws Exception
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        WPFormBuilder wpFormBuilder = new WPFormBuilder();
        wpFormBuilder.param("name", "tom").param("age", 1);
        wpFormBuilder.write(data);
        bs = data.toByteArray();
    }

    @After
    public void after() throws Exception
    {
    }

    /**
     * Method: setEncoding(String encoding)
     */
    @Test
    public void testSetEncoding() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: param(AppValues appValues)
     */
    @Test
    public void testParamAppValues() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: param(String name, Object value)
     */
    @Test
    public void testParamForNameValue() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: clearParams()
     */
    @Test
    public void testClearParams() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: formFiles(WPFormFile... files)
     */
    @Test
    public void testFormFiles() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: build()
     */
    @Test
    public void testBuild() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: write(WPForm wpForm, WPObject wpObject)
     */
    @Test
    public void testWriteForWpFormWpObject() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: write(WPForm wpForm, OutputStream os)
     */
    @Test
    public void testWriteForWpFormOs() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: decode(InputStream in, int bufSize, String defaultEncoding, boolean paramValue2Str)
     */
    @Test
    public void testDecode() throws Exception
    {
//TODO: Test goes here...
        WPForm wpForm = WPFormBuilder.decode(new ByteArrayInputStream(bs), 2048, null, false);
        Iterator<String> names = wpForm.getParamNames();
        while (names.hasNext())
        {
            String name = names.next();
            System.out.println(name + "=" + new String((byte[])wpForm.getParameter(name)));
        }
    }


    /**
     * Method: checkParamCount(int n)
     */
    @Test
    public void testCheckParamCount() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = WPFormBuilder.getClass().getMethod("checkParamCount", int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: write(OutputStream os, InputStream in, byte[] buf)
     */
    @Test
    public void testWrite() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = WPFormBuilder.getClass().getMethod("write", OutputStream.class, InputStream.class, byte[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: writeShortValue(OutputStream os, int n, byte[] buf)
     */
    @Test
    public void testWriteShortValue() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = WPFormBuilder.getClass().getMethod("writeShortValue", OutputStream.class, int.class, byte[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: writeShortLenData(OutputStream os, byte[] bs, byte[] buf)
     */
    @Test
    public void testWriteShortLenData() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = WPFormBuilder.getClass().getMethod("writeShortLenData", OutputStream.class, byte[].class, byte[]
   .class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 

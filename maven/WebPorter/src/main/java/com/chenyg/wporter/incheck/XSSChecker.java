package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;
import org.owasp.esapi.Encoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 需要esapi
 * Created by 刚帅 on 2016/1/8.
 */
public class XSSChecker extends InCheckParser.Checker
{
    private static final Pattern PATTERN_SCRIPT = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_SRC1 = Pattern
            .compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern PATTERN_SRC2 = Pattern
            .compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern PATTERN_SCRIPT_END = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_SCRIPT_START = Pattern
            .compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern PATTERN_EVAL = Pattern
            .compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern PATTERN_E_XPRESSION = Pattern
            .compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern PATTERN_JAVASCRIPT = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_VBSCRIPT = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_ONLOAD = Pattern
            .compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);


    private Object encoder;
    private Method method;
    private Custom custom;

    public interface Custom
    {
        String xssDeal(@NotNULL String value);
    }

    /**
     * 使用esapi。
     *
     * @param encoder
     * @param encodeMethod 调用的方法。
     */
    public XSSChecker(Encoder encoder, String encodeMethod)
    {
        this.encoder = encoder;
        try
        {
            method = encoder.getClass().getMethod(encodeMethod, String.class);
            method.invoke(encoder, "<html></html>");
        } catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        custom = new Custom()
        {
            @Override
            public String xssDeal(@NotNULL String value)
            {
                String toV = null;
                try
                {
                    toV = (String) method.invoke(XSSChecker.this.encoder, value);
                } catch (Exception e)
                {

                }

                return toV;
            }
        };
    }

    public XSSChecker(Custom custom)
    {
        this.custom = custom;
    }


    /**
     *
     * @param isEscap 是否采用转义的方式
     * @param exceptAMP 是否除去对'&'符号的转义
     */
    public XSSChecker(boolean isEscap, final boolean exceptAMP)
    {
        if (isEscap)
        {
            custom = new Custom()
            {
                @Override
                public String xssDeal(@NotNULL String value)
                {
                    return htmlEncode(value, exceptAMP);
                }
            };
        } else
        {

            custom = new Custom()
            {
                @Override
                public String xssDeal(@NotNULL String value)
                {
                    return stripXSS(value);
                }
            };

        }
    }


    public static String htmlEncode(String source, boolean exceptAMP)
    {
        if (source == null)
        {
            return "";
        }

        String html;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++)
        {
            char c = source.charAt(i);
            switch (c)
            {
                case '<':
                    buffer.append("&#60;");
                    break;
                case '>':
                    buffer.append("&#62;");
                    break;
                case '&':
                    if (exceptAMP)
                    {
                        buffer.append('&');
                    }else{
                        buffer.append("&#38;");
                    }

                    break;
                case '"':
                    buffer.append("&#34;");
                    break;
                case '\'':
                    buffer.append("&#39;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        html=html.replaceAll("&#([^0-9])","&#38;&#35;$1");
        return html;
    }

    private String stripXSS(String value)
    {

        // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
        // avoid encoded attacks.
        // value = ESAPI.encoder().canonicalize(value);

        // Avoid null characters
        value = value.replaceAll("", "");
        // Avoid anything between script tags
        value = PATTERN_SCRIPT.matcher(value).replaceAll("");
        // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
        value = PATTERN_SRC1.matcher(value).replaceAll("");
        value = PATTERN_SRC2.matcher(value).replaceAll("");

        // Remove any lonesome </script> tag
        value = PATTERN_SCRIPT_END.matcher(value).replaceAll("");
        // Remove any lonesome <script ...> tag
        value = PATTERN_SCRIPT_START.matcher(value).replaceAll("");
        // Avoid eval(...) e­xpressions
        value = PATTERN_EVAL.matcher(value).replaceAll("");
        // Avoid e­xpression(...) e­xpressions
        value = PATTERN_E_XPRESSION.matcher(value).replaceAll("");
        // Avoid javascript:... e­xpressions
        value = PATTERN_JAVASCRIPT.matcher(value).replaceAll("");
        // Avoid vbscript:... e­xpressions
        value = PATTERN_VBSCRIPT.matcher(value).replaceAll("");
        // Avoid onload= e­xpressions
        value = PATTERN_ONLOAD.matcher(value).replaceAll("");

        return value;
    }

    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        String str = custom.xssDeal(value.toString());
        InCheckParser.CheckResult result = new InCheckParser.CheckResult(str);
        return result;
    }
}

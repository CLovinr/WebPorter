package com.chenyg.wporter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.chenyg.wporter.annotation.ChildIn;
import com.chenyg.wporter.annotation.ChildOut;
import com.chenyg.wporter.annotation.FatherIn;
import com.chenyg.wporter.annotation.ParamExtra;
import com.chenyg.wporter.annotation.DBAnnotation.Key;
import com.chenyg.wporter.util.WPTool;

/**
 * 网址接口化,当非必须参数未提供时，得到的将都是null.
 * <pre>
 * <strong>注意:</strong>
 * 1.子类应是非private类型的，且抽象类会被忽略，必须含有默认构造函数。
 * 2.对应的接口函数应是public的,否则会被忽略。
 * 3.父类的接口函数也会被扫描到.
 * 4.无论构造多少个WebPorter实例，相同类的hashCode是一样的，equals也相等.
 * 5.当接口函数没有提供{@linkplain ChildOut}时，则默认是{@linkplain ChildOut#value()}=={@linkplain com.chenyg.wporter.base
 * .ResponseType#DirectResult}
 * 6.WebPorter类{@linkplain FatherIn#useClassName()}=true,即若子类不使用@FatherIn注解，则默认使用{@linkplain FatherIn#useClassName()
 * }=true的规则进行名称绑定。
 * 7.接口函数可以用{@linkplain ParamExtra}进行参数描述。
 * </pre>
 *
 * @author Administrator
 */
@FatherIn(tiedName = "", useClassName = true)
public abstract class WebPorter implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HashMap<String, CheckPassable> passableMap = null;
    private InCheckParser inCheckParser;
    private static int count;
    private int id;

    boolean isAlreadyExited = false;

    public WebPorter()
    {
        id = count++;
    }


    /**
     * 得到id
     * @return id
     */
    public final int getId()
    {
        return id;
    }


    /**
     * return getClass().hashCode();
     */
    @Override
    public final int hashCode()
    {
        return getClass().hashCode();
    }

    /**
     * <pre>
     *
     * if (obj != null)
     * {
     *     return this.getClass().equals(obj.getClass());
     * }
     * return false;
     *
     * </pre>
     */
    @Override
    public final boolean equals(Object obj)
    {
        if (obj != null)
        {
            return this.getClass().equals(obj.getClass());
        }
        return false;
    }

    /**
     * 根据类名得到绑定名：会去掉末尾的Porter
     *
     * @param c
     * @return
     */
    private static String getTiedNameFromClass(Class<?> c)
    {
        String name = c.getSimpleName();
        if (name.endsWith("Porter"))
        {
            return name.substring(0, name.length() - 6) + "/";
        } else
        {
            return name + "/";
        }
    }

    /**
     * 得到方法绑定名
     *
     * @param method 方法对象
     * @return 绑定名称
     */
    public static String getFunTiedName(Method method)
    {
        ChildIn childIn = method.getAnnotation(ChildIn.class);
        switch (childIn.method())
        {
            case WS_DISCONNECT:
            case WS_OPEN:
                return "";
            default:
                String tiedName = childIn.tiedName().equals("") ? method.getName()
                        : childIn.tiedName();
                return tiedName;
        }
    }

    /**
     * 得到类绑定名
     *
     * @param c 类
     * @return 类绑定名
     */
    public static String getTiedName(Class<?> c)
    {
        FatherIn fatherIn = c.getAnnotation(FatherIn.class);
        if (c == WebPorter.class)
        {
            return fatherIn.tiedName();
        }
        String name = fatherIn.useClassName() &&WPTool.isEmpty(fatherIn.tiedName())? getTiedNameFromClass(c)
                : fatherIn.tiedName();
        if (fatherIn.addSTiedName())
        {
            if (WPTool.isSubclassOf(c.getSuperclass(), WebPorter.class))
            {
                name = getTiedName(c.getSuperclass()) + name;
            }
        }
        return name;
    }

    /**
     * 得到类的绑定名称
     *
     * @return 类绑定名
     */
    public String getTiedName()
    {
        return getTiedName(getClass());
    }

    public void setInCheckParser(InCheckParser inCheckParser)
    {
        this.inCheckParser = inCheckParser;
    }

    /**
     * 设置参数检查转换对象。
     *
     * @return 参数检查对象
     */
    public InCheckParser getInCheckParser()
    {
        return inCheckParser;
    }


    /**
     * 添加检测对象（默认为null）. <br>
     * 只有1)当设置检测类型为{@linkplain com.chenyg.wporter.base.CheckType#NONE}， 或者,2)检测类型不为
     * {@linkplain com.chenyg.wporter.base.CheckType#NONE}也不为null而且检测通过时，才算通过安全检测。
     * (会先进行安全检测，然后才去处理必须与非必须参数)
     *
     * @param tiedName      绑定名字
     * @param checkPassable 通过检查对象
     */
    public void addCheckPassable(String tiedName, CheckPassable checkPassable)
    {
        if (passableMap == null)
        {
            passableMap = new HashMap<String, CheckPassable>();
        }
        passableMap.put(tiedName, checkPassable);
    }

    /**
     * 移除所有绑定的检测对象。
     */
    public void removeAllCheckPassable()
    {
        if (passableMap != null)
        {
            passableMap.clear();
        }
    }

    /**
     * 得到检测对象。 <br>
     * 只有1)当设置检测类型为{@linkplain com.chenyg.wporter.base.CheckType#NONE}， 或者,2)检测类型不为
     * {@linkplain com.chenyg.wporter.base.CheckType#NONE}也不为null而且检测通过时，才算通过安全检测。
     * (会先进行安全检测，然后才去处理必须与非必须参数)
     *
     * @param tiedName 绑定名字
     * @return CheckPassable
     */
    public CheckPassable getCheckPassable(String tiedName)
    {
        return passableMap == null ? null : passableMap.get(tiedName);
    }

    /**
     * 通过扫描@Key注解的字段，来自动绑定类型,见{@linkplain InCheckParser}。
     *
     * @param useKeyValue 是否使用@Key的value值作为键名。
     * @param classes 要扫描的类
     */
    protected void addParserType(boolean useKeyValue, Class<?>... classes)
    {
        for (Class<?> class1 : classes)
        {
            _addParser(useKeyValue, class1);
        }
    }

    private void _addParser(boolean useValue, Class<?> dataClass)
    {
        Field[] fields = dataClass.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(Key.class))
            {
                Key key = field.getAnnotation(Key.class);
                String name;
                if (useValue)
                {

                    name = "".equals(key.value()) ? field.getName()
                            : key.value();
                } else
                {
                    name = field.getName();
                }
                String type = key.type().equals("") ? InCheckParser.getType(field)
                        : key.type();
                getInCheckParser().bindChecker(name, key.udesc(), type);
            }
        }
    }

    /**
     * 退出时调用
     */
    protected void onExiting()
    {

    }

}

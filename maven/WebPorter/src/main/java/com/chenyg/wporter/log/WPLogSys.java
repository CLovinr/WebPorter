package com.chenyg.wporter.log;

/**
 * System.out或System.err
 * Created by 宇宙之灵 on 2015/10/20.
 */
public class WPLogSys implements WPLog<Object>
{
    @Override
    public WPLog<Object> log(Object type, Object... objects)
    {
        return _log(false, false, objects);
    }


    /**
     * 打印当前位置
     */
    private static void printErrLn(Object... objects)
    {
        for (Object object : objects)
        {
            System.err.print(object);
        }
        System.err.println();
    }

    /**
     * 打印当前位置
     */
    private static void printLn(Object... objects)
    {
        for (Object object : objects)
        {
            System.out.print(object);
        }
        System.out.println();
    }

    private WPLog<Object> _log(boolean isErr, boolean printPos, Object... objs)
    {
        if (objs.length == 2 && objs[1] != null && objs[1] instanceof Throwable)
        {
            if (printPos)
            {
                LogUtil.printErrPosLnS(3);
            }
            Throwable t = (Throwable) objs[1];
            if (isErr)
            {
                System.err.println(objs[0]);
            } else
            {
                System.out.println(objs[0]);
            }
            t.printStackTrace();
        } else
        {
            if (isErr)
            {

                if (printPos)
                {
                    LogUtil.printErrPosLnS(3, objs);
                } else
                {
                    printErrLn(objs);
                }
            } else
            {
                if (printPos)
                {
                    LogUtil.printPosLnS(3, objs);
                } else
                {
                    printLn(objs);
                }

            }
        }
        return this;
    }

    @Override
    public WPLog<Object> trace(Object... objects)
    {
        return _log(false, false, objects);
    }

    @Override
    public WPLog<Object> debug(Object... objects)
    {
        return _log(true, true, objects);
    }

    @Override
    public WPLog<Object> info(Object... objects)
    {
        return _log(false, false, objects);
    }

    @Override
    public WPLog<Object> warn(Object... objects)
    {
        return _log(true, false, objects);
    }

    @Override
    public WPLog<Object> error(Object... objects)
    {
        return _log(true, true, objects);
    }

    @Override
    public WPLog<Object> fatal(Object... objects)
    {
        return _log(true, true, objects);
    }
}

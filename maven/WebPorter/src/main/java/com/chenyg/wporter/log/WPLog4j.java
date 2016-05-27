package com.chenyg.wporter.log;


import com.chenyg.wporter.util.WPTool;
import org.apache.log4j.Logger;

/**
 * Created by 宇宙之灵 on 2015/10/20.
 */
public class WPLog4j implements WPLog<WPLog4j.Type>
{

    private Logger logger = null;

    private int codeStack = 3;

    public enum Type
    {
        TRACE, ERROR, INFO, DEBUG, FATAL, WARN
    }

    public WPLog4j(String name)
    {
        logger = Logger.getLogger(name);
        init();
    }

    public WPLog4j(Class<?> c)
    {
        logger = Logger.getLogger(c);
        init();
    }

    public WPLog4j addCodeStackDistance(int distance)
    {
        this.codeStack += distance;
        return this;
    }

    private void init()
    {
        int n = WPTool.subclassOf(this.getClass(), WPLog4j.class);
        if (n != -1)
        {
            codeStack += 1;
        }
    }

    @Override
    public WPLog4j log(Type type, Object... objs)
    {
        return _log(type, objs);
    }

    private WPLog4j _log(Type type, Object... objs)
    {
        String posStr = LogUtil.getCodePos(codeStack);
        if (objs.length == 2 && objs[1] != null && objs[1] instanceof Throwable)
        {
            Throwable t = (Throwable) objs[1];
            objs[0] = posStr + "\n" + objs[0];
            switch (type)
            {
                case TRACE:
                    logger.trace(LogUtil.getCodePos(3));
                    logger.trace(objs[0], t);
                    break;
                case ERROR:
                    logger.error(objs[0], t);
                    break;
                case INFO:
                    logger.info(objs[0], t);
                    break;
                case DEBUG:
                    logger.debug(objs[0], t);
                    break;
                case FATAL:
                    logger.fatal(objs[0], t);
                    break;
                case WARN:
                    logger.warn(objs[0], t);
                    break;
            }
        } else
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(posStr).append("\n");
            for (int i = 0; i < objs.length; i++)
            {
                stringBuilder.append(objs[i]);
            }
            switch (type)
            {
                case TRACE:
                    logger.trace(stringBuilder);
                    break;
                case ERROR:
                    logger.error(stringBuilder);
                    break;
                case INFO:
                    logger.info(stringBuilder);
                    break;
                case DEBUG:
                    logger.debug(stringBuilder);
                    break;
                case FATAL:
                    logger.fatal(stringBuilder);
                    break;
                case WARN:
                    logger.warn(stringBuilder);
                    break;
            }
        }
        return this;
    }

    @Override
    public WPLog4j trace(Object... objects)
    {
        return _log(Type.TRACE, objects);
    }

    @Override
    public WPLog4j debug(Object... objects)
    {
        return _log(Type.DEBUG, objects);
    }

    @Override
    public WPLog4j info(Object... objects)
    {
        return _log(Type.INFO, objects);
    }

    @Override
    public WPLog4j warn(Object... objects)
    {
        return _log(Type.WARN, objects);
    }

    @Override
    public WPLog4j error(Object... objects)
    {
        return _log(Type.ERROR, objects);
    }

    @Override
    public WPLog4j fatal(Object... objects)
    {
        return _log(Type.FATAL, objects);
    }
}

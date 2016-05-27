package com.chenyg.wporter;

import java.util.*;

import com.chenyg.wporter.annotation.ThinkType;
import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.log.WPLogSys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chenyg.wporter.util.FileTool;

/**
 * 配置对象
 */
public abstract class Config
{

    public static final String NAME_WEBPORTER_PATHS = "webPorterPaths";
    public static final String NAME_WPLOG = "wpLog";
    public static final String NAME_WEBPORTER_PATHS_isSys = "isSys";
    public static final String NAME_WEBPORTER_PATHS_porters = "porters";
    public static final String NAME_TIED_PREFIX = "tiedPrefix";
    public static final String NAME_STATE_LISTENERS = "stateListeners";
    public static final String NAME_ENCODING = "encoding";
    public static final String NAME_NOT_FOUND_THINKTYPE = "notFoundThinkType";
    public static final String NAME_GLOBAL_CHECK = "globleCheck";
    public static final String NAME_RESPONSE_LOSEDARGS = "responseLosedArgs";
    public static final String NAME_DEFAULT_CHECKTYPE = "defaulCheckType";
    public static final String NAME_EXLISTENER = "exListener";

    public static final String PARAMS_NAME_PORTER = "porterParams";
    public static final String PARAMS_NAME_USER = "userParams";


    /**
     * 得到接口的InitParamGeter
     *
     * @return 框架参数有关的InitParamGeter
     */
    public abstract InitParamSource getPorterParamGeter();

    /**
     * 得到用户的InitParamGeter
     *
     * @return 用户自定义参数的InitParamGeter
     */
    public abstract InitParamSource getUserParamGeter();


    /**
     * 转换为Config
     *
     * @param configStr
     * @return
     * @throws InitException
     */
    public static Configable toConfig(String configStr)
    {
        JSONArray porterArray, userArray;
        JSONArray pathsArr = null;
        JSONArray stateListenerArr = null;
        try
        {
            JSONObject jsonObject = new JSONObject(configStr);
            userArray = jsonObject.getJSONArray(Config.PARAMS_NAME_USER);
            porterArray = jsonObject.getJSONArray(Config.PARAMS_NAME_PORTER);

            //paths
            for (int i = 0; i < porterArray.length(); i++)
            {
                JSONObject json = porterArray.getJSONObject(i);
                if (NAME_WEBPORTER_PATHS.equals(json.getString("name")))
                {
                    pathsArr = json.getJSONArray("value");
                }
            }
            if (pathsArr == null)
            {
                pathsArr = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("name", NAME_WEBPORTER_PATHS);
                json.put("value", pathsArr);
                porterArray.put(json);
            }

            //state listener
            for (int i = 0; i < porterArray.length(); i++)
            {
                JSONObject json = porterArray.getJSONObject(i);
                if (NAME_STATE_LISTENERS.equals(json.getString("name")))
                {
                    stateListenerArr = json.getJSONArray("value");
                }
            }
            if (stateListenerArr == null)
            {
                stateListenerArr = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("name", NAME_STATE_LISTENERS);
                json.put("value", stateListenerArr);
                porterArray.put(json);
            }
        } catch (JSONException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        final InitParamSource porterParamsGeter = getInitParams(porterArray);
        final InitParamSource userParamsGeter = getInitParams(userArray);
        final JSONArray finalPathsArr = pathsArr;
        final JSONArray finalStateListenerArr = stateListenerArr;
        Configable _config = new Configable()
        {

            @Override
            public void addPorterPaths(boolean isSys, String... paths)
            {
                JSONObject json = new JSONObject();
                try
                {
                    json.put(NAME_WEBPORTER_PATHS_isSys, isSys);

                    JSONArray arr = new JSONArray();
                    for (int i = 0; i < paths.length; i++)
                    {
                        arr.put(paths[i]);
                    }

                    json.put(NAME_WEBPORTER_PATHS_porters, arr);
                    finalPathsArr.put(json);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void addStateListeners(Class<? extends StateListener>... stateListeners)
            {
                for (Class<? extends StateListener> c : stateListeners)
                {
                    finalStateListenerArr.put(c.getName());
                }
            }

            @Override
            public InitParamSource getUserParamGeter()
            {

                return userParamsGeter;
            }

            @Override
            public InitParamSource getPorterParamGeter()
            {

                return porterParamsGeter;
            }
        };

        return _config;
    }

    private static InitParamSource getInitParams(final JSONArray configArray)
    {
        try
        {
            InitParamSource initParamSource = new InitParamSource()
            {
                JSONObject jsonObject = null;

                @Override
                public String getInitParameter(String name)
                {
                    try
                    {
                        if (jsonObject == null)
                        {
                            jsonObject = init();
                        }
                        return jsonObject.getString(name);
                    } catch (JSONException e)
                    {
                        return null;
                    }
                }

                private JSONObject init()
                {
                    jsonObject = new JSONObject();
                    try
                    {
                        for (int i = 0; i < configArray.length(); i++)
                        {
                            JSONObject _jsonObject = configArray.getJSONObject(i);
                            jsonObject.put(_jsonObject.getString("name"), _jsonObject.getString("value"));
                        }
                        return jsonObject;
                    } catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            };
            return initParamSource;
        } catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 得到默认的
     *
     * @return Builder
     */
    public static Builder getDefaultConfigBuilder()
    {
        String config = FileTool.getString(Config.class.getResourceAsStream("/wpconfig/WebPorter.json"));
        try
        {
            JSONObject jsonObject = new JSONObject(config);
            Builder builder = new Builder();

            JSONArray params = jsonObject.getJSONArray(PARAMS_NAME_PORTER);

            for (int i = 0; i < params.length(); i++)
            {
                JSONObject param = params.getJSONObject(i);
                builder.addPorterParam(param.getString("name"), param.getString("value"));
            }

            return builder;

        } catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static abstract class Configable extends Config
    {
        public abstract void addPorterPaths(boolean isSys, String... paths);

        public abstract void addStateListeners(Class<? extends StateListener>... stateListeners);
    }

    public static class SimpleBuilder
    {
        private List<Object[]> packageList = new ArrayList<Object[]>();
        private List<String> stateListeners = new ArrayList<String>();
        private String tiedPrefix = "";
        private String encoding = "utf-8";
        private String notFoundThinkType = ThinkType.DEFAULT.name();
        private boolean responseLosedArgs = true;
        private String defaulCheckType = CheckType.GLOBAL.name();
        private String exListener;
        private String globleCheck;
        private String wpLog;
        private Map<String, String> userParams = new HashMap<String, String>();

        public SimpleBuilder(Class<? extends WPLog<?>> wpLogClass)
        {
            setWPLog(wpLogClass == null ? WPLogSys.class : wpLogClass);
        }

        public Config build()
        {
            Config config = new Config()
            {
                private InitParamSource user = initUserParamGeter();
                private InitParamSource sys = initPorterParamGeter();

                private InitParamSource initPorterParamGeter()
                {
                    try
                    {
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put(NAME_WEBPORTER_PATHS, getPathsArray());
                        jsonObject.put(NAME_TIED_PREFIX, tiedPrefix);
                        jsonObject.put(NAME_ENCODING, encoding);
                        jsonObject.put(NAME_NOT_FOUND_THINKTYPE, notFoundThinkType);
                        jsonObject.put(NAME_RESPONSE_LOSEDARGS, responseLosedArgs);
                        jsonObject.put(NAME_EXLISTENER, exListener);
                        jsonObject.put(NAME_WPLOG, wpLog);
                        jsonObject.put(NAME_GLOBAL_CHECK, globleCheck);
                        jsonObject.put(NAME_DEFAULT_CHECKTYPE, defaulCheckType);

                        JSONArray slisteners = new JSONArray();
                        for (int i = 0; i < stateListeners.size(); i++)
                        {
                            slisteners.put(stateListeners.get(i));
                        }
                        jsonObject.put(NAME_STATE_LISTENERS, slisteners);

                        return new InitParamSource()
                        {
                            @Override
                            public String getInitParameter(String name)
                            {
                                try
                                {
                                    if (jsonObject.has(name))
                                    {
                                        return jsonObject.getString(name);
                                    } else
                                    {
                                        return null;
                                    }

                                } catch (JSONException e)
                                {
                                    throw new RuntimeException(e.getMessage());
                                }
                            }
                        };
                    } catch (JSONException e)
                    {
                        throw new RuntimeException(e.getMessage());
                    }
                }

                private JSONArray getPathsArray() throws JSONException
                {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < packageList.size(); i++)
                    {
                        Object[] objects = packageList.get(i);
                        JSONObject jsonObject = new JSONObject();
                        array.put(jsonObject);
                        jsonObject.put(NAME_WEBPORTER_PATHS_isSys, objects[0]);
                        String[] ps = (String[]) objects[1];
                        JSONArray as = new JSONArray();
                        for (String s : ps)
                        {
                            as.put(s);
                        }
                        jsonObject.put(NAME_WEBPORTER_PATHS_porters, as);
                    }
                    return array;
                }

                private InitParamSource initUserParamGeter()
                {
                    return new InitParamSource()
                    {
                        @Override
                        public String getInitParameter(String name)
                        {
                            return userParams.get(name);
                        }
                    };
                }

                @Override
                public InitParamSource getPorterParamGeter()
                {
                    return sys;
                }

                @Override
                public InitParamSource getUserParamGeter()
                {
                    return user;
                }
            };

            return config;
        }

        /**
         * 添加用户参数值对。
         *
         * @param name  名称
         * @param value 值
         * @return SimpleBuilder
         */
        public SimpleBuilder addUserParam(String name, String value)
        {
            userParams.put(name, value);
            return this;
        }

        /**
         * 设置全局检测
         *
         * @param globleCheck 全局检测的类
         * @return SimpleBuilder
         */
        public SimpleBuilder setGlobleCheck(Class<? extends CheckPassable> globleCheck)
        {
            this.globleCheck = globleCheck.getName();
            return this;
        }

        public SimpleBuilder setWPLog(Class<? extends WPLog<?>> wpLogClass)
        {
            this.wpLog = wpLogClass.getName();
            return this;
        }

        /**
         * 设置异常检测
         *
         * @param exListener 异常监听对象
         * @return SimpleBuilder
         */
        public SimpleBuilder setExListener(Class<? extends RequestExListener> exListener)
        {
            this.exListener = exListener.getName();
            return this;
        }

        /**
         * 默认检测类型，默认为GLOBAL.
         *
         * @param defaulCheckType 默认检测类型
         * @return SimpleBuilder
         */
        public SimpleBuilder setDefaulCheckType(CheckType defaulCheckType)
        {
            this.defaulCheckType = defaulCheckType.name();
            return this;
        }

        /**
         * 是否响应缺乏的参数，默认为true。
         *
         * @param responseLosedArgs 是否响应具体的缺少参数的信息
         * @return SimpleBuilder
         */
        public SimpleBuilder setResponseLosedArgs(boolean responseLosedArgs)
        {
            this.responseLosedArgs = responseLosedArgs;
            return this;
        }

        /**
         * 设置绑定前缀.默认为"".
         *
         * @param tiedPrefix 绑定前缀
         * @return SimpleBuilder
         */
        public SimpleBuilder setTiedPrefix(String tiedPrefix)
        {
            this.tiedPrefix = tiedPrefix;
            return this;
        }

        /**
         * 默认为DEFAULT.
         *
         * @param thinkType
         * @return
         */
        public SimpleBuilder setNotFoundThinkType(ThinkType thinkType)
        {
            this.notFoundThinkType = thinkType.name();
            return this;
        }

        /**
         * 设置编码方式。默认为"utf-8".
         *
         * @param encoding
         * @return
         */
        public SimpleBuilder setEncoding(String encoding)
        {
            this.encoding = encoding;
            return this;
        }

        /**
         * 添加接口包
         *
         * @param isSys
         * @param packages
         * @return
         */
        public SimpleBuilder addPorterPaths(boolean isSys, String... packages)
        {
            packageList.add(new Object[]{isSys, packages});
            return this;
        }

        /**
         * 添加状态监听器.
         *
         * @param stateListeners 状态监听器
         */
        public SimpleBuilder addStateListeners(Class<? extends StateListener>... stateListeners)
        {
            for (Class<? extends StateListener> c : stateListeners)
            {
                this.stateListeners.add(c.getName());
            }
            return this;
        }
    }

    public static class Builder
    {
        private HashMap<String, String> userMap = new HashMap<String, String>();
        private HashMap<String, String> porterMap = new HashMap<String, String>();


        public Builder addPorterParam(String name, String value)
        {
            porterMap.put(name, value);
            return this;
        }

        public Builder addUserParam(String name, String value)
        {
            userMap.put(name, value);
            return this;
        }

        public Config build()
        {
            return new Config()
            {
                private InitParamSource porterGeter = new InitParamSource()
                {

                    @Override
                    public String getInitParameter(String name)
                    {
                        return porterMap.get(name);
                    }
                };

                private InitParamSource userGeter = new InitParamSource()
                {
                    @Override
                    public String getInitParameter(String name)
                    {
                        return userMap.get(name);
                    }
                };

                @Override
                public InitParamSource getUserParamGeter()
                {
                    return userGeter;
                }

                @Override
                public InitParamSource getPorterParamGeter()
                {
                    return porterGeter;
                }
            };
        }
    }

}

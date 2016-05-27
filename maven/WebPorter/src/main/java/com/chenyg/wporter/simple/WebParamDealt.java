package com.chenyg.wporter.simple;

import java.util.ArrayList;
import java.util.HashMap;

import com.chenyg.wporter.*;
import com.chenyg.wporter.WPObject.InNames;
import com.chenyg.wporter.util.WPTool;

/**
 * 处理web参数的（都是String类型的值）。
 */
public class WebParamDealt extends ParamDealt
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean dealParams(WPorterAndMethod wPorterAndMethod, WPObject wpObject, ParamSource paramSource,
            boolean responseLosedArgs) throws LackParamsException, InCheckParser.IllegalParamException

    {


        WPorter._ChildIn childIn = wPorterAndMethod.wPorter.getChildIn();
        WPorter._FatherIn fatherIn = wPorterAndMethod.wPorter.getFatherIn();
        WPorter._ParamExtra paramExtra = wPorterAndMethod.wPorter.getParamExtra();

        String[] necesPF = fatherIn.neceParams();// 必须参数名
        String[] unnecesPF = fatherIn.unneceParams();// 非必须参数名

        String[] necessaries = childIn.neceParams();// 必须参数名
        String[] unnecessaries = childIn.unneceParams();// 非必须参数名

        InNames inNames = new InNames(necesPF, unnecesPF, necessaries, unnecessaries,childIn.innerParams());
        wpObject.inNames = inNames;

        Object[] necesFValues = null, unnecesFValues = null, necesValues = null, unnecesValues = null;
        boolean success = true;
        /*********** Father 必须参数 ************/
        necesFValues = new Object[necesPF.length];

        Handle handle = new Handle(wPorterAndMethod, paramSource);

        Object value;

        ArrayList<String> lackParamNames = null;
        HashMap<String, String> descMap = null;

        if (responseLosedArgs)
        {
            lackParamNames = new ArrayList<String>();
            descMap = new HashMap<String, String>();
        }

        for (int i = 0; i < necesPF.length; i++)
        {
            value = handle.getParam(necesPF[i]);

            if (WPTool.isEmpty(value))
            {
                success = false;
                if (!responseLosedArgs)
                {
                    break;
                }
                lackParamNames.add(necesPF[i]);
                super.addDesc(descMap, necesPF[i], i, wPorterAndMethod);
            } else
            {
                necesFValues[i] = value;
            }
        }
        if (!success && !responseLosedArgs)
        {
            return false;
        }
        wpObject.fns = necesFValues;


        /*********** Father 非必须参数 ************/
        if (unnecesPF.length > 0)
        {
            unnecesFValues = new Object[unnecesPF.length];
            for (int i = 0; i < unnecesFValues.length; i++)
            {
                value = handle.getParam(unnecesPF[i]);
                unnecesFValues[i] = value;
            }
        }
        wpObject.fus = unnecesFValues;

        /*********** Child 必须参数 ************/
        if (necessaries.length > 0)
        {
            necesValues = new Object[necessaries.length];
            for (int i = 0; i < necessaries.length; i++)
            {
                value = handle.getParam(necessaries[i]);

                if (WPTool.isEmpty(value))
                {
                    success = false;
                    if (!responseLosedArgs)
                    {
                        break;
                    }
                    lackParamNames.add(necessaries[i]);
                    super.addDesc(descMap, necessaries[i], i, wPorterAndMethod);
                } else
                {
                    necesValues[i] = value;
                }
            }
        }

        if (!success)
        {
            /**
             * 传递缺少的参数,json数组
             */
            if (responseLosedArgs)
            {
                String[] names = lackParamNames.toArray(new String[0]);
                LackParamsException lackParamsException = new LackParamsException(names, descMap);
                throw lackParamsException;
            }

            return false;
        }
        wpObject.cns = necesValues;


        /*********** Child 非必须参数 ************/
        if (unnecessaries.length > 0)
        {
            unnecesValues = new Object[unnecessaries.length];
            for (int i = 0; i < unnecessaries.length; i++)
            {
                value = getCusValue(handle, unnecessaries[i], i, paramExtra);
                unnecesValues[i] = value;
            }
        }
        wpObject.cus = unnecesValues;
        wpObject.inners = new Object[wpObject.inNames.innerNames.length];
        return true;
    }

}

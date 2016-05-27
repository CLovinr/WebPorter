package com.chenyg.wporter;

/**
 * 接口类型。
 */
public enum WPorterType
{
    SERVLET("SERVLET"), APP("APP"), MINA("MINA");

    private String typeId;

    private WPorterType(String typeId)
    {
        this.typeId = typeId;
    }

    public String getTypeId()
    {
        return typeId;
    }

    public static WPorterType mina(String typeId)
    {
        WPorterType wPorterType = MINA;
        wPorterType.typeId = wPorterType.typeId + typeId;
        return wPorterType;
    }

}

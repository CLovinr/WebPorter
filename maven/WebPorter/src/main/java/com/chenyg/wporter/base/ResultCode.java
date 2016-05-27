package com.chenyg.wporter.base;

/**
 * 对返回码的定义
 *
 * @author Administrator
 */
public enum ResultCode
{
    /**
     * 响应成功，将得到请求的内容。
     */
    SUCCESS(0),

    /**
     * 无访问权限。
     */
    ACCESS_DENIED(HttpStatusCode.UNAUTHORIZED),

    /**
     * 访问资源不可用
     */
    NOT_AVAILABLE(HttpStatusCode.NOT_FOUND),

    /**
     * 服务器发生异常
     */
    SERVER_EXCEPTION(HttpStatusCode.INTERNAL_SERVER_ERROR),

    /**
     * 响应成功，但是操作失败
     */
    OK_BUT_FAILED(-4),

    /**
     * 缺少必须参数(lack of necessary
     * parameters),例如，id为必须参数，当请求为http://baseUrl/xxx?id=
     * &#38;name=Tom和http://baseUrl/xxx?name=Tom都是缺少必须参数.
     */
    LACK_NECE_PARAMS(-5),

    /**
     * 不合法的参数
     */
    ILLEGAL_PARAM(-6),

    /**
     * 异常
     */
    EXCEPTION(-7),

    /**
     * 数据库异常
     */
    DB_EXCEPTION(-8),

    /**
     * 调用接口函数错误
     */
    INVOKE_METHOD_EXCEPTION(-9),
    /**
     * 网络错误
     */
    NET_EXCEPTION(-10),
    /**
     * 调用第三方接口错误
     */
    OTHER_PORTER_EXCEPTION(-11),
    /**
     * 未登陆
     */
    NOT_LOGIN(-12),
    /**
     * 需要登陆
     */
    NEED_LOGIN(-13),
    /**
     * 重定向
     */
    REDIRECT(-14),
    Other;

    private int code;

    ResultCode()
    {
        this.code = -1;
    }

    ResultCode(int code)
    {
        this.code = code;
    }

    /**
     * 转换为结果码。
     *
     * @return 整型的结果码
     */
    public int toCode()
    {
        return code;
    }

    public static ResultCode toResponseCode(int code)
    {
        ResultCode responseCode;
        switch (code)
        {
            case 0:
                responseCode = SUCCESS;
                break;
            case HttpStatusCode.NOT_FOUND:
                responseCode = NOT_AVAILABLE;
                break;
            case HttpStatusCode.INTERNAL_SERVER_ERROR:
                responseCode = SERVER_EXCEPTION;
                break;
            case -4:
                responseCode = OK_BUT_FAILED;
                break;
            case -5:
                responseCode = LACK_NECE_PARAMS;
                break;
            case HttpStatusCode.UNAUTHORIZED:
                responseCode = ACCESS_DENIED;
                break;
            case -6:
                responseCode = ILLEGAL_PARAM;
                break;
            case -7:
                responseCode = EXCEPTION;
                break;
            case -8:
                responseCode = DB_EXCEPTION;
                break;
            case -9:
                responseCode = INVOKE_METHOD_EXCEPTION;
                break;
            case -10:
                responseCode = NET_EXCEPTION;
                break;
            case -11:
                responseCode = OTHER_PORTER_EXCEPTION;
                break;
            case -12:
                responseCode = NOT_LOGIN;
            break;
            case -13:
                responseCode=NEED_LOGIN;
                break;
            case -14:
                responseCode=REDIRECT;
                break;
            default:
                responseCode = Other;
                responseCode.code = code;
                break;
        }
        return responseCode;
    }
}

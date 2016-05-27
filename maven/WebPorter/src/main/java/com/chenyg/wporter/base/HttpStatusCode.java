package com.chenyg.wporter.base;

public class HttpStatusCode
{
    public static final int CONTINUE = 100;

    public static final int SWITCHING_PROTOCOLS = 101;

    public static final int OK = 200;

    public static final int CREATED = 201;

    public static final int ACCEPTED = 202;

    public static final int NON_AUTHORITATIVE_INFORMATION = 203;

    public static final int NO_CONTENT = 204;

    public static final int RESET_CONTENT = 205;

    public static final int PARTIAL_CONTENT = 206;

    public static final int MULTIPLE_CHOICES = 300;

    public static final int MOVED_PERMANENTLY = 301;

    public static final int MOVED_TEMPORARILY = 302;

    public static final int SEE_OTHER = 303;

    public static final int NOT_MODIFIED = 304;

    public static final int USE_PROXY = 305;

    public static final int TEMPORARY_REDIRECT = 307;

    public static final int BAD_REQUEST = 400;

    public static final int UNAUTHORIZED = 401;

    public static final int PAYMENT_REQUIRED = 402;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND = 404;

    public static final int METHOD_NOT_ALLOWED = 405;

    public static final int NOT_ACCEPTABLE = 406;

    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;

    public static final int REQUEST_TIMEOUT = 408;

    public static final int CONFLICT = 409;

    public static final int GONE = 410;

    public static final int LENGTH_REQUIRED = 411;

    public static final int PRECONDITION_FAILED = 412;

    public static final int REQUEST_ENTITY_TOO_LARGE = 413;

    public static final int REQUEST_URI_TOO_LONG = 414;

    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    public static final int EXPECTATION_FAILED = 417;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int NOT_IMPLEMENTED = 501;

    public static final int BAD_GATEWAY = 502;

    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int GATEWAY_TIMEOUT = 504;

    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * 是否是上述定义的某个状态码
     * 
     * @param code
     * @return
     */
    public static boolean isHttpStatusCode(int code)
    {
	if (code >= 200 && code <= 206

		|| code >= 400
		&& code <= 417
		|| code >= 300
		&& code <= 305
		|| code == 307
		|| code >= 100
		&& code <= 101
		|| code >= 500
		&& code <= 505)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
}

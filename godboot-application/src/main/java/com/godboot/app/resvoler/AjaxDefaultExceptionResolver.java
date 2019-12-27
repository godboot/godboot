package com.godboot.app.resvoler;

import com.godboot.framework.entity.ServiceResult;
import com.godboot.framework.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 应用对象 - AjaxDefaultExceptionResolver.
 * <p>
 * <p>
 * 该类于 2018-08-02 15:52:49 首次生成，后由开发手工维护。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 八月 02, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 * @see AjaxDefaultExceptionResolver
 */
@Order(-1000)
@ControllerAdvice
public class AjaxDefaultExceptionResolver implements HandlerExceptionResolver {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ServiceResult serviceResult = ServiceResult.ERROR("未知错误");
        serviceResult.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        if (ex instanceof NoHandlerFoundException) {
            serviceResult.setCode(HttpStatus.NOT_FOUND.value());
        }

        serviceResult.setMessage(ex.getMessage());
        return handleHttpRequestException(ex, request, response, handler, serviceResult);
    }

    /**
     * Handle the case where no request handler method was found for the particular HTTP request method.
     * <p>The default implementation logs a warning, sends an HTTP 405 error, sets the "Allow" header,
     * and returns an empty {@code ModelAndView}. Alternatively, a fallback view could be chosen,
     * or the HttpRequestMethodNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpRequestMethodNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or {@code null} if none chosen
     *                 at the time of the exception (for example, if multipart resolution failed)
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    protected ModelAndView handleHttpRequestException(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler, ServiceResult<String> serviceResult) {
        logger.error(ex.getMessage());

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        response.addHeader("Content-type", "text/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(GsonUtil.getInstanceNoNull().toJson(serviceResult));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
        }

        return new ModelAndView();
    }
}
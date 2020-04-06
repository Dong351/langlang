package com.doublefish.langlang.token;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.exception.TokenAuthFailedException;
import com.doublefish.langlang.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import javax.servlet.ServletException;

@Component
public class TokenMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    private final UserService userService;

    public TokenMethodArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前参数的注解信息
     *
     * @param methodParameter 需要被解析的Controller参数
     * @return
     */
    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter methodParameter) {
        Token annotation = methodParameter.getParameterAnnotation(Token.class);
        return new NamedValueInfo(annotation.value(), annotation.required(), null);
    }

    /**
     * 在这里进行参数的类型转换
     *
     * @param name            {@link Token#value()}
     * @param methodParameter 需要被解析的Controller参数
     * @param request         当前request
     * @return 转换后的参数值
     */
    @Override
    protected Object resolveName(String name, MethodParameter methodParameter, NativeWebRequest request) {
        String token = request.getHeader(name);
        if (token == null) {
            return null;
        }
        User user = userService.queryUserByToken(token);
        if (user == null) {
            throw new TokenAuthFailedException();
        }
        if (methodParameter.getParameterType() == Integer.class) {
            return user.getUid();
        }
        return user;
    }

    /**
     * 解析器是否支持当前参数
     *
     * @param methodParameter 需要被解析的Controller参数
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Token.class);
    }

    /**
     * 当前参数值为空且注解的默认值也为空则抛出异常
     *
     * @param name      参数名
     * @param parameter 需要被解析的Controller参数
     * @throws ServletException
     */
    @Override
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
        throw new MissingServletRequestParameterException(name, parameter.getParameterType().getSimpleName());
    }
}

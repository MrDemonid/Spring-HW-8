package mr.demonid.spring.hw8.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * Перехват и логирование запросов.
 */
@Slf4j
public class BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String userFrom = request.getUserPrincipal() == null ? "Guest" : request.getUserPrincipal().getName();
        String parameters = getRequestParamsAsString(request);

        log.info("-->> Request from {} {} {} {} <<--", userFrom, method, url, parameters);
        return true;
    }

    private String getRequestParamsAsString(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();

            if (paramValues != null && paramName != null && paramValues.length > 0) {
                for (String value : paramValues) {
                    result.append(paramName).append("=").append(value).append(", ");
                }
            }
        }
        // Удаляем последний символ "&", если он присутствует
        if (result.isEmpty()) {
            return "";
        }
        result.setLength(result.length() - 2);
        result.append("]");
        return result.toString();
    }
}

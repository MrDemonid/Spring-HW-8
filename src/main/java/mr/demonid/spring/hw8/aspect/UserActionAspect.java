package mr.demonid.spring.hw8.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Модуль аспекта.
 * Через срезы заданы некоторые ограничения на применение данного аспекта.
 * Используется логгер по умолчанию: SLF4J + Logback (для Spring Boot).
 */
@Aspect
@Component
@Slf4j
public class UserActionAspect {

    /**
     * Все публичные методы.
     */
    @Pointcut("execution(public * *(..))")
    private void allPublic() {}

    @Pointcut("@annotation(mr.demonid.spring.hw8.aspect.TrackUserAction)")
    private void withTrackUserAction() {}

    /**
     * Ограничение: все классы и методы, но только в пакете "mr.demonid.spring.hw8.services"
     */
    @Pointcut("within(mr.demonid.spring.hw8.services..*)")
    private void allServices() {}

    /**
     * Логирование вызова
     * @param point точка соединения.
     * @return Результат выполнения целевого метода не изменяется.
     */
    @Around("withTrackUserAction() && allServices()")
    public Object loggerUserAction(ProceedingJoinPoint point) throws Throwable {
        String method = point.getSignature().getName();
        String clazz = point.getTarget().getClass().getSimpleName();

        // получаем инфу о текущем пользователе
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();                // выполняем целевой метод
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        long time = System.currentTimeMillis() - start;
        // делаем отметку о вызове
        log.info("[{}] completed the method: {}.{} in {} ms", userName, clazz, method, time);
        return result;
    }

}

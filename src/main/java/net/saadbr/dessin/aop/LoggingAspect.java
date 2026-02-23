package net.saadbr.dessin.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author saade
 **/
@Aspect
public class LoggingAspect {
    @Around("execution(* net.saadbr.dessin.model.Dessin.*(..))")
    public Object logAroundDessinMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        System.out.println("[LOG] Début méthode : " + methodName);
        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            System.out.println("[LOG] Fin méthode : " + methodName +
                    " (durée = " + duration + " ms)");
            return result;
        } catch (Throwable ex) {
            System.out.println("[LOG] Exception dans méthode : " + methodName +
                    " -> " + ex.getMessage());
            throw ex;
        }
    }
}

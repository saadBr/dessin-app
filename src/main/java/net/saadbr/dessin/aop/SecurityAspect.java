package net.saadbr.dessin.aop;

import net.saadbr.dessin.security.Role;
import net.saadbr.dessin.security.Secured;
import net.saadbr.dessin.security.SecurityContext;
import net.saadbr.dessin.security.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author saade
 **/
@Aspect
public class SecurityAspect {
    @Before("@annotation(secured)")
    public void checkSecurity(JoinPoint joinPoint, Secured secured) {
        User user = SecurityContext.getCurrentUser();

        if (user == null) {
            throw new SecurityException("Accès refusé : aucun utilisateur authentifié");
        }

        Role[] requiredRoles = secured.value();
        for (Role role : requiredRoles) {
            if (user.hasRole(role)) {
                return;
            }
        }

        throw new SecurityException("Accès refusé pour l'utilisateur " + user.getUsername() +
                " sur la méthode " + joinPoint.getSignature().toShortString());
    }
}

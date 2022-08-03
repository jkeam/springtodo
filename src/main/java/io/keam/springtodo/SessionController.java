package io.keam.springtodo;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class SessionController {
    /**
     * Makes SSO Logout.
     * This endpoint has to be private. Otherwise there will be no token to send logout to KeyCloak.
     *
     * @param request the request
     * @return redirect to logout page
     * @throws ServletException if tomcat session logout throws exception
     */
    @PostMapping(path = "/logout")
    public ModelAndView logout(HttpServletRequest request) throws ServletException {
        keycloakSessionLogout(request);
        tomcatSessionLogout(request);
        return new ModelAndView("redirect:/home");
    }

    private void keycloakSessionLogout(HttpServletRequest request){
        RefreshableKeycloakSecurityContext c = getKeycloakSecurityContext(request);
        KeycloakDeployment d = c.getDeployment();
        c.logout(d);
    }

    private void tomcatSessionLogout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

    private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request){
        return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}

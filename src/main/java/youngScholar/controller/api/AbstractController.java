package youngScholar.controller.api;

import youngScholar.Application;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class AbstractController
{
    public static String getAutoLoginPassword()
    {
        HttpSession httpSession = Application.getRequest().getSession();
        if (httpSession == null || httpSession.getAttribute("autoLogin") == null)
        {
            return null;
        }
        httpSession.removeAttribute("autoLogin");
        return "autoLogin";
    }

    protected void autoLogin(String username)
    {
        HttpServletRequest request = Application.getRequest();
        AuthenticationManager authenticationManager = Application.getBean(AuthenticationManager.class);
        request.getSession().setAttribute("autoLogin", true);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "autoLogin");
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }
}

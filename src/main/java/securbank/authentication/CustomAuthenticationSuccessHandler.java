package securbank.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Get the role of logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        String targetUrl = "";
        if(role.contains("ADMIN")) {
            targetUrl = "/admin/details";
        } else if(role.contains("MANAGER")) {
            targetUrl = "/manager/details";
        } else if(role.contains("EMPLOYEE")) {
            targetUrl = "/manager/details";
        } else if(role.contains("INDIVIDUAL")|role.contains("MERCHANT")) {
            targetUrl = "/external/details";
        }
        return targetUrl;
    }

}

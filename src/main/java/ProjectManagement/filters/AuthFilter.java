package ProjectManagement.filters;

import ProjectManagement.entities.Response;
import ProjectManagement.services.AuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class AuthFilter implements Filter {

    private final AuthService authService;
    private final Pattern bearerPattern = Pattern.compile("^[Bb]earer\\s+(.*)$");
    private final Set<Pattern> publicUrls = Set.of(Pattern.compile("^/board/.*$"), Pattern.compile("^/task/.*$"));
    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("AuthFilter is called with this request: " + request.getRequestURL().toString());
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String bearerToken = request.getHeader("Authorization");
        if (publicUrls.stream().anyMatch(url -> url.matcher(request.getRequestURI()).matches())) {
            if (bearerToken != null) {
                var matcher = bearerPattern.matcher(bearerToken);
                if (matcher.matches()) {
                    String token = matcher.group(1);
                    Response<Integer> userId = authService.validateToken(token);
                    if (userId != null && userId.isSucceed()) {
                        request.setAttribute("userId", userId.getData());
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
            System.out.println("AuthFilter denied the request");
            res.sendError(401, "You are unauthorized to get this resource. Please login first.");
        } else {
            System.out.println("This is a public url, no need to check auth");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

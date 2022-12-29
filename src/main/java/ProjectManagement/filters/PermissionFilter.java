package ProjectManagement.filters;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.UserInBoard;
import ProjectManagement.entities.annotations.AccessLevel;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a filter that checks if the current user has the required access level to access a certain resource.
 * It does so by checking for the presence of an {@link AccessLevel} annotation on the method handling the current request.
 * If the annotation is present, the filter checks if the user's role allows them to access the resource.
 * If the annotation is not present, the request is passed on to the next filter in the chain.
 */
@Component
@Order(3)
public class PermissionFilter implements Filter {
    private final BoardService boardService;
    private final UserService userService;
    private final ApplicationContext appContext;
    private RequestMappingHandlerMapping mappings1;
    private Map<RequestMappingInfo, HandlerMethod> handlerMethods;

    @Autowired
    public PermissionFilter(BoardService boardService, UserService userService, ApplicationContext appContext) {
        this.boardService = boardService;
        this.userService = userService;
        this.appContext = appContext;
        this.mappings1 = (RequestMappingHandlerMapping) this.appContext.getBean("requestMappingHandlerMapping");
        handlerMethods = mappings1.getHandlerMethods();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Performs the filtering of requests.
     *
     * @param servletRequest  the request to filter
     * @param servletResponse the response to filter
     * @param filterChain     the filter chain to execute
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        System.out.println("PermissionFilter is called with this request: " + request.getRequestURL().toString());
        try {
            Response<AccessLevel> hasAccessLevel = checkIfAnnotationPresent(request);
            if (hasAccessLevel.isSucceed()) {
                Response<User> optionalUser = userService.getUserById((int) request.getAttribute("userId"));
                Response<Board> optionalBoard = boardService.getBoard(Integer.parseInt(request.getHeader("boardId")));
                if (optionalUser.isSucceed() && optionalBoard.isSucceed()) {
                    Board board = optionalBoard.getData();
                    User user = optionalUser.getData();
                    request.setAttribute("board", board);
                    request.setAttribute("user", user);
                    Optional<UserInBoard> ub = board.getUser(user.getId());
                    if (ub.isPresent() && isAllowedAccess(hasAccessLevel.getData(), ub.get().getUserRole())) {
                        filterChain.doFilter(servletRequest, servletResponse);

                    } else {
                        System.out.println("PermissionFilter: User doesn't have permission to access this resource");
                        res.sendError(403, "You don't have permission to access this board");

                    }
                } else {
                    System.out.println("PermissionFilter denied the request because user or board not found");
                    res.sendError(400, "User or board not found");

                }
            } else {
                System.out.println("PermissionFilter pass the request because no annotation is present");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            System.out.println("PermissionFilter denied the request because of an exception\n" + e.getMessage());
            res.sendError(500, "Internal Server Error");

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * Checks if the {@link AccessLevel} annotation is present on the method handling the current request.
     *
     * @param request the current request
     * @return a response object containing the {@link AccessLevel} annotation, if found, or an error message otherwise
     * @throws Exception if an exception occurs during the check
     */

    Response<AccessLevel> checkIfAnnotationPresent(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handler = mappings1.getHandler(request);
        if (Objects.nonNull(handler)) {
            HandlerMethod handler1 = (HandlerMethod) handler.getHandler();
            AccessLevel annotation = handler1.getMethod().getAnnotation(AccessLevel.class);
            if (Objects.nonNull(annotation)) {
                return Response.createSuccessfulResponse(annotation);
            }
            return Response.createFailureResponse("No annotation found");
        }
        return Response.createFailureResponse("No handler found");
    }

    /**
     * Determines if the given user role allows access to the resource specified by the given {@link AccessLevel} annotation.
     *
     * @param annotation the {@link AccessLevel} annotation specifying the required access level for the resource
     * @param userRole   the user's role
     * @return true if the user has the required access level, false otherwise
     */
    boolean isAllowedAccess(AccessLevel annotation, UserRole userRole) {
        if (Objects.nonNull(annotation)) {
            return UserRole.hasAccess(userRole, annotation.value());
        } else return true;
    }
}

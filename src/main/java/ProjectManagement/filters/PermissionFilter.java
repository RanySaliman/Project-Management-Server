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
                    if (ub.isPresent()&& isAllowedAccess(hasAccessLevel.getData(), ub.get().getUserRole())) {
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
            res.sendError(500, "Internal Server Error");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

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

    boolean isAllowedAccess(AccessLevel annotation, UserRole userRole) {
        if (Objects.nonNull(annotation)) {
            if (UserRole.hasAccess(userRole, annotation.value())) return true;
            else return false;
        } else return true;
    }
}

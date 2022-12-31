package ProjectManagement.filters;

import ProjectManagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final AuthService authService;


    @Autowired
    public AppConfig(AuthService authService) {
        System.out.println("AppConfig is created");
        this.authService = authService;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        CorsFilter corsFilter = new CorsFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); //set precedence
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter customURLFilter = new AuthFilter(authService);
        registrationBean.setFilter(customURLFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2); //set precedence
        return registrationBean;
    }


}

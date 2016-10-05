package securbank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Ayush Gupta
 *
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admindash_view_accounts").setViewName("admindash_view_accounts");
        registry.addViewController("/admindash_create_account").setViewName("admindash_create_account");
        registry.addViewController("/admindash_system_logs").setViewName("admindash_system_logs");
    }

}

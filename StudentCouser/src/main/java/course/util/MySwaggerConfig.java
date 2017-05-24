package course.util;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

public class MySwaggerConfig {
    @Resource
    private SpringSwaggerConfig springSwaggerConfig;


    /**
     *      * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
     *      * framework - allowing for multiple swagger groups i.e. same code base
     *      * multiple swagger resource listings.
     *      
     */
    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*?");
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "课程",
                "", "", "", "", ""
        );
        return apiInfo;
    }
}
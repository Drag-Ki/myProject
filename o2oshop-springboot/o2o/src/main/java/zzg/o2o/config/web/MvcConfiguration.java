package zzg.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import zzg.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import zzg.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import zzg.o2o.interceptor.superadmin.SuperAdminLoginInterceptor;

@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/baidu/work/image");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
        viewResolver.setApplicationContext(this.applicationContext);
        viewResolver.setCache(false);
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }


    @Value("${kaptcha.border}")
    private String border;

    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;

    @Value("${kaptcha.image.width}")
    private String width;

    @Value("${kaptcha.textproducer.char.string}")
    private String cString;

    @Value("${kaptcha.image.height}")
    private String height;

    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;

    @Value("${kaptcha.noise.color}")
    private String nColor;

    @Value("${kaptcha.textproducer.char.length}")
    private String clength;

    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servlet=new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border", border);// 无边框
        servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor); // 字体颜色
        servlet.addInitParameter("kaptcha.image.width", width);// 图片宽度
        servlet.addInitParameter("kaptcha.textproducer.char.string", cString);// 使用哪些字符生成验证码
        servlet.addInitParameter("kaptcha.image.height", height);// 图片高度
        servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);// 字体大小
        servlet.addInitParameter("kaptcha.noise.color", nColor);// 干扰线的颜色
        servlet.addInitParameter("kaptcha.textproducer.char.length", clength);// 字符个数
        servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);// 字体
        return servlet;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptPath="/shopadmin/**";
        InterceptorRegistration loginIR=registry.addInterceptor(new ShopLoginInterceptor());
        loginIR.addPathPatterns(interceptPath);
        loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
        loginIR.excludePathPatterns("/shopadmin/adduserproductmap");
        loginIR.excludePathPatterns("/shopadmin/exchangeaward");

        InterceptorRegistration permissionIR=registry.addInterceptor(new ShopPermissionInterceptor());
        permissionIR.addPathPatterns(interceptPath);
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
        permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
        permissionIR.excludePathPatterns("/shopadmin/adduserproductmap");
        permissionIR.excludePathPatterns("/shopadmin/exchangeaward");

        interceptPath="/superadmin/**";
        InterceptorRegistration superadminloginIR=registry.addInterceptor(new SuperAdminLoginInterceptor());
        superadminloginIR.addPathPatterns(interceptPath);
        superadminloginIR.excludePathPatterns("/superadmin/login");
        superadminloginIR.excludePathPatterns("/superadmin/logincheck");
        superadminloginIR.excludePathPatterns("superadmin/main");
        superadminloginIR.excludePathPatterns("/superadmin/top");
        superadminloginIR.excludePathPatterns("/superadmin/clearcache4area");
        superadminloginIR.excludePathPatterns("/superadmin/clearcache4headline");
        superadminloginIR.excludePathPatterns("/superadmin/clearcache4shopcategory");
    }
}

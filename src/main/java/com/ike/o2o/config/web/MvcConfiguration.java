package com.ike.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.ike.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.ike.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.apache.catalina.servlets.DefaultServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfigurer  //配置视图解析器 viewResolver
 * ApplicationContextAware  //获取ApplicationContext对象  实现这个接口可以获取spring容器中所有的bean
 */
@Configuration
@EnableWebMvc //开启SpringMVC注解模式  等效于:<mvc:annotation-driven/>
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    //Spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源配置
     *
     * @param registry 解析工具类
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/baidu/work/image/upload/");
    }

    /**
     * 定义默认的请求处理器   等价于:<mvc:default-servlet-handler/>
     *
     * @param configurer 配置对象
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();//设置为可用
    }

    /**
     * 视图解析器
     *
     * @return 视图解析器对象
     */
    @Bean(name = "viewResolver")
    public ViewResolver creatViewResolver() {
        //视图解析器工具
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        //设置spring容器,获取其中的bean
        internalResourceViewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        internalResourceViewResolver.setCache(false);
        //设置解析前缀 加上该前缀在服务期内寻找资源
        internalResourceViewResolver.setPrefix("/WEB-INF/html/");
        //设置视图解析的后缀  加上该后缀查找资源
        internalResourceViewResolver.setSuffix(".html");

        return internalResourceViewResolver;
    }

    /**
     * 文件解析器
     *
     * @return 文件解析器对象
     */
    @Bean(name = "multipartResolver")
    public MultipartResolver creatMultipartResolver() {
        //文件解析器对象
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        //默认字符编码
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        //最大上传限制<!-- 1024*1024*20 上传限制20兆-->
        commonsMultipartResolver.setMaxUploadSize(20971520L);
        //最大缓存限制
        commonsMultipartResolver.setMaxInMemorySize(20971520);
        //返回文件解析器对象
        return commonsMultipartResolver;
    }

    //验证码属性

    @Value("${kaptcha.border}")//是否有边框
    private String kaptcha_border;
    @Value("${kaptcha.textproducer.font.color}")
    private String kaptcha_textproducer_font_color;//字体颜色
    @Value("${kaptcha.image.width}")
    private String kaptcha_image_width;//图片宽度
    @Value("${kaptcha.textproducer.char.string}")
    private String kaptcha_textproducer_char_string;// 使用哪些字符生成验证码
    @Value("${kaptcha.image.height}")
    private String kaptcha_image_height;//图片高度
    @Value("${kaptcha.textproducer.font.size}")
    private String kaptcha_textproducer_font_size;//字体大小
    @Value("${kaptcha.noise.color}")
    private String kaptcha_noise_color;//干扰线的颜色
    @Value("${kaptcha.textproducer.char.length}")
    private String kaptcha_textproducer_char_length;//字符个数
    @Value("${kaptcha.textproducer.font.names}")
    private String kaptcha_textproducer_font_names;//字体

    /**
     * 注册servlet
     * KaptchaServlet servlet 返回处理验证码处理
     *
     * @return ServletRegistrationBean
     * @throws ServletException 异常
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        //捕获对/Kaptcha的请求,并注册一个servlet返回
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
        //设置servlet属性
        servlet.addInitParameter("kaptcha.border", kaptcha_border);
        servlet.addInitParameter("kaptcha.textproducer.font.color", kaptcha_textproducer_font_color);
        servlet.addInitParameter("kaptcha.image.width", kaptcha_image_width);
        servlet.addInitParameter("kaptcha.textproducer.char.string", kaptcha_textproducer_char_string);
        servlet.addInitParameter("kaptcha.image.height", kaptcha_image_height);
        servlet.addInitParameter("kaptcha.textproducer.font.size", kaptcha_textproducer_font_size);
        servlet.addInitParameter("kaptcha.noise.color", kaptcha_noise_color);
        servlet.addInitParameter("kaptcha.textproducer.char.length", kaptcha_textproducer_char_length);
        servlet.addInitParameter("kaptcha.textproducer.font.names", kaptcha_textproducer_font_names);
        return servlet;
    }

    /**
     * 注册拦截器
     *
     * @param registry 注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptPath = "/shopAdmin/**";

        //注册拦截器  引路拦截器对象
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截路径
        loginIR.addPathPatterns(interceptPath);
        loginIR.excludePathPatterns("/shopAdmin/addshopauthmap");

        //注册其它拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截路径
        permissionIR.addPathPatterns(interceptPath);


        //配置放行路径
        //放行列表
        List<String> excludePathPatternList = new ArrayList<>();
        //授权时放行
        excludePathPatternList.add("/shopAdmin/addshopauthmap");
        //shoplist page
        excludePathPatternList.add("/shopAdmin/shoplist");
        excludePathPatternList.add("/shopAdmin/getShopList");
        //shopregister page
        excludePathPatternList.add("/shopAdmin/getShopInitInfo");
        excludePathPatternList.add("/shopAdmin/registerShop");
        excludePathPatternList.add("/shopAdmin/shopOperation");
        //shopmanage page
        excludePathPatternList.add("/shopAdmin/shopManagement");
        excludePathPatternList.add("/shopAdmin/getShopManagementInfo");
        //配置放行列表
        permissionIR.excludePathPatterns(excludePathPatternList);
    }

}

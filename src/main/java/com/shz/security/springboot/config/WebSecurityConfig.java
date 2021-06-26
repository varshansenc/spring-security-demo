package com.shz.security.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)   // 启用基于注解的安全性
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

    @Resource
    UserDetailsService userDetailsService;

    //定义用户信息服务（查询用户信息）
    /*@Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
        return manager;
    }*/

    //密码编码器
    /*@Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return s.equals(DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()));
//            }
//        };
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.userDetailsService(userDetailsService);

        // 跨站请求伪造
        http.csrf().disable();

        http
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAuthority("p1")
//                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/r/**").authenticated()//所有/r/**的请求必须认证通过
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
                .and()
                .formLogin()//允许表单登录
//                .loginPage("/login-view")//登录页面
                .loginProcessingUrl("/login")
                .successForwardUrl("/login-success");   //自定义登录成功的页面地址

        /*
         *   session管理
         *      1. IF_REQUIRED: 如果没有就创建
         *      2. ALWAYS:      如果需要就创建session，(默认登录时创建)
         *      3. NEVER:       不创建session，如果其他地方创建了session，spring security将会使用
         *      4. STATELESS:   不创建session，也不使用session
         */
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        /*
         * 自定义退出
         *      logoutSuccessHandler与logoutSuccessUrl互斥
         */

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-view?logout")
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                          // 进行清理工作
                            // 转发、重定向等
//                    }
//                })
                ;


    }



    // 配置忽略路径
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/a/**")
                .antMatchers("/b/**");
    }
}

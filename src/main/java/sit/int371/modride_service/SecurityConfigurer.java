package sit.int371.modride_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import sit.int371.modride_service.exceptions.CustomAuthenticationFailureHandler;
import sit.int371.modride_service.entities.User;
import sit.int371.modride_service.filters.JwtFilter;
import sit.int371.modride_service.repositories.OldUserRepository;
import sit.int371.modride_service.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private OldUserRepository OldUserRepository;

    @Autowired
    private UserService userService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    // PasswordEncoder
    // NoOpPasswordEncoder.getInstance()

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // ใช้ try-catch ดักได้
    // @Bean
    // public AuthenticationFailureHandler authenticationFailureHandler(){
    // return new CustomAuthenticationFailureHandler();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // filesServer-container
                .antMatchers(HttpMethod.GET, "/api_files/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api_files/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api_files/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api_files/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/api_files/**").permitAll()
                // backend-container
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                // บังคับให้ request ที่ไม่ได้ authen เป็น 401 ให้หมด (เดิมเป็น 403)
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
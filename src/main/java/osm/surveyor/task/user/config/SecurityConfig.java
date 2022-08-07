package osm.surveyor.task.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.util.Role;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // セキュリティ設定を、無視（ignoring）するパスを指定します
        // 通常、cssやjs、imgなどの静的リソースを指定します
        web.ignoring().antMatchers(
    		"/task-bldg/favicon.ico",
    		"/task-bldg/custom/**",
    		"/task-bldg/pref/**",
    		"/task-bldg/js/**",
    		"/task-bldg/css/**",
    		"/task-bldg/img/**",
    		"/task-bldg/data/**",
    		"/task-bldg/webjars/**",
    		"/h2-console/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
		        // 「/login」と「/error」をアクセス可能にします
		        .antMatchers("/task-bldg/login", "/task-bldg/error", "/task-bldg/register").permitAll()
		        .antMatchers("/task-bldg/admin/**").hasRole(Role.ADMIN.name())
		        .anyRequest().authenticated()
		        .and()
		    .formLogin()
		        // ログイン時のURLを指定
		        .loginPage("/task-bldg/login")
		        // 認証後にリダイレクトする場所を指定
		        .defaultSuccessUrl("/task-bldg/city")
		        .and()
		    // ログアウトの設定
		    .logout()
		        // ログアウト時のURLを指定
		        .logoutRequestMatcher(new AntPathRequestMatcher("/task-bldg/logout"))
		        .and()
		    // Remember-Meの認証を許可します
		    // これを設定すると、ブラウザを閉じて、
		    // 再度開いた場合でも「ログインしたまま」にできます
		    .rememberMe();
    }

    /**
     * ユーザ名「admin」と「user」を用意します
     * パスワードは両方とも「password」です
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}

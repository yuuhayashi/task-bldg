package osm.surveyor.task.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // セキュリティ設定を、無視（ignoring）するパスを指定します
        // 通常、cssやjs、imgなどの静的リソースを指定します
        web.ignoring().antMatchers("/city/**", "/css/**", "/img/**", "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
		        // 「/login」と「/error」をアクセス可能にします
		        .antMatchers("/login", "/error").permitAll()
		        .anyRequest().authenticated()
		        .and()
		    .formLogin()
		        // ログイン時のURLを指定
		        .loginPage("/login")
		        // 認証後にリダイレクトする場所を指定
		        .defaultSuccessUrl("/")
		        .and()
		    // ログアウトの設定
		    .logout()
		        // ログアウト時のURLを指定
		        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
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
        auth.inMemoryAuthentication()
	        .withUser("admin")
	            .password(passwordEncoder().encode("password"))
	            .authorities("ROLE_ADMIN")
	            .and()
	        .withUser("user")
	            .password(passwordEncoder().encode("password"))
	            .authorities("ROLE_USER");
    }
}

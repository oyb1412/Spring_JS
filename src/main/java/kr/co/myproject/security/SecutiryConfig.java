package kr.co.myproject.security;

import java.io.IOException;
import java.util.Arrays;
import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecutiryConfig {
	 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		 http
		 
		 .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/error/403")) 
		 .authorizeHttpRequests(authz -> authz
				 .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR, DispatcherType.REQUEST).permitAll()
				 .requestMatchers("/css/**", "/js/**", "/index.html", "/images/**", "/login.html","/api/notice/check", "/noticeCheck.html","/notice-check-page/**",
				 "/api/notice/check/**", "/api/vote/**", "/notice-modify-page/**", "/noticeModify.html", "/api/comment/delete/**", "/api/notice/delete/**",
				 "/api/comment/create/**", "/api/notice/add/**", "/api/notice/modify/**", "/api/user/findPassword/**", "/api/userdata/modify/**", "/api/user/data/**", "/api/admin/ban/**").permitAll()
				 .requestMatchers("/api/user/status","/api/csrf-token","/views/common/header.html", "/views/common/sidebar.html", "/views/common/footer.html").permitAll()
				 .requestMatchers("/WEB-INF/views/**").denyAll()
				 .requestMatchers("/", "/login-page", "/register-page", "/board-check-page/**", "/board-list-page","/login", "/register").permitAll()
				 .requestMatchers("/logout").hasAnyAuthority("ADMIN","MANAGER","MEMBER")
				 .requestMatchers("/notice-check-page/**").hasAnyAuthority("ADMIN","MANAGER","MEMBER")
				 .requestMatchers("/notice-add-page", "/notice-modify-page/**", "/notice-add" , "/notice-delete", "/notice-modify").hasAnyAuthority("ADMIN","MANAGER")

				 .requestMatchers("/my-page", "/board-add-page", "/board-modify-page/**", "/board-vote", "/notice-vote", "/board-report-page", "/board-report").hasAnyAuthority("ADMIN","MANAGER","MEMBER")
				 .requestMatchers("/board-add", "/board-modify", "/board-delete", "/comment-add", "/comment-delete").hasAnyAuthority("ADMIN","MANAGER","MEMBER")
				 .requestMatchers("/admin-page", "/admin-ban", "/dummy-board-add", "/dummy-board-add-page", "/projects-board-add-page", "/projects-board-add").hasAnyAuthority("ADMIN")
				 .anyRequest().authenticated())
		 .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
		 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
		 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
		 .formLogin(login -> login
		 .loginPage("/login-page")
		 .loginProcessingUrl("/login")
		 .failureHandler((request, response, exception) -> {
    if (exception instanceof DisabledException) {
        response.sendRedirect("/login-page?error=ban");
    } else {
        response.sendRedirect("/login-page?error=true");
    }
})
		 .usernameParameter("username")
		 .passwordParameter("password")
		 .successHandler(authenticationSuccessHandler())
		 .permitAll()
		 )
		 .logout(logout -> logout
				 .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				 .logoutSuccessUrl("/")
				 .invalidateHttpSession(true)
				 .deleteCookies("JSESSIONID")
				 .permitAll()
				 );
		 
		 return http.build();
	}

	@Bean
	public HttpFirewall httpFirewall() {
    StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
    strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
    return strictHttpFirewall;
}
	
	

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				
				HttpSession session = request.getSession();
				boolean isManager = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> 
				grantedAuthority.getAuthority().equals("ADMIN") || grantedAuthority.getAuthority().equals("MANAGER"));
			
				if(isManager)
				{
					session.setAttribute("ADMIN", true);
					session.setAttribute("MANAGER", true);
				}
				else
				{
					session.setAttribute("MEMBER", true);
				}
				
				session.setAttribute("username", authentication.getName());
				session.setAttribute("isAuthenticated", true);
				
				response.sendRedirect(request.getContextPath() + "/");
				
				super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "https://localhost:8081"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
}

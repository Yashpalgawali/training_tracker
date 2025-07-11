package com.example.demo.security;

//@Configuration
//@EnableWebSecurity
//@EnableWebMvc
public class Security {
//public class Security extends WebSecurityConfigurerAdapter{

//	@Autowired
//	private DataSource datasource;
//	
//	@Autowired
//	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		// TODO Auto-generated method stub
//
//		auth.jdbcAuthentication()
//		.dataSource(datasource)
//		
//		//Following will select the username from database depending on the username from Login form
//		.usersByUsernameQuery("select username,password,enabled from tbl_users where username=? ")
//		
//		//Following will select the authority(s) depending on the username
//		.authoritiesByUsernameQuery("select username,role from tbl_users where username=?")
//		
//		.passwordEncoder(new BCryptPasswordEncoder())
//		;
//
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception
//	{
//		http
//			.csrf().disable()
//			//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//			//.and() 
//			.authorizeRequests()
//			.antMatchers("/employee/","/appointment/","/appointment/*","/appointment/appointmentbymail/*","/users/otp/**","/users/email/**",
//						 "/users/updatepass/**","/appointment/confappointment/**","/appointment/declineappointment/**",
//						 "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//			.anyRequest()
//			.authenticated()
//			.and()
//			
//			.logout()
//			.invalidateHttpSession(true)
//			.and()
//			.httpBasic();
//	}
//	
//	@Override
//	public void configure(WebSecurity web)throws Exception{
//		web.ignoring().antMatchers("/resources/static/**","/css/**","/js/**");
//	}
//
//
//    @Bean
//    WebMvcConfigurer corsConfigurer()
//	{
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//								.allowedOrigins("*")
//								.allowedMethods("POST","GET","DELETE","PUT","PATCH","OPTIONS");
//			}
//		};
//	}
//
////	public CsrfTokenRepository csrfTokenRepository (){
////		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
////		repository.setHeaderName("X-CSRF-TOKEN");
////		
////		return repository;
////	}

}

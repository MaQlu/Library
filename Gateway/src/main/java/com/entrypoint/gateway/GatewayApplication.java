package com.entrypoint.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.HttpMethod;

@EnableR2dbcRepositories
@SpringBootApplication(exclude={ReactiveSecurityAutoConfiguration.class})
public class GatewayApplication {

	@Autowired
	private AuthFilter authFilter;

	@Autowired
	private AuthFilterAdmin authFilterAdmin;

	@Autowired
	private AuthFilterEmployee authFilterEmployee;

	@Autowired
	private AuthFilterUser authFilterUser;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder){
		return builder.routes()
				
				// books routes
				.route("books for users", p-> p
						.path("/waz/books/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							return f;
						})
						.uri("http://backend:8000")

				)

				.route("books for employee", p-> p
						.path("/waz/books/**")
						.and()
						.method(HttpMethod.PATCH,HttpMethod.POST)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterEmployee);
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("books for admin", p-> p
						.path("/waz/books/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterAdmin);
							return f;
						})
						.uri("http://backend:8000")
				)

				//genres routes
				.route("genres for users", p-> p
						.path("/waz/genres/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("genres for employee", p-> p
						.path("/waz/genres/**")
						.and()
						.method(HttpMethod.PATCH,HttpMethod.POST)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterEmployee);
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("genres for admin", p-> p
						.path("/waz/genres/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterAdmin);
							return f;
						})
						.uri("http://backend:8000")
				)

				//authors routes
				.route("authors for users", p-> p
						.path("/waz/authors/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("authors for employee", p-> p
						.path("/waz/authors/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterEmployee);
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("authors for admin", p-> p
						.path("/waz/authors/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterAdmin);
							return f;
						})
						.uri("http://backend:8000")
				)

				//book-genres routes
				.route("book-genres for users", p-> p
						.path("/waz/book-genres/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("book-genres for employee", p-> p
						.path("/waz/book-genres/**")
						.and()
						.method(HttpMethod.PATCH,HttpMethod.POST)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterEmployee);
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("book-genres for admin", p-> p
						.path("/waz/book-genres/**")
						.filters(f -> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterAdmin);
							return f;
						})
						.uri("http://backend:8000")
				)

				//libraries routes
				.route("libraries for users", p -> p
						.path("/waz/libraries/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("libraries for employee", p-> p
						.path("/waz/libraries/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterEmployee);
							return f;
						})
						.uri("http://backend:8000")
				)

				.route("libraries for admin", p-> p
						.path("/waz/libraries/**")
						.filters(f-> {
							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
							f.filter(authFilterAdmin);
							return f;
						})
						.uri("http://backend:8000/libraries/")
				)
				//allow everything for everyone
//				.route("GET from all" , p-> p
//						.path("/waz/**")
//						.filters( f -> {
//							f.rewritePath("/waz/(?<segment>.*)", "/${segment}");
//							return f;
//						})
//						.uri("http://backend:8000")
//				)

				.build();
	}
}
# SpringAuthJPA

Spring Web
Spring Data JPA
H2 Database

## Version 1
@RestController
SecurityFilterChain
@Entity - Class Post - interface PostRepository extends CrudRepository<Post, Long>
@Bean CommandLineRunner commandLineRunner(PostRepository posts)
h2-console

## Version 2
@Entity - @Table(name = "users") - class User

Method Security
class SecurityConfig - @EnableMethodSecurity 
    - class HomeController - @PreAuthorize("hasRole('ROLE_USER')")/@GetMapping("/user").....

SecurityFilterChain - .userDetailsService(jpaUserDetailsService)
    - JpaUserDetailsService - UserDetails loadUserByUsername() - need a UserDetails to return
        - userOldRepository.findByUsername(username) - return User
        - .map(SecurityUser::new) - SecurityUser implements UserDetails - return SecurityUser(UserDetails)
    - UserRepository - extends CrudRepository<User, Long>

Use No Encoded Password
SecurityConfig - @Bean - PasswordEncoder passwordEncoder() - return NoOpPasswordEncoder.getInstance()

## Version 3
Use user, group, group_members, group_authorities tables

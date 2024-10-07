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

deleteBy
1. @Transactional add to Repository Interface
2. Need to return the query list, e.g. List<GroupMember> deleteByGroupId(Long groupId)


## Test
GET http://localhost:8080/admin/groups

User JSON
{
"username": "test0009",
"password": "password",
"firstname": "FirstT9",
"lastname": "LastT9",
"email": "test0009@bbb.ccc",
"groupName": "Readers",
"newUsername": "test0009II"
}
POST/PUT/DELETE http://localhost:8080/user
POST/DELETE http://localhost:8080/admin/usergroup

Group JSON
{
"groupName": "Readers",
"authority": "Read",
"newGroupName": "ReadersII"
}
GET http://localhost:8080/admin/groupusers
POST/PUT/DELETE http://localhost:8080/admin/group
GET http://localhost:8080/admin/groupauthorities
POST/DELETE http://localhost:8080/admin/groupauthority
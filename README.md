# SpringAuthJPA

Spring Web
Spring Data JPA
H2 Database

## Version 1
@RestController
SecurityFilterChain
@Entity Class Post
interface PostRepository extends CrudRepository<Post, Long>
@Bean CommandLineRunner commandLineRunner(PostRepository posts)
h2-console


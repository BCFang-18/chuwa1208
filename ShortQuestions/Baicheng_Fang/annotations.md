```java
import java.lang.module.Configuration;

Core
@Component // generic spring-mangaged bean
@Service // business logic layer
@Repository // DAO/persistence layer
@Controller // MVC controller that returns views
@RestController // @Controller + @Responsebody, rest controller

Dependency Injection
@Autowired // automatic dependency injection
@Qualifier // choose a specific bean while multiple exist
@Primary // default bean when multiple candidates exist
@Value // inject values from application.properties
@Lazy // lazy initialization

Configuration and Bean Management
@Configuration // java-based configuration class
@Bean // declare a bean manually
@ComponentScan // scan packages for components
@PropertySource // load external property files
@Profile // activate beans for specific environments

Web / REST (Sprint MVC) / map http requests
@RequestMapping 
@GetMapping
@PostMapping
@PutMappting
@DeleteMapping
@PatchMapping
@PathVariable
@RequestParam
@RequestBody
@ResponseBody
@ResponseStatus

Validation
@Valid
@NotNull
@NotBlank
@NotEmpty
@Size
@Min, @Max
@Email

Persistence (JPA / Hibernate)
@Entity // JPA entity
@Table // map entity to table
@Id // PK
@GeneratedValue // auto-generate ID
@Column // map field to column
@OneToMany, @ManyToOne
@ManyToMany, @OneToOne
@JoinColumn
@Transactional

Except Handling
@ExceptionHandler // handle exception in controller
@ControllerAdvice // global exception handling
@RestControllerAdvice // rest version of @ControllerAdvice

Security
@EnableWebSecurity
@PreAuthorize
@PostAuthorize
@Secured

Testing
@SprintBootTest
@WebMvcTest
@DataJpaTest
@MockBean
@AutoConfigureMockMvc
```
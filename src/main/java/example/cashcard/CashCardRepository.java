package example.cashcard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//CrudRepository is an interface supplied by Spring Data. When we extend it (or other sub-Interfaces 
//of Spring Data's Repository), Spring Boot and Spring Data work together to automatically generate 
//the CRUD methods that we need to interact with a database
interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {



}

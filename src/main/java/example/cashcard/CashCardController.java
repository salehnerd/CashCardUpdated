package example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;


//This tells Spring that this class is a Component of type RestController and capable of handling 
//HTTP requests.
@RestController
@RequestMapping("/cashcards")//This is a companion to @RestController that indicates which address requests must have to access this Controller.
class CashCardController {
	
	
	private final CashCardRepository cashCardRepository;
	
	 private CashCardController(CashCardRepository cashCardRepository) {
	      this.cashCardRepository = cashCardRepository;
	   }
	
	/*
	//@GetMapping marks a method as a handler method. GET requests that match cashcards/{requestedID} 
	//will be handled by this method.
	@GetMapping("/{requestedId}")
	private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) { //@PathVariable makes Spring Web aware of the requestedId supplied in the HTTP request.
	   CashCard cashCard = new CashCard(99L, 123.45);
	   return ResponseEntity.ok(cashCard);
	   */
	   
	 @GetMapping("/{requestedId}")
	 private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
		 
		 //We're calling CrudRepository.findById, which returns an Optional. This smart object might 
		 //or might not contain the CashCard for which we're searching. Learn more about Optional here "https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html".
	     Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
	     
	     //If cashCardOptional.isPresent() is true, then the repository successfully found the 
	     //CashCard and we can retrieve it with cashCardOptional.get().
	     if (cashCardOptional.isPresent()) {
	         return ResponseEntity.ok(cashCardOptional.get());
	     //If not, the repository has not found the CashCard.
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 }
	 
	 				//Unlike the GET we added earlier, the POST expects a request "body". This contains the data 
	 @PostMapping  	//submitted to the API. Spring Web will deserialize the data into a CashCard for us.
	 private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
		//As learned in previous lessons and labs, Spring Data's CrudRepository provides methods that support creating, 
		 //reading, updating, and deleting data from a data store. cashCardRepository.save(newCashCardRequest) does just 
		 //as it says: it saves a new CashCard for us, and returns the saved object with a unique id provided by the 
		 //database. Amazing!
	    CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);
	    
	    //This is constructing a URI to the newly created CashCard. This is the URI that the caller can then use to GET the newly-created CashCard.
	    //Note that savedCashCard.id is used as the identifier, which matches the GET endpoint's specification of cashcards/<CashCard.id>.
	    URI locationOfNewCashCard = ucb
	             .path("cashcards/{id}")
	             .buildAndExpand(savedCashCard.id())
	             .toUri();
	    
	    //Finally, we return 201 CREATED with the correct Location header.
	    return ResponseEntity.created(locationOfNewCashCard).build();
	    
	    //Where did UriComponentsBuilder come from?
	    //We were able to add UriComponentsBuilder ucb as a method argument to this POST handler method and it was 
	    //automatically passed in. How so? It was injected from our now-familiar friend, Spring's IoC Container. Thanks, Spring Web!
	 }
	 	

	 @GetMapping							//Pageable is yet another object that Spring Web provides for us. Since we specified the URI parameters of page=0&size=1, pageable will contain the values we need.
	 private ResponseEntity<List<CashCard>> findAll(Pageable pageable)
	 
	 {
		 //PageRequest is a basic Java Bean implementation of Pageable. Things that want paging and sorting implementation often support this, such as some types of Spring Data Repositories.
		 Page<CashCard> page = cashCardRepository.findAll(
				PageRequest.of(
				 pageable.getPageNumber(),
				 pageable.getPageSize(),
				 //The answer is that the getSortOr() method provides default values for the page, size, and sort parameters. The default values come from two different sources:
				 //Spring provides the default page and size values (they are 0 and 20, respectively). A default of 20 for page size explains why all three of our Cash Cards were returned. Again: we didn't need to explicitly define these defaults. Spring provides them "out of the box".
				 //We defined the default sort parameter in our own code, by passing a Sort object to getSortOr():
				 pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount")
						 )));
		 return ResponseEntity.ok(page.getContent());
	}

}
	


package example.cashcard;


import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

//annotation marks the CashCardJsonTest as a test class which uses the Jackson framework 
//(which is included as part of Spring). This provides extensive JSON testing and parsing support. 
//It also establishes all the related behavior to test JSON objects.

@JsonTest
class CashCardJsonTest2 {

	//is an annotation that directs Spring to create an object of the requested type.
    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;
    private CashCard[] cashCards;
    
    
    
    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
        
        //The test code is self-explanatory: It serializes the cashCards variable into JSON, then asserts that list.json should 
       //contain the same data as the serialized cashCards variable.
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }
    
    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected="""
              [
                 { "id": 99, "amount": 123.45 },
                 { "id": 100, "amount": 1.00 },
                 { "id": 101, "amount": 150.00 }
              ]
              """;
       assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
//       assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45));
//       assertThat(json.parseObject(expected).id()).isEqualTo(99);
//       assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }
    
    
}

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import static org.hamcrest.Matchers.*;

import java.time.LocalDate;


public class WeatherAPiTest {

	// This is the test with all the validations
	@Test
	void testDaysSuitableForSurfing() {
		
		Response response = doGetRequest();
	    int iDaysSuitableForSurfing = getNoofDaysGoodForSurfing(response);
		System.out.println("Days suitable for surfing =" + iDaysSuitableForSurfing);
	}
	
	public static Response doGetRequest() {
        RestAssured.defaultParser = Parser.JSON;

        return
        		 given()
		         .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
		         .param("key","09fd954c32484ef880f11e842f5f82d3")
		         .param("city", "Sydney")
		         .get("https://api.weatherbit.io/v2.0/forecast/daily").then().
		         contentType(ContentType.JSON).extract().response();
    }
	public String getDay(String sDate) {
		String[] arrOfStr = sDate.split("-", 5); 
		LocalDate localDate = LocalDate.of(Integer.valueOf(arrOfStr[0]),Integer.valueOf(arrOfStr[1]),Integer.valueOf(arrOfStr[2]));
		String dayOfWeek = String.valueOf(localDate.getDayOfWeek());
		return dayOfWeek;	
	}
	public int getNoofDaysGoodForSurfing(Response response) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		int iNoofThuandFri = 0;
		float iTemp;
		int iNoofDaysGoodForSurfing=0;
		for (int i = 0; i < 16; i++) {
			String sDate = jsonPathEvaluator.get("data.datetime["+i+"]");
			String dayOfWeek = getDay(sDate);
			System.out.println(sDate + "=" + dayOfWeek );
			 if (dayOfWeek == "THURSDAY" ||dayOfWeek== "FRIDAY") {
				 iNoofThuandFri++;
				 iTemp = jsonPathEvaluator.get("data.temp["+i+"]") ;
				 if (iTemp<20 || iTemp>30 ) {
					 
					 System.out.println("Temperature =" + iTemp );
					 iNoofDaysGoodForSurfing++;
				 }
			 }
			}
		System.out.println("No of Thursday's and Fridays=" + iNoofThuandFri  );
		return iNoofDaysGoodForSurfing;}
}

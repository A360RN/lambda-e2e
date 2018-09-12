package pe.com.a360rn.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pe.com.a360rn.dto.Person;

public class LambdaFunctionHandler implements RequestHandler<Object, List<Person>> {

	@Override
	public List<Person> handleRequest(Object input, Context context) {

		List<Person> lstPeople = new ArrayList<>();

		ScanRequest request = new ScanRequest().withTableName("People");

		final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

		try {
			ScanResult result = ddb.scan(request);
			for (Map<String, AttributeValue> item : result.getItems()) {
				Person person = new Person();
				person.setId(Integer.parseInt(item.get("id").getN()));
				person.setName(item.get("name").getS());
				person.setSurname(item.get("surname").getS());
				lstPeople.add(person);
			}
		} catch (Exception e) {
			context.getLogger().log("Error" + e);
		}

		context.getLogger().log("Response: " + lstPeople);

		return lstPeople;
	}

}

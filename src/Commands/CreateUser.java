package Commands;

import Commands.Command;
import Model.User;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class CreateUser extends ConcreteCommand {
	public void execute() throws NoSuchAlgorithmException {
		this.consume("r3");
		HashMap<String, Object> props = parameters;
		Channel channel = (Channel) props.get("channel");
		JSONParser parser = new JSONParser();
        String name;
        String birthdate;
        String bio;
        String phone_number;
        String address;
		try {
			JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
//	        System.out.println("The BODY is: " + body.toString());
			JSONObject params = (JSONObject) parser.parse(body.get("body").toString());
//			System.out.println("The params are: " + body.toString());
			name = params.get("name").toString();
			birthdate = params.get("birthdate").toString();
			bio = params.get("bio").toString();
			address = params.get("address").toString();
			phone_number = params.get("phone_number").toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
		AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
		Envelope envelope = (Envelope) props.get("envelope");
		String response = Restaurant.Create(name, birthdate, bio, phone_number, address);
//		System.out.print("Response ");
//		System.out.println(response);
		sendMessage("database", properties.getCorrelationId(), response);
	}

	@Override
	public void handleApi(HashMap<String, Object> service_parameters) {
		HashMap<String, Object> props = parameters;
		AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
		AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
		String serviceBody = service_parameters.get("body").toString();

		Envelope envelope = (Envelope) props.get("envelope");
		try {
			channel.basicPublish("", properties.getReplyTo(), replyProps, serviceBody.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
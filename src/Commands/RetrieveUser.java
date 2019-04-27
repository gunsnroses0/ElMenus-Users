package Commands;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Commands.Command;
import Model.User;

//import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class RetrieveUser extends ConcreteCommand {

	public void execute() throws NoSuchAlgorithmException {
		this.consume("r1");
		HashMap<String, Object> props = parameters;
		Channel channel = (Channel) props.get("channel");
		JSONParser parser = new JSONParser();
		int id = 0;
		try {
			JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
			String url = body.get("uri").toString();
			url = url.substring(1);
			String[] parametersArray = url.split("/");
			id = Integer.parseInt(parametersArray[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
		AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
		Envelope envelope = (Envelope) props.get("envelope");
		String response = User.getById(id);
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

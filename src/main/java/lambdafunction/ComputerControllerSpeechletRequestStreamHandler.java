package main.java.lambdafunction;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import main.java.settings.Settings;

/**
 * This class is the handler for the AWS Lambda function. The user specific 
 * values for this handler are imported from main.java.settings.Settings.
 * If you would like to use one lambda function for multiple skills
 * (e.g. if you were to create an duplicate skill, but with a different invocation name),
 * then you may need to add multiple applicationsID to the supportedApplicationIDs set.
 */
public final class ComputerControllerSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
	
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add(Settings.applicationID);
    }

    public ComputerControllerSpeechletRequestStreamHandler() {
        super(new ComputerControllerSpeechlet(), supportedApplicationIds);
    }
}

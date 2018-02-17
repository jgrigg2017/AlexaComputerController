package main.java.lambdafunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.slu.Slot;

import java.net.URL;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;

import main.java.settings.Settings;


/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class ComputerControllerSpeechlet implements Speechlet {
	private static final String VOLUME_SLOT = "Volume";
    private static final Logger log = LoggerFactory.getLogger(ComputerControllerSpeechlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        Map<String, Slot> slots = intent.getSlots();
        
        String urlString = Settings.urlString;
        String queryString = "";
        String speechText = "";
        SimpleCard card = new SimpleCard();
        
        switch (intentName) {
	    	case "AMAZON.HelpIntent":
	    		return getHelpResponse();
        	case "HelloWorldIntent":
        		speechText = "HelloWorld";
        		queryString = "VLCPause";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCPlayIntent":
        		speechText = "Pressed Play";
        		queryString = "VLCPlay";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCPauseIntent":
        		speechText = "VLC Paused";
        		queryString = "VLCPause";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCStopIntent":
        		speechText = "Pressed Stop";
        		queryString = "VLCStop";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCNextIntent":
        		speechText = "Pressed Next";
        		queryString = "VLCNext";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCPreviousIntent":
        		speechText = "Pressed Previous";
        		queryString = "VLCPrevious";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCFullscreenIntent":
        		speechText = "VLC Fullscreen";
        		queryString = "VLCFullscreen";
        		card.setTitle("Command Executed:");
        		break;
        	case "VLCVolumeUpIntent":
                Slot volumeSlot = slots.get(VOLUME_SLOT);
                String volumeIncreasedBy;
                if (volumeSlot != null) {
                	volumeIncreasedBy = volumeSlot.getValue();
                } else {
                	volumeIncreasedBy = "5";
                }
            	queryString = "VLCVolumeUp=" + volumeIncreasedBy;
        		speechText = "Increased VLC volume by " + volumeIncreasedBy;
        		card.setTitle("Command Executed:");
        		break;
        	default:
        		throw new SpeechletException("Invalid Intent");
        }
        
		card.setContent(speechText);
		
        try {
	        URL url = new URL(urlString + "?" + queryString);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        int responseCode = con.getResponseCode();
		    con.connect();
		    con.getInputStream();
        } catch (MalformedURLException e) {
    		System.out.println("whoops");
    	} catch (IOException e) {
    		System.out.println("whoops");
    	}
	    
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);

    }


    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome Response";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ComputerController");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

   

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "Use me to control your computer";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help Response");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}

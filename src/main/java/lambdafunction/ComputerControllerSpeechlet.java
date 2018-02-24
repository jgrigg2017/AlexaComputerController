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
import java.net.SocketTimeoutException;
import java.io.IOException;

import main.java.settings.Settings;


/**
 * ComputerControllerSpeechlet is the main code used for the Amazon Alexa skill.
 * It defines what will happen when the skill is launched, exited, and spoken to.
 * It will be called upon by the AWS Lambda function.
 */
public class ComputerControllerSpeechlet implements Speechlet {
	// The slot constant string variables refer to the IntentSchema.json slot names.
	private static final String AMOUNT_SLOT = "Amount";
    private static final Logger log = LoggerFactory.getLogger(ComputerControllerSpeechlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        /*
         * Any initialization logic goes here. 
         * TODO: Add a check for whether the server is running. 
         * If not running, issue voice response and exit skill.
         */
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
        
        // Retrieve the intent name and slot(s).
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        Map<String, Slot> slots = intent.getSlots();
        
        // urlString will be initialized will all parts except the query string.
        // speechText will be spoken by Alexa after a command and written on the Simple Card.
        String urlString = Settings.urlString;
        String queryString = "";
        String speechText = "";
        String cardTitle = "Command Executed:";
        SimpleCard card = new SimpleCard();
        Slot amountSlot;
        PlainTextOutputSpeech outputSpeech;
        
        // Define the speech text, query string, parameter values, and card titles for each Intent.
        switch (intentName) {
    		case "UnknownIntent":
    			outputSpeech = new PlainTextOutputSpeech();
    			outputSpeech.setText("I'm sorry, I didn't understand.");
    			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
    			repromptSpeech.setText("Could you repeat that?");
    			Reprompt reprompt = new Reprompt();
    	        reprompt.setOutputSpeech(repromptSpeech);
    			return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        	case "AMAZON.CancelIntent":
                outputSpeech = new PlainTextOutputSpeech();
                outputSpeech.setText("Canceled");
                return SpeechletResponse.newTellResponse(outputSpeech);
        	case "AMAZON.StopIntent":
                outputSpeech = new PlainTextOutputSpeech();
                outputSpeech.setText("Stopped");
                return SpeechletResponse.newTellResponse(outputSpeech);
	    	case "AMAZON.HelpIntent":
	    		return getHelpResponse();
        	case "VLCPlayIntent":
        		speechText = "Pressed Play";
        		queryString = "VLCPlay";
        		break;
        	case "VLCPauseIntent":
        		speechText = "VLC Paused";
        		queryString = "VLCPause";
        		break;
        	case "VLCStopIntent":
        		speechText = "Pressed Stop";
        		queryString = "VLCStop";
        		break;
        	case "VLCNextIntent":
        		speechText = "Pressed Next";
        		queryString = "VLCNext";
        		break;
        	case "VLCPreviousIntent":
        		speechText = "Pressed Previous";
        		queryString = "VLCPrevious";
        		break;
        	case "VLCFullscreenIntent":
        		speechText = "VLC Fullscreen";
        		queryString = "VLCFullscreen";
        		break;
        	case "VLCVolumeUpIntent":
                amountSlot = slots.get(AMOUNT_SLOT);
                String volumeIncreasedBy;
                if (amountSlot != null) {
                	volumeIncreasedBy = amountSlot.getValue();
                } else {
                	volumeIncreasedBy = "5";
                }
            	queryString = "VLCVolumeUp=" + volumeIncreasedBy;
        		speechText = "Increased VLC volume by " + volumeIncreasedBy;
        		break;
        	case "VLCVolumeDownIntent":
                amountSlot = slots.get(AMOUNT_SLOT);
                String volumeDecreasedBy;
                if (amountSlot != null) {
                	volumeDecreasedBy = amountSlot.getValue();
                } else {
                	volumeDecreasedBy = "5";
                }
            	queryString = "VLCVolumeDown=" + volumeDecreasedBy;
        		speechText = "Decreased VLC volume by " + volumeDecreasedBy;
        		break;
        	case "WinShutdownIntent":
        		speechText = "Shutting Down Computer";
        		queryString = "WinShutdown";
        		break;
        	case "WinRestartIntent":
        		speechText = "Resarting Computer";
        		queryString = "WinRestart";
        		break;
        	case "WinShutdownAbortIntent":
        		speechText = "Attempting to abort shutdown.";
        		queryString = "WinShutdownAbort";
        		break;
        	case "WinLogOffIntent":
        		speechText = "Logging off computer.";
        		queryString = "WinLogOff";
        		break;
        	case "WinLockIntent":
        		speechText = "Locking computer.";
        		queryString = "WinLock";
        		break;
        	case "WinHibernateIntent":
        		speechText = "Computer Hibernating";
        		queryString = "WinHibernate";
        		break;
        	case "WinMinimizeAllIntent":
        		speechText = "Minimizing all windows.";
        		queryString = "WinMinimizeAll";
        		break;
        	case "WinMinimizeMostIntent":
        		speechText = "Minimizing all except active window.";
        		queryString = "WinMinimizeMost";
        		break;
        	default:
        		throw new SpeechletException("Invalid Intent");
        }
        
        card.setTitle(cardTitle);
		card.setContent(speechText);
		
        try {
	        URL url = new URL(urlString + "?" + queryString);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setConnectTimeout(2000);
		    con.connect();
		    con.getInputStream();
        } catch (SocketTimeoutException e) {
        	PlainTextOutputSpeech timeoutSpeech = new PlainTextOutputSpeech();
        	timeoutSpeech.setText("Server not available");
        	return SpeechletResponse.newTellResponse(timeoutSpeech);
    	} catch (MalformedURLException e) {
    		System.out.println("Malformed URL Exception");
    	} catch (IOException e) {
    		System.out.println("IO Exception");
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
        String speechText = "Hello";

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
        String speechText = "Use me to control your computer. You can say things like: pause V L C or press play."
        		+ "Use your Alexa phone app to view more commands.";

        // Create the Simple card content.
        String cardText = "Example commands include: \n"
        		+ "press play \n"
        		+ "press stop \n"
        		+ "press pause \n"
        		+ "play next \n"
        		+ "play previous \n"
        		+ "make VLC fullscreen \n"
        		+ "turn the volume up \n"
        		+ "turn the volume up by {number} \n"
        		+ "turn the volume down \n"
        		+ "turn the volume down by {number} \n";
 
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(cardText);
        

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}

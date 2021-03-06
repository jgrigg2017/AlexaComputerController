# ALEXA COMPUTER CONTROLLER

## Purpose:

This Github project demonstrates how to control a computer using an Amazon Alexa device, such as an Amazon Echo or the Amazon Alexa phone app. The project provides example code for how to control VLC media player and some basic Windows events like shutting down or restarting the computer using Java's Robot class, which simulates keyboard and mouse events. The code can be easily modified to allow for voice activation of any script on the computer.

### How it works:

1. The user first starts up a basic http server ([Server.java](src/main/server/Server.java)), which listens for any command requests and executes the associated command scripts. The server should be kept running whenever the computer is on, so that it is always ready to execute Alexa commands.

2. The user speaks a command to their Alexa device, e.g. "Turn up the volume on VLC by 25."

3. Alexa's language interpreting algorithm will use the user-defined intent schema and slot definitions to parse the request and determine that the user is requesting the "VLCVolumeUp" intent with an "Amount" slot value of 25 (type AMAZON.NUMBER).

4. The intent and slot value(s) are passed to the user created AWS Lambda function. Here, [ComputerControllerSpeechlet.java](/src/main/java/lambdafunction/ComputerControllerSpeechlet.java) has defined what should happen for each intent. In this case, a query string representing the intent and slot values is created and is submitted to the computer's server as an HTTP GET request.

5. The computer's server receives the HTTP GET request and executes the proper command.

### Usage Example:
User: "Alexa, tell computer to press stop" <br />
(Alexa connects to http://[server address]/ComputerController/?VLCStop) <br />
([Server.java](src/main/server/Server.java) receives the request and executes VLCCommands.stop()) <br />
Alexa: "VLC stopped"

## SETUP

### Initial Setup:

1) Install [Java](https://www.java.com/en/download/help/download\_options.xml) and [Maven](http://maven.apache.org/download.cgi). Instructions for installing Maven can be found [here](http://maven.apache.org/install.html).

2) Clone this Github project to your computer.

3) Edit [Settings-TODO.java](/src/main/java/settings/Settings-TODO.java) to include your computer's WAN IP address and rename the file "Settings.java"

4) Unless your computer is directly connected to the internet, you will likely need to set up port forwarding on your router to forward port 80 traffic (the http standard port) to your computer's LAN IP address.

### Alexa Skill Setup (Part I):

1) Go to your [Amazon Developer Console](https://developer.amazon.com/edw/home.html#/) and set up a new [custom Alexa Skill](https://developer.amazon.com/docs/custom-skills/understanding-custom-skills.html).

![](https://i.imgur.com/ZTJSeB0.png)

 
2) You will need to give your skill a name and an [invocation name](https://developer.amazon.com/docs/custom-skills/choose-the-invocation-name-for-a-custom-skill.html). After you save the skill, copy/paste your skill's [Application Id](https://developer.amazon.com/docs/custom-skills/handle-requests-sent-by-alexa.html#getting-the-application-id-for-a-skill) into \src\main\java\settings\Settings.java to update the applicationId string variable. Additionally, write down the Application ID. It will be used later during setup of the Lambda function.

![](https://i.imgur.com/WJT1peI.png)


3) Set up the skill's [speech interface](https://developer.amazon.com/docs/custom-skills/define-the-interaction-model-in-json-and-text.html).

- Select the "Interaction Model" tab on the left side menu.
- Copy the contents of [IntentSchema.json](/src/main/java/speechAssets/IntentSchema.json) into the "Intent Schema" entry field.
- Copy the contents of [SampleUtterances.txt](/src/main/java/speechAssets/SampleUtterances.txt) into the "Sample Utterances" entry field.
- Click the "Save" button at the bottom of the screen.

### Building the .jar file:

1. Open command prompt and change directory to the folder containing the project's [pom.xml](/pom.xml) file.
2. Enter the following command: <br />
```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies package
```
3. This will create a /target/ folder containing the zip file "alexa-computer-controller-1.0-jar-with-dependencies.jar" inside your project folder.

### AWS Lambda Setup:

1) Go to your [AWS Lambda console.](https://console.aws.amazon.com/lambda/) AWS Lambda can be used for free for up to 1 million requests per month, which is plenty.

2) In the top right, make sure your server is set to a region that supports Alexa Skills (Tokyo, Ireland, N. Virginia, or Oregon).

![](https://i.imgur.com/Qr9Nq7w.png)

3) Create a new function (orange "Create function" button).

- Select "Author from scratch."
- Name your function whatever you want. I called it "ComputerController"
- Select "Java 8" as the Runtime.
- Select "Choose an existing role."
- Choose "lambda\_basic\_execution."

4) Upload your code.

- In the "Function Code" section, upload "alexa-computer-controller-1.0-jar-with-dependencies.jar"
- Set "Runtime" to "Java 8"
- Set the Handler as
"main.java.lambdafunction.ComputerControllerSpeechletRequestStreamHandler"
- Click "Save" in the top right.
- Write down the ARN number found in the top right. This number will be used during the Alexa skill setup.


5) Define what will trigger your Lambda function.

- Under "Add triggers," add an Alexa Skills Kit trigger.
- To configure the trigger, you can either disable "Skill ID verification" or copy/paste the Alexa Skill Application ID under "Skill ID."
- Click "Add" at the bottom, then "Save" in the top right.

![](https://i.imgur.com/cutox06.png)

**Note:** For a more detailed guide, refer to the [Amazon developer documentation](https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#create-a-lambda-function-for-an-alexa-skill).

### Alexa Skill Setup (Part II):

1. Return to your skill in the Amazon Developer Console.
2. Select "Configuration" on the left side panel.
3. For "Service Endpoint Type" select "AWS Lambda ARN (Amazon Resource Name)."
4. In the "Default" entry field, enter your Lambda function's ARN that you wrote down earlier. It looks something like this: "arn:aws:lambda:us-east-1:58651155688:function:ComputerController"
5. Select "Next" at the bottom of the screen.
6. This should complete the setup of your custom Alexa Skill. Make sure your computer is running [Server.java](src/main/server/Server.java) and you can now test your skill either through the Amazon Developer Console or through an Alexa device such as an Amazon Echo that is linked to your Amazon Developer account.

## Make your own custom commands:

1) Define the [speech interface](https://developer.amazon.com/docs/custom-skills/define-the-interaction-model-in-json-and-text.html) by editing the [SampleUtterances.txt](/src/main/java/speechAssets/SampleUtterances.txt) and [IntentSchema.json](/src/main/java/speechAssets/IntentSchema.json). Open your Alexa skill in the Amazon Developer Console, copy/paste the new Sample Utterances and Intent Schema, and save the changes made to your skill.

2) Edit [ComputerControllerSpeechlet](/src/main/java/lambdafunction/ComputerControllerSpeechlet.java).onIntent() to add a url query string that will be linked to your command.

3) Write a script for your custom ui/gui automation function.

4) Open [Server.java](src/main/server/Server.java), import your custom command, and add a switch case to Server.executeCommands() that will activate your script when the appropriate query string is used.

5) Use Maven to build a new .jar file and upload it to your AWS Lambda function.

///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Title:            Team Project
//Files:            Main.java
//Semester:         CS 400, Spring 2019
//Due:              May 2, 10:00 pm
//
//Authors:           Evan Corden, Robin Stauffer, Ryan Hemmila
//Emails:            corden@wisc.edu, rstauffer@isc.edu, rhemmila@wisc.edu
//Lecturer's Name:   Deb Deppeler
//Lab Section:       001
//
////////////////////80 columns wide////////////////////////////////////////////

package application;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Final Quiz Generator UI class
 * 
 * @authors Evan Corden, Robin Stauffer, Ryan Hemmila
 *
 */
public class Main extends Application {
	QuizBank questions = new QuizBank(); // The main database of questions
	int questionsInBase = 0; // The number of questions in the QuizBank
	Label numQuestions; // The main statement of number of questions in the bank
	ListView<String> topicsList; // ListView to choose topics for a quiz
	ObservableList<String> availableTopics; // List containing the topics currently in the database
    boolean done = false; // Set to true when question is answered
    Iterator<Question> currIter; // Holds the iterator for the current quiz being taken
    Scene dashboardScene; // Global reference to the main dashboard
    Window primaryStage; // Global reference to the primary stage

    /**
     * Main application entry point
     */
	@Override
	public void start(Stage primaryStage) {
      // Set title
      primaryStage.setTitle("Quiz Generator: A-Team 29");
      primaryStage.setOnCloseRequest(confirmCloseEventHandler);
      
      try {
        // Create main title label
        Label topTitle = new Label("Quiz Generator");
        BorderPane.setAlignment(topTitle, Pos.CENTER);
        topTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
  
        // Create right panel
        // Create label to display number of questions in the database
        numQuestions = new Label("Number of Questions in Database: " + questionsInBase);
        numQuestions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
        // Create label for manage database section
        Label manageLabel = new Label("Manage Database:");
        manageLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
  
        // Create button to launch pop-up to add one new question
        Button addQuestion = new Button("Add New Question");
        setAddQuestion(addQuestion);
  
        // Create button to launch pop-up screen to load a new file of questions
        Button loadQuestions = new Button("Load Questions From File");
        setLoadQuestions(loadQuestions);
  
        // Create button to launch pop-up to save current question database to a file
        Button saveQuestions = new Button("Save Questions to File");
        setSaveQuestions(saveQuestions);
  
        // Create right VBox
        VBox rightBox = new VBox(20);
        rightBox.getChildren().addAll(manageLabel, numQuestions, addQuestion, loadQuestions,
            saveQuestions);
  
        // Create left panel
        // Create label for generate quiz section
        Label generateLabel = new Label("Generate Quiz:");
        generateLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
  
        // Create list view for topics selection
        topicsList = new ListView<String>();
        topicsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableTopics = FXCollections.observableArrayList();
        availableTopics.addAll(questions.getAllTopics());
        topicsList.setItems(availableTopics);
  
        // Create label for number of questions per quiz
        Label labelPerQuiz = new Label("Select Number of Questions Per Quiz: ");
        labelPerQuiz.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
        // Create text field for number of questions
        TextField questionsPerQuiz = new TextField();
        questionsPerQuiz.setPrefWidth(40);
        HBox questionsPerBox = new HBox(labelPerQuiz, questionsPerQuiz);
  
        // Create button to start quiz
        Button startQuiz = new Button("Start Quiz");
        startQuiz.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            // List of all selected topics
            ObservableList<String> selections = topicsList.getSelectionModel().getSelectedItems();
            
            // Check for no selected topics
            if (selections.size() == 0) {
              showWarningAlert("Please select at least one topic");
              return;
            }
            
            String numQuestionsPerQuiz = questionsPerQuiz.getText();
            
            // Check the user entered the number of questions
            if (numQuestionsPerQuiz.equals("")) {
              showWarningAlert("Please enter a number of questions");
              return;
            }
  
            // Attempt to get number of questions
            Integer numQ = -1;
            try {
              numQ = Integer.parseInt(numQuestionsPerQuiz);
            } catch (NumberFormatException e) { // user response not an integer
              showErrorAlert("Number of questions must be a positive integer");
              e.printStackTrace();
              return;
            }
  
            if (numQ <= 0) { // user response not positive
              showErrorAlert("Number of questions must be > zero");
              return;
            }
  
            // If no errors, generate quiz
            UserQuiz myQuiz = questions.generateQuiz(selections, numQ);
            currIter = myQuiz.getQuestionIterator();
            int questionNumber = 1;
  
            // Show the quiz on the stage
            showQuiz(primaryStage, questionNumber, myQuiz);
          }
        });
  
        // Create label and HBox for select topics
        Label topicsLabel = new Label("Select Topics: ");
        topicsLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        Label topicsInstructions = new Label("On a mac, hold down \ncommand to choose \nmultiple topics");
        topicsInstructions.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10));
        VBox topicsLabelWithInstructions = new VBox(topicsLabel, topicsInstructions);
  
        HBox selectTopics = new HBox(topicsLabelWithInstructions, topicsList);
        selectTopics.setSpacing(10);
  
        // Create left VBox
        VBox leftBox = new VBox(20);
        leftBox.getChildren().addAll(generateLabel, selectTopics, questionsPerBox, startQuiz);
  
        // Create center spacing region
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
  
        // Create HBox for center pane
        HBox center = new HBox(20);
        center.getChildren().addAll(leftBox, region, rightBox);
  
        // Create border pane
        BorderPane root = new BorderPane();
  
        // Set margins
        root.setPadding(new Insets(30, 30, 30, 30));
  
        // Arrange pieces in panes
        root.setTop(topTitle);
        root.setCenter(center);
  
        // Set up background color
        BackgroundFill background_fill =
            new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        root.setBackground(background);
  
        // Create scene and display
        dashboardScene = new Scene(root, 800, 400);
        dashboardScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
      } catch (Exception e) {
        e.printStackTrace();
      }
	}
	
	/**
     * Creates the pop-up window to allow the user to add one question to the question bank, and allows 
     * the button passed through in the parameters to access that screen.
     * 
     * @param addQuestion - the button that will take the user to the pop-up screen
     */
	private void setAddQuestion(Button addQuestion) {
	  EventHandler<MouseEvent> addQuestionPress = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent e) {
          BorderPane addQuestionForm = new BorderPane();
          final Stage addQuestionDialog = new Stage();
          addQuestionDialog.setTitle("Add Individual Question");
          addQuestionDialog.initModality(Modality.APPLICATION_MODAL);
          addQuestionDialog.initOwner(primaryStage);
  
          // Main title label
          Label addQuestionTitle = new Label("Add Individual Question");
          addQuestionTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
          BorderPane.setAlignment(addQuestionTitle, Pos.CENTER);
  
          // question text label
          Label questionTextLabel = new Label("Question Text: ");
          questionTextLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
          // Text area for question text
          TextArea questionText = new TextArea();
  
          // Create image label
          Label imageLabel = new Label("Optional Image File: ");
          imageLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
          // Image file name text field
          TextField imageFile = new TextField();
          imageFile.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          imageFile.setPrefWidth(300);
  
          // Create topic label
          Label topicLabel = new Label("Topic: ");
          topicLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
          // Image file name text field
          TextField topicField = new TextField();
          topicField.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          topicField.setPrefWidth(300);
  
          // Create choice labels
          Label choiceText1 = new Label("Answer Choice # 1: ");
          choiceText1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          Label choiceText2 = new Label("Answer Choice # 2: ");
          choiceText2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          Label choiceText3 = new Label("Answer Choice # 3: ");
          choiceText3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          Label choiceText4 = new Label("Answer Choice # 4: ");
          choiceText4.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          Label choiceText5 = new Label("Answer Choice # 5: ");
          choiceText5.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
  
          // Label for correct answer chooser
          Label correctChoice = new Label("Which choice is correct?");
          correctChoice.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
          
          // Create combo box for correct answer
          ObservableList<String> correctOptions = FXCollections.observableArrayList();
          final ComboBox<String> correctChoiceCombo = new ComboBox<String>(correctOptions);
          ArrayList<String> userChoices = new ArrayList<>();
  
          // Create choice text fields
          TextField[] possibleChoices = new TextField[5];
  
          // initialize the choice text fields, and set an event handler for each text field that 
          // updates the combo box whenever a text field (choice) is changed
          for (int i = 0; i < 5; i++) {
            possibleChoices[i] = new TextField();
            possibleChoices[i].setPrefWidth(450);
            possibleChoices[i].setOnKeyReleased(new EventHandler<KeyEvent>() {
              @Override
              public void handle(KeyEvent event) {
                userChoices.clear();
  
                for (int i = 0; i < 5; i++)
                  if (!possibleChoices[i].getText().equals(""))
                    userChoices.add(possibleChoices[i].getText());
                
                correctOptions.clear();
                correctOptions.addAll(userChoices);
              }
            });
          }
          
          // Button for submitting the question
          Button addButton = new Button("Add");
          
          // Check for any obvious errors (such as not filling out a required field) then 
          // add the question to the bank
          addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              // Question fields
              String qText = null;
              String iFile = "none";
              String topicText = null;
              ArrayList<String> choices = new ArrayList<>();
              String correctAns = null;
  
              // Holds everything necessary before submission
              String warningMessage = "";
  
              // Check all required fields
              if (questionText.getText().equals(""))
                warningMessage = "\nQuestion text";
              else
                qText = questionText.getText();
  
              if (topicField.getText().equals(""))
                warningMessage = warningMessage + "\nTopic";
              else
                topicText = topicField.getText();
  
              if (userChoices.size() == 0)
                warningMessage = warningMessage + "\nAt least one answer choice";
              else
                choices = userChoices;
  
              // check for duplicates by adding all choices to a set
              Set<String> set = new HashSet<>(userChoices);
              if (set.size() < userChoices.size()) {
                warningMessage = warningMessage + "\nNo duplicate choices";
              }
  
              if (correctChoiceCombo.getValue() == null)
                warningMessage = warningMessage + "\nCorrect answer choice";
              else
                correctAns = correctChoiceCombo.getValue();
  
                // If provided, check if image file exists in directory
                if (!imageFile.getText().equals("")) {
                  iFile = imageFile.getText();
                  File test = new File(iFile);
                  if (!test.exists())
                    warningMessage = warningMessage + "\nError: Provided image file does not exist";
                }
  
                // Some errors must be fixed before adding the new question
                if (!warningMessage.equals("")) { 
                  showWarningAlert("Please provide the following information: " + warningMessage);
                  return;
                }
  
                // No errors, add question to bank
                questions.add(qText, iFile, topicText, choices, correctAns);
  
                // Update number of questions in base
                questionsInBase = questions.getNumQuestions();
                numQuestions.setText("Number of Questions in Database: " + questionsInBase);
  
                // If new topic, update available topics
                if (!availableTopics.contains(topicText)) {
                  availableTopics.add(topicText);
                  Collections.sort(availableTopics);
                  topicsList.setItems(availableTopics);
                }
  
                addQuestionDialog.close();
            }
            }
          );
  
  
          // Create boxes and arrange
          HBox questionTextBox = new HBox();
          questionTextBox.getChildren().addAll(questionTextLabel, questionText);
  
          HBox topicBox = new HBox();
          topicBox.getChildren().addAll(topicLabel, topicField);
  
          HBox imageBox = new HBox();
          imageBox.getChildren().addAll(imageLabel, imageFile);
  
          HBox choice1 = new HBox();
          choice1.getChildren().addAll(choiceText1, possibleChoices[0]);
          HBox choice2 = new HBox();
          choice2.getChildren().addAll(choiceText2, possibleChoices[1]);
          HBox choice3 = new HBox();
          choice3.getChildren().addAll(choiceText3, possibleChoices[2]);
          HBox choice4 = new HBox();
          choice4.getChildren().addAll(choiceText4, possibleChoices[3]);
          HBox choice5 = new HBox();
          choice5.getChildren().addAll(choiceText5, possibleChoices[4]);
  
          HBox correctChoiceBox = new HBox();
          correctChoiceBox.getChildren().addAll(correctChoice, correctChoiceCombo);
          VBox choicesVBox = new VBox(5);
          choicesVBox.getChildren().addAll(choice1, choice2, choice3, choice4, choice5);
  
          VBox centerBox = new VBox(15);
          centerBox.getChildren().addAll(questionTextBox, topicBox, imageBox, choicesVBox,
              correctChoiceBox, addButton);
  
          addQuestionForm.setTop(addQuestionTitle);
          addQuestionForm.setCenter(centerBox);
  
          // Set margins
          addQuestionForm.setPadding(new Insets(10, 10, 10, 10));
          Scene addQuestionScene = new Scene(addQuestionForm, 700, 500);
  
          addQuestionDialog.setScene(addQuestionScene);
          addQuestionDialog.show();
        }
      };
  
      // Registering the event filter
      addQuestion.addEventFilter(MouseEvent.MOUSE_CLICKED, addQuestionPress);
  
    }
	
	/**
     * Creates the pop-up window to allow the user to load questions to the question bank from
     * a file, and allows the button passed through in the parameters to access that screen.
     * 
     * @param loadQuestions - the button that will take the user to the pop-up screen
     */
	private void setLoadQuestions(Button loadQuestions) {
	    
      loadQuestions.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          BorderPane loadQuestionsForm = new BorderPane();
          final Stage loadQuestionsDialog = new Stage();
          loadQuestionsDialog.setTitle("Load Questions From File");
          loadQuestionsDialog.initModality(Modality.APPLICATION_MODAL);
          loadQuestionsDialog.initOwner(primaryStage);
  
          // Main title label
          Label loadQuestionsTitle = new Label("Load Questions From File");
          loadQuestionsTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
          BorderPane.setAlignment(loadQuestionsTitle, Pos.CENTER);
  
          // Instructions label
          Label instructions = new Label("Enter a file name below to load questions from a file: ");
          instructions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
          // TextField for file
          TextField filePath = new TextField();
          filePath.setPrefWidth(400);
  
          // Button to load questions
          Button loadButton = new Button("Load");
          loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              
              // Check that file name was provided
              if (filePath.getText().equals("")) {
                showWarningAlert("Please provide a file name");
                return;
  
              } else { // Attempt to load questions from file
                try { 
                  questions.load(filePath.getText());
  
                } catch (IOException e) {
                  showErrorAlert("There was a problem with the file."
                      + "\nPlease check that the file is in the application directory");
                  e.printStackTrace();
                  return;
  
                } catch (ParseException e) {
                  showErrorAlert("There was a problem parsing the file."
                      + "\nPlease check that the file is in the correct format");
                  e.printStackTrace();
                  return;
                }
              }

              // Update number of questions in base
              questionsInBase = questions.getNumQuestions();
              numQuestions.setText("Number of Questions in Database: " + questionsInBase);
  
              // Update available topics
              availableTopics = FXCollections.observableArrayList();
              availableTopics.addAll(questions.getAllTopics());
              Collections.sort(availableTopics);
              topicsList.setItems(availableTopics);
  
              loadQuestionsDialog.close();
            }
          });
  
          // Create boxes and arrange
          VBox centerBox = new VBox(25);
          centerBox.getChildren().addAll(instructions, filePath, loadButton);
          loadQuestionsForm.setTop(loadQuestionsTitle);
          loadQuestionsForm.setCenter(centerBox);
  
          // Set margins
          loadQuestionsForm.setPadding(new Insets(10, 10, 10, 10));
          Scene loadQuestionsScene = new Scene(loadQuestionsForm, 600, 300);
  
          loadQuestionsDialog.setScene(loadQuestionsScene);
          loadQuestionsDialog.show();
        }
      });
    }
	
	/**
     * Creates the pop-up window to allow the user to save questions to a specified file, and allows 
     * the button passed through in the parameters to access that screen.
     * 
     * @param loadQuestions the button that will take the user to the pop-up screen
     */
	private void setSaveQuestions (Button saveQuestions) {
	  
      saveQuestions.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          BorderPane saveQuestionsForm = new BorderPane();
          final Stage saveQuestionsDialog = new Stage();
          saveQuestionsDialog.setTitle("Load Questions From File");
          saveQuestionsDialog.initModality(Modality.APPLICATION_MODAL);
          saveQuestionsDialog.initOwner(primaryStage);
  
          // Main title label
          Label saveQuestionsTitle = new Label("Save All Questions To File");
          saveQuestionsTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
          BorderPane.setAlignment(saveQuestionsTitle, Pos.CENTER);
  
          // Instructions label
          Label instructions =
              new Label("Enter a file name below to save all questions in the database: ");
          instructions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
  
          // TextField for file
          TextField filePath = new TextField();
          filePath.setPrefWidth(400);
  
          // Button to save questions
          Button saveButton = new Button("Save");
          saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
		      if(saveQuestionsToFile(filePath.getText())) {
		    	  saveQuestionsDialog.close();
		      }
            }
          });
  
          // Create boxes and arrange
          VBox centerBox = new VBox(25);
          centerBox.getChildren().addAll(instructions, filePath, saveButton);
          saveQuestionsForm.setTop(saveQuestionsTitle);
          saveQuestionsForm.setCenter(centerBox);
  
          // Set margins
          saveQuestionsForm.setPadding(new Insets(10, 10, 10, 10));
          Scene saveQuestionsScene = new Scene(saveQuestionsForm, 600, 300);
  
          saveQuestionsDialog.setScene(saveQuestionsScene);
          saveQuestionsDialog.show();
        }
      });
    }
	
	/**
	 * Event handler that is fired when the user clicks the exit button
	 * A popup is displayed that allows the user to save the current questions
	 * or to exit without saving them
	 */
	private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
		// UI Elements
		Label saveQuestionsLabel = new Label("File name to save questions to:");
		VBox wrapper = new VBox(20);
		wrapper.setAlignment(Pos.CENTER);
		TextField fileLocation = new TextField();
		VBox.setMargin(fileLocation, new Insets(10));
		Button save = new Button("Save");
		Button exitWithoutSaving = new Button("Exit without Saving");
		wrapper.getChildren().addAll(saveQuestionsLabel, fileLocation, save, exitWithoutSaving);
		Scene resultScene = new Scene(wrapper, 400, 200);
		save.setMinWidth(250);
		exitWithoutSaving.setMinWidth(250);
		
		// Setup event handlers 
		exitWithoutSaving.setOnMouseClicked(e -> Platform.exit());
		save.setOnMouseClicked(e -> {
			if(saveQuestionsToFile(fileLocation.getText())) {
				Platform.exit();
			}
		});
		
		// Setup stage
		final Stage saveDialog = new Stage();
		saveDialog.setTitle("Quit Application");
		saveDialog.initModality(Modality.APPLICATION_MODAL);
		saveDialog.initOwner(primaryStage);
		saveDialog.setScene(resultScene);
		saveDialog.show();
		
		// Prevent the window from closing
		event.consume();
    };
	
    /**
     * Attempts to save the current quiz database to a specified file
     * 
     * @param filePath is the file to save the questions to
     * @returns whether or not the save was successful
     */
	private boolean saveQuestionsToFile(String filePath) {
		if (filePath.equals("")) {
			showWarningAlert("Please provide a file name");
			return false;
		} else {
			try {
				questions.save(filePath);

			} catch (IOException e) {
				showErrorAlert("Error writing to the file.");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Display the scene for a specific quiz question
	 * 
	 * @param primaryStage is the stage to display the scene
	 * @param questionNumber is the current question number
	 * @param quiz is the active quiz
	 */
	private void showQuiz(Stage primaryStage, int questionNumber, Quiz quiz) {
		// Create border pane
		BorderPane questionPane = new BorderPane();
		Question myQuestion = currIter.next();
		
		// Main question title label
		Label questionTitle = new Label("Question " + questionNumber);
		questionTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		BorderPane.setAlignment(questionTitle, Pos.CENTER);
		
		// Image 
		String iFile = myQuestion.getImageFile();
		ImageView imageView = null;
		File imageFile = new File(iFile);
		if (imageFile.exists() && !imageFile.isDirectory()) {
			Image image = new Image(iFile);
			imageView = new ImageView(image);
			imageView.setFitHeight(200); 
			imageView.setFitWidth(200);
		} else {
			imageView = new ImageView();
			imageView.setFitHeight(200); 
			imageView.setFitWidth(200);
		}
	    
	    // Question text label
	    Text questionText = new Text("Question: " + myQuestion.getQuestionText());
	    questionText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
	    questionText.wrappingWidthProperty().bind(questionPane.widthProperty());
	    
	    // Topic label
	    Label myTopic = new Label("Topic: " + myQuestion.getTopic());
	    myTopic.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 13));
	    
	    VBox textAndLabel = new VBox(10);
	    textAndLabel.getChildren().addAll(questionText, myTopic);
	    
	    // Answer choices
	    ToggleGroup myToggleGroup = new ToggleGroup();
	    ArrayList<RadioButton> buttons = myQuestion.getButtons();
	    myToggleGroup.getToggles().addAll(buttons);
	    VBox choicesBox = new VBox(10);
	    choicesBox.getChildren().addAll(buttons);

	    // Submission result text
	    Label resultLabel = new Label();
	    Button nextButton = new Button();
	    Region fillerRegion = new Region();
	    HBox.setHgrow(fillerRegion, Priority.ALWAYS);
	    
	    HBox bottomBox = new HBox(20);
	    bottomBox.getChildren().addAll(resultLabel, fillerRegion, nextButton);
	    nextButton.setVisible(false);
	    HBox.setMargin(nextButton, new Insets(20));
	    resultLabel.setFont(Font.font(40));
	    
	    // Go to the next stage
	    nextButton.setOnMouseClicked(
	    		new EventHandler<MouseEvent>() {
	    			@Override
	    			public void handle(MouseEvent event) {
	    				// If it has another question goto that question
	    				if(currIter.hasNext()) {
	    					showQuiz(primaryStage, questionNumber + 1, quiz);
	    				} else {
	    					// Else end the quiz
	    					endQuiz(primaryStage, quiz, questionNumber);
	    				}
	    			}
	    		});
	    
	    // Submit Answer Button
	    Button submit = new Button("Submit Answer");
	    submit.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						RadioButton selectedRadioButton = (RadioButton) myToggleGroup.getSelectedToggle();
						if(selectedRadioButton != null) {
							// Show results of question submission
							submit.setDisable(true);
							if(selectedRadioButton.getText().equals(myQuestion.getCorrectChoice())) {
								resultLabel.setText("CORRECT");
								resultLabel.setTextFill(Color.GREEN);
								quiz.correct();
							} else {
								resultLabel.setText("INCORRECT");
								resultLabel.setTextFill(Color.RED);
							}
							// Update next button's properties
							if(currIter.hasNext()) {
								nextButton.setText("Next");
							} else {
								nextButton.setText("Finish");
							}
							nextButton.setVisible(true);
						} else {
							showWarningAlert("Please select an answer");
						}
					}
				});
					
	    // Set up center box
	    VBox centerBox2 = new VBox(20);
	    centerBox2.getChildren().addAll(imageView, textAndLabel, choicesBox, submit, bottomBox);
	    
		// Set margins
		questionPane.setPadding(new Insets(10, 10, 10, 10));

		// Arrange pieces in panes
		questionPane.setTop(questionTitle);
		questionPane.setCenter(centerBox2);
		
		Scene questionScene = new Scene(questionPane,800,600);
		questionScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(questionScene);
		
	}
	
	/**
	 * Display a warning alert
	 * 
	 * @param text is the alert text
	 */
	private void showWarningAlert(String text) {
		showAlert(text, AlertType.WARNING);
	}
	
	/**
	 * Displays an error alert
	 * 
	 * @param text is the alert text
	 */
	private void showErrorAlert(String text) {
		showAlert(text, AlertType.ERROR);
	}
	
	/**
	 * Shows an alert and waits for the user to respond OK
	 * 
	 * @param text is the alert text
	 * @param alertType is the alert type
	 */
	private void showAlert(String text, AlertType alertType) {
		Alert alert = new Alert(alertType, text);
		alert.showAndWait().filter(response -> response == ButtonType.OK);
	}
	
	/**
	 * Display the results scene for a quiz
	 * 
	 * @param primaryStage is the stage to display the scene
	 * @param quiz is the active quiz
	 * @param questionsAnswered is the number of questions answered
	 */
	private void endQuiz(Stage primaryStage, Quiz quiz, int questionsAnswered) {
		BorderPane root = new BorderPane();
		
		// Create title
		Label title = new Label("Quiz Completed");
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setMargin(title, new Insets(10));
		title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
		
		// Setup quiz score and decimal formatter
		float score = quiz.score();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		// Create results layout
		VBox results = new VBox(20);
		Label numberAnswered = new Label("Questions Answered: " + questionsAnswered);
		numberAnswered.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		results.setAlignment(Pos.CENTER_LEFT);
		
		// Create correct questions layout
		Label questionsCorrect = new Label("Questions Correct: " + quiz.getNumCorrect());
		questionsCorrect.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		
		Label scoreLabel = new Label("Percent Correct: " + df.format(score * 100) + "%");
		scoreLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		
		// Setup return button
		Button returnButton = new Button("Return to Dashboard");
		BorderPane.setAlignment(returnButton, Pos.CENTER);
		BorderPane.setMargin(returnButton, new Insets(25));
		
		// Set button to return to dashboard on click
		returnButton.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						primaryStage.setScene(dashboardScene);
					}
				});
		
		// Set root UI background
		results.getChildren().addAll(questionsCorrect, numberAnswered, scoreLabel);
		BackgroundFill backgroundFill = new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(backgroundFill);
		root.setBackground(background);
		root.setTop(title);
		root.setLeft(results);
		root.setBottom(returnButton);
		
		// Create and show scene
		Scene resultScene = new Scene(root, 800, 400);
		resultScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(resultScene);
	}

	/**
	 * Main method to launch the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

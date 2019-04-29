package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

	@Override
	public void start(Stage primaryStage) {
		// Set title
		primaryStage.setTitle("Quiz Generator: A-Team 29");

		try {
			// Create main title label
			Label topTitle = new Label("Quiz Generator");
			BorderPane.setAlignment(topTitle, Pos.CENTER);
			topTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));

			// RIGHT RIGHT RIGHT RIGHT
			// Create label to display number of questions in the database
			numQuestions = new Label("Number of Questions in Database: " + questionsInBase);
			numQuestions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

			// Create label for manage database section
			Label manageLabel = new Label("Manage Database:");
			manageLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

			// Create button to launch pop-up to add one new question
			Button addQuestion = new Button("Add New Question");

			// Create event handler for addQuestion
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

					// Create choice text fields
					TextField choiceField1 = new TextField();
					choiceField1.setPrefWidth(450);
					TextField choiceField2 = new TextField();
					choiceField2.setPrefWidth(450);
					TextField choiceField3 = new TextField();
					choiceField3.setPrefWidth(450);
					TextField choiceField4 = new TextField();
					choiceField4.setPrefWidth(450);
					TextField choiceField5 = new TextField();
					choiceField5.setPrefWidth(450);

					// Label for correct answer chooser
					Label correctChoice = new Label("Which choice is correct?");
					correctChoice.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
					// Create combo box for correct answer
					ObservableList<String> correctOptions = 
							FXCollections.observableArrayList(
									"Answer 1",
									"Answer 2",
									"Answer 3",
									"Answer 4",
									"Answer 5"
									);
					final ComboBox<String> correctChoiceCombo = new ComboBox<String>(correctOptions);

					// Button for submission
					Button addButton = new Button("Add");
					addButton.setOnMouseClicked(
							new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									String qText = null;
									String iFile = "none";
									String topicText = null;
									String [] choices = new String[5];
									String correctAns = null;

									// Holds everything necessary before submission
									String warningMessage = "";

									// Check all required fields
									if (questionText.getText().equals("")) {
										warningMessage = "\nQuestion text";

									} else {
										qText = questionText.getText();
									}

									if (topicField.getText().equals("")) {
										warningMessage = warningMessage + "\nTopic";

									} else {
										topicText = topicField.getText();
									}

									if (choiceField1.getText().equals("")) {
										warningMessage = warningMessage + "\nAt least one answer choice";

									} else {
										choices[0] = choiceField1.getText();
									}

									if (!choiceField2.getText().equals("")) {
										choices[1] = choiceField2.getText();

									}

									if (!choiceField3.getText().equals("")) {
										choices[2] = choiceField3.getText();

									}

									if (!choiceField4.getText().equals("")) {
										choices[3] = choiceField4.getText();

									}

									if (!choiceField5.getText().equals("")) {
										choices[4] = choiceField5.getText();

									}

									if (correctChoiceCombo.getValue() == null) {
										warningMessage = warningMessage + "\nCorrect answer choice";

									} else {
										String tmp = correctChoiceCombo.getValue();
										if (tmp.equals("Answer 1")) {
											correctAns = choices[0];
										} else if (tmp.equals("Answer 2")) {
											correctAns = choices[1];
										} else if (tmp.equals("Answer 3")) {
											correctAns = choices[2];
										} else if (tmp.equals("Answer 4")) {
											correctAns = choices[3];
										} else if (tmp.equals("Answer 5")) {
											correctAns = choices[4];
										} 
									}

									// If provided, check if image file exists in directory
									if (!imageFile.getText().equals("")) {
										iFile = imageFile.getText();
										File test = new File(iFile);
										if (!test.exists()) {
											warningMessage = warningMessage + "\nError: Provided image file does not exist";
										}
									}

									if (!warningMessage.equals("")) {
										Alert alert = new Alert(AlertType.WARNING, 
												"Please provide the following information: " + warningMessage);
										alert.showAndWait().filter(
												response -> response == ButtonType.OK);
										return;
									}

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
					choice1.getChildren().addAll(choiceText1, choiceField1);
					HBox choice2 = new HBox();
					choice2.getChildren().addAll(choiceText2, choiceField2);
					HBox choice3 = new HBox();
					choice3.getChildren().addAll(choiceText3, choiceField3);
					HBox choice4 = new HBox();
					choice4.getChildren().addAll(choiceText4, choiceField4);
					HBox choice5 = new HBox();
					choice5.getChildren().addAll(choiceText5, choiceField5);

					HBox correctChoiceBox = new HBox();
					correctChoiceBox.getChildren().addAll(correctChoice, correctChoiceCombo);
					VBox choicesVBox = new VBox(5);
					choicesVBox.getChildren().addAll(choice1, choice2, choice3, choice4, choice5);

					VBox centerBox = new VBox(15);
					centerBox.getChildren().addAll(questionTextBox, topicBox, imageBox, 
							choicesVBox, correctChoiceBox, addButton);

					addQuestionForm.setTop(addQuestionTitle);
					addQuestionForm.setCenter(centerBox);

					// Set margins
					addQuestionForm.setPadding(new Insets(10, 10, 10, 10));
					Scene addQuestionScene = new Scene(addQuestionForm,700,500);

					addQuestionDialog.setScene(addQuestionScene);
					addQuestionDialog.show();
				} 
			};  
			// Registering the event filter 
			addQuestion.addEventFilter(MouseEvent.MOUSE_CLICKED, addQuestionPress); 

			// Create button to launch pop-up screen to load
			// a new file of questions
			Button loadQuestions = new Button("Load Questions From File");
			loadQuestions.setOnMouseClicked(
					new EventHandler<MouseEvent>() {
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

							// Button to add
							Button addButton = new Button("Add");
							addButton.setOnMouseClicked(
									new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent event) {
											if (filePath.getText().equals("")) {
												Alert alert = new Alert(AlertType.WARNING, 
														"Please provide a file name");
												alert.showAndWait().filter(
														response -> response == ButtonType.OK);
												return;

											} else {
												try {
													questions.load(filePath.getText());

												} catch (IOException e) {
													Alert alert = new Alert(AlertType.ERROR, 
															"There was a problem with the file."
																	+ "\nPlease check that the file is in the application directory");
													alert.showAndWait().filter(
															response -> response == ButtonType.OK);
													e.printStackTrace();
													return;

												} catch (ParseException e) {
													Alert alert = new Alert(AlertType.ERROR, 
															"There was a problem parsing the file."
																	+ "\nPlease check that the file is in the correct format");
													alert.showAndWait().filter(
															response -> response == ButtonType.OK);
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
							centerBox.getChildren().addAll(instructions, filePath, addButton);
							loadQuestionsForm.setTop(loadQuestionsTitle);
							loadQuestionsForm.setCenter(centerBox);

							// Set margins
							loadQuestionsForm.setPadding(new Insets(10, 10, 10, 10));
							Scene loadQuestionsScene = new Scene(loadQuestionsForm,600,300);

							loadQuestionsDialog.setScene(loadQuestionsScene);
							loadQuestionsDialog.show();
						}
					}
					);

			// Create button to launch pop-up to save current question
			// database to a file
			Button saveQuestions = new Button("Save Questions to File");
			saveQuestions.setOnMouseClicked(
					new EventHandler<MouseEvent>() {
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
							Label instructions = new Label("Enter a file name below to save all questions in the database: ");
							instructions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

							// TextField for file
							TextField filePath = new TextField();
							filePath.setPrefWidth(400);

							// Button to add
							Button saveButton = new Button("Save");
							saveButton.setOnMouseClicked(
									new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent event) {
											if (filePath.getText().equals("")) {
												Alert alert = new Alert(AlertType.WARNING, 
														"Please provide a file name");
												alert.showAndWait().filter(
														response -> response == ButtonType.OK);
												return;

											} else {
												try {
													questions.save(filePath.getText());

												} catch (IOException e) {
													Alert alert = new Alert(AlertType.ERROR, 
															"Error writing to the file.");
													alert.showAndWait().filter(
															response -> response == ButtonType.OK);
													e.printStackTrace();
													return;
												}
											}

											saveQuestionsDialog.close();
										}
									});

							// Create boxes and arrange
							VBox centerBox = new VBox(25);
							centerBox.getChildren().addAll(instructions, filePath, saveButton);
							saveQuestionsForm.setTop(saveQuestionsTitle);
							saveQuestionsForm.setCenter(centerBox);

							// Set margins
							saveQuestionsForm.setPadding(new Insets(10, 10, 10, 10));
							Scene saveQuestionsScene = new Scene(saveQuestionsForm,600,300);

							saveQuestionsDialog.setScene(saveQuestionsScene);
							saveQuestionsDialog.show();
						}
					}
					);

			// Create right VBox
			VBox rightBox = new VBox(20);
			rightBox.getChildren().addAll(manageLabel, numQuestions, addQuestion, 
					loadQuestions, saveQuestions);

			// LEFT LEFT LEFT LEFT
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
			startQuiz.setOnMouseClicked(
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							ObservableList<String> selections = topicsList.getSelectionModel().getSelectedItems();
							// Check for no selected topics
							if (selections.size() == 0) {
								Alert alert = new Alert(AlertType.WARNING, 
										"Please select at least one topic");
								alert.showAndWait().filter(
										response -> response == ButtonType.OK);
								return;
							} 

							String numQuestionsPerQuiz = questionsPerQuiz.getText();
							if (numQuestionsPerQuiz.equals("")) {
								Alert alert = new Alert(AlertType.WARNING, 
										"Please enter a number of questions");
								alert.showAndWait().filter(
										response -> response == ButtonType.OK);
								return;
							}

							// Get number of questions
							Integer numQ = -1; 
							try {
								numQ = Integer.parseInt(numQuestionsPerQuiz);

							} catch (NumberFormatException e) {
								Alert alert = new Alert(AlertType.ERROR, 
										"Number of questions must be a positive integer");
								alert.showAndWait().filter(
										response -> response == ButtonType.OK);
								e.printStackTrace();
								return;
							}

							if (numQ <= 0) {
								Alert alert = new Alert(AlertType.ERROR, 
										"Number of questions must be > zero");
								alert.showAndWait().filter(
										response -> response == ButtonType.OK);
								return;
							}
							
							// If no errors, generate quiz
							UserQuiz myQuiz = questions.generateQuiz(selections, numQ);
							currIter = myQuiz.getQuestionIterator();
							int questionNumber = 1;
							
							// Show the quiz on the stage
							showQuiz(primaryStage, questionNumber);
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
			BackgroundFill background_fill = new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY);
			Background background = new Background(background_fill);
			root.setBackground(background);

			// Create scene and display
			Scene dashboardScene = new Scene(root,800,400);
			dashboardScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(dashboardScene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showQuiz(Stage primaryStage, int questionNumber) {
		// Create border pane
		BorderPane questionPane = new BorderPane();
		Question myQuestion = currIter.next();
		done = false;
		
		// Main question title label
		Label questionTitle = new Label("Question " + questionNumber);
		questionTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		BorderPane.setAlignment(questionTitle, Pos.CENTER);
		
		// Image 
		String iFile = myQuestion.getImageFile();
		ImageView imageView = null;
		if (!iFile.contentEquals("none")) {
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
	    Label questionText = new Label("Question: " + myQuestion.getQuestionText());
	    questionText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
	    
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

	    // Submit Answer Button
	    Button submit = new Button("Submit Answer");
	    submit.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//showQuiz()
					}
				});
					
	    // Set up center box
	    VBox centerBox2 = new VBox(20);
	    centerBox2.getChildren().addAll(imageView, textAndLabel, choicesBox, submit);
	    
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
	 * Main method to launch the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
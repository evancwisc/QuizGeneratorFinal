package application;

import java.util.ArrayList;

import javafx.scene.control.RadioButton;

/**
 * Class used to store data about a specific quiz question
 */
 public class Question {
  
  private String correctChoice;
  private String questionText;
  private Integer questionId;
  private String imageFile;
  private ArrayList<String> choices;
  private String topic;
  private ArrayList<RadioButton> buttons;
  
  /**
   * Construct that accepts question data and assigns it to the 
   * available fields
   */
  public Question(String questionText, String imageFile, String topic, ArrayList<String> choices, String correctChoice) {
    this.correctChoice = correctChoice;
    this.questionText = questionText;
    this.imageFile = imageFile;
    this.choices = choices;
    this.topic = topic;
    this.buttons = new ArrayList<RadioButton>(6);
    
    // Create radio button format of choices for fast question generation
    for (String choice : choices) {
    	RadioButton button = new RadioButton(choice);
    	buttons.add(button);
    }
  }
  
  /**
   * @returns the question's correct choice
   */
  public String getCorrectChoice() {
    return this.correctChoice;
  }
  
  /**
   * @returns the question's text
   */
  public String getQuestionText() {
    return this.questionText;
  }
  
  /**
   * @returns the question's Id
   */
  public Integer getQuestionId() {
    return this.questionId;
  }
  
  /**
   * @returns the question's image file
   */
  public String getImageFile() {
    return this.imageFile;
  }
  
  /**
   * @returns the question's choices
   */
  public ArrayList<String> getChoices() {
    return this.choices;
  }
  
  /**
   * @returns the question's topic
   */
  public String getTopic() {
    return this.topic;
  }
  
  /**
   * @returns the question's radio buttons list
   */
  public ArrayList<RadioButton> getButtons() {
    return this.buttons;
  }
}
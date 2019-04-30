package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.parser.ParseException;
import javafx.collections.ObservableList;

/**
 * An interface for a data structure
 * that contains all questions
 */
public interface QuizBankADT {

  /**
   * Constructs a new quiz object based on the given topics
   * with a number of questions equal to questionCount
   *
   * @param topics is an ObservableList of all topics the questions should be on
   * @param questionCount is the number of questions in the quiz
   * @returns a quiz with the specified number of questions
   */
  Quiz generateQuiz(ObservableList<String> topics, Integer questionCount);
  
  /**
   * Saves the current questions in the QuizBank to the file specified
   * in JSON format
   *
   * @param fileName is the file to save to
   * @throws IOException if the file could not be written
   */
  void save(String fileName) throws IOException;
  
  /**
   * Loads the JSON file specified by the fileName into the QuizBank
   * 
   * @param fileName is the file to load
   * @throws ParseException if the file had an incorrect format
   * @throws IOException if the file could not be read from
   * @throws FileNotFoundException if the file does not exist
   */
  void load(String fileName) throws FileNotFoundException, IOException, ParseException;
  
  /**
   * Generates an individual Question object and adds it to the QuizBank
   *
   * @param questionText is the question's text
   * @param imageFile is the question's imageFile
   * @param topic is the question's topic
   * @param choices are the question choices
   * @param correctChoice is the correct choice to the question
   */
  void add(String questionText, String imageFile, String topic, ArrayList<String> choices, String correctChoice);
  
  /**
   * @returns all available topics
   */
  Set<String> getAllTopics();
   
  /**
   * @param topic is the specific topic
   * @returns the total number of questions in the database
   */
  int getNumQuestions(); 
}

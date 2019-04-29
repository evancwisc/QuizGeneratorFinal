package application;

import java.util.Iterator;

/**
 * A generic interface for an individual quiz
 */
public interface Quiz {

  /**
   * @returns an iterator of questions in the Quiz
   */
  Iterator<Question> getQuestionIterator();
  
  /**
   * Reports a correct answer to the current question
   */
  void correct();
  
  /**
   * @returns the number of questions answered correctly
   */
  int getNumCorrect();
  
  /**
   * @returns the score the user received on the quiz
   */
  float score();
  
}

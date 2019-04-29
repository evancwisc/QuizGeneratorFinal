package application;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A specific quiz instance generated from a question bank
 */
public class UserQuiz implements Quiz {

  private ArrayList<Question> list;
  private int numCorrect;
  
  /**
   * Constructs a new quiz object using the passed question list
   */
  public UserQuiz(ArrayList<Question> questionList) {
    this.list = questionList;
    this.numCorrect = 0;
  }
  
  public void correct() {
    this.numCorrect++;
  }
  
  public float score() {
    return this.numCorrect / this.list.size();
  }
  
  public int getNumCorrect() {
    return this.numCorrect;
  }
  
  public Iterator<Question> getQuestionIterator() {
    return this.list.iterator();
  }
}

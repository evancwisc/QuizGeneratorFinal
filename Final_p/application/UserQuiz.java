///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Title:            Team Project
//Files:            UserQuiz.java
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
    return this.numCorrect / (float)this.list.size();
  }
  
  public int getNumCorrect() {
    return this.numCorrect;
  }
  
  public Iterator<Question> getQuestionIterator() {
    return this.list.iterator();
  }
}

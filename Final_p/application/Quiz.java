///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Title:            Team Project
//Files:            Quiz.java
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

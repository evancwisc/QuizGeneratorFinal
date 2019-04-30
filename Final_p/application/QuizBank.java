package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.collections.ObservableList;

/**
 * Class that stores all questions
 * 
 * @authors Ryan Hemmila, Evan Corden, Robin Stauffer
 */
public class QuizBank implements QuizBankADT {
	private int numQuestions; // Total number of questions in the database
	// HashMap to store topics with array list of questions in topic
	private HashMap<String, ArrayList<Question>> bank;

	/**
	 * Constructor for empty QuizBank
	 */
	public QuizBank() {
		numQuestions = 0;
		bank = new HashMap<String, ArrayList<Question>>();

	}

	/**
	 * Constructs a new quiz object based on the given topics
	 * with a number of questions equal to questionCount
	 *
	 * @param topics is an array of all topics the questions should be on
	 * @param questionCount is the number of questions in the quiz
	 * @returns a quiz with the specified number of questions
	 */
	public UserQuiz generateQuiz(ObservableList<String> topics, Integer questionCount) {
		HashSet<Question> usedQuestions = new HashSet<Question>();
		ArrayList<Question> quizQuestions = new ArrayList<Question>();
		int topicCount = questionCount / topics.size();
		int extraQuestions = questionCount - (topicCount * topics.size());
		// Make initial pass to add an equal number of questions from each topic
		for(String topic : topics) {
			int used = 0;
			if(bank.containsKey(topic)) {
				ArrayList<Question> questions = bank.get(topic);
				Collections.shuffle(questions); // to assure they are random
				for(int i = 0; i < questions.size() && i < topicCount; i++) {
					quizQuestions.add(questions.get(i));
					usedQuestions.add(questions.get(i));
					used++;
				}
			}
			extraQuestions += topicCount - used;
		}
		// Make second pass to add extra questions if needed
		if(extraQuestions > 0) {
			for(String topic : topics) {
				if(bank.containsKey(topic)) {
					for(Question question : bank.get(topic)) {
						if(!usedQuestions.contains(question)) {
							quizQuestions.add(question);
							usedQuestions.add(question);
							if(--extraQuestions == 0) {
								return new UserQuiz(quizQuestions);
							}
						}
					}
				}
			}
		}
		return new UserQuiz(quizQuestions);
	}

	/**
	 * Saves the current questions in the QuizBank to the file specified
	 * in JSON format
	 *
	 * @param fileName is the file to save to
	 * @throws IOException if the file could not be written
	 */
	@SuppressWarnings("unchecked")
  public void save(String filename) throws IOException {
		JSONObject root = new JSONObject();
		JSONArray questionsArray = new JSONArray();
		for(String topic : getAllTopics()) {
			for(Question question : bank.get(topic)) {
				JSONObject questionObject = new JSONObject();
				JSONArray choicesArray = new JSONArray();
				questionObject.put("meta-data", "unused");
				questionObject.put("questionText", question.getQuestionText());
				questionObject.put("topic", question.getTopic());
				questionObject.put("image", question.getImageFile());
				for(String choice : question.getChoices()) {
					JSONObject choiceObject = new JSONObject();
					choiceObject.put("isCorrect", choice == question.getCorrectChoice() ? "T" : "F");
					choiceObject.put("choice", choice);
					choicesArray.add(choiceObject);
				}
				questionObject.put("choiceArray", choicesArray);
				questionsArray.add(questionObject);
			}
		}
		root.put("questionArray", questionsArray);
		FileWriter file = new FileWriter(filename);
		file.write(root.toJSONString());
		file.flush();
	}

	/**
	 * Loads the JSON file specified by the fileName into the QuizBank
	 * 
	 * @param fileName is the file to load
	 * @throws ParseException if the file had an incorrect format
	 * @throws IOException if the file could not be read from
	 * @throws FileNotFoundException if the file does not exist
	 */
	public void load(String filename) throws FileNotFoundException, IOException, ParseException {
		Object obj = (new JSONParser()).parse(new FileReader(filename));
		JSONObject jo = (JSONObject)obj;
		Iterator questionsIterator = ((JSONArray)jo.get("questionArray")).iterator();
		while(questionsIterator.hasNext()) {
			JSONObject question = (JSONObject)questionsIterator.next();
			String questionText = (String)question.get("questionText");
			String topic = (String)question.get("topic");
			String image = (String)question.get("image");
			Iterator choicesIterator = ((JSONArray)question.get("choiceArray")).iterator();
			ArrayList<String> choices = new ArrayList<String>();
			String correctChoice = null;
			while(choicesIterator.hasNext()) {
				JSONObject choiceObj = (JSONObject)choicesIterator.next();
				String isCorrect = (String)choiceObj.get("isCorrect");
				String choice = (String)choiceObj.get("choice");
				choices.add(choice);
				if(isCorrect.equals("T")) {
					correctChoice = choice;
				}
			}
			addQuestion(new Question(questionText, image, topic, choices, correctChoice));
		}
	}

	/**
	 * Generates an individual Question object and adds it to the QuizBank
	 *
	 * @param questionText is the question's text
	 * @param imageFile is the question's imageFile
	 * @param topic is the question's topic
	 * @param choices are the question choices
	 * @param correctChoice is the correct choice to the question
	 */
	public void add(String questionText, String imageFile, String topic, ArrayList<String> choices, String correctChoice) {
		addQuestion(new Question(questionText, imageFile, topic, choices, correctChoice));
	}

	/**
	 * @returns all available topics
	 */
	public Set<String> getAllTopics() {
		return bank.keySet();
	}

	/**
	 * @param topic is the specific topic
	 * @returns the total number of questions in the database
	 */
	public int getNumQuestions() {
		return numQuestions;
	}

	/**
	 * Adds a question to the quiz bank
	 *
	 * @param question is the question to add
	 */
	private void addQuestion(Question question) {
		// If new topic
		if(!bank.containsKey(question.getTopic())) {
			bank.put(question.getTopic(), new ArrayList<Question>());
		}

		bank.get(question.getTopic()).add(question);
		numQuestions++;
	}
}

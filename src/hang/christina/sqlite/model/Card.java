package hang.christina.sqlite.model;

public class Card {
	
	int id;
	int deck_id;
	String question;
	String answer;
	String created_at;
	
	// constructors
	public Card() {
	}
	
	public Card(int did, String question, String answer) {
		this.deck_id = did;
		this.question = question;
		this.answer = answer;
	}
	
	public Card(int id, int did, String question, String answer) {
		this.id = id;
		this.deck_id = did;
		this.question = question;
		this.answer = answer;
	}
	
	// setters
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDeckId(int did) {
		this.deck_id = did;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}
	
	// getters
	public long getId() {
		return this.id;
	}
	
	public long getDeckId() {
		return this.deck_id;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public String getAnswer() {
		return this.answer;
	}

}

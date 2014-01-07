package hang.christina.sqlite.model;

public class Deck {
	
	int id;
	String deck_name;
	double score;
	String created_at;
	
	// constructors
	public Deck() {
		
	}
	
	public Deck(int id, String deck_name) {
		this.id = id;
		this.deck_name = deck_name;
	}
	
	// setter
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDeckName(String deck_name) {
		this.deck_name = deck_name;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}
	
	// getter
	public long getId() {
		return this.id;
	}
	
	public String getDeckName() {
		return this.deck_name;
	}
	
	public double getScore() {
		return this.score;
	}

}

package hang.christina.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	public static final String DECK_NAME = "DECK_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createDeck(View v) {
		Intent i = new Intent(this, ManageDeck.class);
		// open window for user to input deck name
		// save new deck to db and send name to ManageDeck activity
		String deckName = "";
		i.putExtra(DECK_NAME, deckName);
		startActivity(i);
	}
	
	public void viewDecks(View v) {
		Intent i = new Intent(this, DeckList.class);
		startActivity(i);
	}

}

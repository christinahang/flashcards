package hang.christina.flashcards;

import hang.christina.sqlite.helper.DatabaseHelper;
import hang.christina.sqlite.model.Deck;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeckList extends Activity {
	
	private DatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deck_list);
		
		dbHelper = new DatabaseHelper(getApplicationContext());
		final ListView decksList = (ListView) findViewById(R.id.decks);
		
		List<Deck> decks = dbHelper.getAllDecks();
		DeckAdapter deckAdapter = new DeckAdapter(this, decks);
		decksList.setAdapter(deckAdapter);
		
		decksList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int pos, long id) {
				Intent i = new Intent(getApplicationContext(), ManageDeck.class);
				String deckName = (String) parent.getItemAtPosition(pos);
				System.out.println(deckName);
				i.putExtra(MainActivity.DECK_NAME, deckName);
				startActivity(i);
			}
		});
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deck_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DeckAdapter extends ArrayAdapter<Deck> {
		private final Context context;
		private final List<Deck> decks;
		
		public DeckAdapter(Context context, List<Deck> decks) {
			super(context, R.layout.deck_list_row, decks);
			this.context = context;
			this.decks = decks;
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.deck_list_row, parent, false);
			TextView deckNameTV = (TextView) rowView.findViewById(R.id.deck_name);
			TextView deckScoreTV = (TextView) rowView.findViewById(R.id.deck_score);
			Deck deck = decks.get(pos);
			deckNameTV.setText(deck.getDeckName());
			deckScoreTV.setText(Double.toString(deck.getScore()));
			return rowView;
		}
	}

}

package hang.christina.sqlite.helper;

import hang.christina.sqlite.model.Card;
import hang.christina.sqlite.model.Deck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "flashcards";

	// Table Names
	private static final String TABLE_DECK = "decks";
	private static final String TABLE_CARD = "cards";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// DECK Table - column names
	private static final String KEY_DECK_NAME = "deck_name";
	private static final String KEY_SCORE = "score";

	// CARD Table - column names
	private static final String KEY_DECK_ID = "deck_id";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_ANSWER = "answer";

	// Table Create Statements
	// Deck table
	private static final String CREATE_TABLE_DECK = "CREATE TABLE "
			+ TABLE_DECK + "(" + KEY_ID + "INTEGER PRIMARY KEY," + KEY_DECK_NAME
			+ " TEXT," + KEY_SCORE + " FLOAT," + KEY_CREATED_AT + " DATETIME"
			+ ")";

	// Card table
	private static final String CREATE_TABLE_CARD = "CREATE TABLE "
			+ TABLE_CARD + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DECK_ID
			+ " INTEGER," + KEY_QUESTION + " TEXT," + KEY_ANSWER + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_DECK);
		db.execSQL(CREATE_TABLE_CARD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);

		onCreate(db);
	}

	// CRUD Deck
	public long createDeck(Deck deck) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DECK_NAME, deck.getDeckName());
		values.put(KEY_SCORE, deck.getScore());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long deck_id = db.insert(TABLE_DECK, null, values);
		return deck_id;
	}

	public Deck getDeck(long deck_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_DECK + " WHERE "
				+ KEY_ID + " = " + deck_id;
		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		Deck deck = new Deck();
		deck.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		deck.setDeckName(c.getString(c.getColumnIndex(KEY_DECK_NAME)));
		deck.setScore(c.getDouble(c.getColumnIndex(KEY_SCORE)));
		deck.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

		return deck;
	}
	
	public List<Deck> getAllDecks() {
		List<Deck> decks = new ArrayList<Deck>();
		String selectQuery = "SELECT * FROM " + TABLE_DECK;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// loop through all rows and add to list
		if (c.moveToFirst()) {
			do {
				Deck deck = new Deck();
				deck.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				deck.setDeckName(c.getString(c.getColumnIndex(KEY_DECK_NAME)));
				deck.setScore(c.getDouble(c.getColumnIndex(KEY_SCORE)));
				deck.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				decks.add(deck);
			} while (c.moveToNext());
		}
		return decks;
	}
	
	public int updateDeck(Deck deck) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_DECK_NAME, deck.getDeckName());
		values.put(KEY_SCORE, deck.getScore());
		
		return db.update(TABLE_DECK, values, KEY_ID + " = ?",
				new String[] { String.valueOf(deck.getId()) });
	}
	
	public void deleteDeck(Deck deck) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		// delete all Cards within this Deck
		List<Card> allCardsInDeck = getAllCardsByDeck(deck.getDeckName());
		for (Card card : allCardsInDeck) {
			deleteCard(card);
		}
		
		db.delete(TABLE_DECK, KEY_ID + " = ?",
				new String[] { String.valueOf(deck.getId()) });
	}
	
	// CRUD Card
	public long createCard(Card card) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_DECK_ID, card.getDeckId());
		values.put(KEY_QUESTION, card.getQuestion());
		values.put(KEY_ANSWER, card.getAnswer());
		values.put(KEY_CREATED_AT, getDateTime());
		
		long card_id = db.insert(TABLE_CARD, null, values);
		return card_id;
	}
	
	public Card getCard(long card_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_CARD + " WHERE "
				+ KEY_ID + " = " + card_id;
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null) {
			c.moveToFirst();
		}
		
		Card card = new Card();
		card.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		card.setDeckId(c.getInt(c.getColumnIndex(KEY_DECK_ID)));
		card.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));
		card.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
		card.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		
		return card;
	}
	
	public List<Card> getAllCardsByDeck(String deck_name) {
		List<Card> cards = new ArrayList<Card>();
		
		String selectQuery = "SELECT * FROM " + TABLE_CARD + " c,"
				+ TABLE_DECK + " d WHERE d." + KEY_DECK_NAME + " = '"
				+ deck_name + "'" + " AND d." + KEY_ID + " = " + "c."
				+ KEY_DECK_ID;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c.moveToFirst()) {
			int deck_id = c.getInt(c.getColumnIndex(KEY_DECK_ID));
			do {
				Card card = new Card();
				card.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				card.setDeckId(deck_id);
				card.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));
				card.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
				card.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				cards.add(card);
			} while (c.moveToNext());
		}
		return cards;
	}
	
	public int updateCard(Card card) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION, card.getQuestion());
		values.put(KEY_ANSWER, card.getAnswer());
		
		return db.update(TABLE_CARD, values, KEY_ID + " = ?", new String[] { String.valueOf(card.getId()) });
	}
	
	public void deleteCard(Card card) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CARD, KEY_ID + " = ?",
				new String[] { String.valueOf(card.getId()) });
	}

	// get datetime
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

}

package tp1.p2.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import tp1.p2.control.Level;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.control.exceptions.RecordException;
import tp1.p2.view.Messages;

public class Record {
	
	private Level level;
	private int score;
	
	private Record(int score, Level level) {
		this.score = score;
		this.level = level;
	}
	
	public static Record readLine(String[] words) throws GameException {
		
		if(words.length != 2) throw new RecordException(Messages.RECORD_READ_ERROR);
		
		Level thisLineLevel = Level.valueOfIgnoreCase(words[0]);
		if(thisLineLevel == null) throw new RecordException(Messages.RECORD_READ_ERROR);
		
		int scoreForTheLevel;
		try {
			scoreForTheLevel = Integer.parseInt(words[1]);
		}
		
		catch (NumberFormatException nfe) {
			throw new RecordException(Messages.RECORD_READ_ERROR, nfe);
		}
		return new Record(scoreForTheLevel, thisLineLevel);
	}
	
	public static Record loadFile(Level level_playing) throws GameException, IOException{
		try (BufferedReader file = new BufferedReader (new FileReader(Messages.RECORD_FILENAME))) {
	
			Record current_record = null;
			boolean already_read = false;
			String line = file.readLine();
			while(line != null && !already_read) {
				String[] words = line.toLowerCase().trim().split(":");
				current_record = readLine(words);
				if(current_record.level.equals(level_playing)) already_read = true;
				line = file.readLine();
			}
			if(!already_read) current_record = new Record(0, level_playing);
			return current_record;	
		}
		
		catch(NumberFormatException nfe) {
			throw new RecordException(Messages.RECORD_READ_ERROR, nfe);
		}
		catch(FileNotFoundException fnfe) {
			throw new RecordException(Messages.RECORD_READ_ERROR, fnfe);
		}
		
	}
	
	public int getScoreForLevel() {
		return this.score;
	}
	
	public Level getLevel() {
		return this.level;
	}

	public boolean update(int possible_new_score) {
		boolean change = false;
		if(possible_new_score > this.score) {
			change = true;
			this.score = possible_new_score;
		}
		return change;
	}
	
	public void save() throws IOException, RecordException {
		try (BufferedWriter file = new BufferedWriter(new FileWriter(Messages.RECORD_FILENAME))){
			StringBuilder buffer = new StringBuilder();
			buffer.append(this.level.name()).append(":").append(this.score).append(Messages.LINE_SEPARATOR);		
			file.write(buffer.toString());
		}
		catch(IOException ioe) {
			throw new RecordException(Messages.RECORD_WRITE_ERROR, ioe);
		}
	}
	

	
	
}

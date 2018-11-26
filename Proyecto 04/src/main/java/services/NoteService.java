
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.NoteRepository;
import domain.Note;

@Service
@Transactional
public class NoteService {

	@Autowired
	private NoteRepository	noteRepository;


	public Note create(String mandatoryComment) {

		Note note = new Note();
		List<String> optionalComments = new ArrayList<String>();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		note.setMoment(thisMoment);
		note.setMandatoryComment(mandatoryComment);
		note.setOptionalComments(optionalComments);

		return note;
	}

	public Note save(Note note) {
		return this.noteRepository.save(note);
	}

	public Note update(Note note) {
		return this.save(note);
	}

	public void delete(Note note) {
		this.noteRepository.delete(note);
	}

	public List<Note> findAll() {
		return this.noteRepository.findAll();
	}
}

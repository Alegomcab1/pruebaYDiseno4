
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.NoteRepository;

@Service
@Transactional
public class NoteService {

	@Autowired
	private NoteRepository	noteRepository;

}

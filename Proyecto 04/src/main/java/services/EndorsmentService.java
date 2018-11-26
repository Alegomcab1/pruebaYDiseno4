
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EndorsmentRepository;
import domain.Endorser;
import domain.Endorsment;

@Service
@Transactional
public class EndorsmentService {

	// Managed repository ------------------------------------------

	@Autowired
	private EndorsmentRepository	endorsmentRepository;


	// Supporting Services ------------------------------------------

	// Simple CRUD methods ------------------------------------------

	public Endorsment createEndorsment() {
		Endorsment result = new Endorsment();
		Date thisMoment = new Date();
		List<String> comments = new ArrayList<String>();
		Endorser endorser = new Endorser();

		result.setComments(comments);
		result.setMoment(thisMoment);
		result.setWrittenBy(endorser);
		result.setWrittenTo(endorser);

		return result;

	}

	public Collection<Endorsment> findAll() {
		return this.endorsmentRepository.findAll();
	}

	public Endorsment findOne(int id) {
		return this.endorsmentRepository.findOne(id);
	}

	public Endorsment save(Endorsment endorsment) {
		return this.endorsmentRepository.save(endorsment);
	}

	public void delete(Endorsment endorsment) {
		this.endorsmentRepository.delete(endorsment);
	}

}

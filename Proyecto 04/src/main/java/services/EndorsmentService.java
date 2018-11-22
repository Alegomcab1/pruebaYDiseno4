
package services;

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

	public Endorsment createEndorsment(final Endorser writtenBy, final Endorser writtenTo, final List<String> comments) {
		final Endorsment result = new Endorsment();
		//final List<String> comments = new ArrayList<String>();
		final Date thisMoment = new Date();

		result.setComments(comments);
		result.setMoment(thisMoment);
		result.setWrittenBy(writtenBy);
		result.setWrittenTo(writtenTo);

		return result;

	}
	public Endorsment saveEndorsment(final Endorsment endorsment) {
		return this.endorsmentRepository.save(endorsment);
	}
}

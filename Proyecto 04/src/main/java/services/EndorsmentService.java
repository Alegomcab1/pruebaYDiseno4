
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
		Endorser writtenTo = EndorserService.createEndorser();
		Endorser writtenBy = EndorserService.createEndorser();

		result.setComments(comments);
		result.setMoment(thisMoment);
		result.setWrittenBy(writtenBy);
		result.setWrittenTo(writtenTo);

		return result;

	}

	public Collection<Endorsment> findAll() {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		return this.endorsmentRepository.findAll();
	}

	public Endorsment findOne(int id) {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		//TODO id en Endorsment?
		return this.endorsmentRepository.findOne(id);
	}

	public Endorsment save(Endorsment endorsment) {
		return this.endorsmentRepository.save(endorsment);
	}

	public void delete(Endorsment endorsment) {
		//TODO Bastante seguro de que esto solo lo deberia de poder hacer un ADMIN, además mirar si hay restricciones a la hora de eliminarlo
		this.endorsmentRepository.delete(endorsment);
	}

}

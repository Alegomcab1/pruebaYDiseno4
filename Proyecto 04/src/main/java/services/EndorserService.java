
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EndorserRepository;
import domain.Actor;
import domain.Endorser;
import domain.Endorsment;
import domain.Score;

@Service
@Transactional
public class EndorserService {

	// Managed repository ------------------------------------------

	@Autowired
	private EndorserRepository	endorserRepository;

	// Supporting Services ------------------------------------------

	@Autowired
	private EndorsmentService	endorsmentService;

	//TODO Discutir sobre los Autowired en cuanto a padres e hijos
	@Autowired
	private ActorService		actorService;
	@Autowired
	private CustomerService		customerService;


	// Simple CRUD methods ------------------------------------------

	public Endorser createEndorser(final Actor a, final Score score) {
		final Endorser result = new Endorser();
		final List<Endorsment> endorsments = new ArrayList<Endorsment>();

		//TODO Eliminar los parametros de entrada?

		result.setAddress(a.getAddress());
		result.setBoxes(a.getBoxes());
		result.setEmail(a.getEmail());
		result.setEndorsments(endorsments);
		result.setMiddleName(a.getMiddleName());
		result.setName(a.getName());
		result.setPhoneNumber(a.getPhoneNumber());
		result.setPhoto(a.getPhoto());
		//TODO Si inicializo a AVERAGE el Score podria eliminar el parametro de entrada y usar este create en HandyWorker
		result.setScore(score);
		result.setSocialProfiles(a.getSocialProfiles());
		result.setSurname(a.getSurname());

		return result;

	}

	public Endorser saveEndorser(final Endorser endorser) {
		return this.endorserRepository.save(endorser);
	}
}

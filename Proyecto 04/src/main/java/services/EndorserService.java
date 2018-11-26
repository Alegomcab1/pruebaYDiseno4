package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EndorserRepository;
import domain.Actor;
import domain.Endorser;
import domain.Endorsment;

@Service
@Transactional
public class EndorserService {

    // Managed repository
    // ---------------------------------------------------------------------------------------------------

    @Autowired
    private EndorserRepository endorserRepository;

    // Supporting Services
    // --------------------------------------------------------------------------------------------------

    @Autowired
    private EndorsmentService endorsmentService;

    // TODO Discutir sobre los Autowired en cuanto a padres e hijos
    @Autowired
    private HandyWorkerService handyWorkerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ActorService actorService;

    // Simple CRUD methods
    // --------------------------------------------------------------------------------------------------

    public Endorser createEndorser(Actor a) {
	Endorser result = new Endorser();
	List<Endorsment> endorsments = new ArrayList<Endorsment>();
	result = (Endorser) this.actorService.createActor(a.getName(), a
		.getMiddleName(), a.getSurname(), a.getPhoto(), a.getEmail(), a
		.getPhoneNumber(), a.getAddress(), a.getUserAccount()
		.getUsername(), a.getUserAccount().getPassword());
	result.setEndorsments(endorsments);
	result.setScore(0);

	return result;

    }

    public Collection<Endorser> findAll() {
	// TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
	return this.endorserRepository.findAll();
    }

    public Endorser findOne(int id) {
	// TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
	return this.endorserRepository.findOne(id);
    }

    public Endorser save(Endorser endorser) {
	return this.endorserRepository.save(endorser);
    }

    public void delete(Endorser endorser) {
	// TODO Bastante seguro de que esto solo lo deberia de poder hacer un
	// ADMIN, además mirar si hay restricciones a la hora de eliminarlo
	this.endorserRepository.delete(endorser);
    }
}

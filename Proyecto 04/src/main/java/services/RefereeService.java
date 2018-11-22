
package services;

import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.RefereeRepository;
import domain.Complaint;
import domain.Referee;

@Service
@Transactional
public class RefereeService {

	@Autowired
	private RefereeRepository	refereeRepository;

	//Supporting service

	@Autowired
	private ReportService		reportService;


	//Simple CRUD methods
	public Collection<Referee> findAll() {
		final Collection<Referee> res = this.refereeRepository.findAll();
		return res;
	}


	//Methods
	Collection<Complaint>	unassignedComplaints	= this.refereeRepository.findUnassignedComplaints();


	public void assignComplaint(Collection<Complaint> unassignedComplaints) {
		unassignedComplaints = this.unassignedComplaints;
		Random rnd = new Random();
		int i = rnd.nextInt(unassignedComplaints.size());
		for (Complaint c : unassignedComplaints)
			c.setReferee((Referee) this.findAll().toArray()[i]);
	}

}

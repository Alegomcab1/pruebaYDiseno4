
package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Complaint;
import domain.Note;
import domain.Referee;
import domain.Report;

@Service
@Transactional
public class RefereeService {

	// Managed repository ------------------------------------------

	@Autowired
	private RefereeRepository		refereeRepository;

	// Supporting Services ------------------------------------------

	@Autowired
	private ComplaintService		complaintService;
	@Autowired
	private NoteService				noteService;
	@Autowired
	private ReportService			reportService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ConfigurationService	configurationService;


	//Aux
	private Referee securityAndReferee() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();
		Referee loggedReferee = this.refereeRepository.getRefereeByUsername(username);

		Assert.isTrue(userAccount.getAuthorities().contains("REFEREE"));

		return loggedReferee;
	}

	// CRUD Methods -------------------------------------------------

	public Referee create(String name, String middleName, String surname, String photo, String email, String phoneNumber, String address, String userName, String password) {

		Referee referee = new Referee();
		referee = (Referee) this.actorService.createActor(name, middleName, surname, photo, email, phoneNumber, address, userName, password);
		List<Authority> authorities = new ArrayList<Authority>();
		referee.getUserAccount().setAuthorities(authorities);

		UserAccount userAccountReferee = new UserAccount();
		Authority authority = new Authority();
		authority.setAuthority(Authority.REFEREE);
		authorities.add(authority);

		return referee;
	}
	public Referee save(Referee referee) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("REFEREE"));

		// Comprobacion en todos los SAVE de los ACTORES
		Assert.isTrue(referee.getId() == 0 || userAccount.equals(referee.getUserAccount()));
		return this.refereeRepository.save(referee);
	}

	public Referee update(Referee referee) {
		return this.save(referee);
	}

	public void delete(Referee referee) {
		this.refereeRepository.delete(referee);
	}

	public List<Referee> findAll() {
		return this.refereeRepository.findAll();
	}

	// Methods ------------------------------------------------------

	public List<Complaint> unassignedComplaints() {
		Referee loggedReferee = this.securityAndReferee();
		return (List<Complaint>) this.refereeRepository.complaintsUnassigned();
	}

	public void assingComplaint(Complaint complaint) {
		Referee loggedReferee = this.securityAndReferee();
		List<Referee> lr = this.findAll();
		Random rnd = new Random();
		int i = rnd.nextInt(lr.size());
		Referee r = lr.get(i);
		List<Complaint> unassignedComplaints = (List<Complaint>) this.refereeRepository.complaintsUnassigned();
		Complaint comp = new Complaint();
		for (Complaint c : unassignedComplaints)
			if (c == complaint)
				c = comp;
		Assert.notNull(comp);
		complaint.setReferee(r);
		this.complaintService.save(complaint);
		this.configurationService.isActorSuspicious(loggedReferee);
	}

	public List<Complaint> selfAssignedComplaint(Referee r) {
		Referee loggedReferee = this.securityAndReferee();
		List<Complaint> res = new ArrayList<>();
		List<Complaint> complaints = this.complaintService.findAll();
		Complaint comp = new Complaint();
		for (Complaint c : complaints)
			if (c.getReferee() == r)
				c = comp;
		Assert.notNull(comp);
		res.add(comp);
		this.refereeRepository.save(r);
		this.configurationService.isActorSuspicious(loggedReferee);
		return res;
	}

	public Note writeNoteReport(Report report, String mandatoryComment, List<String> optionalComments) {
		Referee loggedReferee = this.securityAndReferee();
		List<Report> lr = loggedReferee.getReports();
		Report rep = new Report();
		for (Report r : lr)
			if (r == report)
				r = rep;
		Assert.notNull(rep);
		Note note = this.noteService.create(mandatoryComment, optionalComments);
		report.getNotes().add(note);
		this.reportService.save(report);
		this.configurationService.isActorSuspicious(loggedReferee);
		return note;
	}

	public Note writeComment(String comment, Note note) {
		Referee loggedReferee = this.securityAndReferee();
		List<Note> notes = (List<Note>) this.refereeRepository.notesReferee(loggedReferee.getId());
		Note no = new Note();
		for (Note n : notes)
			if (n == note)
				n = no;
		Assert.notNull(no);
		note.getOptionalComments().add(comment);
		this.noteService.save(note);
		this.configurationService.isActorSuspicious(loggedReferee);
		return note;
	}

	public Report writeReportRegardingComplaint(Complaint complaint, String description, List<String> attachments) {
		Referee loggedReferee = this.securityAndReferee();
		List<Complaint> lc = this.complaintService.findAll();
		Report res = new Report();
		Complaint com = new Complaint();
		for (Complaint c : lc)
			if (c == complaint) {
				com = c;
				res = this.reportService.create(description, attachments);
				loggedReferee.getReports().add(res);
				com.getReports().add(res);
			}
		this.complaintService.save(com);
		this.refereeRepository.save(loggedReferee);
		this.reportService.save(res);
		this.configurationService.isActorSuspicious(loggedReferee);
		return res;
	}

	public Report modifiedReport(Report report) {
		Referee loggedReferee = this.securityAndReferee();
		Assert.isTrue(!report.getFinalMode());
		Report rp = this.reportService.save(report);
		this.configurationService.isActorSuspicious(loggedReferee);
		return rp;
	}

	public void eliminateReport(Report report) {
		Referee loggedReferee = this.securityAndReferee();
		Assert.isTrue(!report.getFinalMode());
		this.reportService.delete(report);
		this.configurationService.isActorSuspicious(loggedReferee);
	}

}

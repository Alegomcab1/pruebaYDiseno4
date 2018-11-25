
package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RefereeRepository;
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
	private RefereeRepository	refereeRepository;

	// Supporting Services ------------------------------------------

	private ComplaintService	complaintService;
	private NoteService			noteService;
	private ReportService		reportService;


	//Aux
	private Referee securityAndReferee() {
		final UserAccount userAccount = LoginService.getPrincipal();
		final String username = userAccount.getUsername();
		final Referee loggedReferee = this.refereeRepository.getRefereeByUsername(username);

		Assert.isTrue(userAccount.getAuthorities().contains("REFEREE"));

		return loggedReferee;
	}

	// CRUD Methods -------------------------------------------------

	public List<Referee> findAll() {
		return this.refereeRepository.findAll();
	}

	// Methods ------------------------------------------------------

	public List<Complaint> unassignedComplaints() {
		final Referee loggedReferee = this.securityAndReferee();
		return (List<Complaint>) this.refereeRepository.complaintsUnassigned();
	}

	public void assingComplaint(Complaint complaint) {
		final Referee loggedReferee = this.securityAndReferee();
		List<Referee> lr = this.findAll();
		Random rnd = new Random();
		int i = rnd.nextInt(lr.size());
		Referee r = lr.get(i);
		List<Complaint> unassignedComplaints = (List<Complaint>) this.refereeRepository.complaintsUnassigned();
		Complaint comp = null;
		for (Complaint c : unassignedComplaints)
			if (c == complaint)
				c = comp;
		Assert.notNull(comp);
		complaint.setReferee(r);
		this.complaintService.save(complaint);
	}

	public List<Complaint> selfAssignedComplaint(Referee r) {
		final Referee loggedReferee = this.securityAndReferee();
		List<Complaint> res = new ArrayList<>();
		List<Complaint> complaints = this.complaintService.findAll();
		Complaint comp = null;
		for (Complaint c : complaints)
			if (c.getReferee() == r)
				c = comp;
		Assert.notNull(comp);
		res.add(c);
		this.refereeRepository.save(r);
		return res;
	}

	public Note writeNoteReport(Report report, Note note) {
		final Referee loggedReferee = this.securityAndReferee();
		List<Report> lr = loggedReferee.getReports();
		Report rep = null;
		for (Report r : lr)
			if (r == report)
				r = rep;
		Assert.notNull(rep);
		this.noteService.create(note);
		report.getNotes().add(note);
		this.reportService.save(report);
		return note;
	}

	public Note writeComment(String comment, Note note) {
		final Referee loggedReferee = this.securityAndReferee();
		List<Note> notes = (List<Note>) this.refereeRepository.notesReferee(loggedReferee.getId());
		Note no = null;
		for (Note n : notes)
			if (n == note)
				n = no;
		Assert.notNull(no);
		note.getOptionalComments().add(comment);
		this.noteService.save(note);
		return note;
	}

}

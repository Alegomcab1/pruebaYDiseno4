
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Complaint;
import domain.Customer;
import domain.Endorser;
import domain.Endorsment;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Report;

@Service
@Transactional
public class CustomerService {

	//Managed repository
	@Autowired
	private CustomerRepository	customerRepository;

	//Supporting services
	@Autowired
	private FixUpTaskService	fixUpTaskService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private ComplaintService	complaintService;
	@Autowired
	private ApplicationService	applicationService;
	@Autowired
	private NoteService			noteService;
	@Autowired
	private ReportService		reportService;
	@Autowired
	private RefereeService		refereeService;
	@Autowired
	private EndorsmentService	endorsmentService;


	//Simple CRUD methods: TODO Verificaciones
	public Customer create(Endorser endorser) {

		final Customer customer = (Customer) this.actorService.createActor(endorser.getName(), endorser.getMiddleName(), endorser.getSurname(), endorser.getPhoto(), endorser.getEmail(), endorser.getPhoneNumber(), endorser.getAddress(), endorser
			.getUserAccount().getUsername(), endorser.getUserAccount().getPassword());

		customer.setScore(endorser.getScore());

		return this.customerRepository.save(customer);
	}

	public Collection<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	public Customer findOne(final int customerId) {
		return this.customerRepository.findOne(customerId);
	}

	public Customer save(final Customer customer) {
		return this.customerRepository.save(customer);
	}

	public void delete(final Customer customer) {
		this.customerRepository.delete(customer);
	}

	//Auxiliar methods
	private Customer securityAndCustomer() {
		final UserAccount userAccount = LoginService.getPrincipal();
		final String username = userAccount.getUsername();
		final Customer loggedCustomer = this.customerRepository.getCustomerByUsername(username);

		Assert.isTrue(userAccount.getAuthorities().contains("CUSTOMER"));

		return loggedCustomer;
	}

	//Métodos solicitados
	public Collection<FixUpTask> showFixUpTasks(int customerId) {
		return this.customerRepository.findFixUpTasksById(customerId);
	}

	//FixUpTasks
	public Collection<FixUpTask> showFixUpTasks() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findFixUpTasksById(loggedCustomer.getId());
	}

	public FixUpTask getFixUpTask(final int fixUpTaskId) {
		final Customer loggedCustomer = this.securityAndCustomer();
		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (final FixUpTask f : fixUpTasks)
			if (f.getId() == fixUpTaskId) {
				fixUpTask = f;
				break;
			}

		Assert.notNull(fixUpTask);

		return fixUpTask;
	}

	public FixUpTask createFixUpTask(final FixUpTask fixUpTask) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.save(fixUpTask.getId());

		final List<FixUpTask> listf = new ArrayList<>();
		listf.addAll(loggedCustomer.getFixUpTasks());
		listf.add(fixUpTask);
		loggedCustomer.setFixUpTasks(listf);

		final Customer customerSaved = this.save(loggedCustomer);

		return fixUpTaskSaved;

	}

	public FixUpTask updateFixUpTask(final FixUpTask fixUpTask) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFound = null;
		for (final FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFound = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFound.equals(null));

		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.save(fixUpTask);

		/*
		 * final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		 * listf.remove(fixUpTaskFound);
		 * listf.add(fixUpTaskSaved);
		 * loggedCustomer.setFixUpTasks(listf);
		 * 
		 * final Customer customerSaved = this.save(loggedCustomer);
		 */

		return fixUpTaskSaved;

	}

	public void deleteFixUpTask(FixUpTask fixUpTask) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFounded = null;
		for (final FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFounded = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFounded.equals(null));

		this.fixUpTaskService.delete(fixUpTaskFounded);

		/*
		 * final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		 * listf.remove(fixUpTaskFounded);
		 * loggedCustomer.setFixUpTasks(listf);
		 * 
		 * this.save(loggedCustomer);
		 */
	}

	//COMPLAINTS
	public Collection<Complaint> showComplaints() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findComplaintsById(loggedCustomer.getId());
	}

	public Complaint getComplaint(int complaintId) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Collection<Complaint> complaints = this.customerRepository.findComplaintsById(loggedCustomer.getId());

		Complaint complaintFound = null;
		for (final Complaint c : complaints)
			if (complaintId == c.getId()) {
				complaintFound = c;
				break;
			}

		Assert.notNull(complaintFound);

		return complaintFound;

	}

	public Complaint createComplaint(final FixUpTask fixUpTask, final Complaint complaint) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Complaint complaintSaved = this.complaintService.create(complaint);

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFound = null;
		for (final FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFound = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFound.equals(null));

		final List<Complaint> complaints = (List<Complaint>) fixUpTaskFound.getComplaints();
		complaints.add(complaint);
		fixUpTaskFound.setComplaints(complaints);

		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.save(fixUpTaskFound);

		final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		listf.add(fixUpTaskFound);
		loggedCustomer.setFixUpTasks(listf);

		final Customer customerSaved = this.save(loggedCustomer);

		return complaintSaved;
	}

	//APPLICATIONS
	public Collection<Application> showApplications() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findApplicationsById(loggedCustomer.getId());
	}

	//TODO Validación credit cards
	public Application editApplication(final Application application) {
		return null;
	}

	//NOTES
	public Note createNote(Report report, Note note) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Report> reports = this.customerRepository.findReportsById(loggedCustomer.getId());

		Report reportFound = null;
		for (final Report r : reports)
			if (report.getId() == r.getId()) {
				reportFound = r;
				break;
			}

		Assert.notNull(reportFound);

		List<Note> notes = report.getNotes();
		notes.add(note);

		report.setNotes(notes);

		Note noteSaved = this.noteService.save(note);
		this.reportService.save(report);

		return noteSaved;
	}

	public Note addComent(Note note, String comment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Note> notes = this.customerRepository.findNotesById(loggedCustomer.getId());

		Note noteFound = null;
		for (final Note n : notes)
			if (note.getId() == n.getId()) {
				noteFound = n;
				break;
			}

		Assert.notNull(noteFound);

		List<String> comments = noteFound.getOptionalComments();
		comments.add(comment);

		Note noteSaved = this.noteService.save(noteFound);

		return noteSaved;
	}

	//ENDORSMENTS
	public Collection<Endorsment> showEndorsments() {
		Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.AllEndorsmentsById(loggedCustomer.getId());
	}

	public Endorsment getEndorsment(int endorsmentId) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Endorsment> endorsments = this.customerRepository.AllEndorsmentsById(loggedCustomer.getId());

		Endorsment endorsmentFound = null;
		for (final Endorsment e : endorsments)
			if (e.getId() == endorsmentId) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		return endorsmentFound;
	}

	public Endorsment createEndorsment(Endorsment endorsment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Assert.isTrue(endorsment.getWrittenTo().getClass().equals(HandyWorker.class));

		HandyWorker handyWorker = (HandyWorker) endorsment.getWrittenTo();

		Collection<HandyWorker> handyWorkers = this.customerRepository.handyWorkersById(loggedCustomer.getId());

		HandyWorker handyWorkerFound = null;
		for (final HandyWorker h : handyWorkers)
			if (h.getId() == handyWorker.getId()) {
				handyWorkerFound = h;
				break;
			}

		Assert.notNull(handyWorkerFound);

		return this.endorsmentService.saveEndorsment(endorsment);
	}

	public Endorsment updateEndorsment(Endorsment endorsment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Endorsment> endorsments = this.customerRepository.endorsmentsOfById(loggedCustomer.getId());

		Endorsment endorsmentFound = null;
		for (final Endorsment e : endorsments)
			if (e.getId() == endorsment.getId()) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		return this.endorsmentService.saveEndorsment(endorsment);
	}

	public void deleteEndorsment(Endorsment endorsment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Endorsment> endorsments = this.customerRepository.endorsmentsOfById(loggedCustomer.getId());

		Endorsment endorsmentFound = null;
		for (final Endorsment e : endorsments)
			if (e.getId() == endorsment.getId()) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		this.endorsmentService.delete(endorsment);
	}
}

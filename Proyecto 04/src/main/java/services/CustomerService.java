
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Category;
import domain.Complaint;
import domain.CreditCard;
import domain.Customer;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Report;
import domain.Status;
import domain.Warranty;

@Service
@Transactional
public class CustomerService {

	// Managed repository
	@Autowired
	private CustomerRepository		customerRepository;

	// Supporting services
	@Autowired
	private FixUpTaskService		fixUpTaskService;
	@Autowired
	private ComplaintService		complaintService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private NoteService				noteService;
	@Autowired
	private ReportService			reportService;
	@Autowired
	private EndorsmentService		endorsmentService;
	@Autowired
	private EndorserService			endorserService;
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private HandyWorkerService		handyWorkerService;


	// Simple CRUD methods
	public Customer create(String name, String middleName, String surname, String photo, String email, String phoneNumber, String address, String userName, String password, Integer score) {

		Customer result = (Customer) this.endorserService.createEndorser(name, middleName, surname, photo, email, phoneNumber, address, userName, password, score);

		return result;
	}

	public Collection<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	public Customer findOne(int customerId) {
		return this.customerRepository.findOne(customerId);
	}

	public Customer save(Customer customer) {
		return this.customerRepository.save(customer);
	}

	public void delete(Customer customer) {
		this.customerRepository.delete(customer);
	}

	//Auxiliar methods
	public Customer securityAndCustomer() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();
		Customer loggedCustomer = this.customerRepository.getCustomerByUsername(username);

		List<Authority> authorities = (List<Authority>) loggedCustomer.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("CUSTOMER"));

		return loggedCustomer;
	}

	public static boolean validateCreditCardNumber(String str) {

		int[] ints = new int[str.length()];
		for (int i = 0; i < str.length(); i++)
			ints[i] = Integer.parseInt(str.substring(i, i + 1));
		for (int i = ints.length - 2; i >= 0; i = i - 2) {
			int j = ints[i];
			j = j * 2;
			if (j > 9)
				j = j % 10 + 1;
			ints[i] = j;
		}
		int sum = 0;
		for (int i = 0; i < ints.length; i++)
			sum += ints[i];
		if (sum % 10 == 0)
			return true;
		else
			return false;
	}

	//Métodos solicitados
	public Collection<FixUpTask> showFixUpTasks(int customerId) {
		return this.customerRepository.findFixUpTasksById(customerId);
	}

	//FixUpTasks
	public Collection<FixUpTask> showFixUpTasks() {
		Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findFixUpTasksById(loggedCustomer.getId());
	}

	public FixUpTask getFixUpTask(int fixUpTaskId) {
		Customer loggedCustomer = this.securityAndCustomer();
		Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (FixUpTask f : fixUpTasks)
			if (f.getId() == fixUpTaskId) {
				fixUpTask = f;
				break;
			}

		Assert.notNull(fixUpTask);

		return fixUpTask;
	}

	public FixUpTask createFixUpTask(String description, String address, Double maxPrice, Date realizationTime, Collection<Warranty> warranties, Collection<Phase> phases, Collection<Category> categories, Collection<Complaint> complaints,
		Collection<Application> applications) {
		Customer loggedCustomer = this.securityAndCustomer();

		FixUpTask fixUpTask = this.fixUpTaskService.create(description, address, maxPrice, realizationTime, warranties, phases, categories, complaints, applications);

		FixUpTask fixUpTaskSaved = this.fixUpTaskService.save(fixUpTask);

		List<FixUpTask> listf = new ArrayList<>();
		listf.addAll(loggedCustomer.getFixUpTasks());
		listf.add(fixUpTask);
		loggedCustomer.setFixUpTasks(listf);

		this.save(loggedCustomer);

		this.configurationService.isActorSuspicious(loggedCustomer);

		return fixUpTaskSaved;

	}

	public FixUpTask updateFixUpTask(FixUpTask fixUpTask) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFound = null;
		for (FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFound = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFound.equals(null));

		FixUpTask fixUpTaskSaved = this.fixUpTaskService.save(fixUpTask);

		this.configurationService.isActorSuspicious(loggedCustomer);

		return fixUpTaskSaved;

	}

	public void deleteFixUpTask(FixUpTask fixUpTask) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFounded = null;
		for (FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFounded = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFounded.equals(null));

		List<Application> la = (List<Application>) fixUpTaskFounded.getApplications();
		List<HandyWorker> lh = (List<HandyWorker>) this.handyWorkerService.findAll();
		for (Application a : la) {
			for (HandyWorker h : lh)
				if (h.getApplications().contains(a)) {
					h.getApplications().remove(a);
					this.handyWorkerService.save(h);
				}

			this.applicationService.delete(a);
		}
		fixUpTaskFounded.getApplications().removeAll(la);

		List<Finder> lfind = (List<Finder>) this.finderService.findAll();
		for (Finder find : lfind)
			if (find.getFixUpTasks().contains(fixUpTaskFounded))
				find.getFixUpTasks().remove(fixUpTaskFounded);
		this.fixUpTaskService.save(fixUpTaskFounded);

		List<FixUpTask> fixUpTasks2 = loggedCustomer.getFixUpTasks();
		fixUpTasks2.remove(fixUpTaskFounded);
		loggedCustomer.setFixUpTasks(fixUpTasks2);
		this.customerRepository.save(loggedCustomer);

		this.fixUpTaskService.delete(fixUpTaskFounded);
	}
	//COMPLAINTS
	public Collection<Complaint> showComplaints() {
		Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findComplaintsById(loggedCustomer.getId());
	}

	public Complaint getComplaint(int complaintId) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Complaint> complaints = this.customerRepository.findComplaintsById(loggedCustomer.getId());

		Complaint complaintFound = null;
		for (Complaint c : complaints)
			if (complaintId == c.getId()) {
				complaintFound = c;
				break;
			}

		Assert.notNull(complaintFound);

		return complaintFound;
	}

	public Complaint createComplaint(FixUpTask fixUpTask, String description, List<String> attachments) {
		Customer loggedCustomer = this.securityAndCustomer();

		Complaint complaint = this.complaintService.create(description, attachments);

		Complaint complaintSaved = this.complaintService.save(complaint);

		Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTaskFound = null;
		for (FixUpTask f : fixUpTasks)
			if (fixUpTask.getId() == f.getId()) {
				fixUpTaskFound = f;
				break;
			}

		Assert.isTrue(!fixUpTaskFound.equals(null));

		List<Complaint> complaints = (List<Complaint>) fixUpTaskFound.getComplaints();
		complaints.add(complaint);
		fixUpTaskFound.setComplaints(complaints);

		this.fixUpTaskService.save(fixUpTaskFound);

		this.configurationService.isActorSuspicious(loggedCustomer);

		return complaintSaved;
	}

	//APPLICATIONS
	public Collection<Application> showApplications() {
		Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findApplicationsById(loggedCustomer.getId());
	}

	public Application editApplication(Application application, CreditCard creditCard) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Application> applications = this.customerRepository.findApplicationsById(loggedCustomer.getId());

		Application applicationFound = null;
		for (Application a : applications)
			if (application.getId() == a.getId()) {
				applicationFound = a;
				break;
			}

		Assert.isTrue(!applicationFound.equals(null));
		Assert.isTrue(applicationFound.getStatus().equals(Status.PENDING));

		if (application.getStatus().equals(Status.ACCEPTED))
			Assert.notNull(creditCard);
		Long number = creditCard.getNumber();
		Assert.isTrue(CustomerService.validateCreditCardNumber(number.toString()));

		application.setCreditCard(creditCard);

		Application applicationSave = this.applicationService.save(application);

		this.configurationService.isActorSuspicious(loggedCustomer);

		return applicationSave;
	}

	//NOTES
	public Note createNote(Report report, String mandatoryComment, List<String> optionalComments) {
		Customer loggedCustomer = this.securityAndCustomer();

		Note note = this.noteService.create(mandatoryComment, optionalComments);

		Collection<Report> reports = this.customerRepository.findReportsById(loggedCustomer.getId());

		Report reportFound = null;
		for (Report r : reports)
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

		this.configurationService.isActorSuspicious(loggedCustomer);

		return noteSaved;
	}

	public Note addComent(Note note, String comment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Note> notes = this.customerRepository.findNotesById(loggedCustomer.getId());

		Note noteFound = null;
		for (Note n : notes)
			if (note.getId() == n.getId()) {
				noteFound = n;
				break;
			}

		Assert.notNull(noteFound);

		List<String> comments = noteFound.getOptionalComments();
		comments.add(comment);

		Note noteSaved = this.noteService.save(noteFound);

		this.configurationService.isActorSuspicious(loggedCustomer);

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
		for (Endorsment e : endorsments)
			if (e.getId() == endorsmentId) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		return endorsmentFound;
	}

	public void loggedAsEndorser() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("CUSTOMER"));
	}

	public Endorsment createEndorsment(Endorser writtenTo) {
		this.loggedAsEndorser();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Customer customer = this.customerRepository.getCustomerByUsername(userAccount.getUsername());

		Endorsment endorsment = new Endorsment();

		List<String> comments = new ArrayList<String>();
		endorsment.setComments(comments);
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		endorsment.setMoment(thisMoment);
		endorsment.setWrittenBy(customer);
		endorsment.setWrittenTo(writtenTo);

		return endorsment;
	}
	/*
	 * public Endorsment createEndorsment(Endorsment endorsment) {
	 * Customer loggedCustomer = this.securityAndCustomer();
	 * 
	 * Assert.isTrue(endorsment.getWrittenBy().getUserAccount().equals(loggedCustomer));
	 * 
	 * Collection<HandyWorker> hks = new ArrayList<HandyWorker>();
	 * hks = this.customerRepository.handyWorkersById(loggedCustomer.getId());
	 * 
	 * HandyWorker hk = this.handyWorkerService.handyWorkerByUsername(endorsment.getWrittenTo().getUserAccount().getUsername());
	 * Assert.isTrue(hks.contains(hk));
	 * 
	 * Endorsment newEndorsment = this.endorsmentService.save(endorsment);
	 * List<Endorsment> end = endorsment.getWrittenTo().getEndorsments();
	 * end.add(newEndorsment);
	 * 
	 * Endorser endorser1 = this.endorserService.findOne(endorsment.getWrittenTo().getId());
	 * endorser1.setEndorsments(end);
	 * this.endorserService.save(endorser1);
	 * 
	 * end = endorsment.getWrittenBy().getEndorsments();
	 * end.add(endorsment);
	 * Endorser endorser2 = this.endorserService.findOne(endorsment.getWrittenBy().getId());
	 * endorser2.setEndorsments(end);
	 * this.endorserService.save(endorser2);
	 * this.endorsmentService.save(newEndorsment);
	 * return newEndorsment;
	 * }
	 */

	public Endorsment updateEndorsment(Endorsment endorsment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Endorsment> endorsments = this.customerRepository.endorsmentsOfById(loggedCustomer.getId());

		Endorsment endorsmentFound = null;
		for (Endorsment e : endorsments)
			if (e.getId() == endorsment.getId()) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		Endorsment endorsmentSave = this.endorsmentService.save(endorsment);

		this.configurationService.isActorSuspicious(loggedCustomer);

		return endorsmentSave;
	}

	public void deleteEndorsment(Endorsment endorsment) {
		Customer loggedCustomer = this.securityAndCustomer();

		Collection<Endorsment> endorsments = this.customerRepository.endorsmentsOfById(loggedCustomer.getId());

		Endorsment endorsmentFound = null;
		for (Endorsment e : endorsments)
			if (e.getId() == endorsment.getId()) {
				endorsmentFound = e;
				break;
			}

		Assert.notNull(endorsmentFound);

		this.endorsmentService.delete(endorsment);
	}

	//REPORTS
	public Report showReport(Report report) {
		Customer loggedCustomer = this.securityAndCustomer();
		Assert.isTrue(report.getFinalMode());
		return this.reportService.findOne(report.getId());
	}

	public List<Report> listReports() {
		Customer loggedCustomer = this.securityAndCustomer();
		List<Report> lr = this.reportService.findAll();
		for (Report report : lr)
			Assert.isTrue(report.getFinalMode());
		return lr;
	}

	public List<HandyWorker> getHandyWorkersById(int customerId) {
		return (List<HandyWorker>) this.customerRepository.handyWorkersById(customerId);
	}
}

package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.Category;
import domain.Complaint;
import domain.CreditCard;
import domain.Customer;
import domain.Endorsment;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Report;
import domain.Status;
import domain.Warranty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml" })
@Transactional
public class CustomerServiceTest extends AbstractTest {

    // Service under test

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FixUpTaskService fixUpTaskService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private EndorsmentService endorsmentService;

    @Autowired
    private HandyWorkerService handyWorkerService;

    @Autowired
    private WarrantyService warrantyService;

    // TEST---------------------------------------------------------------------

    // Testeado
    @Test
    public void testCreditCardNumber() {
	super.authenticate("PacoCustomer");
	Assert.isTrue(CustomerService
		.validateCreditCardNumber("4536000785192332"));
	super.authenticate(null);
    }

    // Testeado
    @Test
    public void testCreditCardNumberInvalid() {
	super.authenticate("PacoCustomer");
	Assert.isTrue(!CustomerService
		.validateCreditCardNumber("4536000785192337"));
	super.authenticate(null);
    }

    @Test
    public void testShowFixUpTaskLoggedCustomer() {
	super.authenticate("PacoCustomer");
	Customer customer = this.customerService.securityAndCustomer();
	Assert.isTrue(customer.getFixUpTasks().size() == 4);
	super.authenticate(null);
    }

    @Test
    public void testShowFixUpTaskPerCustomer() {
	super.authenticate("PacoCustomer");
	Customer customer = new Customer();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	Assert.isTrue(this.customerService.showFixUpTasks(customer.getId())
		.size() == 4);
	super.authenticate(null);
    }

    @Test
    public void testGetFixUpTask() {
	super.authenticate("PacoCustomer");
	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);
	Assert.isTrue(this.customerService.getFixUpTask(fixUpTask.getId())
		.getDescription() == this.fixUpTaskService.findOne(
		fixUpTask.getId()).getDescription());
	super.authenticate(null);
    }

    // Tested
    @Test
    public void testCreateFixUpTask() {
	super.authenticate("PacoCustomer");

	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR, 2018);
	cal.set(Calendar.MONTH, Calendar.DECEMBER);
	cal.set(Calendar.DAY_OF_MONTH, 12);
	Date realizationTime = cal.getTime();

	List<Warranty> warranties = new ArrayList<Warranty>();
	Warranty warranty = this.warrantyService.create("titulo",
		new ArrayList<String>(), new ArrayList<String>(), true);
	Warranty warrantySaved = this.warrantyService.save(warranty);
	warranties.add(this.warrantyService.findOne(warrantySaved.getId()));

	List<Category> categories = (List<Category>) this.fixUpTaskService
		.findAll().get(0).getCategories();

	FixUpTask fix = this.fixUpTaskService.create("Description",
		"Direction", 5., realizationTime, warranties,
		new ArrayList<Phase>(), categories, new ArrayList<Complaint>(),
		new ArrayList<Application>());
	FixUpTask fixSaved = this.fixUpTaskService.save(fix);

	Assert.notNull(this.fixUpTaskService.findOne(fixSaved.getId()));

	super.authenticate(null);
    }

    @Test
    public void testUpdateFixUpTask() {
	super.authenticate("PacoCustomer");
	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);

	FixUpTask res = this.customerService.getFixUpTask(fixUpTask.getId());
	res.setDescription("hola");
	FixUpTask saved = this.customerService.updateFixUpTask(res);
	Assert.isTrue(saved.getDescription().contains("hola"));
	super.authenticate(null);
    }

    @Test
    public void testDeleteFixUpTask() {
	super.authenticate("PacoCustomer");

	Customer loggedCustomer = this.customerService.securityAndCustomer();
	FixUpTask fixUpTask = loggedCustomer.getFixUpTasks().get(0);

	this.customerService.deleteFixUpTask(fixUpTask);
	Assert.isTrue(this.fixUpTaskService.findOne(fixUpTask.getId()).equals(
		null));

	super.authenticate(null);
    }

    @Test
    public void testShowComplaints() {
	super.authenticate("PacoCustomer");
	Customer customer = this.customerService.securityAndCustomer();
	Assert.isTrue(this.customerService.showComplaints().size() != 11);
	super.authenticate(null);
    }

    @Test
    public void testGetComplaint() {

	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);
	List<Complaint> complaints = new ArrayList<Complaint>();
	Complaint complaint = new Complaint();

	complaints = (List<Complaint>) fixUpTask.getComplaints();
	complaint = complaints.get(0);

	super.authenticate("PacoCustomer");
	Assert.isTrue(this.customerService.getComplaint(complaint.getId())
		.getDescription() == this.complaintService.findOne(
		complaint.getId()).getDescription());
	super.authenticate(null);
    }

    @Test
    public void testCreateComplaint() {
	super.authenticate("PacoCustomer");

	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);

	FixUpTask res = this.customerService.getFixUpTask(fixUpTask.getId());
	int numberComplaint = this.complaintService.findAll().size();
	this.customerService.createComplaint(res, "descripcionn",
		new ArrayList<String>());
	int numberComplaint2 = this.complaintService.findAll().size();
	Assert.isTrue(numberComplaint + 1 == numberComplaint2);
	super.authenticate(null);
    }

    @Test
    public void testShowApplications() {
	super.authenticate("PacoCustomer");
	Customer customer = this.customerService.securityAndCustomer();
	Assert.isTrue(this.customerService.showApplications().size() != 9);
	super.authenticate(null);
    }

    @Test
    public void testEditApplication() {
	super.authenticate("PacoCustomer");

	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);

	List<Application> applications = new ArrayList<Application>();
	applications = (List<Application>) fixUpTask.getApplications();
	Application application = new Application();

	application = applications.get(0);

	Application res = this.applicationService.findOne(application.getId());
	res.setStatus(Status.ACCEPTED);
	CreditCard creditCard = new CreditCard();
	Application saved = this.customerService.editApplication(res,
		creditCard);
	Assert.isTrue(saved.getStatus().equals(Status.ACCEPTED));
	super.authenticate(null);
    }

    @Test
    public void testEditApplicationDani() {
	super.authenticate("PacoCustomer");
	List<Application> la = (List<Application>) this.applicationService
		.findAll();
	Application res = la.get(1);
	CreditCard creditCard = new CreditCard();
	creditCard.setBrandName("VISA");
	creditCard.setHolderName("Paco");
	creditCard.setCvvCode(667);
	creditCard.setExpirationMonth(06);
	creditCard.setExpirationYear(2021);
	Long num = 4539234009047017L;
	creditCard.setNumber(num);
	Application saved = this.customerService.editApplication(res,
		creditCard);
	Application a = this.applicationService.save(saved);
	Assert.isTrue(creditCard.getNumber().equals(num));
	super.authenticate(null);
    }

    @Test
    public void testCreateNote() {
	super.authenticate("PacoCustomer");

	Customer customer = new Customer();
	FixUpTask fixUpTask = new FixUpTask();
	customer = this.customerService.getCustomerByUsername("PacoCustomer");
	fixUpTask = customer.getFixUpTasks().get(0);
	List<Complaint> complaints = new ArrayList<Complaint>();
	Complaint complaint = new Complaint();
	complaints = (List<Complaint>) fixUpTask.getComplaints();

	complaint = complaints.get(0);
	List<Report> reports = new ArrayList<Report>();
	reports = complaint.getReports();
	Report report = new Report();
	report = reports.get(0);

	Report r = this.reportService.findOne(report.getId());
	int numberNotes = this.noteService.findAll().size();
	this.customerService.createNote(r, "hello", new ArrayList<String>());
	int numberNotes2 = this.noteService.findAll().size();
	Assert.isTrue(numberNotes + 1 == numberNotes2);
	super.authenticate(null);
    }

    @Test
    public void testCreateNoteDani() {
	super.authenticate("PacoCustomer");
	List<Report> lr = this.reportService.findAll();
	Report r = lr.get(0);
	Note note = this.customerService.createNote(r, "hello",
		new ArrayList<String>());
	Note save = this.noteService.save(note);
	List<Note> ln = this.noteService.findAll();
	Assert.isTrue(ln.contains(save));
	super.authenticate(null);
    }

    @Test
    public void testAddComent() {
	super.authenticate("PacoCustomer");
	Note n = this.noteService.findOne(1811);
	int comment = n.getOptionalComments().size();
	Note res = this.customerService.addComent(n, "hello");
	int comment2 = res.getOptionalComments().size();
	Assert.isTrue(comment + 1 == comment2);
	super.authenticate(null);
    }

    @Test
    public void testShowEndorsments() {
	super.authenticate("PacoCustomer");
	Customer customer = this.customerService.securityAndCustomer();
	Assert.isTrue(this.customerService.showEndorsments().size() == 2);
	super.authenticate(null);
    }

    @Test
    public void testGetEndorsment() {
	super.authenticate("PacoCustomer");
	Assert.isTrue(this.customerService.getEndorsment(1963).getMoment() == this.endorsmentService
		.findOne(1963).getMoment());
	super.authenticate(null);
    }

    @Test
    public void testCreateEndorsment() {
	super.authenticate("PacoCustomer");
	HandyWorker hk = this.handyWorkerService.findOne(1844);
	int numberEndorsments = this.endorsmentService.findAll().size();
	this.customerService.createEndorsment(new ArrayList<String>(), hk);
	int numberEndorsments2 = this.endorsmentService.findAll().size();
	Assert.isTrue(numberEndorsments + 1 == numberEndorsments2);
	super.authenticate(null);
    }

    @Test
    public void testUpdateEndorsment() {
	super.authenticate("PacoCustomer");
	Endorsment res = this.customerService.getEndorsment(1963);
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR, 2018);
	cal.set(Calendar.MONTH, Calendar.DECEMBER);
	cal.set(Calendar.DAY_OF_MONTH, 12);
	Date startDate = cal.getTime();
	res.setMoment(startDate);
	Endorsment saved = this.customerService.updateEndorsment(res);
	Assert.isTrue(saved.getMoment() == startDate);
	super.authenticate(null);
    }

    @Test
    public void testDeleteEndorsment() {
	super.authenticate("PacoCustomer");
	Endorsment res = this.customerService.getEndorsment(1963);
	int numberOfEndorsments = this.endorsmentService.findAll().size();
	this.customerService.deleteEndorsment(res);
	int numberOfEndosrments2 = this.endorsmentService.findAll().size();
	Assert.isTrue(numberOfEndorsments - 1 == numberOfEndosrments2);
	super.authenticate(null);
    }

    @Test
    public void testShowReport() {
	super.authenticate("PacoCustomer");
	Report res = this.reportService.findOne(1810);
	Assert.isTrue(this.customerService.showReport(res).getDescription() == this.reportService
		.findOne(1810).getDescription());
	super.authenticate(null);
    }

    @Test
    public void testListReports() {
	super.authenticate("PacoCustomer");
	Customer customer = this.customerService.securityAndCustomer();
	Assert.isTrue(this.customerService.listReports().size() == 4);
	super.authenticate(null);
    }
}

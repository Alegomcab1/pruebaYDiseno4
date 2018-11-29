
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
import domain.Warranty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CustomerServiceTest extends AbstractTest {

	//Service under test

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;

	@Autowired
	private ComplaintService	complaintService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ReportService		reportService;

	@Autowired
	private NoteService			noteService;

	@Autowired
	private EndorsmentService	endorsmentService;

	@Autowired
	private HandyWorkerService	handyWorkerService;

	@Autowired
	private WarrantyService		warrantyService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private RefereeService		refereeService;


	//TEST---------------------------------------------------------------------

	@Test
	public void testCreditCardNumber() {
		super.authenticate("PacoCustomer");
		Assert.isTrue(CustomerService.validateCreditCardNumber("4536000785192332"));
		super.authenticate(null);
	}

	@Test
	public void testCreditCardNumberInvalid() {
		super.authenticate("PacoCustomer");
		Assert.isTrue(!CustomerService.validateCreditCardNumber("4536000785192337"));
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
		Assert.isTrue(this.customerService.showFixUpTasks(1873).size() == 4);
		super.authenticate(null);
	}

	@Test
	public void testGetFixUpTask() {
		super.authenticate("PacoCustomer");
		Assert.isTrue(this.customerService.getFixUpTask(1936).getDescription() == this.fixUpTaskService.findOne(1936).getDescription());
		super.authenticate(null);
	}

	@Test
	public void testCreateFixUpTask() {
		super.authenticate("PacoCustomer");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 12);
		Date realizationTime = cal.getTime();

		List<Warranty> warranties = new ArrayList<Warranty>();
		Warranty warranty = this.warrantyService.create("titulo", new ArrayList<String>(), new ArrayList<String>(), true);
		Warranty warrantySaved = this.warrantyService.save(warranty);
		warranties.add(this.warrantyService.findOne(warrantySaved.getId()));

		List<Category> categories = (List<Category>) this.fixUpTaskService.findAll().get(0).getCategories();

		FixUpTask fix = this.fixUpTaskService.create("Description", "Direction", 5., realizationTime, warranties, new ArrayList<Phase>(), categories, new ArrayList<Complaint>(), new ArrayList<Application>());
		FixUpTask fixSaved = this.fixUpTaskService.save(fix);

		Assert.notNull(this.fixUpTaskService.findOne(fixSaved.getId()));

		super.authenticate(null);
	}

	@Test
	public void testUpdateFixUpTask() {
		super.authenticate("PacoCustomer");
		FixUpTask res = this.customerService.getFixUpTask(1936);
		res.setDescription("hola");
		FixUpTask saved = this.customerService.updateFixUpTask(res);
		Assert.isTrue(saved.getDescription().contains("hola"));
		super.authenticate(null);
	}

	@Test
	public void testDeleteFixUpTask() {
		super.authenticate("PacoCustomer");
		Customer loggedCustomer = this.customerService.securityAndCustomer();
		FixUpTask res = loggedCustomer.getFixUpTasks().get(0);
		int numberOfFixUpTasks = this.fixUpTaskService.findAll().size();
		this.customerService.deleteFixUpTask(res);
		int numberOfFixUpTasks2 = this.fixUpTaskService.findAll().size();
		Assert.isTrue(numberOfFixUpTasks - 1 == numberOfFixUpTasks2);
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
		super.authenticate("PacoCustomer");
		Assert.isTrue(this.customerService.getComplaint(1937).getDescription() == this.complaintService.findOne(1937).getDescription());
		super.authenticate(null);
	}

	@Test
	public void testCreateComplaint() {
		super.authenticate("PacoCustomer");
		FixUpTask res = this.customerService.getFixUpTask(1936);
		Complaint c = this.customerService.createComplaint(res, "descripcionn", new ArrayList<String>());
		c.setReferee(this.refereeService.findAll().get(0));
		Complaint saved = this.complaintService.save(c);
		List<Complaint> lc = this.complaintService.findAll();
		Assert.isTrue(lc.contains(saved));
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
		List<Application> la = (List<Application>) this.applicationService.findAll();
		Application res = la.get(1);
		CreditCard creditCard = new CreditCard();
		creditCard.setBrandName("VISA");
		creditCard.setHolderName("Paco");
		creditCard.setCvvCode(667);
		creditCard.setExpirationMonth(06);
		creditCard.setExpirationYear(2021);
		Long num = 4539234009047017L;
		creditCard.setNumber(num);
		Application saved = this.customerService.editApplication(res, creditCard);
		Application a = this.applicationService.save(saved);
		Assert.isTrue(a.getCreditCard().getNumber().equals(num));
		super.authenticate(null);
	}

	@Test
	public void testCreateNote() {
		super.authenticate("PacoCustomer");
		List<Report> lr = this.reportService.findAll();
		Report r = lr.get(0);
		Note note = this.customerService.createNote(r, "hello", new ArrayList<String>());
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
		Assert.isTrue(this.customerService.getEndorsment(1963).getMoment() == this.endorsmentService.findOne(1963).getMoment());
		super.authenticate(null);
	}

	@Test
	public void testCreateEndorsment() {
		super.authenticate("PacoCustomer");
		Customer customer = this.customerService.securityAndCustomer();
		List<String> comments = new ArrayList<String>();
		comments.add("waaaaa");
		List<HandyWorker> hks = this.customerService.getHandyWorkersById(customer.getId());
		HandyWorker h = hks.get(0);
		List<Customer> lc = (List<Customer>) this.customerService.findAll();
		Customer c = lc.get(0);
		Endorsment e2 = this.customerService.createEndorsment(c);
		Endorsment save = this.endorsmentService.save(e2);
		List<Endorsment> le = (List<Endorsment>) this.endorsmentService.findAll();
		Assert.isTrue(le.contains(save));
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
		Assert.isTrue(this.customerService.showReport(res).getDescription() == this.reportService.findOne(1810).getDescription());
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

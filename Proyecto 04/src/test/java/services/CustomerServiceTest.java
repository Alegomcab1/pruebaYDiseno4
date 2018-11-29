
package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

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
		Date startDate = (Date) cal.getTime();
		int fixUpTask = this.fixUpTaskService.findAll().size();
		this.customerService.createFixUpTask("description", "avda. reina", 100.02, startDate, new ArrayList<Warranty>(), new ArrayList<Phase>(), new ArrayList<Category>(), new ArrayList<Complaint>(), new ArrayList<Application>());
		int fixUpTask2 = this.fixUpTaskService.findAll().size();
		Assert.isTrue(fixUpTask + 1 == fixUpTask2);
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
		FixUpTask res = this.customerService.getFixUpTask(1936);
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
		int numberComplaint = this.complaintService.findAll().size();
		this.customerService.createComplaint(res, "descripcionn", new ArrayList<String>());
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
		Application res = this.applicationService.findOne(1954);
		res.setStatus(Status.ACCEPTED);
		CreditCard creditCard = new CreditCard();
		Application saved = this.customerService.editApplication(res, creditCard);
		Assert.isTrue(saved.getStatus().equals(Status.ACCEPTED));
		super.authenticate(null);
	}

	@Test
	public void testCreateNote() {
		super.authenticate("PacoCustomer");
		Report r = this.reportService.findOne(1810);
		int numberNotes = this.noteService.findAll().size();
		this.customerService.createNote(r, "hello", new ArrayList<String>());
		int numberNotes2 = this.noteService.findAll().size();
		Assert.isTrue(numberNotes + 1 == numberNotes2);
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
		Date startDate = (Date) cal.getTime();
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

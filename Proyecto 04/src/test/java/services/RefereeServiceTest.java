
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Complaint;
import domain.Note;
import domain.Referee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RefereeServiceTest extends AbstractTest {

	//arbitrasoRF id = 1435;

	@Autowired
	private RefereeService		refereeService;
	@Autowired
	private ComplaintService	complaintService;
	@Autowired
	private ReportService		reportService;


	@Test(expected = NullPointerException.class)
	public void testSecurity() {
		super.authenticate("admin");
		this.refereeService.securityAndReferee();
		super.authenticate(null);
	}

	@Test
	public void testGetLoggedReferee() {
		super.authenticate("arbitrasoRF");
		Referee loggedReferee = this.refereeService.securityAndReferee();
		Assert.isTrue(loggedReferee.getAddress().equals("Almendralejo"));

		super.authenticate(null);
	}

	@Test
	public void testComplaintsUnassigned() {
		super.authenticate("arbitrasoRF");
		Collection<Complaint> complaints = this.refereeService.complaintsUnassigned();
		Assert.isTrue(complaints.size() == 1); //Hay un complaint sin asignar

		super.authenticate(null);
	}

	@Test
	public void testAssignComplaint() {
		super.authenticate("arbitrasoRF");
		List<Complaint> complaints = (List<Complaint>) this.refereeService.complaintsUnassigned();
		Complaint complaintUnassigned = complaints.get(0);
		Complaint complaintAssigned = this.refereeService.assingComplaint(complaintUnassigned);
		List<Complaint> complaints2 = (List<Complaint>) this.refereeService.complaintsUnassigned();
		Assert.isTrue(complaints2.isEmpty());

		complaintAssigned.setReferee(null);
		this.complaintService.save(complaintAssigned);
		List<Complaint> complaints3 = (List<Complaint>) this.refereeService.complaintsUnassigned();
		Assert.isTrue(!complaints3.isEmpty());

		super.authenticate(null);
	}

	@Test
	public void testSelfAssignedComplaints() {
		super.authenticate("arbitrasoRF");
		List<Complaint> complaintsAssigned = this.refereeService.selfAssignedComplaints();
		Assert.isTrue(complaintsAssigned.size() == 3); //Tiene 3 asignados

		super.authenticate(null);
	}

	@Test
	public void testWriteReportRegardingComplaint() {
		super.authenticate("arbitrasoRF");

		Referee loggedReferee = this.refereeService.securityAndReferee();
		Complaint complaint = this.complaintService.findOne(1403); //Complaint asignado al referee
		Assert.notNull(complaint);

		int numberOfReports = this.reportService.findAll().size();
		int numberOfReportsOfReferee = loggedReferee.getReports().size();
		int numberOfReportsOfComplaints = complaint.getReports().size();

		this.refereeService.writeReportRegardingComplaint(complaint, "Descripcion", new ArrayList<String>(), new ArrayList<Note>());

		int numberOfReports2 = this.reportService.findAll().size();
		int numberOfReportsOfReferee2 = this.refereeService.securityAndReferee().getReports().size();
		int numberOfReportsOfComplaints2 = this.complaintService.findOne(1403).getReports().size();

		Assert.isTrue(numberOfReports + 1 == numberOfReports2);
		Assert.isTrue(numberOfReportsOfReferee + 1 == numberOfReportsOfReferee2);
		Assert.isTrue(numberOfReportsOfComplaints + 1 == numberOfReportsOfComplaints2);

		super.authenticate(null);
	}

}


package services;

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
import domain.Referee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RefereeServiceTest extends AbstractTest {

	@Autowired
	private RefereeService	refereeService;


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

		super.authenticate(null);
	}
}

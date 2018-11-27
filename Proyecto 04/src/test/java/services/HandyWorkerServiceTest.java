
package services;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;
import domain.FixUpTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HandyWorkerServiceTest extends AbstractTest {

	//Service under test

	@Autowired
	private HandyWorkerService	handyWorkerService;
	@Autowired
	private CustomerService		customerService;
	@Autowired
	private ActorService		actorService;


	@Test
	public void testShowFixUpTask() {
		Actor h = new Actor();
		h = this.actorService.getActorByUsername("PepeHW");
		Customer customer = new Customer();
		super.authenticate("PepeHW");

		//	Assert.isTrue(expression)
	}
	@Test
	public void testgetFixUpTaskPerCustomer() {
		Actor h = new Actor();
		h = this.actorService.getActorByUsername("PepeHW");
		Customer customer = new Customer();
		super.authenticate("PepeHW");

		customer = this.customerService.getCustomerByUserName("PacoCustomer");

		Map<Customer, Collection<FixUpTask>> m = this.handyWorkerService.getFixUpTaskPerCustomer(customer.getFixUpTasks().get(0).getId());

		Assert.isTrue(m.keySet().contains(customer) && m.get(customer).size() == customer.getFixUpTasks().size());
	}

}

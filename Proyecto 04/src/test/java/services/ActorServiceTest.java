
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	//Service under test

	@Autowired
	private ActorService	actorService;


	//TEST---------------------------------------------------------------------

	/*
	 * public void loggedAsActor() {
	 * UserAccount userAccount;
	 * userAccount = LoginService.getPrincipal();
	 * Assert.isTrue(userAccount.getAuthorities().size() > 0);
	 * }
	 */

	@Test
	public void testloggedAsActor() {
		Collection<Actor> all;
		super.authenticate("PacoCustomer");
		all = this.actorService.findAll();
		Assert.isTrue(all.size() > 0);
		super.authenticate(null);
	}

	/*
	 * @Test
	 * public void testCreateHandyWorker() {
	 * 
	 * HandyWorker handyWorker = new HandyWorker();
	 * UserAccount userAccount = new UserAccount();
	 * List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
	 * List<Box> boxes = new ArrayList<Box>();
	 * 
	 * //Actor
	 * handyWorker.setName("Alejandro");
	 * handyWorker.setMiddleName("");
	 * handyWorker.setSurname("Gomez Caballero");
	 * handyWorker.setPhoto("https://trello.com/b/MD1aM3qn/proyecto-4-dp");
	 * handyWorker.setEmail("alegomcab1@gmail.com");
	 * handyWorker.setPhoneNumber("+34615392784");
	 * handyWorker.setAddress("C/Piruleta");
	 * 
	 * handyWorker.setBoxes(boxes);
	 * handyWorker.setSocialProfiles(socialProfiles);
	 * handyWorker.setUserAccount(userAccount);
	 * 
	 * //UserAccount
	 * userAccount.setUsername("alegomcab1");
	 * userAccount.setPassword("rutherfordio");
	 * List<Authority> authorities = new ArrayList<Authority>();
	 * Authority handyWorkerAut = new Authority();
	 * handyWorkerAut.setAuthority("HANDYWORKER");
	 * 
	 * authorities.add(handyWorkerAut);
	 * userAccount.setAuthorities(authorities);
	 * 
	 * //Boxes
	 * Box spamBox = new Box();
	 * List<Message> messages1 = new ArrayList<>();
	 * spamBox.setIsSystem(true);
	 * spamBox.setMessages(messages1);
	 * spamBox.setName("Spam Box");
	 * 
	 * Box trashBox = new Box();
	 * List<Message> messages2 = new ArrayList<>();
	 * trashBox.setIsSystem(true);
	 * trashBox.setMessages(messages2);
	 * trashBox.setName("Trash Box");
	 * 
	 * Box sentBox = new Box();
	 * List<Message> messages3 = new ArrayList<>();
	 * sentBox.setIsSystem(true);
	 * sentBox.setMessages(messages3);
	 * sentBox.setName("Sent Box");
	 * 
	 * Box receivedBox = new Box();
	 * List<Message> messages4 = new ArrayList<>();
	 * receivedBox.setIsSystem(true);
	 * receivedBox.setMessages(messages4);
	 * receivedBox.setName("Received Box");
	 * 
	 * //addBoxes
	 * boxes.add(receivedBox);
	 * boxes.add(sentBox);
	 * boxes.add(spamBox);
	 * boxes.add(trashBox);
	 * 
	 * //HandyWorker
	 * handyWorker.setMake(handyWorker.getName() + handyWorker.getMiddleName() + handyWorker.getSurname());
	 * handyWorker.setScore(Score.NOT);
	 * //Tener en cuenta Application y Curriculum
	 * 
	 * HandyWorker saved;
	 * Collection<Actor> handyWorkers;
	 * 
	 * saved = this.actorService.saveHandyWorker(handyWorker);
	 * handyWorkers = this.actorService.findAll();
	 * 
	 * Assert.isTrue(handyWorkers.contains(saved));
	 * }
	 */
}

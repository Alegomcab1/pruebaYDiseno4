
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ActorRepository;
import domain.Actor;
import domain.HandyWorker;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository	actorRepository;


	public HandyWorker saveHandyWorker(final HandyWorker handyWorker) {
		return this.actorRepository.save(handyWorker);
	}

	public Collection<Actor> findAll() {
		Collection<Actor> result;
		result = this.actorRepository.findAll();
		return result;
	}

	/*
	 * public HandyWorker createHandyWorker() {
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
	 * return handyWorker;
	 * }
	 */
}

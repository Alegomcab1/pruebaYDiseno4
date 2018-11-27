
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Message;
import domain.Priority;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository		messageRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ConfigurationService	configurationService;


	// Actualizar caja que tiene el mensaje EN ESTE ORDEN
	// ACTUALIZAR CAJA SIN EL MENSAJE
	// BORRAR EL MENSAJE
	public void delete(Message m) {
		this.messageRepository.delete(m);
	}

	// Metodo para enviar un mensaje a un tio (O varios, que tambien puede ser)
	// Hay que hacer la comprobacion de SPAM
	public void sendMessage(String subject, String body, Priority priority, List<Actor> recipients) {

		this.actorService.loggedAsActor();
		for (Actor a : recipients) {

			Box boxRecieved = new Box();
			Box boxSpam = new Box();
			Message message = new Message();
			message = this.create(subject, body, priority, recipients);
			message = this.save(message);
			boxRecieved = this.boxService.getRecievedBoxByActor(a);
			boxSpam = this.boxService.getSpamBoxByActor(a);

			// Guardar la box con ese mensaje;

			if (this.configurationService.isActorSuspicious(a)) {
				boxSpam.getMessages().add(message);
				this.boxService.save(boxSpam);
			} else {
				boxRecieved.getMessages().add(message);
				this.boxService.save(boxRecieved);
			}
		}
	}

	public Message save(Message message) {
		return this.messageRepository.save(message);
	}

	public Message create(String Subject, String body, Priority priority, List<Actor> recipients) {

		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		List<String> tags = new ArrayList<String>();

		Message message = new Message();

		message.setMoment(thisMoment);
		message.setSender(actor);
		message.setSubject(Subject);
		message.setBody(body);
		message.setPriority(priority);
		message.setReceivers(recipients);
		message.setTags(tags);

		return message;
	}

	public void updateMessage(Message message, Box box) { // Posible problema
		// con copia

		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		for (Box b : actor.getBoxes()) {
			if (b.getMessages().contains(message)) {
				List<Message> list = b.getMessages();
				list.remove(message);
				b.setMessages(list);
				this.boxService.save(b);
			}
			if (b.getName() == box.getName()) {
				List<Message> list = b.getMessages();
				list.add(message);
				b.setMessages(list);
				this.boxService.save(b);
			}
		}
	}

	public void deleteMessageToTrashBox(Message message) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		Box currentBox = this.boxService.getCurrentBoxByMessage(message);
		Box trash = this.boxService.getTrashBoxByActor(actor);

		// When an actor removes a message from a box other than trash box, it
		// is moved to the trash box;

		if (currentBox.equals(trash))
			for (Box b : actor.getBoxes())
				this.messageRepository.delete(message);
		else
			this.updateMessage(message, trash);
		// this.messageRepository.save(message); Si se pone en el metodo
		// updateMessage no hace falta aqui
	}
}

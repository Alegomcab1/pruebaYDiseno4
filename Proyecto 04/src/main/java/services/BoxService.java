package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private MessageService messageService;

    public Box create(String name, Box fatherBox) {

	Box box = new Box();
	List<Message> messages = new ArrayList<Message>();
	box.setName(name);
	box.setIsSystem(false);
	box.setMessages(messages);
	box.setFatherBox(fatherBox);

	return box;
    }

    public Box save(Box box) {
	Assert.isTrue(!box.getIsSystem());
	return this.boxRepository.save(box);
    }

    public Box updateBox(Box box) {
	Assert.isTrue(!box.getIsSystem());
	return this.save(box);
    }

    public void deleteBox(Box box) {
	Assert.isTrue(!box.getIsSystem());
	List<Box> sonBoxes = this.boxRepository.getSonsBox(box);
	if (sonBoxes.size() == 0) {
	    for (Message m : box.getMessages())
		this.messageService.delete(m);
	    this.boxRepository.delete(box);
	} else
	    for (Box sonBox : sonBoxes)
		this.deleteBox(sonBox);
	this.deleteBox(box);
    }

    public List<Box> findAll() {
	return this.boxRepository.findAll();
    }

    public Box getRecievedBoxByActor(Actor a) {
	return this.boxRepository.getRecievedBoxByActor(a);
    }

    public Box getSpamBoxByActor(Actor a) {
	return this.boxRepository.getSpamBoxByActor(a);
    }

    public Box getTrashBoxByActor(Actor a) {
	return this.boxRepository.getTrashBoxByActor(a);
    }

    public Box getCurrentBoxByMessage(Message m) {
	return this.boxRepository.getCurrentBoxByMessage(m);

    }

}

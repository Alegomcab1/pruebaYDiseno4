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

    public Box updateBox(Box box, String name, Box fatherBox) {
	Assert.isTrue(!box.getIsSystem());
	box.setFatherBox(fatherBox);
	box.setName(name);
	return this.save(box);
    }

    public void deleteBox(Box box) {
	Assert.isTrue(!box.getIsSystem());
	for (Message m : box.getMessages())
	    this.messageService.delete(m);
	this.boxRepository.delete(box);
    }

    public List<Box> findAll() {
	return this.boxRepository.findAll();
    }

    public Box getRecievedBoxByActor(Actor a) {
	return this.boxRepository.getRecievedBoxByActor(a);
    }

}

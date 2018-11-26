package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Section;

@Service
@Transactional
public class SectionService {

    @Autowired
    private SectionService sectionService;

    public Section create(String title, String text, Integer number) {
	Section section = new Section();
	List<String> pictures = new ArrayList<String>();

	section.setSectionTitle(title);
	section.setText(text);
	section.setSectionPictures(pictures);

	return section;
    }

    public Section save(Section s) {

	return this.sectionService.save(s);
    }

    public List<Section> findAll() {

	return this.sectionService.findAll();
    }

    public Section findOne(Integer id) {

	return this.sectionService.findOne(id);
    }

    public void delete(Section s) {
	this.sectionService.delete(s);
    }

}

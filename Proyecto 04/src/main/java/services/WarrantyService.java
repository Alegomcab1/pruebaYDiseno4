
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.WarrantyRepository;
import domain.Warranty;

@Service
@Transactional
public class WarrantyService {

	@Autowired
	private WarrantyRepository	warrantyRepository;


	// CRUD Methods

	public Warranty create(String title) {

		Warranty warranty = new Warranty();
		List<String> terms = new ArrayList<String>();
		List<String> laws = new ArrayList<String>();
		warranty.setTitle(title);
		warranty.setIsDraftMode(true);
		warranty.setTerms(terms);
		warranty.setLaws(laws);

		return warranty;
	}

	public Warranty save(Warranty warranty) {
		if (warranty.getIsDraftMode() == true)
			return this.warrantyRepository.save(warranty);
		else
			throw new IllegalAccessError();
	}

	public Warranty update(Warranty warranty) {
		if (warranty.getIsDraftMode() == true)
			return this.save(warranty);
		else
			throw new IllegalAccessError();
	}

	public void delete(Warranty warranty) {
		if (warranty.getIsDraftMode() == true)
			this.warrantyRepository.delete(warranty);
	}

	public List<Warranty> findAll() {
		return this.warrantyRepository.findAll();
	}

}

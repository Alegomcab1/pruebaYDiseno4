
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	private List<Category>	subCategories;
	private String			name;

	private Category		fatherCategory;


	public Category() {		//Created for Json purposes
		super();
	}

	@Valid
	@ManyToOne(optional = true)
	public Category getFatherCategory() {
		return this.fatherCategory;
	}

	public void setFatherCategory(Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

	@OneToMany
	@Valid
	public List<Category> getSubCategories() {
		return this.subCategories;
	}

	public void setSubCategories(final List<Category> subCategories) {
		this.subCategories = subCategories;
	}

	@Column(unique = true)
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}

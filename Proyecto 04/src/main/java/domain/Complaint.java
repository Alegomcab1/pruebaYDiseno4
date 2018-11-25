
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

@Entity
@Access(AccessType.PROPERTY)
public class Complaint extends DomainEntity {

	private String			ticker;
	private LocalDate		moment;
	private String			description;
	private List<String>	attachments;

	private List<Report>	reports;


	@NotBlank
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@Past
	@NotNull
	public LocalDate getMoment() {
		return this.moment;
	}

	public void setMoment(final LocalDate localDate) {
		this.moment = localDate;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Valid
	@ElementCollection(targetClass = String.class)
	public List<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final List<String> attachments) {
		this.attachments = attachments;
	}

	@OneToMany
	public List<Report> getReports() {
		return this.reports;
	}
	
	public void setReports(final List<Report> reports) {
		this.reports = reports;
	}

}

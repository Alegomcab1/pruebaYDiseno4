
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private List<String>	spamWords;
	private Integer			iva21;
	private List<String>	cardType;
	private String			spainTelephoneCode;


	@ElementCollection(targetClass = String.class)
	public List<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(List<String> spamWords) {
		this.spamWords = spamWords;
	}

	@NotNull
	public Integer getIva21() {
		return this.iva21;
	}

	public void setIva21(Integer iva21) {
		this.iva21 = iva21;
	}

	@ElementCollection(targetClass = String.class)
	public List<String> getCardType() {
		return this.cardType;
	}

	public void setCardType(List<String> cardType) {
		this.cardType = cardType;
	}

	@NotBlank
	public String getSpainTelephoneCode() {
		return this.spainTelephoneCode;
	}

	public void setSpainTelephoneCode(String spainTelephoneCode) {
		this.spainTelephoneCode = spainTelephoneCode;
	}

}

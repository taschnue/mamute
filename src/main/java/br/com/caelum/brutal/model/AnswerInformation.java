package br.com.caelum.brutal.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class AnswerInformation implements UpdatableInformation {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String markedDescription;

	@Lob
	@Length(min = 30)
	@NotEmpty
	private String description;

	@ManyToOne(optional = false)
	private final User author;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Embedded
	private Moderation moderation;

	private UpdateStatus status;

	private String ip;

	@ManyToOne
    private Answer answer;
	
	/**
	 * @deprecated hibernate only
	 */
	AnswerInformation() {
		this("", null);
	}

	public AnswerInformation(String description, CurrentUser currentUser) {
		if (currentUser == null) {
			this.author = null;
			this.ip = null;
		} else {
			this.author = currentUser.getCurrent();
			this.ip = currentUser.getIp();
		}
		setDescription(description);
	}
	
	public AnswerInformation(String description, CurrentUser currentUser, Answer existentAnswer) {
	    this(description, currentUser);
	    setAnswer(existentAnswer);
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (this.moderation != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderation = new Moderation(moderator);
	}

	private void setDescription(String description) {
		this.description = description;
		this.markedDescription = MarkDown.parse(description);
	}

	public String getDescription() {
		return description;
	}

	public String getMarkedDescription() {
		return markedDescription;
	}

	public User getAuthor() {
		return author;
	}

	public void setInitStatus(UpdateStatus status) {
		if (this.status != null) {
			throw new IllegalStateException(
					"Status can only be setted once. Afterwards it should BE MODERATED!");
		}
		this.status = status;
	}

    void setAnswer(Answer answer) {
        this.answer = answer;
    }
    
    public DateTime getCreatedAt() {
        return createdAt;
    }
    
    public Answer getAnswer() {
        return answer;
    }

}

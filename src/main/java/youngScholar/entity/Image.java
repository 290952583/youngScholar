package youngScholar.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
@Entity
public class Image
{
	@Id
	private String name;

	@Column(nullable = false, columnDefinition = "mediumblob")
	@Size(min = 10240, max = 10240 * 1024)
	private byte[] data;
}

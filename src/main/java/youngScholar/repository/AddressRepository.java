package youngScholar.repository;

import youngScholar.entity.Address;
import youngScholar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>
{
	List<Address> findAllByUser(User user);

	int countByUser(User user);
}

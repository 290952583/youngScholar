package youngScholar.repository;

import youngScholar.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
	User findByMobile(String mobile);

	@Modifying
	@Query("update User set invitationCount = invitationCount + 1 where invitationCode = ?1")
	void addInvitationCount(String code);

    @Query("select u from User u where id = ?#{principal.id}")
    User getCurrentUser();
}
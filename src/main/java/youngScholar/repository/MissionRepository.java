package youngScholar.repository;

import youngScholar.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, String>
{
    List<Mission> findAllByUserAndStatusIn(User user, Mission.Status[] status, Pageable pageable);

    List<Mission> findAllByAcceptUserAndStatusIn(User user, Mission.Status[] status, Pageable pageable);

    List<Mission> findAllByUserSchoolAndStatusIn(School school, Mission.Status[] status, Pageable pageable);
}

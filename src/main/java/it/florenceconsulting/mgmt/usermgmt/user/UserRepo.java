package it.florenceconsulting.mgmt.usermgmt.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query("FROM User WHERE (:lastname IS NULL OR lastname = :lastname) AND (:firstname IS NULL OR firstname = :firstname)")    
    public List<User> findByNames(@Param("lastname") String lastname, @Param("firstname") String firstname);

    public List<User> findByEmail(@Param("email") String email);
}

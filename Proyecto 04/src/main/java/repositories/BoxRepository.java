package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

    //
    @Query("select b from Actor a inner join a.boxes b where b.name = 'Received messages' and a = ?1")
    public Box getRecievedBoxByActor(Actor actor);
}

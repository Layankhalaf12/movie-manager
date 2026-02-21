package ca.khalaf.a2layan.data;

import ca.khalaf.a2layan.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("""
            select m from Movie m
            where lower(m.title) like lower(concat('%', :query, '%'))
               or lower(m.director) like lower(concat('%', :query, '%'))
               or lower(m.genre) like lower(concat('%', :query, '%'))
            """)
    List<Movie> search(@Param("query") String query);
}

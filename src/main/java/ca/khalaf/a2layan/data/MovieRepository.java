// Student Name: Layan Khalaf | Student ID: 000000000
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
            where (:title is null or lower(m.title) like lower(concat('%', :title, '%')))
              and (:genre is null or lower(m.genre) like lower(concat('%', :genre, '%')))
              and (:minRating is null or m.rating >= :minRating)
            order by m.title asc
            """)
    List<Movie> filterMovies(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("minRating") Integer minRating
    );
}

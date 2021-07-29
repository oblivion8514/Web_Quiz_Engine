package engine.model;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface QuizRepository extends PagingAndSortingRepository<Quiz,Integer>{
}

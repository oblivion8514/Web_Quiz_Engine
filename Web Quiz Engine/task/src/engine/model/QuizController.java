package engine.model;
import engine.user.User;
import engine.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SolvedRepository solvedRepository;

    @GetMapping()
    public Page<Quiz> getQuizzesFromPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    @GetMapping(path = "/completed")
    public Page<Solution> getSolvedQuizzes(@RequestParam(name = "page", defaultValue = "0") int page, @AuthenticationPrincipal User user) {
        return solvedRepository.findAllByCompletedBy(user.getEmail(), PageRequest.of(page, 10, Sort.by("completedAt").descending()));
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable int id){
        return this.quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(String.format("quiz #%d does not exist.",id) ) );
    }

    @PostMapping()
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz,@AuthenticationPrincipal User user){
        quiz.setAuthor(user.getEmail());
        return quizRepository.save(quiz);
    }
    @PostMapping("/{id}/solve")
    public QuizResult getResult(@PathVariable int id,@RequestBody QuizAnswer answer,
                                @AuthenticationPrincipal User user){
        Quiz quiz=getQuizById(id);
        if (Objects.equals(answer.getAnswer(), quiz.getAnswer())){
            solvedRepository.save(new Solution(quiz.getId(), LocalDateTime.now(), user.getEmail()));
            return QuizResult.SUCCESS;
        }
        else{
            return QuizResult.FAIL;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable int id, @AuthenticationPrincipal User user){
        Quiz quiz=getQuizById(id);
        if (!quiz.getAuthor().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,String.format("Unauthorized: Cannot delete quiz"));
        }
        else {
            quizRepository.delete(quiz);
        }
    }

    @PutMapping("{id}")
    public void updateQuiz(@PathVariable int id, @Valid @RequestBody Quiz newQuiz,
                           @AuthenticationPrincipal User user){
        Quiz quiz=getQuizById(id);
        if (!quiz.getAuthor().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,String.format("Unauthorized: Cannot delete quiz"));
        }
        else {
            quiz.setTitle(newQuiz.getTitle());
            quiz.setText(newQuiz.getText());
            quiz.setOptions(newQuiz.getOptions());
            quiz.setAnswer(newQuiz.getAnswer());
            quizRepository.save(quiz);
        }
    }
}

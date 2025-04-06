package org.example.labwork55.config;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dto.OptionDto;
import org.example.labwork55.dto.QuestionDto;
import org.example.labwork55.dto.QuizDto;
import org.example.labwork55.dto.UserDto;
import org.example.labwork55.service.QuizService;
import org.example.labwork55.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.stream.Collectors;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserService userService;
    private final QuizService quizService;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            if (userCount != null && userCount > 0) {
                return;
            }

            jdbcTemplate.execute("INSERT INTO role (id, role) VALUES (1, 'USER')");
            jdbcTemplate.execute("INSERT INTO role (id, role) VALUES (2, 'ADMIN')");

            createDemoUsers();

            createDemoQuizzes();
        };
    }

    private void createDemoUsers() {
        UserDto admin = UserDto.builder()
                .username("admin")
                .email("admin@example.com")
                .password("Admin123")
                .build();
        userService.registerUser(admin);

        jdbcTemplate.update("UPDATE users SET role_id = 2 WHERE email = ?", "admin@example.com");

        UserDto user = UserDto.builder()
                .username("user")
                .email("user@example.com")
                .password("User123")
                .build();
        userService.registerUser(user);
    }

    private void createDemoQuizzes() {
        QuizDto generalKnowledgeQuiz = QuizDto.builder()
                .title("Общие знания")
                .description("Тест на общую эрудицию")
                .questions(List.of(
                        createQuestion("Столица Франции?",
                                List.of("Лондон", "Париж", "Берлин", "Мадрид"), 1),
                        createQuestion("Сколько планет в Солнечной системе?",
                                List.of("7", "8", "9", "10"), 1),
                        createQuestion("Кто написал \"Война и мир\"?",
                                List.of("Достоевский", "Толстой", "Пушкин", "Чехов"), 1)
                ))
                .build();
        quizService.createQuiz(generalKnowledgeQuiz, "admin@example.com");

        QuizDto programmingQuiz = QuizDto.builder()
                .title("Основы программирования")
                .description("Тест на знание основ программирования")
                .questions(List.of(
                        createQuestion("Что такое SQL?",
                                List.of("Язык программирования", "Язык запросов к базам данных", "Операционная система", "Фреймворк"), 1),
                        createQuestion("Что означает аббревиатура JVM?",
                                List.of("Java Virtual Machine", "JavaScript Virtual Module", "Java Visual Maker", "Just Verify Memory"), 0),
                        createQuestion("Какой метод вызывается первым в Java-приложении?",
                                List.of("init()", "start()", "run()", "main()"), 3)
                ))
                .build();
        quizService.createQuiz(programmingQuiz, "admin@example.com");
    }

    private QuestionDto createQuestion(String text, List<String> optionTexts, int correctIndex) {
        List<OptionDto> options = createOptions(optionTexts, correctIndex);
        return QuestionDto.builder()
                .questionText(text)
                .options(options)
                .build();
    }

    private List<OptionDto> createOptions(List<String> optionTexts, int correctIndex) {
        return optionTexts.stream()
                .map(text -> OptionDto.builder()
                        .optionText(text)
                        .isCorrect(optionTexts.indexOf(text) == correctIndex)
                        .build())
                .collect(Collectors.toList());
    }
}
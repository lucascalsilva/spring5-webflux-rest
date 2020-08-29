package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.constants.ApplicationConstants.API_CATEGORIES_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    WebTestClient webTestClient;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getAllCategories(){
        given(categoryRepository.findAll()).willReturn(Flux.just(Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()));

        webTestClient.get().uri(API_CATEGORIES_PATH)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById(){
        given(categoryRepository.findById("1")).willReturn(Mono.just(Category.builder().id("1").description("Cat1").build()));

        webTestClient.get().uri(API_CATEGORIES_PATH + "/1")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void testCreateCategory(){
        given(categoryRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Cat1").build());

        webTestClient.post().uri(API_CATEGORIES_PATH)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testUpdateCategory(){
        Category category = Category.builder().description("Cat1").build();

        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(category));

        Mono<Category> categoryMono = Mono.just(category);

        webTestClient.put().uri(API_CATEGORIES_PATH + "/1")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk();
    }
}
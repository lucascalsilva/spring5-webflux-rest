package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.constants.ApplicationConstants.API_CATEGORIES_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_CATEGORIES_PATH)
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> getCategoryById(@PathVariable String id){
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream){
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> updateCategory(@PathVariable String id, @RequestBody  Category category){
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> patchCategory(@PathVariable String id, @RequestBody  Category category){
        Mono<Category> foundCategoryMono = categoryRepository.findById(id);

        return foundCategoryMono.flatMap(foundCategory -> {
            if(category.getDescription() != null && (category.getDescription() == null || !foundCategory.getDescription().equals(category.getDescription()))){
                foundCategory.setDescription(category.getDescription());
                return categoryRepository.save(foundCategory);
            }
            else{
                return foundCategoryMono;
            }
        });
    }


}

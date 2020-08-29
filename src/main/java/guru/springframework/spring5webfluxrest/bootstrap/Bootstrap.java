package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadVendors();
    }

    public void loadCategories(){
        if(categoryRepository.count().block() == 0){
            String[] categories = {"Fruits", "Nuts", "Breads", "Meats", "Eggs"};
            List<Category> categoryList = new ArrayList<Category>();

            for(String category : categories){
                categoryList.add(Category.builder().id(UUID.randomUUID().toString())
                        .description(category).build());
            }

            categoryRepository.saveAll(categoryList).collectList().block();
        }
    }

    public void loadVendors(){
        if(vendorRepository.count().block() == 0){
            String[] vendors = {"Lucas;da Silva", "Luiz;da Silva"};
            List<Vendor> vendorList = new ArrayList<Vendor>();

            for(String vendor : vendors){
                String[] vendorSplit = vendor.split(";");
                if(vendorSplit.length == 2) {
                    vendorList.add(Vendor.builder().id(UUID.randomUUID().toString()).firstName(vendorSplit[0]).lastName(vendorSplit[1]).build());
                }
            }

            vendorRepository.saveAll(vendorList).collectList().block();
        }
    }


}

package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.constants.ApplicationConstants.API_VENDORS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VENDORS_PATH)
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Vendor> getAllVendors(){
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor){
        Mono<Vendor> foundVendorMono = vendorRepository.findById(id);

        return foundVendorMono.flatMap(foundVendor -> {
            Boolean isChanged = false;
            if(vendor.getFirstName() != null && (foundVendor.getFirstName() == null || !foundVendor.getFirstName().equals(vendor.getFirstName()))){
                foundVendor.setFirstName(vendor.getFirstName());
                isChanged = true;
            }
            if(vendor.getLastName() != null && (foundVendor.getLastName() == null || !foundVendor.getLastName().equals(vendor.getLastName()))){
                foundVendor.setLastName(vendor.getLastName());
                isChanged = true;
            }
            if(isChanged){
                return vendorRepository.save(foundVendor);
            }
            else{
                return foundVendorMono;
            }
        });
    }
}

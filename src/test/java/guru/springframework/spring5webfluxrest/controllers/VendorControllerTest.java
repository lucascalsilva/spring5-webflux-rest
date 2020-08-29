package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.NotExtensible;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.constants.ApplicationConstants.API_CATEGORIES_PATH;
import static guru.springframework.spring5webfluxrest.constants.ApplicationConstants.API_VENDORS_PATH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorController vendorController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getAllVendors() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Name1").lastName("LastName1").build(),
                        Vendor.builder().firstName("Name2").lastName("LastName2").build()));

        webTestClient.get().uri(API_VENDORS_PATH)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        given(vendorRepository.findById("1"))
                .willReturn(Mono.just(Vendor.builder().id("1").firstName("Name1").lastName("LastName1").build()));


        webTestClient.get().uri(API_VENDORS_PATH + "/1")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void testCreateVendor(){
        given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Name1").lastName("Lastname1").build());

        webTestClient.post().uri(API_VENDORS_PATH)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testUpdateVendor(){
        Vendor vendor = Vendor.builder().firstName("Name1").lastName("Lastname1").build();

        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(vendor));

        Mono<Vendor> vendorMono = Mono.just(vendor);

        webTestClient.put().uri(API_VENDORS_PATH + "/1")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }
}
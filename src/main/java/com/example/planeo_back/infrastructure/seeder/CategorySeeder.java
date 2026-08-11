package com.example.planeo_back.infrastructure.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CategorySeeder implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(CategorySeeder.class);

    public CategorySeeder() {
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.getOptionValues("seeder") != null){
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").getFirst().split(","));
            if(seeder.contains("category")) {
                seed();
                log.info("Success run role seeder");
            }
        }else{
            log.info("Role seeder skipped");
        }
    }

    private void seed() {

    }
}

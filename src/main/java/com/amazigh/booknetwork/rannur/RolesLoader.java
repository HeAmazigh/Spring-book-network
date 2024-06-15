package com.amazigh.booknetwork.rannur;

import aj.org.objectweb.asm.TypeReference;
import com.amazigh.booknetwork.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RolesLoader implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(RolesLoader.class);
  private final RoleRepository roleRepository;
  private final ObjectMapper objectMapper;

  public RolesLoader(RoleRepository roleRepository, ObjectMapper objectMapper) {
    this.roleRepository = roleRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    if (roleRepository.count() == 0) {
      String ROLES_JSON = "/data/roles.json";
      log.info("Load data to database from json file: {}", ROLES_JSON);
      try(InputStream inputStream = TypeReference.class.getResourceAsStream(ROLES_JSON)) {
        Roles response = objectMapper.readValue(inputStream, Roles.class);
        roleRepository.saveAll(response.roles());
      }catch(IOException e) {
        throw new RuntimeException("Failed to load data from json file", e);
      }
    }
  }
}

package com.apidynamics.test.server_demo.repository;


import com.apidynamics.test.server_demo.entity.ApiProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiProviderRepository extends JpaRepository<ApiProvider, Integer> {
}

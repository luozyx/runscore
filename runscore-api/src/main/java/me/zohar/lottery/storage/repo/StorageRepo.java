package me.zohar.lottery.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.storage.domain.Storage;

public interface StorageRepo extends JpaRepository<Storage, String>, JpaSpecificationExecutor<Storage> {

}

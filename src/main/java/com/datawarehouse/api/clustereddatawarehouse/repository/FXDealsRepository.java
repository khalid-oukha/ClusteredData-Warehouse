package com.datawarehouse.api.clustereddatawarehouse.repository;

import com.datawarehouse.api.clustereddatawarehouse.domain.FxDeals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FXDealsRepository extends JpaRepository<FxDeals, UUID> {
}

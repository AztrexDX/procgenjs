package com.aztrex.procgenjs.modules.serviceModules.workspace.database.repository;

import com.aztrex.procgenjs.modules.serviceModules.workspace.database.model.WorkspaceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface WorkspaceRepository
                extends PagingAndSortingRepository<WorkspaceRecord, Long>, ListCrudRepository<WorkspaceRecord, Long>,
                JpaRepository<WorkspaceRecord, Long>, JpaSpecificationExecutor<WorkspaceRecord> {
}
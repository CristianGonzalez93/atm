package com.zinkworks.atm.repository;

import com.zinkworks.atm.repository.domain.Bill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

  List<Bill> findAllByOrderByBillValueDesc();
}

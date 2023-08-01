package com.toy4.domain.employee.repository;

import com.toy4.domain.employee.domain.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  // 이메일 중복 여부 확인
  boolean existsByEmail(String email);

  // 전화번호 중복 여부 확인
  boolean existsByPhone(String phone);

  // 이메일로 회원 조회
  Optional<Employee> findByEmail(String email);

}

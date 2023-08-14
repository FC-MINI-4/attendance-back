package com.toy4.domain.employee.repository;

import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.type.EmployeeRole;
import com.toy4.domain.status.type.StatusType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  // 이메일 중복 여부 확인
  boolean existsByEmail(String email);
  
  // 회원 조회
  Optional<Employee> findById(Long id);

  // 전화번호 중복 여부 확인
  boolean existsByPhone(String phone);

  // 이메일로 회원 조회
  Optional<Employee> findByEmail(String email);

  // 인증토큰으로 회원 조회
  Optional<Employee> findByAuthToken(String authToken);

  // 입력받은 권한과 입력받은 상태 리스트에 포함되는 유저 리스트 조회
  List<Employee> findByRoleAndStatusTypeIn(EmployeeRole role, List<StatusType> statusTypes);
}

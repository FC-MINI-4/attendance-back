package com.toy4.domain.employee.service.impl;

import com.toy4.domain.dayOffByPosition.domain.DayOffByPosition;
import com.toy4.domain.dayOffByPosition.exception.DayOffByPositionException;
import com.toy4.domain.dayOffByPosition.repository.DayOffByPositionRepository;
import com.toy4.domain.dayOffHistory.repository.info.DayOffHistoryCustomRepository;
import com.toy4.domain.department.domain.Department;
import com.toy4.domain.department.exception.DepartmentException;
import com.toy4.domain.department.repository.DepartmentRepository;
import com.toy4.domain.department.type.DepartmentType;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.dto.ChangePassword;
import com.toy4.domain.employee.dto.ResetPassword;
import com.toy4.domain.employee.dto.Signup;
import com.toy4.domain.employee.dto.ValidateMatchPassword;
import com.toy4.domain.employee.dto.response.*;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.employee.service.EmployeeService;
import com.toy4.domain.position.domain.Position;
import com.toy4.domain.position.exception.PositionException;
import com.toy4.domain.position.repository.PositionRepository;
import com.toy4.domain.position.type.PositionType;
import com.toy4.domain.status.domain.Status;
import com.toy4.domain.status.exception.StatusException;
import com.toy4.domain.status.repository.StatusRepository;
import com.toy4.domain.status.type.StatusType;
import com.toy4.global.component.MailComponents;
import com.toy4.global.file.component.EmployeeProfileImageService;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.toy4.domain.employee.type.EmployeeRole.USER;
import static com.toy4.domain.position.type.PositionType.STAFF;
import static com.toy4.domain.status.type.StatusType.JOINED;
import static com.toy4.global.response.type.ErrorCode.*;
import static com.toy4.global.response.type.SuccessCode.SUCCESS;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ResponseService responseService;
    private final DayOffByPositionRepository dayOffByPositionRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final StatusRepository statusRepository;
    private final DayOffHistoryCustomRepository dayOffHistoryCustomRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailComponents mailComponents;
    private final EmployeeProfileImageService employeeProfileImageService;

    @Override
    @Transactional
    public void updateEmployeeInfo(EmployeeInfo dto, MultipartFile profileImageFile) {

        Employee employee = findEmployee(dto.getEmployeeId());

        String profileImagePath = employeeProfileImageService.getDefaultFile();
        String employeeImagePath = employee.getProfileImagePath();

        if (employeeImagePath != null) {
            employeeProfileImageService.removeIfFileExists(employeeImagePath);
        }

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            profileImagePath = employeeProfileImageService.saveFile(profileImageFile);
        }

        Department department = getDepartmentByType(dto.getDepartmentType());
        Position position = getPositionByType(dto.getPositionType());

        employee.updateEmployeeInfo(department, position, dto, profileImagePath);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public CommonResponse<?> getEmployeeInfo(Long id) {
        Employee employee = findEmployee(id);

        PersonalInfoResponse response = PersonalInfoResponse.from(employee);
        return responseService.success(response, SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public CommonResponse<?> getMyPage(Long id) {
      
        Employee employee = findEmployee(id);
     
        MyPageResponse response = MyPageResponse.from(employee);
        return responseService.success(response, SUCCESS);
    }

    @Override
    @Transactional
    public void updatePersonalInfo(PersonalInfo dto, MultipartFile profileImageFile) {

        Employee employee = findEmployee(dto.getEmployeeId());

        String profileImagePath = employeeProfileImageService.getDefaultFile();
        String employeeImagePath = employee.getProfileImagePath();

        if (employeeImagePath != null) {
            employeeProfileImageService.removeIfFileExists(employeeImagePath);
        }

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            profileImagePath = employeeProfileImageService.saveFile(profileImageFile);
        }

        Department department = getDepartmentByType(dto.getDepartmentType());

        employee.updatePersonalInfo(department, dto, profileImagePath);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public CommonResponse<?> getEmployeeDayOffInfo() {
        List<EmployeeDayOffInfoResponse> employeeDayOffInfos = dayOffHistoryCustomRepository.getEmployeeDayOff();

        if (employeeDayOffInfos.isEmpty()) {
            return responseService.failure(DAY_OFF_HISTORIES_NOT_FOUND);
        }

        return responseService.success(employeeDayOffInfos, SUCCESS);
    }

    @Override
    public void validateUniqueEmail(String email) {
        // 1. 유효성 검사(이메일 중복 확인)
        validateEmailDuplication(email);
    }

    @Override
    @Transactional
    public SignupResponse signup(Signup request) {
        // 1. 유효성 검사(이메일 및 전화번호 중복 확인)
        validateEmailDuplication(request.getEmail());
        validatePhoneDuplication(request.getPhone());

        // 2. 유효성 검사(비밀번호 일치 여부 확인)
        validatePasswordMatch(request.getPassword(), request.getConfirmPassword());

        // 3. 기본 프로필 이미지 가져오기
        String profileImagePath = employeeProfileImageService.getDefaultFile();

        // 4. 기본 정보(부서, 직급, 상태) 가져오기
        Department department = getDepartmentByType(request.getDepartmentType());
        Position position = getPositionByType(STAFF);
        Status status = getStatusByType(JOINED);

        // 5. 직급별 연차 개수 확인
        DayOffByPosition dayOffByPosition = getDayOffByPosition(position.getId());

        // 6. 회원 정보 저장
        Employee employee = employeeRepository.save(Employee.builder()
                .authToken(UUID.randomUUID().toString())
                .profileImagePath(profileImagePath)
                .position(position)
                .department(department)
                .status(status)
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .hireDate(request.getHireDate())
                .dayOffRemains((float) dayOffByPosition.getAmount())
                .role(USER)
                .build());

        return SignupResponse.builder().id(employee.getId()).build();
    }

    @Override
    public void sendPasswordChangeEmail(String email) {

        Employee employee = getEmployeeByEmail(email);

        String title = "[MINI-4] 비밀번호 변경 안내";
        String text = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "  <div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 500px; height: 600px; border-top: 4px solid #00a7e1; margin: 100px auto; padding: 30px 20px; box-sizing: border-box;\">" +
                "    <h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400; color: #00a7e1;\">" +
                "      <span style=\"font-size: 15px; margin: 0 0 10px 3px;\">MINI-4</span><br />" +
                "      <span style=\"color: #00a7e1\">비밀번호 변경</span> 안내입니다." +
                "    </h1>\n" +
                "    <p style=\"font-size: 16px; line-height: 26px; margin-top: 30px; padding: 0 5px; color: #333;\">" +
                employee.getName() +
                "        님 안녕하세요.<br />" +
                "        아래 <b style=\"color: #00a7e1\">'비밀번호 변경'</b> 버튼을 클릭하여 사이트를 이동해주세요.<br />" +
                "        감사합니다." +
                "    </p>" +
                "    <a style=\"color: #FFF; text-decoration: none; text-align: center;\" href=\"https://soonyang.vercel.app/reset-pw?authToken=" + employee.getAuthToken() + "\" target=\"_blank\">" +
                "        <p style=\"display: inline-block; width: 250px; height: 45px; margin: 30px auto; background: #00a7e1; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                "            비밀번호 변경" +
                "        </p>" +
                "    </a>" +
                "    <div style=\"border-top: 1px solid #00a7e1; padding: 5px;\"></div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        mailComponents.sendMail(employee.getEmail(), title, text);
    }

    @Override
    public void resetPassword(ResetPassword request) {
        // 1. 유효성 검사(인증토큰)
        Employee employee = getEmployeeByAuthToken(request.getAuthToken());

        // 2. 유효성 검사(비밀번호 일치 여부 확인)
        validatePasswordMatch(request.getPassword(), request.getConfirmPassword());

        // 3. 비밀번호 변경
        employee.updatePassword(passwordEncoder.encode(request.getPassword()));
        employee.updateNewAuthToken(UUID.randomUUID().toString());
        employeeRepository.save(employee);
    }

    @Override
    public void changePassword(ChangePassword request, Long employeeId) {
        // 1. 유효성 검사(고유 회원 아이디)
        Employee employee = getEmployeeById(employeeId);

        // 2. 유효성 검사(비밀번호 일치 여부 확인)
        validatePasswordWithDB(request.getCurrentPassword(), employee.getPassword());
        validatePasswordMatch(request.getPassword(), request.getConfirmPassword());

        // 3. 비밀번호 변경
        employee.updatePassword(passwordEncoder.encode(request.getPassword()));
        employeeRepository.save(employee);
    }

    @Override
    public void validateMatchPasswordWithDB(ValidateMatchPassword request, Long EmployeeId) {
        // 1. 유효성 검사(고유 회원 아이디)
        Employee employee = getEmployeeById(EmployeeId);

        // 2. 유효성 검사(비밀번호 일치 여부 확인)
        validatePasswordWithDB(request.getPassword(), employee.getPassword());
    }

    /** 입력받은 비밀번호가 올바른지 확인 */
    private void validatePasswordWithDB(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new EmployeeException(MISMATCH_PASSWORD);
        }
    }

    /** 고유값으로 회원 조회 */
    private Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
    }

    /** 이메일로 회원 조회 */
    private Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeException(INVALID_EMAIL));
    }

    /** authToken 회원 조회 */
    private Employee getEmployeeByAuthToken(String authToken) {
        return employeeRepository.findByAuthToken(authToken)
                .orElseThrow(() -> new EmployeeException(LOAD_USER_FAILED));
    }

    /** 이메일 중복 여부 확인 */
    private void validateEmailDuplication(String email) {
        if (employeeRepository.existsByEmail(email)) {
            throw new EmployeeException(ALREADY_EXISTS_EMAIL);
        }
    }

    /** 전화번호 중복 여부 확인 */
    private void validatePhoneDuplication(String phone) {
        if (employeeRepository.existsByPhone(phone)) {
            throw new EmployeeException(ALREADY_EXISTS_PHONE);
        }
    }

    /** 비밀번호 일치 여부 확인 */
    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new EmployeeException(MISMATCH_PASSWORD);
        }
    }

    /** 직급 조회 */
    private Position getPositionByType(PositionType type) {
        return positionRepository.findByType(type)
                .orElseThrow(() -> new PositionException(INVALID_REQUEST_POSITION_TYPE));
    }

    /** 회원 상태 조회 */
    private Status getStatusByType(StatusType type) {
        return statusRepository.findByType(type)
                .orElseThrow(() -> new StatusException(INVALID_REQUEST_STATUS_TYPE));
    }

    /** 회원 부서 조회 */
    private Department getDepartmentByType(DepartmentType type) {
        return departmentRepository.findByType(type)
                .orElseThrow(() -> new DepartmentException(INVALID_REQUEST_DEPARTMENT_TYPE));
    }

    /** 직급별 연차 개수 조회 */
    private DayOffByPosition getDayOffByPosition(Long positionId) {
        return dayOffByPositionRepository.findByPositionId(positionId)
                .orElseThrow(() -> new DayOffByPositionException(INVALID_REQUEST_POSITION_ID));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }
}


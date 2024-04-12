package kr.game.sale.service;

import kr.game.sale.entity.form.RoleListForm;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender sender;

    @Transactional
    public void addUser(Users users) {
        String initPassword = users.getPassword();
        String enPassword = bCryptPasswordEncoder.encode(initPassword);
        users.setPassword(enPassword);
        users.setUserRole(UserRole.ROLE_USER);
        userRepository.save(users);
    }

    public boolean isEmailExist(String username) {
        Users users = userRepository.findByUsername(username);
        return users != null;
    }

    public String verifyWithEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        String code = generateVerificationCode();
        msg.setTo(email);
        msg.setSubject("더조은 게임즈 인증코드입니다.");
        msg.setText(code);
        sender.send(msg);
        return code;
    }

    // 인증번호 생성 메소드
    private String generateVerificationCode() {
        Random random = new Random();
        int num = random.nextInt(999999); // 0부터 999999까지의 난수 생성
        return String.format("%06d", num); // 6자리 숫자로 포맷
    }

    //admin 라인
    // user 리스트 가지고오기
    public List<Users> getUserList(){
        return userRepository.findAllByUsernameNot("admin");
    }

    public Users getOneUsers(Long id){
        return userRepository.findById(id).isEmpty()? null : userRepository.findById(id).get();
    }
    // 권한 변경
    public void userRoleUpdate(RoleListForm role){
        Users u = getOneUsers(role.getId());
        if(role.getRole().equals("MANAGER")){
            u.setUserRole(UserRole.ROLE_MANAGER);
        }else if(role.getRole().equals("USER")){
            u.setUserRole(UserRole.ROLE_USER);
        }
        log.info("user = {}",u);
        userRepository.save(u);
    }

    public void adminUsersOneDelete(Long id){
        log.info("user = {}",id);
        userRepository.deleteById(id);
    }

}

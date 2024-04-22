package kr.game.sale.service;

import jakarta.persistence.EntityManager;
import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.entity.form.PaymentView;
import kr.game.sale.entity.form.RoleListForm;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.admin.PaymentRepository;
import kr.game.sale.repository.admin.RefundRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender sender;
    private final EntityManager em;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    @Transactional
    public void addUser(Users users) {
        String initPassword = users.getPassword();
        String enPassword = bCryptPasswordEncoder.encode(initPassword);
        users.setPassword(enPassword);
        users.setUserPhone(users.getUserPhone());
        users.setUserRole(UserRole.ROLE_USER);
        userRepository.save(users);
    }

    public boolean isEmailExist(String username) {
        return userRepository.findByUsername(username).isEmpty();
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
    public List<Users> getUserList() {
        return userRepository.findAllByUsernameNot("admin");
    }

    public Users getOneUsers(Long id) {
        return userRepository.findById(id).isEmpty() ? null : userRepository.findById(id).get();
    }

    public Users getOneUernameUser(String username) {
        return userRepository.findByUsername(username).isEmpty() ? null : userRepository.findByUsername(username).get();
    }

    // 권한 변경
    @Transactional
    public void userRoleUpdate(RoleListForm role) {
        Users u = getOneUsers(role.getId());
        if (role.getRole().equals("MANAGER")) {
            u.setUserRole(UserRole.ROLE_MANAGER);
        } else if (role.getRole().equals("USER")) {
            u.setUserRole(UserRole.ROLE_USER);
        }
        log.info("user = {}", u);
        userRepository.save(u);
    }

    @Transactional
    public void adminUsersOneDelete(Long id) {
        log.info("user = {}", id);
        userRepository.deleteById(id);
    }

    public Users getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Optional<Users> users = userRepository.findByUsername(username);
        return users.isEmpty() ? null : users.get();
    }

    @Transactional
    public Users updateUser(Users userForm) {
        String initPassword = userForm.getPassword();
        String enPassword = bCryptPasswordEncoder.encode(initPassword);

        Users users = em.find(Users.class, getLoggedInUser().getId());
        users.setPassword(enPassword);
        users.setUserNickname(userForm.getUserNickname());
        users.setUserPhone(userForm.getUserPhone());
        return users;
    }

    // 회원탈퇴
    @Transactional
    public void userResign() {
        Users users = getLoggedInUser();
        if (users.getUsername().equals("admin"))
            return;
        userRepository.deleteById(users.getId());
    }

    // 환불객체 만들기
    @Transactional
    public void makeRefund(String paymentOrdernum, String reason) {
        List<Payment> pmList = paymentRepository.findAllByPaymentOrdernum(paymentOrdernum);

        String paymentIds = "";
        for (int i = 0; i < pmList.size(); i++) {
            if (i != 0)
                paymentIds += ",";
            paymentIds += pmList.get(i).getPaymentId();
        }

        for (Payment pm : pmList) {
            pm.setPaymentResult("환불처리중");
        }

        if (!pmList.isEmpty()) {
            Refund rf = Refund.builder()
                    .paymentList(pmList)
                    .refundReason(reason)
                    .refundWhether(false)
                    .paymentIds(paymentIds)
                    .build();
            log.info("페이먼트아이디들 : {}", paymentIds);
            refundRepository.save(rf);
        }
    }

    public List<Payment> findAllByUser(Users user) {
        return paymentRepository.findAllByUser(user);
    }

    //orderNumList
    public List<String> orderNumList(List<Payment> paymentList) {
        List<String> ordernum = new ArrayList<>();
        ordernum.add(paymentList.get(0).getPaymentOrdernum());
        int i = 0;
        for (Payment list : paymentList) {
            if (!ordernum.get(i).equals(list.getPaymentOrdernum())) {
                ordernum.add(list.getPaymentOrdernum());
                i += 1;
            }
        }
        return ordernum;
    }

    private List<Payment> paymentOneView(String number, List<Payment> list) {
        List<Payment> lists = new ArrayList<>();
        for (Payment pay : list) {
            if (number.equals(pay.getPaymentOrdernum())) {
                log.info("pay : {}", pay);
                lists.add(pay);
            }
        }
        return lists;
    }

    public List<PaymentView> paymentViewList(List<Payment> paymentList, List<String> ordernumList) {
        List<PaymentView> list = new ArrayList<>();
        String number = "";
        for (String num : ordernumList) {
            PaymentView payment = new PaymentView();
            if (!number.equals(num)) {
                number = num;
                payment.setOrdernum(number);
                payment.setPaymentList(paymentOneView(number, paymentList));
                payment.setPaymentResult(payment.getPaymentList().get(0).getPaymentResult());
            }
            list.add(payment);
        }
        return list;
    }

    public Page<Users> userListPageing(Pageable pageable) {
        return userRepository.searchUser(pageable);
    }

}

package ru.koChyan.Auction.service;

import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.dao.UserDAO;
import ru.koChyan.Auction.domain.Role;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDAO userDAO;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s);

        if (user == null)
            throw new UsernameNotFoundException("Такой пользователь не найден");

        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        //Если пользователь c таким username уже есть в БД, сообщаем об ошибке
        if (userFromDb != null) {
            return false;
        }

        //Если пользователь с таким email уже есть в БД, сообщаем об ошибке
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (userFromDb != null) {
            return false;
        }

        //иначе создаем пользователя
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setBalance(0L);
        user.setActivationCode(UUID.randomUUID().toString());

        //шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);
        //Если у пользователя есть почта и она не состоит из пробелов
        //то отправляем ему код для подтверждения аккаунта
        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmptyOrWhitespaceOnly(user.getEmail())) {

            String message = String.format(
                    "Здравствуйте, %s! \n" +
                            "Добро пожаловать на аукцион Ambey.\n" +
                            "Для подтверждения регистрации, пожалуйста, перейдите по ссылке:\n" +
                            "http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        //если пользователь не найден, то выводим false
        if (user == null) {
            return false;
        }

        //помечаем, что код активирован
        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form, Long balance) {

        user.setUsername(username);
        user.setBalance(balance);

        //помещаем в Set все существующие роли
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        //очистим список ролей пользователя
        user.getRoles().clear();

        //установим роли, которые были получены из формы
        for (String key : form.keySet())
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }

        userRepo.save(user);
    }

    public void updateProfile(User user, String newPassword, String newEmail) {
        String userEmail = user.getEmail();

        //проверяем старую и новую почту на равенство null и друг другу
        boolean isEmailChanged = (userEmail != null && !userEmail.equals(newEmail) ||
                newEmail != null && !newEmail.equals(userEmail));

        //если email изменился и он не null
        //то ищем, есть ли уже другой пользователь
        //с таким email в БД
        User userFromDb = userRepo.findByEmail(newEmail);

        //Если в БД пользователь с таким email не найден
        //то изменяем почту пользователю из формы
        if (userFromDb == null)
            if (isEmailChanged) {
                user.setEmail(newEmail);

                //если пользователь установил новый непустой email
                if (!StringUtils.isEmptyOrWhitespaceOnly(newEmail)) {

                    //то устанавливаем ему код активации
                    user.setActivationCode(UUID.randomUUID().toString());
                }
            }

        //если пользователь ввел новый непустой пароль, то устанавливаем его
        if (!StringUtils.isEmptyOrWhitespaceOnly(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepo.save(user);

        //отправляем код активации, если email был изменен
        //и не был занят другим пользователем
        if (userFromDb == null)
            if (isEmailChanged) {
                sendMessage(user);
            }
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }
}
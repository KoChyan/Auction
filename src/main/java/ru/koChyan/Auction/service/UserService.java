package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.Role;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.UserDto;
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
    private ResourceBundle resourceBundle;


    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s);

        if (user == null)
            throw new UsernameNotFoundException("Такой пользователь не найден");

        return user;
    }

    public boolean addUser(UserDto userDto) {
        // создаем пользователя
        User user = new User();

        //перепишем значения с userDto
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setBalance(0L);
        user.setActivationCode(UUID.randomUUID().toString());

        //шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        // отправляем пользователю код для подтверждения аккаунта
        sendActivationCode(user);

        return true;
    }

    private void sendActivationCode(User user) {
        String message = String.format(
                resourceBundle.getString("message.toNewUser"),
                user.getActivationCode()
        );

        String subject = resourceBundle.getString("subject.toNewUser");

        mailSender.send(user.getEmail(), subject, message);
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        //если пользователь не найден, то выводим false
        if (user == null)
            return false;

        //помечаем, что код активирован
        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);
        return true;
    }

    public void saveUser(User user, Map<String, String> form, Long balance) {
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

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void updateProfile(User user, UserDto userFromForm) {
        // если не null и не из пробелов/пустой
        boolean isEmailChanged = userFromForm.getNewEmail() != null && !userFromForm.getNewEmail().isBlank();

        if (isEmailChanged) {
            user.setEmail(userFromForm.getNewEmail());

            //то устанавливаем ему код активации
            user.setActivationCode(UUID.randomUUID().toString());

            sendActivationCode(user);
        }

        boolean isPasswordChanged = userFromForm.getNewPassword() != null && !userFromForm.getNewPassword().isBlank();

        if (isPasswordChanged)
            user.setPassword(passwordEncoder.encode(userFromForm.getNewPassword()));

        if (isEmailChanged || isPasswordChanged)
            userRepo.save(user);
    }

    public Optional<User> getById(Long id) {
        return userRepo.findById(id);
    }

    public boolean isExistsByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail) != null;
    }

    public boolean isExistsByUsername(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }
}